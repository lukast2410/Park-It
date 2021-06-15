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

import tiffanytiph.com.parkit.R;
import tiffanytiph.com.parkit.adapter.ParkingHistoryAdapter;
import tiffanytiph.com.parkit.model.Transaction;

public class Cars extends Fragment implements ParkingHistoryAdapter.HistoryClickListener {
    private ArrayList<Transaction> carHistory;
    private ParkingHistoryAdapter parkingHistoryAdapter;
    RecyclerView rvCarHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cars, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeCarHistory(view);
    }

    private void initializeCarHistory(View view) {
        rvCarHistory = (RecyclerView) view.findViewById(R.id.rvCarHistory);
        carHistory = Transaction.getMyCarTransactionHistory(getContext());
        parkingHistoryAdapter = new ParkingHistoryAdapter(carHistory, this);
        rvCarHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCarHistory.setItemAnimator(new DefaultItemAnimator());
        rvCarHistory.setAdapter(parkingHistoryAdapter);
    }

    @Override
    public void onHistoryClicked(int position) {

    }
}