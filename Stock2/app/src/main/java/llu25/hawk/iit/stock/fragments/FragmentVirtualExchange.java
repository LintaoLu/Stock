package llu25.hawk.iit.stock.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import llu25.hawk.iit.stock.R;

public class FragmentVirtualExchange extends Fragment
{
    View view;

    public FragmentVirtualExchange() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.virtual_stock_exchange_fragment, container, false);
        return view;
    }
}
