import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 刘中睿
 * @Date: 2024/3/18 17:55
 * @Description:
 */
public class ContinuousRequestClient {
    public static void main(String[] args) {
        while (true) {
            try (SocketChannel socketChannel = SocketChannel.open()) {
                // 创建一个客户端套接字通道

                // 连接到服务器
                socketChannel.connect(new InetSocketAddress("localhost", 8080));

                // 循环发送请求
                // 发送请求消息
                String requestMessage = "Request from client";
                ByteBuffer buffer = ByteBuffer.wrap(requestMessage.getBytes(StandardCharsets.UTF_8));
                socketChannel.write(buffer);

                // 清空缓冲区并准备接收响应
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

                // 模拟间隔，以便观察输出
                Thread.sleep(10);
            } catch (IOException | InterruptedException e) {
                System.err.println("连接中断，尝试重新连接...");
                try {
                    // 等待一段时间再尝试重新连接
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
