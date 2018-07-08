package todo.app.taskmonitor;

public class users {
    private String name;
    private String date;
    private String location;
    private String task_name;
    private String url;
    private String time;
    public users()
    {

    }

    public users(String name, String date, String location, String task_name, String url, String time) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.task_name = task_name;
        this.url = url;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
