package wang.oo0oo.wordcount;

import wang.oo0oo.wordcount.dao.TaskDAO;
import wang.oo0oo.wordcount.dao.UserDAO;
import wang.oo0oo.wordcount.pojo.User;

import java.util.List;
import java.util.Map;

public class GlobalConfig {





    public static User loginUser = null;

    public static List<User> userList = null;

    public static Map<String, Integer> numberOfSubmittedTasksList = null;

    public static void init() {
        UserDAO userDAO = new UserDAO();
        TaskDAO taskDAO = new TaskDAO();

        userList = userDAO.listAll();
        numberOfSubmittedTasksList = taskDAO.getNumberOfSubmittedTasksList();
    }

}
