package info.kgeorgiy.ja.pashkov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//note -- supress??
public class HelloUDPNonblockingClient implements HelloClient {
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

        //note -- разделить бы код по функциям, слишком громоздко по дизайну
        final SocketAddress address = new InetSocketAddress(host, port);
        try (Selector selector = Selector.open()) {
            final List<DatagramChannel> channels = openChannels(selector, address, prefix, requests, threads);
            try {
                eventLoop(selector);
            } finally {
                closeChannels(channels);
            }
        } catch (final IOException ignored) {
        }
    }

    private List<DatagramChannel> openChannels(
            final Selector selector,
            final SocketAddress address,
            final String prefix,
            final int requests,
            final int threads
    ) throws IOException {
        final List<DatagramChannel> channels = new ArrayList<>();
        for (int threadId = 1; threadId <= threads; threadId++) {
            final DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.connect(address);
            final int receiveBufferSize = channel.socket().getReceiveBufferSize();
            channel.register(selector, SelectionKey.OP_WRITE,
                    new ClientAttachment(prefix, threadId, requests, receiveBufferSize));
            channels.add(channel);
        }
        return channels;
    }

    private void eventLoop(final Selector selector) throws IOException {
        while (!Thread.currentThread().isInterrupted() && !selector.keys().isEmpty()) {
            selector.select(selectTimeout(selector));
            handleSelectedKeys(selector);
            processTimeouts(selector);
        }
    }

    private void handleSelectedKeys(final Selector selector) {
        if (selector.selectedKeys().isEmpty()) {
            return;
        }
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
            final SelectionKey key = iterator.next();
            iterator.remove();
            if (!key.isValid()) {
                continue;
            }
            processKey(key);
        }
    }

    private void processTimeouts(final Selector selector) {
        final long now = System.currentTimeMillis();
        for (final SelectionKey key : selector.keys()) {
            if (!key.isValid()) {
                continue;
            }
            final ClientAttachment attachment = (ClientAttachment) key.attachment();
            if (attachment.waitingForResponse && now - attachment.lastSendTime >= HelloUDPCommon.SO_TIMEOUT_MS) {
                key.interestOps(SelectionKey.OP_WRITE);
            }
        }
    }

    private void closeChannels(final List<DatagramChannel> channels) {
        for (final DatagramChannel channel : channels) {
            try {
                channel.close();
            } catch (final IOException ignored) {
            }
        }
    }

    private long selectTimeout(final Selector selector) {
        long timeout = HelloUDPCommon.SO_TIMEOUT_MS;
        final long now = System.currentTimeMillis();
        for (final SelectionKey key : selector.keys()) {
            if (!key.isValid()) {
                continue;
            }
            final ClientAttachment attachment = (ClientAttachment) key.attachment();
            if (attachment.waitingForResponse) {
                timeout = Math.min(timeout, Math.max(0, HelloUDPCommon.SO_TIMEOUT_MS - (now - attachment.lastSendTime)));
            }
        }
        return timeout;
    }

    private void processKey(final SelectionKey key) {
        final DatagramChannel channel = (DatagramChannel) key.channel();
        final ClientAttachment attachment = (ClientAttachment) key.attachment();

        try {
            if (key.isWritable()) {
                if (attachment.requestId > attachment.totalRequests) {
                    channel.close();
                    return;
                }
                attachment.currentRequest = HelloUDPCommon.request(
                        attachment.prefix,
                        attachment.threadId,
                        attachment.requestId
                );
                final byte[] requestBytes = HelloUDPCommon.requestBytes(attachment.currentRequest);
                if (requestBytes.length > attachment.sendBuffer.capacity()) {
                    attachment.sendBuffer = ByteBuffer.allocate(requestBytes.length);
                }
                attachment.sendBuffer.clear();
                attachment.sendBuffer.put(requestBytes);
                attachment.sendBuffer.flip();
                channel.write(attachment.sendBuffer);
                attachment.lastSendTime = System.currentTimeMillis();
                attachment.waitingForResponse = true;
                key.interestOps(SelectionKey.OP_READ);
            } else if (key.isReadable()) {
                attachment.receiveBuffer.clear();
                final SocketAddress from = channel.receive(attachment.receiveBuffer);
                if (from == null) {
                    return;
                }
                attachment.receiveBuffer.flip();
                final String response = StandardCharsets.UTF_8.decode(attachment.receiveBuffer).toString();
                if (HelloUDPCommon.isValidResponse(
                        response,
                        attachment.currentRequest,
                        attachment.requestId,
                        attachment.threadId
                )) {
                    System.out.println(attachment.currentRequest);
                    System.out.println(response);
                    attachment.requestId++;
                    attachment.waitingForResponse = false;
                }
                key.interestOps(SelectionKey.OP_WRITE);
            }
        } catch (final IOException e) {
            try {
                channel.close();
            } catch (final IOException ignored) {
            }
        }
    }

    private static final class ClientAttachment {
        private final String prefix;
        private final int threadId;
        private final int totalRequests;
        private final ByteBuffer receiveBuffer;
        private ByteBuffer sendBuffer;

        private int requestId = 1;
        private String currentRequest = "";
        private long lastSendTime;
        private boolean waitingForResponse;

        private ClientAttachment(
                final String prefix,
                final int threadId,
                final int totalRequests,
                final int receiveBufferSize
        ) {
            this.prefix = prefix;
            this.threadId = threadId;
            this.totalRequests = totalRequests;
            this.receiveBuffer = ByteBuffer.allocate(Math.max(1, receiveBufferSize)); // note -- socket.getReceiveBufferSize()
            this.sendBuffer = ByteBuffer.allocate(128);
        }
    }

    public static void main(final String[] args) {
        if (args == null || args.length != 5) {
            System.err.println("Usage: HelloUDPNonblockingClient host port prefix requests threads");
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

        new HelloUDPNonblockingClient().run(args[0], port, args[2], requests, threads);
    }
}
