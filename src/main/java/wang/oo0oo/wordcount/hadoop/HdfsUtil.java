package wang.oo0oo.wordcount.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;

public class HdfsUtil {

    private FileSystem client;

    /**
     * 初始化函数（由SpringBoot主程序初始化模块调用）
     */
    public static void init() {
        //设置本地（Windows）的HADOOP根目录【远程调试】
        System.setProperty("HADOOP_HOME", "E:\\hadoop-2.7.7");
        //指定hadoop用户名
        System.setProperty("HADOOP_USER_NAME", "root");
        //设置本地（Windows）的HADOOP家目录【远程调试】
        System.setProperty("hadoop.home.dir", "E:\\hadoop-2.7.7");

    }

    private FileSystem getClient() throws IOException {
        if (client == null) {
            //创建HDFS连接对象client
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://bigdata:9000");
            client = FileSystem.get(conf);
        }
        return client;
    }

    public void closeClient() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param hdfsPath hdfs文件夹绝对路径。例如"/user/root/dir"
     * @return 执行结果
     */
    public boolean mkdir(String hdfsPath) {
        try {
            return getClient().mkdirs(new Path(hdfsPath));
        } catch (IOException e) {
            return false;
        }
    }



    /**
     * 上传文件到HDFS
     *
     * @param localPath 本地文件绝对路径。例如"/root/a.txt"
     * @param hdfsPath  hdfs绝对路径。例如"/user/root/dir/a.txt"
     * @return 执行结果
     */
    public boolean upload(String localPath, String hdfsPath) {
        InputStream input = null;
        OutputStream output = null;
        try {
            //创建本地文件的输入流
            input = new FileInputStream(localPath);
            //创建HDFS的输出流
            output = getClient().create(new Path(hdfsPath));
            //写文件到HDFS
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            //防止输出数据不完整
            output.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            //关闭输入输出流
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //使用工具类IOUtils上传或下载
        //IOUtils.copy(input, output);

    }

    /**
     * 下载文件到本地
     *
     * @param localPath 本地文件绝对路径。例如"/root/a.txt"
     * @param hdfsPath  hdfs绝对路径。例如"/user/root/dir/a.txt"
     * @return 执行结果
     */
    public boolean download(String localPath, String hdfsPath) {
        OutputStream output = null;
        InputStream input = null;
        try {
            //创建本地文件的输出流
            output = new FileOutputStream(localPath);
            //创建HDFS的输入流
            input = getClient().open(new Path(hdfsPath));
            //写文件到HDFS
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            //防止输出数据不完整
            output.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            //关闭输入输出流
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //使用工具类IOUtils上传或下载
        //IOUtils.copy(input, output);
    }

    /**
     * 判断hdfs文件是否存在
     * @param hdfsPath hdfs绝对路径。例如"/user/root/dir/a.txt"
     * @return 执行结果
     * @throws IOException 异常
     */
    public boolean fileExists(String hdfsPath) throws IOException {
        return getClient().exists(new Path(hdfsPath));
    }

}
