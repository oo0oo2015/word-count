package wang.oo0oo.wordcount.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;

public class WordCount {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {

            byte[] bt = value.getBytes();
            InputStream ip = new ByteArrayInputStream(bt);
            Reader read = new InputStreamReader(ip);
            IKSegmenter iks = new IKSegmenter(read, true);
            Lexeme t;
            while ((t = iks.next()) != null) {
                word.set(t.getLexemeText());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static boolean doWordCountJob(String[] args) throws Exception {

//        //注意：这里直接覆盖了程序的输入参数列表，后期如有需要记得修改
//        args = new String[2];
//        //输入目录（待统计文件目录）
//        args[0] = "hdfs://bigdata:9000/user/joe/wordcount/input";
//        //输出目录（统计结果输出目录）
//        args[1] = "hdfs://bigdata:9000/user/joe/wordcount/output";

        System.out.println("args[0]：" + args[0]);
        System.out.println("args[1]：" + args[1]);

        //设置本地（Windows）的HADOOP根目录【远程调试】
        System.setProperty("HADOOP_HOME", "E:\\hadoop-2.7.7");
        //指定hadoop用户名
        System.setProperty("HADOOP_USER_NAME", "root");
        //设置本地（Windows）的HADOOP家目录【远程调试】
        System.setProperty("hadoop.home.dir", "E:\\hadoop-2.7.7");

        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: wordcount <in> <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCount.TokenizerMapper.class);
        job.setCombinerClass(WordCount.IntSumReducer.class);
        job.setReducerClass(WordCount.IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        return job.waitForCompletion(true);
    }
}
