package tiffanytiph.com.parkit.parking_history_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tiffanytiph.com.parkit.ParkingHistoryActivity;
import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.adapter.ParkingHistoryAdapter;
import tiffanytiph.com.parkit.model.Transaction;

public class All extends Fragment implements ParkingHistoryAdapter.HistoryClickListener {
    private ArrayList<Transaction> allHistory;
    private ParkingHistoryAdapter parkingHistoryAdapter;
    RecyclerView rvAllHistory;

    public static All newInstance(){
        return new All();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAllHistory(view);
    }

    private void initializeAllHistory(View view) {
        rvAllHistory = (RecyclerView) view.findViewById(R.id.rvAllHistory);
        allHistory = Transaction.getMyTransactionHistory(getContext());
        parkingHistoryAdapter = new ParkingHistoryAdapter(allHistory, this);
        rvAllHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllHistory.setItemAnimator(new DefaultItemAnimator());
        rvAllHistory.setAdapter(parkingHistoryAdapter);
    }

    @Override
    public void onHistoryClicked(int position) {

    }
}