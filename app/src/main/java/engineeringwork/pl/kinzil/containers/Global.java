package engineeringwork.pl.kinzil.containers;

import android.app.Application;

public class Global extends Application {

    private static int actualSpeed = 0;

    public static synchronized int getActualSpeed() {
        return actualSpeed;
    }
    public static synchronized void setActualSpeed(int someVariable) {
        actualSpeed = someVariable;
    }

    private static String map;

    public static synchronized String getMap() {
        return map;
    }
    public static synchronized void setMap(String someVariable) {
        map = someVariable;
    }

}
