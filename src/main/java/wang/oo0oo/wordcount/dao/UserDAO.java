package wang.oo0oo.wordcount.dao;

import wang.oo0oo.wordcount.pojo.User;
import wang.oo0oo.wordcount.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from common_user";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {



            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt(1);
            }


        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public void add(User user) {

        String sql = "insert into common_user values(null,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getSalt());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void addWithoutSalt(User user) {

        String sql = "insert into common_user values(null,?,?,null)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void update(User user) {

        String sql = "update common_user set password= ?, salt= ? where user_name = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, user.getPassword());
            ps.setString(2, user.getSalt());
            ps.setString(3, user.getUserName());

            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public void delete(String userName) {

        String sql = "delete from common_user where user_name = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, userName);

            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public User get(String userName) {
        User user = null;

        String sql = "select * from common_user where user_name = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Integer id = rs.getInt(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                String salt = rs.getString(4);
                user = new User(id, name, password, salt);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return user;
    }

    public List<User> listAll() {
        List<User> users = new ArrayList<>();

        String sql = "select * from common_user";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                String salt = rs.getString(4);
                User user = new User(id, name, password, salt);
                users.add(user);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return users;
    }

    /**
     * 测试用
     * @param args
     */
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        User test = new User();
        test.setUserName("李四");
        test.setPassword("abc001");
        userDAO.addWithoutSalt(test);
        List<User> userList = userDAO.listAll();
        for (User u : userList) {
            System.out.println(u);
        }
    }

}