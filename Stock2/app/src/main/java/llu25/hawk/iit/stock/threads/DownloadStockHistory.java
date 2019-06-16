package llu25.hawk.iit.stock.threads;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import llu25.hawk.iit.stock.StockDetailActivity;

public class DownloadStockHistory extends AsyncTask<Void, Void, String>
{
    private static String DATA_URL_FIRST = "https://cloud.iexapis.com/stable/stock/";
    private static String DATA_URL_SECOND = "/chart/2y/tops?token=pk_e87bfd7e8784496ab1d843edfec56f23";
    private String DATA_URL;
    private WeakReference<StockDetailActivity> weakReference;

    public DownloadStockHistory(StockDetailActivity stockDetailActivity, String stockSymbol)
    {
        this.weakReference = new WeakReference<>(stockDetailActivity);
        DATA_URL = DATA_URL_FIRST + stockSymbol + DATA_URL_SECOND;
    }

    @Override
    protected void onPostExecute(String s)
    {
        StockDetailActivity stockDetailActivity = weakReference.get();
        if(stockDetailActivity != null)
        {
            stockDetailActivity.updateStockDetailJson(s);
        }
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        Uri dataUri = Uri.parse(DATA_URL);
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
}
