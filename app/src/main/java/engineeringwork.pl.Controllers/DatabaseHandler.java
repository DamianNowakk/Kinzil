package engineeringwork.pl.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DB_RidesHistory";

    private static final String TABLE_RIDES = "Rides";
    private static final String KEY_ID = "id";
    private static final String KEY_SPEED = "speed";
    private static final String KEY_DATE = "date";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RIDES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SPEED + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        // Create tables again
        onCreate(db);
    }

    public void addRide(Ride ride) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SPEED, ride.speed);
        values.put(KEY_DATE, ride.date.toString());

        // Inserting Row
        db.insert(TABLE_RIDES, null, values);
        db.close(); // Closing database connection
    }

    public int getRidesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RIDES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Integer count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
