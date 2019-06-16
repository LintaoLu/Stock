package llu25.hawk.iit.stock.adpater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import llu25.hawk.iit.stock.info.StockInfo;

public class UserStockDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user.db";

    private static final String TABLE_STOCK = "user_stock_table";
    private static final String COLUMN_STOCK_SYMBOL = "stock_symbol";
    private static final String COLUMN_COMPANY_NAME = "company_name";
    private static final String TAG = "UserStockDBHandler";

    public UserStockDBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_STOCK + " (" +
                COLUMN_STOCK_SYMBOL + " TEXT primary key, " +
                COLUMN_COMPANY_NAME + " TEXT not null);";
        Log.d(TAG, "onCreate: " + query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(db);
    }

    public ArrayList<StockInfo> loadStockInfo()
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StockInfo> stockInfoes = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_STOCK,
                new String[]{ COLUMN_STOCK_SYMBOL, COLUMN_COMPANY_NAME},
                null,
                null,
                null,
                null,
                null);

        if(cursor != null)
        {
            cursor.moveToFirst();

            for(int i = 0; i < cursor.getCount(); i++)
            {
                String symbol = cursor.getString(0);
                String companyName = cursor.getString(1);
                StockInfo s = new StockInfo(symbol, companyName);
                stockInfoes.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return stockInfoes;
    }

    public void addItem(String symbol, String company)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STOCK_SYMBOL, symbol);
        values.put(COLUMN_COMPANY_NAME, company);
        try {db.insert(TABLE_STOCK, null, values); }
        catch(Exception e) { e.printStackTrace(); }
        db.close();
    }

    public void removeItem(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_STOCK, COLUMN_STOCK_SYMBOL + " = ?", new String[]{name});
        db.close();
    }
}
