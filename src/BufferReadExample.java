import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferReadExample {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("C:\\Users\\missright\\Desktop\\test.txt", "rw")) {
            // 文件通道
            FileChannel channel = file.getChannel();

            // 缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 从通道读取数据到缓冲区
            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                // 切换缓冲区为读模式
                buffer.flip();

                // 从缓冲区读取数据并输出到控制台
                while (buffer.hasRemaining()) {
                    System.out.println((char) buffer.get());
                }

                // 清空缓冲区
                buffer.clear();

                // 从通道读取数据到缓冲区
                bytesRead = channel.read(buffer);
            }

            // 关闭通道
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}