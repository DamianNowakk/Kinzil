package engineeringwork.pl.kinzil.containers;

public class User {

    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
}
