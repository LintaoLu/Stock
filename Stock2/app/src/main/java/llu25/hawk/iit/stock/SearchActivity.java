package llu25.hawk.iit.stock;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import llu25.hawk.iit.stock.fragments.FragmentStock;
import llu25.hawk.iit.stock.info.StockInfo;
import llu25.hawk.iit.stock.search.SearchItem;
import llu25.hawk.iit.stock.threads.DownloadSingleStock;

public class SearchActivity extends AppCompatActivity
{
    private FloatingSearchView mSearchView;
    private static final int ONLINE = 1, LOCAL = 0;
    private int searchType = LOCAL;
    private List<SearchItem> newSuggestions = new ArrayList<>();
    private HashMap<String, String> hm;
    private List<StockInfo> suggestions;
    private StockInfo stockInfo;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_search);

        String type = getIntent().getStringExtra("SearchType");
        if(type.equals("HashMap"))
        {
            hm = MainActivity.getStockSymbolName();
            searchType = ONLINE;
        }
        else suggestions = FragmentStock.getUserSavedStock();

        mSearchView = findViewById(R.id.floating_search_view);

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                //get suggestions based on newQuery
                if(newQuery != null && !newQuery.equals(""))
                {
                    newSuggestions.clear();
                    if(searchType == ONLINE)
                    {

                        for(String s : hm.keySet())
                        {
                            if(s.contains(newQuery.toUpperCase()))
                                newSuggestions.add(new SearchItem(s, hm.get(s)));
                        }
                    }
                    else
                    {
                        for (StockInfo stockInfo : suggestions)
                        {
                            if(stockInfo.getStockSymbol().contains(newQuery.toUpperCase()))
                                newSuggestions.add(new SearchItem(stockInfo.getStockSymbol(), stockInfo.getCompanyName()));
                        }
                    }
                    //pass them on to the search view
                    mSearchView.swapSuggestions(newSuggestions);
                }
            }
        });

        mSearchView.setOnHomeActionClickListener(
                new FloatingSearchView.OnHomeActionClickListener() {
                    @Override
                    public void onHomeClicked() {

                    }
                });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if(item.getItemId() == R.id.float_search)
                {
                    String newQuery = mSearchView.getQuery();
                    if(newQuery != null && !newQuery.equals(""))
                    {
                        newSuggestions.clear();
                        if(searchType == ONLINE)
                        {
                            String companyName = hm.get(newQuery.toUpperCase());
                            if (companyName != null)
                                newSuggestions.add(new SearchItem(newQuery.toUpperCase(),
                                        companyName));
                            else
                                Toast.makeText(SearchActivity.this, "No such stock. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            for (StockInfo stockInfo : suggestions)
                            {
                                if(stockInfo.getStockSymbol().equals(newQuery.toUpperCase()))
                                    newSuggestions.add(new SearchItem(stockInfo.getStockSymbol(),
                                            stockInfo.getCompanyName()));
                                break;
                            }
                            if(newSuggestions.size() == 0)
                                Toast.makeText(SearchActivity.this, "No such stock. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        mSearchView.swapSuggestions(newSuggestions);
                    }
                }
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback()
        {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition)
            {
                String info = textView.getText().toString();
                int counter = 0;
                StringBuilder symbol = new StringBuilder();
                for(int i = 0; i < info.length(); i++)
                {
                    counter++;
                    symbol.append(info.charAt(i));
                    if(info.charAt(i) == '\n') break;
                }
                symbol.setLength(symbol.length()-1);
                SpannableString ss1=  new SpannableString(info);
                ss1.setSpan(new RelativeSizeSpan(1f), 0, counter-1, 0); // set size
                ss1.setSpan(new ForegroundColorSpan(getColor(R.color.black)), 0, counter, 0);// set color
                ss1.setSpan(new RelativeSizeSpan(0.8f), counter, info.length(), 0); // set size
                textView.setText(ss1);

                textView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        new DownloadSingleStock(SearchActivity.this,
                                new StockInfo(symbol.toString(), ""),
                                false, false).execute();
                        hideKeyboard(SearchActivity.this);
                    }
                });
            }
        });
    }

    public void setStockInfo(StockInfo s)
    {
        stockInfo = s;
        Intent intent = new Intent(getBaseContext(), StockDetailActivity.class);
        intent.putExtra("stock", stockInfo);
        startActivity(intent);
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
