package com.longer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

/**
 * 测试 IO 流
 */
@SpringBootTest
public class IOTest {
    /* 所谓 IO 流，就是将内存中的数据存入硬盘，将硬盘中的数据存入到内存里面
     * InputStream 读 OutputStream 写，俗称 IO
     * I 输出流，只能从文件中读取数据，而不能向文件中写入数据
     * O 输入流，只能向文件中写入数据，不能从中读取数据
     * 字节流的操作最小数据单元为 8 位的字节
     * 字符流的操作最小数据单元为 16 为的字符
     * 字节流继承于 InputStream OutputStream
     * 字符流继承于 InputStreamReader OutputStreamWriter
     * 在 java.io 包中还有许多其它的流，主要是为了提高性能和使用方便 */

    // 字节
    private InputStream is = null;// I
    private OutputStream os = null;// O

    // 字符
    private BufferedReader reader = null;// I
    private BufferedWriter writer = null;// O

    @Test// 测试字节流：读
    public void testByteStreamI() {
        try {
            is = new FileInputStream("d:/java.txt");// 参数为要指定读取的文件路径，将指定文件变成字节流

            // 从里面一个字节一个字节的读，而且返回的是这个字节对应的 ascii 码
            // for (int i = 0; i < 10; i++) {
                // System.out.println((char) is.read());
            // }

            // 定义一个 byte 数组，用来存储一次读取出来的所有字节
            byte[] buffer = new byte[3];
            // is.read(buffer);// 装到 buffer 这个数组里面，里面装 3 个字节
            // for (byte b : buffer) {
                // System.out.println((char) b);
            // }

            /* 上面是一次读取数组定义的所有字节个数，但是如果一次读取不完呢，下面就要使用到另一个判断方法了
             * is.read(buffer) 的返回值是 int 它会将每次读取的字节个数返回，当字节全部读取完以后会返回 -1 */

            // System.out.println(is.read(buffer));
            // System.out.println(is.read(buffer));

            // 这样我们只用判断它的返回值是不是 -1 就可以停止循环了
            int len = 0;// 存储每次读取到了多少个，因为没有就会返回 -1
            while ((len = is.read(buffer)) != -1) {
                // 用 foreach 循环出字节
                // for (byte b : buffer) {
                    // System.out.println((char) b);
                // }

                // 但是上面的方法还是有点问题，如果每次只循环 3 个字节，文本里面只有 8 个字节，那么他会自动补一个字节在里面，现在换一种循环方法
                for (int i = 0; i < len; i++) {
                    System.out.print((char) buffer[i] + "\t");
                }
                System.out.println();
            }
            // 以上为 InputStream 读的测试，做法没有意义，理解它的运行过程即可
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();// 关流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test// 测试字节流：写
    public void testByteStreamO() throws IOException {
        os = new FileOutputStream("d:/java.txt");

        // 写，如果写入一个新的文件，这样的写法会直接覆盖掉原来的内容
        // os.write(97);// 97 转换成 ascii a

        // 定义一个 byte 数组，将数字里面的内容写入指定文件
        byte[] buffer = {97, 98, 99, 100};
        os.write(buffer);

        os.close();// 关流
        // 理解了 IO 的读和写，现在我们取下面测试读取指定文件
    }

    @Test// 测试读取指定文件
    public void testCtrlCCtrlV() {
        try {
            is = new FileInputStream("d:/图片/由乃.png");// 读
            os = new FileOutputStream("d:/图片/我妻由乃.png");// 写

            byte[] buffer = new byte[1024];// 默认每次最大读取 1k
            int len = 0;// 每次读取的字节个数
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);// 每次写入从第 0 个字节开始，到剩余的字节结束
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();// 关闭输出流
                os.close();// 关闭输入流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* 上面是字节流，主要用于二进制传输文件的，下面测试字符流，主要用户传输文本 */

    @Test// 测试字符流：读
    public void testCharacterStreamI() {
        try {
            // Reader reader = new FileReader("d:/java.txt");// 可以使用更高级的流包装一下
            reader = new BufferedReader(new FileReader("d:/java.txt"));// 字符缓冲流

            // 读
            // String line = reader.readLine();// 读取一行
            // System.out.println(line);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();// 关闭缓冲流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test// 测试字符流：写
    public void testCharacterStreamO() {
        try {
            // Writer writer = new FileWriter("d:/java.txt");// 可以使用更高级的流包装一下
            writer = new BufferedWriter(new FileWriter("d:/java.txt"));

            // 写
            writer.write("麻了");
            writer.newLine();// 换行
            writer.write("麻中麻");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();// 关闭缓冲流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test// 测试文件
    public void testFile() {
        // File file = new File("d:/test.txt");// 电脑中的文件及文件夹这里统称 file
        // file.mkdir();// 创建文件夹

        // File file = new File("d:/a/b/c");
        // file.mkdirs();// 创建文件夹中夹

        File file = new File("d:/java.txt");
        try {
            file.createNewFile();// 创建新文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 以上为基础的 IO 测试，更多方法请自行查看 api */
}