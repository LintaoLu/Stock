package llu25.hawk.iit.stock.adpater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class AuthenticateDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user.db";

    private static final String TABLE_AUTH = "user_auth_table";
    private static final String COLUMN_EMAIL = "_email";
    private static final String COLUMN_PASSWD = "_password";
    private static final String COLUMN_NAME = "_name";

    private static final String TAG = "AuthenticateDBHandler";

    public AuthenticateDBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_AUTH + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWD + " TEXT ," +
                COLUMN_NAME + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTH);
        onCreate(db);
    }

    public boolean addUser(String email, String passwd, String name)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWD, passwd);
        values.put(COLUMN_NAME, name);
        SQLiteDatabase db = getWritableDatabase();
        boolean result = db.insert(TABLE_AUTH, null, values) > 0;
        db.close();
        return result;
    }

    public String[] searchUser(String email)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select "+COLUMN_PASSWD+", "+COLUMN_NAME+", "+COLUMN_EMAIL+" " +
                "from " + TABLE_AUTH + " " +
                "where " + COLUMN_EMAIL + "=?",
                new String[] { email });
        if(cursor == null) return null;
        cursor.moveToFirst();
        String[] result = new String[3];
        result[0] = cursor.getString(0);
        Log.d(TAG, "===============================searchUser: " + result[0]);
        result[1] = cursor.getString(1);
        Log.d(TAG, "===============================searchUser: " + result[1]);
        result[2] = cursor.getString(2);
        Log.d(TAG, "===============================searchUser: " + result[2]);
        cursor.close();

        return result;
    }
}