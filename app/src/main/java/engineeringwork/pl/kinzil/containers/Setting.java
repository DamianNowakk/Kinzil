package engineeringwork.pl.kinzil.containers;

public class Setting {
    private int id;
    private String login;
    private int wheelSize; //mm
    private int weight;

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

    public int getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.wheelSize = wheelSize;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Setting(int id, String login, int wheelSize, int weight) {
        this.id = id;
        this.login = login;
        this.wheelSize = wheelSize;
        this.weight = weight;
    }

    public Setting(String login, int wheelSize, int weight) {
        this.login = login;
        this.wheelSize = wheelSize;
        this.weight = weight;
    }

    public Setting(String login) {
        this.login = login;
    }

    public Setting() { }
}
