package wang.oo0oo.wordcount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import wang.oo0oo.wordcount.hadoop.HdfsUtil;

import java.util.List;

@SpringBootApplication
@Controller
public class WordCountApplication {

    public static void main(String[] args) {

        //必要的初始化工作
        GlobalConfig.init();
        //初始化hdfs相关设置
        HdfsUtil.init();


        //主程序
        SpringApplication.run(WordCountApplication.class, args);
    }

}
