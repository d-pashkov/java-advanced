package info.kgeorgiy.ja.pashkov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HelloUDPServer implements HelloServer {
    private static final String RESPONSE_PREFIX = "Hello, ";
    private static final int TASKS_PER_THREAD_LIMIT = 256;

    private ExecutorService workers;
    private DatagramSocket socket;
    private Thread receiver;
    private volatile boolean started;

    @Override
    public synchronized void start(final int port, final int threads) {
        if (threads <= 0) {
            throw new IllegalArgumentException("threads must be positive");
        }
        close();

        workers = new ThreadPoolExecutor(
                threads,
                threads,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(Math.max(threads, 1) * TASKS_PER_THREAD_LIMIT)
        );
        try {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(HelloUDPCommon.SO_TIMEOUT_MS);
        } catch (final SocketException e) {
            throw new IllegalStateException("Cannot start server", e);
        }

        started = true;
        receiver = new Thread(this::receiveLoop, "HelloUDPServer-receiver");
        receiver.start();
    }

    private void receiveLoop() {
        final int bufferSize;
        try {
            bufferSize = socket.getReceiveBufferSize();
        } catch (final SocketException e) {
            return;
        }

        while (started && !Thread.currentThread().isInterrupted()) {
            final DatagramSocket currentSocket = socket;
            if (currentSocket == null || currentSocket.isClosed()) {
                break;
            }
            try {
                final DatagramPacket packet = new DatagramPacket(
                        new byte[bufferSize],
                        bufferSize
                );
                currentSocket.receive(packet);
                final byte[] requestData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), requestData, 0, packet.getLength());
                final SocketAddress target = packet.getSocketAddress();

                workers.submit(() -> respond(target, requestData));
            } catch (final SocketTimeoutException ignored) {
            } catch (final RejectedExecutionException e) {
            } catch (final IOException ignored) {
            }
        }
    }

    private void respond(final SocketAddress target, final byte[] requestData) {
        final String request = new String(requestData, StandardCharsets.UTF_8);
        final byte[] responseData = (RESPONSE_PREFIX + request).getBytes(StandardCharsets.UTF_8);
        final DatagramPacket response = new DatagramPacket(
                responseData,
                responseData.length,
                target
        );
        final DatagramSocket currentSocket = socket;
        if (currentSocket == null || currentSocket.isClosed()) {
            return;
        }
        try {
            currentSocket.send(response);
        } catch (final IOException ignored) {
        }
    }

    @Override
    public synchronized void close() {
        started = false;
        final DatagramSocket currentSocket = socket;
        if (currentSocket != null) {
            currentSocket.close();
        }
        if (receiver != null) {
            receiver.interrupt();
            try {
                receiver.join();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            receiver = null;
        }
        if (workers != null) {
            workers.shutdownNow();
            workers = null;
        }
        socket = null;
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Usage: HelloUDPServer port threads");
            return;
        }

        final Integer port = HelloUDPCommon.parsePositiveInt(args[0]);
        final Integer threads = HelloUDPCommon.parsePositiveInt(args[1]);
        if (port == null || threads == null) {
            System.err.println("Port and threads must be positive integers");
            return;
        }

        final HelloUDPServer server = new HelloUDPServer();
        server.start(port, threads);
    }
}
