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
 * @Date: 2024/3/18 10:56
 * @Description:
 */
public class SelectorExample {
    public static void main(String[] args) {
        try (Selector selector = Selector.open()) {
            // 创建一个选择器
            // 打开一个服务器套接字通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress((8080)));
            serverSocketChannel.configureBlocking(false);

            // 将服务器套接字通道注册到选择器上，并指定监听连接事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 阻塞等待事件发生
                selector.select();

                // 获取已经发生的事件集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

                // 遍历事件集合并处理事件
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    // 如果是连接事件
                    if (key.isAcceptable()) {
                        // 接收客户端连接请求
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);

                        // 将客户端套接字通道注册到选择器上，并指定监听读取事件
                        socketChannel.register(selector, SelectionKey.OP_READ);

                        System.out.println("客户端连接成功：" + socketChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        // 读取客户端发送的数据
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int byteRead = socketChannel.read(buffer);
                        if (byteRead > 0) {
                            buffer.flip();
                            byte[] data = new byte[byteRead];
                            buffer.get(data);
                            String receiveMessage = new String(data, StandardCharsets.UTF_8);
                            System.out.println("收到客户端数据：" + receiveMessage);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
