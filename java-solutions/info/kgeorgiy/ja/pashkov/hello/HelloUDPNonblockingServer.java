package info.kgeorgiy.ja.pashkov.hello;

import info.kgeorgiy.java.advanced.hello.NewHelloServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("resource")
public class HelloUDPNonblockingServer implements NewHelloServer {
    private Selector selector;
    private Thread worker;
    private volatile boolean running;

    @Override
    @SuppressWarnings("resource")
    public synchronized void start(final int threads, final Map<Integer, String> ports) {
        if (threads <= 0) {
            throw new IllegalArgumentException("threads must be positive");
        }
        if (ports == null) {
            throw new IllegalArgumentException("ports must be non-null");
        }

        close();

        try {
            selector = Selector.open();
            final Map<Integer, String> copy = validatePorts(ports);
            if (copy.isEmpty()) {
                selector.close();
                selector = null;
                return;
            }
            for (final Map.Entry<Integer, String> entry : copy.entrySet()) {
                final DatagramChannel channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.bind(new InetSocketAddress(entry.getKey()));
                final int capacity = channel.socket().getReceiveBufferSize();
                channel.register(selector, SelectionKey.OP_READ, new PortAttachment(entry.getValue(), capacity));
            }
            running = true;
            worker = new Thread(this::eventLoop, "HelloUDPNonblockingServer");
            worker.start();
        } catch (final IOException e) {
            close();
            throw new IllegalStateException("Cannot start server", e);
        }
    }

    private void eventLoop() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                selector.select();
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                    if (key.isWritable()) {
                        write(key);
                    }
                }
            } catch (final IOException e) {
                if (running) {
                    break;
                }
            }
        }
    }

    private void read(final SelectionKey key) throws IOException {
        final DatagramChannel channel = (DatagramChannel) key.channel();
        final PortAttachment attachment = (PortAttachment) key.attachment();
        final ByteBuffer buffer = attachment.buffer;

        buffer.clear();
        final SocketAddress source = channel.receive(buffer);
        if (source == null) {
            setInterestOps(key, attachment);
            return;
        }
        buffer.flip();
        final String request = StandardCharsets.UTF_8.decode(buffer).toString();
        final byte[] responseBytes = attachment.format.replace("%%", request).getBytes(StandardCharsets.UTF_8);

        attachment.enqueue(new PendingResponse(source, ByteBuffer.wrap(responseBytes)));
        setInterestOps(key, attachment);
    }

    private void write(final SelectionKey key) throws IOException {
        final DatagramChannel channel = (DatagramChannel) key.channel();
        final PortAttachment attachment = (PortAttachment) key.attachment();
        while (attachment.hasPending()) {
            final PendingResponse response = attachment.peek();
            channel.send(response.data(), response.target());
            if (response.data().hasRemaining()) {
                break;
            }
            attachment.completeHead();
        }
        setInterestOps(key, attachment);
    }

    private void setInterestOps(final SelectionKey key, final PortAttachment attachment) {
        if (!key.isValid()) {
            return;
        }
        int ops = 0;
        ops |= SelectionKey.OP_READ;
        if (attachment.hasPending()) {
            ops |= SelectionKey.OP_WRITE;
        }
        key.interestOps(ops);
    }

    private Map<Integer, String> validatePorts(final Map<Integer, String> ports) {
        final Map<Integer, String> copy = new HashMap<>(ports.size());
        for (final Map.Entry<Integer, String> entry : ports.entrySet()) {
            final Integer port = entry.getKey();
            final String format = entry.getValue();
            if (port == null || port <= 0 || port > 65535) {
                throw new IllegalArgumentException("Invalid port: " + port);
            }
            if (format == null || format.isBlank()) {
                throw new IllegalArgumentException("Response format must be non-empty");
            }
            copy.put(port, format);
        }
        return Collections.unmodifiableMap(copy);
    }

    @Override
    public synchronized void close() {
        running = false;
        if (selector != null) {
            selector.wakeup();
        }
        if (worker != null) {
            worker.interrupt();
            try {
                worker.join();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            worker = null;
        }
        if (selector != null) {
            for (final SelectionKey key : selector.keys()) {
                try {
                    key.channel().close();
                } catch (final IOException ignored) {
                }
            }
            try {
                selector.close();
            } catch (final IOException ignored) {
            }
            selector = null;
        }
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Usage: HelloUDPNonblockingServer port threads");
            return;
        }

        final Integer port = HelloUDPCommon.parsePositiveInt(args[0]);
        final Integer threads = HelloUDPCommon.parsePositiveInt(args[1]);
        if (port == null || threads == null) {
            System.err.println("Port and threads must be positive integers");
            return;
        }

        final HelloUDPNonblockingServer server = new HelloUDPNonblockingServer();
        server.start(threads, Map.of(port, "Hello, %%"));
    }

    private static final class PortAttachment {
        private final String format;
        private final ByteBuffer buffer;
        private final ArrayDeque<PendingResponse> queue = new ArrayDeque<>();

        private PortAttachment(final String format, final int receiveBufferSize) {
            this.format = format;
            this.buffer = ByteBuffer.allocate(receiveBufferSize);
        }

        private void enqueue(final PendingResponse response) {
            queue.addLast(response);
        }

        private PendingResponse peek() {
            return queue.peekFirst();
        }

        private boolean hasPending() {
            return !queue.isEmpty();
        }

        private void completeHead() {
            queue.removeFirst();
        }
    }

    private record PendingResponse(
            SocketAddress target,
            ByteBuffer data
    ) {
    }
}
