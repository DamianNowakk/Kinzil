package engineeringwork.pl.kinzil.containers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Counter.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String USER_COL_0 = "LOGIN";
    private static final String USER_COL_1 = "PASSWORD";

    private static final String TABLE_TRIP = "Trip";
    private static final String TRIP_COL_0 = "ID";
    private static final String TRIP_COL_1 = "LOGIN";
    private static final String TRIP_COL_2 = "MAXSPEED";
    private static final String TRIP_COL_3 = "AVGSPEED";
    private static final String TRIP_COL_4 = "DISTANCE";
    private static final String TRIP_COL_5 = "CALORIES";
    private static final String TRIP_COL_6 = "TIME";
    private static final String TRIP_COL_7 = "DATE";
    private static final String TRIP_COL_8 = "MAP";
    private static final String TRIP_COL_9 = "RIDETIME";
    private static final String TRIP_COL_10 = "AVGSPEEDNOSTOP";

    private static final String TABLE_SETTINGS = "Settings";
    private static final String SETTINGS_COL_0 = "ID";
    private static final String SETTINGS_COL_1 = "LOGIN";
    private static final String SETTINGS_COL_2 = "WHEELSIZE";
    private static final String SETTINGS_COL_3 = "WEIGHT";

    private static final String TABLE_MAPSETTINGS = "MapSettings";
    private static final String MAPSETTINGS_COL_0 = "ID";
    private static final String MAPSETTINGS_COL_1 = "LOGIN";
    private static final String MAPSETTINGS_COL_2 = "TRACKING";
    private static final String MAPSETTINGS_COL_3 = "SATELLITE";
    private static final String MAPSETTINGS_COL_4 = "ZOOM";
    private static final String MAPSETTINGS_COL_5 = "ROUTE";
    private static final String MAPSETTINGS_COL_6 = "SECONADRYROUTE";


    //singleton
    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + USER_COL_0 + " TEXT PRIMARY KEY,"
                + USER_COL_1 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_TRIP + "("
                + TRIP_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TRIP_COL_1 + " TEXT,"
                + TRIP_COL_2 + " REAL,"
                + TRIP_COL_3 + " REAL,"
                + TRIP_COL_4 + " REAL,"
                + TRIP_COL_5 + " INTEGER,"
                + TRIP_COL_6 + " TEXT,"
                + TRIP_COL_7 + " TEXT,"
                + TRIP_COL_8 + " TEXT,"
                + TRIP_COL_9 + " TEXT,"
                + TRIP_COL_10 + " REAL" + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + SETTINGS_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SETTINGS_COL_1 + " TEXT,"
                + SETTINGS_COL_2 + " INTEGER,"
                + SETTINGS_COL_3 + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_MAPSETTINGS + "("
                + MAPSETTINGS_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MAPSETTINGS_COL_1 + " TEXT,"
                + MAPSETTINGS_COL_2 + " INTEGER,"
                + MAPSETTINGS_COL_3 + " INTEGER,"
                + MAPSETTINGS_COL_4 + " INTEGER,"
                + MAPSETTINGS_COL_5 + " INTEGER,"
                + MAPSETTINGS_COL_6 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAPSETTINGS);
        onCreate(db);
    }

    public void deleteDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 1);
    }

    public boolean userInsert(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_0, user.getLogin());
        contentValues.put(USER_COL_1, user.getPassword());
        long result = db.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }

    public User getUser(String login) {
        User user = new User();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_USER + " where " + USER_COL_0 + " = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToNext();
            user.setLogin(cursor.getString(0)) ;
            user.setPassword(cursor.getString(1));
        } finally {
            cursor.close();
        }

        return user;
    }

    public boolean mapSettingInsert(MapSetting mapSetting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPSETTINGS_COL_1, mapSetting.getLogin());
        contentValues.put(MAPSETTINGS_COL_2, mapSetting.isTracking());
        contentValues.put(MAPSETTINGS_COL_3, mapSetting.isSatellite());
        contentValues.put(MAPSETTINGS_COL_4, mapSetting.getZoom());
        contentValues.put(MAPSETTINGS_COL_5, mapSetting.isShowRoute());
        contentValues.put(MAPSETTINGS_COL_6, mapSetting.isShowSecondaryRoute());
        long result = db.insert(TABLE_MAPSETTINGS, null, contentValues);
        return result != -1;
    }

    public boolean mapSettingUpdate(MapSetting mapSetting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPSETTINGS_COL_0, mapSetting.getId());
        contentValues.put(MAPSETTINGS_COL_1, mapSetting.getLogin());
        contentValues.put(MAPSETTINGS_COL_2, mapSetting.isTracking());
        contentValues.put(MAPSETTINGS_COL_3, mapSetting.isSatellite());
        contentValues.put(MAPSETTINGS_COL_4, mapSetting.getZoom());
        contentValues.put(MAPSETTINGS_COL_5, mapSetting.isShowRoute());
        contentValues.put(MAPSETTINGS_COL_6, mapSetting.isShowSecondaryRoute());
        long result = db.update(TABLE_MAPSETTINGS, contentValues, "ID = ?", new String[]{Integer.toString(mapSetting.getId())});
        return result != -1;
    }

    public MapSetting getMapSettings(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        MapSetting mapSetting = new MapSetting();
        String query = "select * from " + TABLE_MAPSETTINGS + " where " + MAPSETTINGS_COL_0 + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToNext();
            mapSetting.setId(cursor.getInt(0));
            mapSetting.setLogin(cursor.getString(1));
            if (cursor.getString(2).equals("1"))
                mapSetting.setTracking(true);
            else
                mapSetting.setTracking(false);
            if (cursor.getString(3).equals("1"))
                mapSetting.setSatellite(true);
            else
                mapSetting.setSatellite(false);
            mapSetting.setZoom(cursor.getInt(4));
            if (cursor.getString(5).equals("1"))
                mapSetting.setShowRoute(true);
            else
                mapSetting.setShowRoute(false);
            if (cursor.getString(6).equals("1"))
                mapSetting.setShowSecondaryRoute(true);
            else
                mapSetting.setShowSecondaryRoute(false);

        } finally {
            cursor.close();
        }
        return mapSetting;
    }

    public MapSetting getFirstLoginMapSettings(String login) {
        SQLiteDatabase db = this.getWritableDatabase();
        MapSetting mapSetting = new MapSetting();
        String query = "select * from " + TABLE_MAPSETTINGS + " where " + MAPSETTINGS_COL_1 + " = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToNext();
            mapSetting.setId(cursor.getInt(0));
            mapSetting.setLogin(cursor.getString(1));
            if (cursor.getString(2).equals("1"))
                mapSetting.setTracking(true);
            else
                mapSetting.setTracking(false);
            String a = cursor.getString(3);
            if (a.equals("1"))
                mapSetting.setSatellite(true);
            else
                mapSetting.setSatellite(false);
            mapSetting.setZoom(cursor.getInt(4));
            if (cursor.getString(5).equals("1"))
                mapSetting.setShowRoute(true);
            else
                mapSetting.setShowRoute(false);
            if (cursor.getString(6).equals("1"))
                mapSetting.setShowSecondaryRoute(true);
            else
                mapSetting.setShowSecondaryRoute(false);
        } finally {
            cursor.close();
        }
        return mapSetting;
    }

    public boolean settingInsert(Setting setting)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SETTINGS_COL_1, setting.getLogin());
        contentValues.put(SETTINGS_COL_2, setting.getWheelSize());
        contentValues.put(SETTINGS_COL_3, setting.getWeight());
        long result = db.insert(TABLE_SETTINGS, null, contentValues);
        return result != -1;
    }

    public boolean settingUpdate(Setting setting)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SETTINGS_COL_0, setting.getId());
        contentValues.put(SETTINGS_COL_1, setting.getLogin());
        contentValues.put(SETTINGS_COL_2, setting.getWheelSize());
        contentValues.put(SETTINGS_COL_3, setting.getWeight());
        long result = db.update(TABLE_SETTINGS, contentValues, "ID = ?", new String[]{Integer.toString(setting.getId())});
        return result != -1;
    }

    public Setting getFirstLoginSetting(String login)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Setting setting = new Setting();
        String query = "select * from " + TABLE_SETTINGS + " where " + SETTINGS_COL_1 + " = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            cursor.moveToNext();
            setting.setId(cursor.getInt(0));
            setting.setLogin(cursor.getString(1));
            setting.setWheelSize(cursor.getInt(2));
            setting.setWeight(cursor.getInt(3));
        } finally {
            cursor.close();
        }
        return setting;
    }

    public boolean tripInsert(Trip trip) {  // trzeba przetestowac
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRIP_COL_1, trip.getLogin());
        contentValues.put(TRIP_COL_2, trip.getMaxSpeed());
        contentValues.put(TRIP_COL_3, trip.getDistance());
        contentValues.put(TRIP_COL_4, trip.getAvgSpeed());
        contentValues.put(TRIP_COL_5, trip.getCalories());
        contentValues.put(TRIP_COL_6, trip.getTime());
        contentValues.put(TRIP_COL_7, trip.getDate());
        contentValues.put(TRIP_COL_8, trip.getMap());
        contentValues.put(TRIP_COL_9, trip.getRideTime());
        contentValues.put(TRIP_COL_10, trip.getAvgSpeedNoStop());
        long result = db.insert(TABLE_TRIP, null, contentValues);
        return result != -1;
    }

    public ArrayList<Trip> getTrip(String login) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Trip> tripList = new ArrayList<>();
        String query = "select * from " + TABLE_TRIP + " where " + TRIP_COL_1 + " = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            return null;
        try {
            for(int i=0; i < cursor.getCount(); i++) {
                Trip trip = new Trip();
                cursor.moveToNext();
                trip.setId(cursor.getInt(0));
                trip.setLogin(cursor.getString(1));
                trip.setMaxSpeed(cursor.getFloat(2));
                trip.setDistance(cursor.getFloat(3));
                trip.setAvgSpeed(cursor.getFloat(4));
                trip.setCalories(cursor.getInt(5));
                trip.setTime(cursor.getString(6));
                trip.setDate(cursor.getString(7));
                trip.setMap(cursor.getString(8));
                trip.setRideTime(cursor.getString(9));
                trip.setAvgSpeedNoStop(cursor.getFloat(10));
                tripList.add(trip);
            }
        } finally {
            cursor.close();
        }
        return tripList;
    }

    public boolean deleteTrip(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_TRIP, "ID = ?", new String[]{Integer.toString(id)});
        return result != -1;
    }

    public void deleteAllTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_TRIP);
    }


}
