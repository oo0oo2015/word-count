package wang.oo0oo.wordcount.service;

import wang.oo0oo.wordcount.GlobalConfig;
import wang.oo0oo.wordcount.dao.TaskDAO;
import wang.oo0oo.wordcount.hadoop.HdfsUtil;
import wang.oo0oo.wordcount.hadoop.WordCount;
import wang.oo0oo.wordcount.pojo.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskService {


    public static final String HADOOP_IP = "bigdata";
    public static final String HDFS_PORT = "9000";
    public static final String HDFS_PROJECT_DIRECTORY_PATH = "/user/root/word_count_files";

    private HdfsUtil hdfsUtil = new HdfsUtil();
    private TaskDAO taskDAO = new TaskDAO();

    private Map<String, Integer> resultMap = new LinkedHashMap<>();

    /**
     * 【核心】
     */
    public Map<String, Integer> createWordCountJob(File folder) {
        if (folder == null || GlobalConfig.loginUser == null || !folder.exists() || !folder.isDirectory()) {
            resultMap.put("系统或参数出错", 0);
            return resultMap;
        }
        Integer taskId = GlobalConfig.numberOfSubmittedTasksList.get(GlobalConfig.loginUser.getUserName()) + 1;

        //这个是任务对象，等会儿写数据库用的
        Task task = new Task();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        task.setCommitUserName(GlobalConfig.loginUser.getUserName());
        task.setCommitTaskId(taskId);
        task.setCommitTime(new Timestamp(System.currentTimeMillis()));

        //创建hdfs文件夹
        String hdfsUserHomePath = String.format("hdfs://%s:%s/%s/%s",
                HADOOP_IP, HDFS_PORT, HDFS_PROJECT_DIRECTORY_PATH, GlobalConfig.loginUser.getUserName());
        String hdfsUserTaskPath = String.format("%s/%s", hdfsUserHomePath, taskId);
        try {
            if (!hdfsUtil.fileExists(hdfsUserTaskPath)) {
                if (!hdfsUtil.mkdir(hdfsUserTaskPath)) {
                    System.out.println("[HDFS]警告：创建文件夹 " + hdfsUserTaskPath + " 失败");
                    task.setTaskStatus("创建HDFS任务文件夹失败");
                    taskDAO.add(task);
                    resultMap.put("创建HDFS任务文件夹失败", 0);
                    return resultMap;
                }
            }
        } catch (IOException e) {
            System.out.println("[HDFS]警告：HDFS出错！");
            task.setTaskStatus("HDFS出错");
            taskDAO.add(task);
            resultMap.put("HDFS出错", 0);
            return resultMap;
        }


        //遍历目录中的文件
        String[] names = folder.list();
        String folderPath = folder.getAbsolutePath();
        for (String name : Objects.requireNonNull(names)) {
            String hdfsFilePath = String.format("%s/%s", hdfsUserTaskPath, name);
            //传文件上去
            boolean uploadStatus = hdfsUtil.upload(String.format("%s/%s", folder.getAbsolutePath(), name), hdfsFilePath);
            if (!uploadStatus) {
                resultMap.put("HDFS文件上传失败", 0);
                return resultMap;
            }
        }
        //创建wordcount任务
        String[] args = new String[2];
        args[0] = hdfsUserTaskPath;
        args[1] = hdfsUserTaskPath + "/output";
        //等待任务结束
        boolean taskStatus = false;
        try {
            taskStatus = WordCount.doWordCountJob(args);
        } catch (Exception e) {
            System.out.println("[MapReduce]任务执行异常！");
            task.setTaskStatus("MapReduce任务执行异常");
            taskDAO.add(task);
        }
        //获取任务结果
        if (taskStatus) {
            System.out.println("[Service]任务执行成功");
        } else {
            System.out.println("[MapReduce]任务执行失败！");
            task.setTaskStatus("MapReduce任务执行失败");
            taskDAO.add(task);
            resultMap.put("MapReduce任务执行失败", 0);
            return resultMap;
        }

        //TODO:输出结果(现在只能输出part-r-0000)
        String localOutputDirectoryPath = String.format("%s/output", folder.getAbsolutePath());
        File localOutputDirectory = new File(localOutputDirectoryPath);
        localOutputDirectory.mkdir();

        String localOutputFilePath = String.format("%s/part-r-00000.txt", localOutputDirectoryPath);
        String hdfsOutputFilePath = String.format("%s/output/part-r-00000", hdfsUserTaskPath);
        if (hdfsUtil.download(localOutputFilePath, hdfsOutputFilePath)) {
            File localOutputFile = new File(localOutputFilePath);
            try (FileReader reader = new FileReader(localOutputFile);
                 BufferedReader br = new BufferedReader(reader)
            ) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!"".equals(line)) {
                        String[] splitResultLine = line.split("\t");
                        resultMap.put(splitResultLine[0], Integer.parseInt(splitResultLine[1]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            task.setFinishTime(new Timestamp(System.currentTimeMillis()));
            task.setTaskStatus("FINISHED");
            taskDAO.add(task);

            hdfsUtil.closeClient();
            GlobalConfig.numberOfSubmittedTasksList.put(GlobalConfig.loginUser.getUserName(), taskId + 1);

            return resultMap;
        }




        resultMap.put("未获取到输出信息", 0);
        return resultMap;
    }
}
