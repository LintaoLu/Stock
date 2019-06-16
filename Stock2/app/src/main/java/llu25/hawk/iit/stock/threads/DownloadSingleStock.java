package llu25.hawk.iit.stock.threads;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import llu25.hawk.iit.stock.SearchActivity;
import llu25.hawk.iit.stock.fragments.FragmentStock;
import llu25.hawk.iit.stock.info.StockInfo;

public class DownloadSingleStock extends AsyncTask<Void, Void, String>
{
    private Object reference;
    private StockInfo stock;
    private boolean insertDatabase, isFromFragment;
    private static final String DATA_URL_1 = "https://api.worldtradingdata.com/api/v1/stock?symbol=";
    private static final String DATA_URL_2 = "&api_token=WbMdSjnYxOApcldqyhM5s5WIcVTwQI5vnNmmRO63WoNxbjuHSzqnXi1kmVgL";
    private static final String TAG = "DownloadSingleStock";

    public DownloadSingleStock(Object ma, StockInfo stock, boolean insertDatabase, boolean isFromFragment)
    {
        reference = ma;
        this.insertDatabase = insertDatabase;
        this.isFromFragment = isFromFragment;
        this.stock = stock;
    }

    @Override
    protected void onPostExecute(String s)
    {
        StockInfo tempStock = parseJason(s);
        if(isFromFragment) ((FragmentStock) reference).updateStockInfoList(tempStock, insertDatabase);
        else ((SearchActivity) reference).setStockInfo(tempStock);
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        String newUrl = DATA_URL_1 + stock.getStockSymbol() + DATA_URL_2;
        Uri dataUri = Uri.parse(newUrl);
        String urlToUse = dataUri.toString();
        StringBuilder sb = new StringBuilder();

        try
        {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null)
                sb.append(line).append("\n");
        }
        catch(Exception e) { return null; }

        return sb.toString();
    }

    private StockInfo parseJason(String s)
    {
        StockInfo tempStock;
        try
        {
            JSONArray jsonArray = new JSONObject(s).getJSONArray("data");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String latestPrice = jsonObject.getString("price");
            String stockChange = jsonObject.getString("day_change");
            String changePercent = jsonObject.getString("change_pct");
            String companyName = jsonObject.getString("name");
            String exchangeCenter = jsonObject.getString("stock_exchange_short");
            String closeYesterday = jsonObject.getString("close_yesterday");
            String priceOpen = jsonObject.getString("price_open");
            String dayHigh = jsonObject.getString("day_high");
            String dayLow = jsonObject.getString("day_low");;
            Double percent = Math.round(Double.parseDouble(changePercent) * 100.0) / 100.0;
            tempStock = new StockInfo(stock.getStockSymbol(), companyName, latestPrice, String.valueOf(percent),
                    exchangeCenter, stockChange, closeYesterday, priceOpen, dayHigh, dayLow);
            return tempStock;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
