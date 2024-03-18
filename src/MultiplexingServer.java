import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: 刘中睿
 * @Date: 2024/3/18 18:21
 * @Description:
 */
public class MultiplexingServer {
    public static void main(String[] args) {
        try {
            //
            Selector selector = Selector.open();

            //
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);

            //
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                //
                selector.select();

                //
                Set<SelectionKey> selectededKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectededKeys.iterator();

                //
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    //
                    if (key.isAcceptable()) {
                        //
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);

                        //
                        clientChannel.register(selector, SelectionKey.OP_READ);

                        System.out.println("客户端连接成功：" + clientChannel.getRemoteAddress());

                    } else if (key.isReadable()) {
                        //
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = clientChannel.read(buffer);
                        if (bytesRead > 0) {
                            buffer.flip();
                            byte[] data = new byte[bytesRead];
                            buffer.get(data);
                            String message = new String(data, StandardCharsets.UTF_8);
                            System.out.println("收到客户端数据：" + message);

                            // 响应客户端
                            String responseMessage = "Hello, I received your message: " + message;
                            ByteBuffer responseBuffer = ByteBuffer.wrap(responseMessage.getBytes(StandardCharsets.UTF_8));
                            clientChannel.write(responseBuffer);
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
