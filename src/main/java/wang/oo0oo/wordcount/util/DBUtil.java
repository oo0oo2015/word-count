package wang.oo0oo.wordcount.util;
  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String DATABASE = "hadoop";
    private static final String ENCODING = "UTF-8";
    private static final String LOGIN_NAME = "root";
    private static final String PASSWORD = "admin";
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
  
    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?userSSL=true&useUnicode=true&characterEncoding=%s&serverTimezone=GMT%%2B8", IP, PORT, DATABASE, ENCODING);
        return DriverManager.getConnection(url, LOGIN_NAME, PASSWORD);
    }

    public static void main(String[] args) {

    }
}