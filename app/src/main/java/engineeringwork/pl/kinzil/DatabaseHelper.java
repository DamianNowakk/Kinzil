package engineeringwork.pl.kinzil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {



    private static final String DATABASE_NAME = "Counter.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String USER_COL_0 = "ID";
    private static final String USER_COL_1 = "LOGIN";

    private static final String TABLE_TRIP = "Trip";
    private static final String TRIP_COL_0 = "ID";
    private static final String TRIP_COL_1 = "LOGIN";
    private static final String TRIP_COL_2 = "MAXSPEED";
    private static final String TRIP_COL_3 = "AVGSPEED";
    private static final String TRIP_COL_4 = "TIME";
    private static final String TRIP_COL_5 = "DISTANCE";
    private static final String TRIP_COL_6 = "CALORIES";
    private static final String TRIP_COL_7 = "MAP";

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
                + USER_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_COL_1 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + TABLE_TRIP + "("
                + TRIP_COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TRIP_COL_1 + " TEXT,"
                + TRIP_COL_2 + " REAL,"
                + TRIP_COL_3 + " REAL,"
                + TRIP_COL_4 + " TEXT,"
                + TRIP_COL_5 + " REAL,"
                + TRIP_COL_6 + " INTEGER,"
                + TRIP_COL_7 + " TEXT" + ")";
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


}
