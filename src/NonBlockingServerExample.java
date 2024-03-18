import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author: 刘中睿
 * @Date: 2024/3/18 15:44
 * @Description:
 */
public class NonBlockingServerExample {
    public static void main(String[] args) {
        try {
            // 打开一个服务器套接字通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);

            System.out.println("服务器启动，端口 8080");
            while (true) {
                // 非阻塞模式下，accpet方法立即返回，如果没有连接则返回 null
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    System.out.println("接受到客户端连接：" + socketChannel.getRemoteAddress());

                    // 处理客户端请求
                    handleRequest(socketChannel);
                } else {
                    // 没有连接时可以进行其他操作
//                    System.out.println("没有客户端连接");
//                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取客户端请求
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[bytesRead];
            buffer.get(data);
            System.out.println("收到客户端数据：" + new String(data));

            // 响应客户端请求
            String response = "Hello, Client!";
            buffer.clear();
            buffer.put(response.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
        }

        // 关闭套接字通道
        socketChannel.close();
    }
}
