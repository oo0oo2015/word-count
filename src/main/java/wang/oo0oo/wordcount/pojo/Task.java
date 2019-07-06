package wang.oo0oo.wordcount.pojo;

import java.sql.Timestamp;
import java.util.Date;

public class Task {

    private Integer id;
    private String commitUserName;
    private Integer commitTaskId;
    private Timestamp commitTime;
    private Timestamp finishTime;
    private String taskStatus;

    public Task() {
    }

    public Task(Integer id, String commitUserName, Integer commitTaskId, Timestamp commitTime, Timestamp finishTime, String taskStatus) {
        this.id = id;
        this.commitUserName = commitUserName;
        this.commitTaskId = commitTaskId;
        this.commitTime = commitTime;
        this.finishTime = finishTime;
        this.taskStatus = taskStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommitUserName() {
        return commitUserName;
    }

    public void setCommitUserName(String commitUserName) {
        this.commitUserName = commitUserName;
    }

    public Integer getCommitTaskId() {
        return commitTaskId;
    }

    public void setCommitTaskId(Integer commitTaskId) {
        this.commitTaskId = commitTaskId;
    }

    public Timestamp getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Timestamp commitTime) {
        this.commitTime = commitTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", commitUserName='" + commitUserName + '\'' +
                ", commitTaskId=" + commitTaskId +
                ", commitTime=" + commitTime +
                ", finishTime=" + finishTime +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}
