package engineeringwork.pl.Controllers;

import java.util.Date;

public class Ride {
    public Integer id;
    public String speed;
    public Date date;

    public Ride(Integer id, String speed, Date date) {
        this.id = id;
        this.speed = speed;
        this.date = date;
    }

    public Ride(String speed, Date date) {
        this.speed = speed;
        this.date = date;
    }
}
