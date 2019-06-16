package llu25.hawk.iit.stock;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.HashMap;
import llu25.hawk.iit.stock.adpater.ViewPagerAdapter;
import llu25.hawk.iit.stock.fragments.FragmentNews;
import llu25.hawk.iit.stock.fragments.FragmentStock;
import llu25.hawk.iit.stock.fragments.FragmentVirtualExchange;
import llu25.hawk.iit.stock.threads.DownloadStockName;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static HashMap<String, String> stockSymbolName = new HashMap<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        mDrawerLayout = findViewById(R.id.main_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        try { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        catch (Exception e) {
            Toast.makeText(this, "ActionBarDrawerToggle failed", Toast.LENGTH_SHORT).show();
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        //add fragments
        viewPagerAdapter.addFragment(new FragmentStock(), "stock");
        viewPagerAdapter.addFragment(new FragmentNews(), "news");
        viewPagerAdapter.addFragment(new FragmentVirtualExchange(), "trade");
        //adapter set up
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //Download all stock symbols and companies' names.
        new DownloadStockName(this).execute();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        String user_name = getIntent().getStringExtra("userName");
        String user_email = getIntent().getStringExtra("userEmail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item)) return true;

        switch (item.getItemId())
        {
            case R.id.main_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("SearchType", "Local");
                startActivity(intent);
                break;
        }
        return true;
    }

    public void updateStockName(HashMap<String, String> hashMap) { stockSymbolName = hashMap; }

    public static HashMap<String, String> getStockSymbolName() { return stockSymbolName; }
}
