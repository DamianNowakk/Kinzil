package engineeringwork.pl.kinzil.containers;

public class MapSetting {
    private int id;
    private String login;
    private boolean isTracking; //mm
    private boolean isSatellite;
    private int zoom;
    private boolean showRoute;
    private boolean showSecondaryRoute;

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

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

    public boolean isSatellite() {
        return isSatellite;
    }

    public void setSatellite(boolean satellite) {
        isSatellite = satellite;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public boolean isShowRoute() {
        return showRoute;
    }

    public void setShowRoute(boolean showRoute) {
        this.showRoute = showRoute;
    }

    public boolean isShowSecondaryRoute() {
        return showSecondaryRoute;
    }

    public void setShowSecondaryRoute(boolean showSecondaryRoute) {
        this.showSecondaryRoute = showSecondaryRoute;
    }


    public MapSetting(int id, String login, boolean isTracking, boolean isSatellite, int zoom, boolean showRoute, boolean showSecondaryRoute) {
        this.id = id;
        this.login = login;
        this.isTracking = isTracking;
        this.isSatellite = isSatellite;
        this.zoom = zoom;
        this.showRoute = showRoute;
        this.showSecondaryRoute = showSecondaryRoute;
    }

    public MapSetting(String login, boolean isTracking, boolean isSatellite, int zoom, boolean showRoute, boolean showSecondaryRoute) {
        this.login = login;
        this.isTracking = isTracking;
        this.isSatellite = isSatellite;
        this.zoom = zoom;
        this.showRoute = showRoute;
        this.showSecondaryRoute = showSecondaryRoute;
    }

    public MapSetting(String login) {
        this.login = login;
    }

    public MapSetting() {
    }
}
