package engineeringwork.pl.kinzil.containers;

public class MapSetting {
    private int id;
    private String login;
    private boolean isTracking; //mm
    private boolean isSatellite;
    private int zoom;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public boolean isSatellite() {
        return isSatellite;
    }

    public void setSatellite(boolean satellite) {
        isSatellite = satellite;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public MapSetting(int id, String login, boolean isTracking, boolean isSatellite, int zoom, int type) {
        this.id = id;
        this.login = login;
        this.isTracking = isTracking;
        this.isSatellite = isSatellite;
        this.zoom = zoom;
        this.type = type;
    }

    public MapSetting(String login, boolean isTracking, boolean isSatellite, int zoom, int type) {
        this.login = login;
        this.isTracking = isTracking;
        this.isSatellite = isSatellite;
        this.zoom = zoom;
        this.type = type;
    }

    public MapSetting(String login) {
        this.login = login;
    }

    public MapSetting() {
    }
}
