package engineeringwork.pl.kinzil.containers;

public class Trip {
    private int id;
    private String login;
    private double maxSpeed;
    private double avgSpeed;
    private double distance;
    private int calories;
    private String time;
    private String date;
    private String map;
    private String rideTime;
    private double avgSpeedNoStop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMap() { return map; }

    public double getAvgSpeedNoStop() {
        return avgSpeedNoStop;
    }

    public void setAvgSpeedNoStop(double avgSpeedNoStop) {
        this.avgSpeedNoStop = avgSpeedNoStop;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Trip(int id, String login, double maxSpeed, double avgSpeed, double distance, int calories, String time, String date, String map, String rideTime, double avgSpeedNoStop) {
        this.id = id;
        this.login = login;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
        this.distance = distance;
        this.calories = calories;
        this.time = time;
        this.date = date;
        this.map = map;
        this.rideTime = rideTime;
        this.avgSpeedNoStop = avgSpeedNoStop;
    }

    public Trip(int id, String login, double maxSpeed, double avgSpeed,
                double distance, int calories, String time, String date, String map)
    {
        this.id = id;
        this.login = login;
        this.maxSpeed = maxSpeed;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
        this.distance = distance;
        this.calories = calories;
        this.time = time;
        this.date = date;
        this.map = map;
    }

    public Trip(String login, String date)
    {
        this.login = login;
        this.date = date;
    }

    public Trip()
    {

    }
}
