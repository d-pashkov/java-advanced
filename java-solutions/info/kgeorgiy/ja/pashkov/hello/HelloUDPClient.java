package info.kgeorgiy.ja.pashkov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPClient implements HelloClient {
    @Override
    public void run(
            final String host,
            final int port,
            final String prefix,
            final int requests,
            final int threads
    ) {
        if (requests <= 0 || threads <= 0) {
            return;
        }

        final InetSocketAddress address = new InetSocketAddress(host, port);
        final ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int threadId = 1; threadId <= threads; threadId++) {
            final int currentThreadId = threadId;
            pool.submit(() -> processThread(address, prefix, requests, currentThreadId));
        }

        HelloUDPCommon.awaitTermination(pool);
    }

    private void processThread(
            final InetSocketAddress address,
            final String prefix,
            final int requests,
            final int threadId
    ) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(HelloUDPCommon.SO_TIMEOUT_MS);
            final int bufferSize = socket.getReceiveBufferSize();
            final byte[] responseBuffer = new byte[bufferSize];
            final DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);

            for (int requestId = 1; requestId <= requests; requestId++) {
                final String request = HelloUDPCommon.request(prefix, threadId, requestId);
                final byte[] requestBytes = HelloUDPCommon.requestBytes(request);
                final DatagramPacket requestPacket = new DatagramPacket(
                        requestBytes,
                        requestBytes.length,
                        address
                );

                while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                    try {
                        socket.send(requestPacket);
                        responsePacket.setLength(responseBuffer.length);
                        socket.receive(responsePacket);
                        final String response = new String(
                                responsePacket.getData(),
                                responsePacket.getOffset(),
                                responsePacket.getLength(),
                                StandardCharsets.UTF_8
                        );

                        if (HelloUDPCommon.isValidResponse(response, request, requestId, threadId)) {
                            System.out.println(request);
                            System.out.println(response);
                            break;
                        }
                    } catch (final SocketTimeoutException ignored) {
                    } catch (final IOException ignored) {
                    }
                }
            }
        } catch (final IOException ignored) {
        }
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 5) {
            System.err.println("Usage: HelloUDPClient host port prefix requests threads");
            return;
        }
        for (final String arg : args) {
            if (arg == null) {
                System.err.println("All arguments must be non-null");
                return;
            }
        }

        final Integer port = HelloUDPCommon.parsePositiveInt(args[1]);
        final Integer requests = HelloUDPCommon.parsePositiveInt(args[3]);
        final Integer threads = HelloUDPCommon.parsePositiveInt(args[4]);
        if (port == null || threads == null || requests == null) {
            System.err.println("Port, requests and threads must be positive integers");
            return;
        }

        new HelloUDPClient().run(args[0], port, args[2], requests, threads);
    }
}
