package wang.oo0oo.wordcount.dao;

import wang.oo0oo.wordcount.pojo.Task;
import wang.oo0oo.wordcount.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDAO {

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from common_task";
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

    public void add(Task task) {

        String sql = "insert into common_task values(null,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            ps.setString(1, task.getCommitUserName());
            ps.setInt(2, task.getCommitTaskId());
            ps.setTimestamp(3, task.getCommitTime());
            ps.setTimestamp(4, task.getFinishTime());
            ps.setString(5, task.getTaskStatus());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                task.setId(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void update(Task task) {

        String sql = "update common_task set commit_user_name= ?, commit_task_id= ?, commit_time= ?, finish_time= ?, task_status= ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, task.getCommitUserName());
            ps.setInt(2, task.getCommitTaskId());
            ps.setTimestamp(3, task.getCommitTime());
            ps.setTimestamp(4, task.getFinishTime());
            ps.setString(5, task.getTaskStatus());
            ps.setInt(6, task.getId());

            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public void delete(Integer id) {

        String sql = "delete from common_task where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setInt(1, id);

            ps.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public Task get(String userName, Integer taskId) {
        Task task = null;

        String sql = "select * from common_task where commit_user_name = ? and commit_task_id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, userName);
            ps.setInt(2, taskId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Integer id = rs.getInt(1);
                String commitUserName = rs.getString(2);
                Integer commitTaskId = rs.getInt(3);
                Timestamp commitTime = rs.getTimestamp(4);
                Timestamp finishTime = rs.getTimestamp(5);
                String taskStatus = rs.getString(6);
                task = new Task(id, commitUserName, commitTaskId, commitTime, finishTime, taskStatus);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return task;
    }

    public List<Task> listAll() {
        List<Task> tasks = new ArrayList<>();

        String sql = "select * from common_task";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt(1);
                String commitUserName = rs.getString(2);
                Integer commitTaskId = rs.getInt(3);
                Timestamp commitTime = rs.getTimestamp(4);
                Timestamp finishTime = rs.getTimestamp(5);
                String taskStatus = rs.getString(6);
                Task task = new Task(id, commitUserName, commitTaskId, commitTime, finishTime, taskStatus);
                tasks.add(task);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return tasks;
    }

    public Map<String, Integer> getNumberOfSubmittedTasksList() {
        Map<String, Integer> numberOfSubmittedTasksList = new HashMap<>();

        String sql = "select commit_user_name, MAX(commit_task_id) from common_task group by commit_user_name";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String commitUserName = rs.getString(1);
                Integer commitTaskId = rs.getInt(2);
                numberOfSubmittedTasksList.put(commitUserName, commitTaskId);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return numberOfSubmittedTasksList;
    }

    public static void main(String[] args) {
        TaskDAO taskDAO = new TaskDAO();
        Task task = new Task();
        task.setCommitUserName("测试者");
        task.setCommitTaskId(1);
        task.setCommitTime(new Timestamp(System.currentTimeMillis()));
        task.setFinishTime(new Timestamp(System.currentTimeMillis()));
        taskDAO.add(task);
    }
}
