package engineeringwork.pl.kinzil.containers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String MAPSETTINGS_COL_5 = "TYPE";


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
                + TRIP_COL_8 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + SETTINGS_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SETTINGS_COL_1 + " TEXT,"
                + SETTINGS_COL_2 + " REAL,"
                + SETTINGS_COL_3 + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_MAPSETTINGS + "("
                + MAPSETTINGS_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MAPSETTINGS_COL_1 + " TEXT,"
                + MAPSETTINGS_COL_2 + " INTEGER,"
                + MAPSETTINGS_COL_3 + " INTEGER,"
                + MAPSETTINGS_COL_4 + " INTEGER,"
                + MAPSETTINGS_COL_5 + " INTEGER" + ")";
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

    public boolean userInsert(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_0, user.getLogin());
        contentValues.put(USER_COL_1, user.getPassword());
        long result = db.insert(TABLE_USER, null, contentValues);
        return result != -1;
    }

    public User getUser(User user)
    {
        String login;
        String password;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + TABLE_USER  + " where " + USER_COL_0 +" = '" + user.getLogin() + "'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            return null;
        try{
            cursor.moveToNext();
            login = cursor.getString(0);
            password = cursor.getString(1);
        } finally {
            cursor.close();
        }

        return new User(login, password);
    }

    public boolean mapSettingInsert(MapSetting mapSetting)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPSETTINGS_COL_1, mapSetting.getLogin() );
        contentValues.put(MAPSETTINGS_COL_2, mapSetting.isTracking());
        contentValues.put(MAPSETTINGS_COL_3, mapSetting.isSatellite());
        contentValues.put(MAPSETTINGS_COL_4, mapSetting.getZoom());
        contentValues.put(MAPSETTINGS_COL_5, mapSetting.getType());
        long result = db.insert(TABLE_MAPSETTINGS, null, contentValues);
        return result != -1;
    }

    public boolean mapSettingUpdate(MapSetting mapSetting)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MAPSETTINGS_COL_0, mapSetting.getId() );
        contentValues.put(MAPSETTINGS_COL_1, mapSetting.getLogin() );
        contentValues.put(MAPSETTINGS_COL_2, mapSetting.isTracking());
        contentValues.put(MAPSETTINGS_COL_3, mapSetting.isSatellite());
        contentValues.put(MAPSETTINGS_COL_4, mapSetting.getZoom());
        contentValues.put(MAPSETTINGS_COL_5, mapSetting.getType());
        long result = db.update(TABLE_MAPSETTINGS, contentValues, "ID = ?", new String[] { Integer.toString(mapSetting.getId()) } );
        return result != -1;
    }

    public MapSetting getMapSettings(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MapSetting mapSetting = new MapSetting();
        String query = "select * from " + TABLE_MAPSETTINGS  + " where " + MAPSETTINGS_COL_0 +" = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            return null;
        try{
            cursor.moveToNext();
            mapSetting.setId(cursor.getInt(0));
            mapSetting.setLogin(cursor.getString(1));
            if(cursor.getString(2).equals("1"))
                mapSetting.setTracking(true);
            else
                mapSetting.setTracking(false);
            String a = cursor.getString(3);
            if(a.equals("1"))
                mapSetting.setSatellite(true);
            else
                mapSetting.setSatellite(false);
            mapSetting.setZoom(cursor.getInt(4));
            mapSetting.setType(cursor.getInt(5));
        } finally {
            cursor.close();
        }
        return mapSetting;
    }

    public MapSetting getFirstLoginMapSettings(String login)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        MapSetting mapSetting = new MapSetting();
        String query = "select * from " + TABLE_MAPSETTINGS  + " where " + MAPSETTINGS_COL_1 +" = '" + login + "'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() == 0)
            return null;
        try{
            cursor.moveToNext();
            mapSetting.setId(cursor.getInt(0));
            mapSetting.setLogin(cursor.getString(1));
            if(cursor.getString(2).equals("1"))
                mapSetting.setTracking(true);
            else
                mapSetting.setTracking(false);
            String a = cursor.getString(3);
            if(a.equals("1"))
                mapSetting.setSatellite(true);
            else
                mapSetting.setSatellite(false);
            mapSetting.setZoom(cursor.getInt(4));
            mapSetting.setType(cursor.getInt(5));
        } finally {
            cursor.close();
        }
        return mapSetting;
    }



}
