package llu25.hawk.iit.stock;

import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import llu25.hawk.iit.kchartlib0.chart.BaseKChartView;
import llu25.hawk.iit.kchartlib0.chart.KChartView;
import llu25.hawk.iit.kchartlib0.chart.formatter.DateFormatter;
import llu25.hawk.iit.stock.chart.DataRequest;
import llu25.hawk.iit.stock.chart.KChartAdapter;
import llu25.hawk.iit.stock.chart.KLineEntity;
import llu25.hawk.iit.stock.info.StockInfo;
import llu25.hawk.iit.stock.threads.DownloadStockHistory;

public class StockDetailActivity extends AppCompatActivity {

    @BindView(R.id.title_view)
    RelativeLayout mTitleView;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_percent)
    TextView mTvPercent;
    @BindView(R.id.ll_status)
    LinearLayout mLlStatus;
    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;
    private String json, stockSymbol, closeYesterday, dayHigh, dayLow, priceOpen, change, changeRate, currentPrice;
    private TextView openPriceTV, dayHighTV, dayLowTV, priceTV, percentTV, closeYesterdayTV, stockSymbolTV;
    private static final String TAG = "StockDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 1);
        if (type == 0) {
            setContentView(R.layout.activity_example);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            setContentView(R.layout.activity_example_light);
        }

        StockInfo stockInfo = (StockInfo) getIntent().getSerializableExtra("stock");

        stockSymbolTV = findViewById(R.id.kActivity_stock_symbol);
        stockSymbol = stockInfo.getStockSymbol();
        stockSymbolTV.setText(stockSymbol);

        closeYesterdayTV = findViewById(R.id.kActivity_closeYesterday);
        closeYesterday = stockInfo.getCloseYesterday();
        closeYesterdayTV.setText("CLOSE(P):  " + closeYesterday);

        dayHighTV = findViewById(R.id.kActivity_dayHight);
        dayHigh = stockInfo.getDayHigh();
        dayHighTV.setText("HIGH:  " + dayHigh);

        dayLowTV = findViewById(R.id.kActivity_dayLow);
        dayLow = stockInfo.getDayLow();
        dayLowTV.setText("LOW:   " + dayLow);

        openPriceTV = findViewById(R.id.kActivity_openPrice);
        priceOpen = stockInfo.getPriceOpen();
        openPriceTV.setText("OPEN:  " + priceOpen);

        currentPrice = stockInfo.getCurrentPrice();
        change = stockInfo.getStockChange();
        changeRate = stockInfo.getStockRiseRate();

        priceTV = findViewById(R.id.tv_price);
        percentTV = findViewById(R.id.tv_percent);

        if(Double.parseDouble(changeRate) >= 0)
        {
            priceTV.setText(currentPrice);
            priceTV.setTextColor(getColor(R.color.chart_green));
            percentTV.setText(change + "    +" + changeRate + "%");
            percentTV.setTextColor(getColor(R.color.chart_green));
        }
        else
            {
                priceTV.setText(currentPrice);
                priceTV.setTextColor(getColor(R.color.chart_red));
                percentTV.setText(change + "  -" + changeRate + "%");
                percentTV.setTextColor(getColor(R.color.chart_red));
            }

        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        mKChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
    }

    public void updateStockDetailJson(String s) {
        json = s;
        DataRequest.updateHistoryJson(json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<KLineEntity> data = DataRequest.getALL(StockDetailActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addFooterData(data);
                        mKChartView.startAnimation();
                        mKChartView.refreshEnd();
                    }
                });
            }
        }).start();
    }

    private void initData() {
        mKChartView.showLoading();
        new DownloadStockHistory(this, stockSymbol).execute();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLlStatus.setVisibility(View.GONE);
            mKChartView.setGridRows(3);
            mKChartView.setGridColumns(8);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLlStatus.setVisibility(View.VISIBLE);
            mKChartView.setGridRows(4);
            mKChartView.setGridColumns(4);
        }
    }
}
