package llu25.hawk.iit.stock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_PASS = "user_info";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PASSWD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String TAG = "MyDBHandler";

    public MyDBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_PASS + " (" +
                COLUMN_ID + " VARCHAR PRIMARY KEY, " +
                COLUMN_PASSWD + " VARCHAR ," +
                COLUMN_NAME + " VARCHAR " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASS);
        onCreate(db);
    }

    public boolean addUser(String id, String passwd, String name)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_PASSWD, passwd);
        values.put(COLUMN_NAME, name);
        SQLiteDatabase db = getWritableDatabase();
        boolean result = db.insert(TABLE_PASS, null, values) > 0;
        db.close();
        return result;
    }

    public boolean deleteUser(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        boolean result = db.delete(TABLE_PASS, COLUMN_ID + "=" + name, null) > 0;
        db.close();
        return result;
    }

    public String[] searchUser(String userId)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_PASS,
                new String[]{ COLUMN_PASSWD, COLUMN_NAME },
                null,
                null,
                null,
                null,
                null);

        if(cursor == null) return null;
        cursor.moveToFirst();
        Log.d(TAG, "searchUser: " + cursor.getString(0));
        String[] result = new String[2];
        result[0] = cursor.getString(0);
        result[1] = cursor.getString(1);
        cursor.close();
        return result;
    }
}