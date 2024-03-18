import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 刘中睿
 * @Date: 2024/3/18 15:24
 * @Description:
 */
public class ClientExample {
    public static void main(String[] args) {
        try {
            // 创建一个客户端套接字通道，并连接到服务器
            SocketChannel socketChannel = SocketChannel.open();
            // 设置为非阻塞模式
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 8080));

            // 等待连接完成
            while (!socketChannel.finishConnect()) {
                // 等待连接完成
            }

            // 向服务器发送数据
            String message = "send message";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.clear();

            // 接收响应消息
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[bytesRead];
                buffer.get(data);
                String responseMessage = new String(data, StandardCharsets.UTF_8);
                System.out.println("收到服务器响应：" + responseMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
