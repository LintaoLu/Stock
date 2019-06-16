package llu25.hawk.iit.stock.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import llu25.hawk.iit.stock.R;
import llu25.hawk.iit.stock.StockDetailActivity;
import llu25.hawk.iit.stock.info.StockInfo;

public class RecyclerViewAdapterStock extends RecyclerView.Adapter<RecyclerViewAdapterStock.MyViewHolderStock>
{
    Context mContext;
    List<StockInfo> mStockInfo;
    private static final String TAG = "RecyclerViewAdapterStoc";

    public RecyclerViewAdapterStock(Context mContext, List<StockInfo> mStockInfo)
    {
        this.mContext = mContext;
        this.mStockInfo = mStockInfo;
    }

    @NonNull
    @Override
    public MyViewHolderStock onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_stock, viewGroup, false);
        MyViewHolderStock vHolder = new MyViewHolderStock(v);
        vHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockInfo temp = mStockInfo.get(i);
                Intent intent = new Intent(mContext, StockDetailActivity.class);
                intent.putExtra("stock", temp);
                mContext.startActivity(intent);
            }
        });
        vHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mStockInfo.remove(i);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setIcon(R.drawable.ic_warning);

                builder.setMessage("Are you sure to delete this item '" + mStockInfo.get(i).getStockSymbol()+"' ?");
                builder.setTitle("Delete Stock");

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderStock myViewHolderStock, int i)
    {
        StockInfo temp = mStockInfo.get(i);
        myViewHolderStock.stockNameTV.setText(temp.getStockSymbol());
        myViewHolderStock.stockPriceTV.setText(temp.getCurrentPrice());
        myViewHolderStock.stockRaiseRateTV.setText(temp.getStockRiseRate()+"%");
        myViewHolderStock.stockCompanyNameTV.setText(temp.getCompanyName());
        if(Double.parseDouble(temp.getStockRiseRate()) > 0)
        {
            myViewHolderStock.stockPriceTV.setTextColor(Color.parseColor("#008000"));
            myViewHolderStock.stockRaiseRateTV.setTextColor(Color.parseColor("#008000"));
        }
        else
            {
                myViewHolderStock.stockPriceTV.setTextColor(Color.parseColor("#FF0000"));
                myViewHolderStock.stockRaiseRateTV.setTextColor(Color.parseColor("#FF0000"));
            }
    }

    @Override
    public int getItemCount()
    {
        return mStockInfo.size();
    }

    public static class MyViewHolderStock extends RecyclerView.ViewHolder
    {
        private TextView stockNameTV, stockCompanyNameTV, stockPriceTV, stockRaiseRateTV;
        private LinearLayout layout;

        public MyViewHolderStock(@NonNull View itemView)
        {
            super(itemView);
            layout = itemView.findViewById(R.id.single_stock);
            stockNameTV = itemView.findViewById(R.id.stock_name);
            stockPriceTV = itemView.findViewById(R.id.stock_price);
            stockRaiseRateTV = itemView.findViewById(R.id.stock_percent);
            stockCompanyNameTV = itemView.findViewById(R.id.stock_company_name);
        }
    }
}