import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.RandomAccess;

/**
 * @Author: 刘中睿
 * @Date: 2024/3/18 10:39
 * @Description:
 */
public class BufferExample {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("C:\\Users\\missright\\Desktop\\output.txt", "rw")) {
            String data = "Hello, Java NIO !";
            FileChannel channel = file.getChannel();

            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(data.getBytes());

            // 切换缓冲区为读模式
            buffer.flip();

            // 将缓冲区的数据写入到文件通道中
            channel.write(buffer);

            // 关闭通道
            channel.close();
            System.out.println("success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
