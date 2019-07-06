package wang.oo0oo.wordcount.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import wang.oo0oo.wordcount.hadoop.HdfsUtil;
import wang.oo0oo.wordcount.hadoop.WordCount;
import wang.oo0oo.wordcount.GlobalConfig;
import wang.oo0oo.wordcount.WebMvcConfig;
import wang.oo0oo.wordcount.service.TaskService;
import wang.oo0oo.wordcount.util.FileUtil;
import wang.oo0oo.wordcount.util.JsonUtil;

@Controller
@RequestMapping("/book")
public class BookController {

    private Map<String, Integer> result = null;

    @RequestMapping("/upload")
    public void upload(@RequestParam("cover") MultipartFile uploadFile,
                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        byte[] content = uploadFile.getBytes();

        //保存文件到具体目录，此处为E:/book/upload + 用户名 + 用户任务id
        Integer id = GlobalConfig.numberOfSubmittedTasksList.get(GlobalConfig.loginUser.getUserName());
        String subDirectoryPath = String.format("/%s/%s", GlobalConfig.loginUser.getUserName(), (id + 1));
        String path = WebMvcConfig.FILE_DIR + subDirectoryPath;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //获取文件后缀
        String fileSuffix = FileUtil.getSuffix(uploadFile.getOriginalFilename());
        //后缀合法性检测
        if (!isLegalSuffix(fileSuffix)) {
            return;
        }

        //设置文件名
        File file = new File(folder.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + fileSuffix);
        file.createNewFile();
        FileUtil.writeFile(file, content);

        //写到服务器文件（算了算了，也不一定要写）
//        response.getWriter().write("/upload/" + file.getName());

        TaskService taskService = new TaskService();
        result = taskService.createWordCountJob(folder);
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("[FINISH]任务完成！");
    }

    @RequestMapping("/get_result")
    public void getResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtil.toJson(true, result));
    }

    //文件后缀名合法性检测（使用正则表达式）
    private static final Pattern FILE_SUFFIX_PATTERN = Pattern.compile(
            ".*(.txt|.htm|.html|.xml|.asp|.bat|.c|.cpp|.java|.bas|.prg|.cmd|.log)$");

    private boolean isLegalSuffix(String suffix) {
        Matcher matcher = FILE_SUFFIX_PATTERN.matcher(suffix);
        return matcher.matches();
    }
}
