package llu25.hawk.iit.stock.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import llu25.hawk.iit.stock.MainActivity;
import llu25.hawk.iit.stock.R;
import llu25.hawk.iit.stock.SearchActivity;
import llu25.hawk.iit.stock.adpater.RecyclerViewAdapterStock;
import llu25.hawk.iit.stock.adpater.UserStockDBHandler;
import llu25.hawk.iit.stock.info.StockInfo;
import llu25.hawk.iit.stock.threads.DownloadSingleStock;
import llu25.hawk.iit.stock.threads.DownloadStockName;

public class FragmentStock extends Fragment
{
    View view;
    private RecyclerView myRecyclerView;
    private static List<StockInfo> userSavedStock = new ArrayList<>();
    private UserStockDBHandler userStockDBHandler;
    private RecyclerViewAdapterStock recyclerViewAdapterStock;
    private static boolean flag = false;
    private ImageView addStock;
    private static final String TAG = "FragmentStock";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.stock_fragment, container, false);
        userStockDBHandler = new UserStockDBHandler(getActivity());
        myRecyclerView = view.findViewById(R.id.stock_recyclerview);
        recyclerViewAdapterStock = new RecyclerViewAdapterStock(getContext(), userSavedStock);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerViewAdapterStock);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myRecyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        myRecyclerView.addItemDecoration(dividerItemDecoration);

        //userStockDBHandler.onUpgrade(userStockDBHandler.getWritableDatabase(), 1, 2);

        if(!flag)
        {
            ArrayList<StockInfo> temp = new ArrayList<>();
            try { temp.addAll(userStockDBHandler.loadStockInfo()); }
            catch (Exception e) { e.printStackTrace(); }

            if(temp.size() == 0) new DownloadSingleStock(this, new StockInfo("FB",
                    "Facebook Inc."), true, true).execute();
            else
                {
                    for(StockInfo e : temp)
                    {
                        new DownloadSingleStock(this, new StockInfo(e.getStockSymbol(), e.getCompanyName()),
                                false, true).execute();
                    }
                }
        }
        flag = true;

        addStock = view.findViewById(R.id.addStockImage);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("SearchType", "HashMap");
                startActivity(intent);
            }
        });

        return view;
    }

    public void updateStockInfoList(StockInfo temp, boolean insertDatabase)
    {
        userSavedStock.add(temp);
        if(insertDatabase) userStockDBHandler.addItem(temp.getStockSymbol(), temp.getCompanyName());
        recyclerViewAdapterStock.notifyDataSetChanged();
    }

    public static List<StockInfo> getUserSavedStock() { return userSavedStock; }
}
