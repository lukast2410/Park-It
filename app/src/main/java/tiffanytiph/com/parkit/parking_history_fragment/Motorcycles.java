package tiffanytiph.com.parkit.parking_history_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.adapter.ParkingHistoryAdapter;
import tiffanytiph.com.parkit.model.Transaction;

public class Motorcycles extends Fragment implements ParkingHistoryAdapter.HistoryClickListener {
    private ArrayList<Transaction> motorcycleHistory;
    private ParkingHistoryAdapter parkingHistoryAdapter;
    RecyclerView rvMotorcycleHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_motorcycles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMotorcycleHistory(view);
    }

    private void initializeMotorcycleHistory(View view) {
        rvMotorcycleHistory = (RecyclerView) view.findViewById(R.id.rvMotorcycleHistory);
        motorcycleHistory = Transaction.getMyMotorcycleTransactionHistory(getContext());
        parkingHistoryAdapter = new ParkingHistoryAdapter(motorcycleHistory, this);
        rvMotorcycleHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMotorcycleHistory.setItemAnimator(new DefaultItemAnimator());
        rvMotorcycleHistory.setAdapter(parkingHistoryAdapter);
    }

    @Override
    public void onHistoryClicked(int position) {

    }
}