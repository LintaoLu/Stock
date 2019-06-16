package llu25.hawk.iit.stock.threads;

import android.net.Uri;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import llu25.hawk.iit.stock.MainActivity;

public class DownloadStockName extends AsyncTask<Void, Void, String>
{
    private static final String DATA_URL = "https://api.iextrading.com/1.0/ref-data/symbols";
    private WeakReference<MainActivity> weakReference;

    public DownloadStockName(MainActivity mainActivity)
    {
        this.weakReference = new WeakReference<>(mainActivity);
    }

    @Override
    protected void onPostExecute(String s)
    {
        MainActivity mainActivity = weakReference.get();
        if(mainActivity != null)
        {
            HashMap<String, String> temp = parseJason(s);
            mainActivity.updateStockName(temp);
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

    private HashMap<String, String> parseJason(String s)
    {
        HashMap<String, String> hm = new HashMap<>();

        try
        {
            JSONArray jObjMain = new JSONArray(s);
            for(int i = 0; i < jObjMain.length(); i++)
            {
                JSONObject jStockInfo = (JSONObject) jObjMain.get(i);
                String symbol = jStockInfo.getString("symbol");
                String company = jStockInfo.getString("name");
                hm.put(symbol, company);
            }
            return hm;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
