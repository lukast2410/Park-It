package tiffanytiph.com.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Random;

import tiffanytiph.com.parkit.adapter.NearbyAdapter;
import tiffanytiph.com.parkit.adapter.SearchHistoryAdapter;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.util.DBOpenHelper;

public class SearchActivity extends AppCompatActivity implements NearbyAdapter.NearbyClickListener, SearchHistoryAdapter.SearchClickListener {
    private ArrayList<ParkingLot> nearbyParkingLot;
    private ArrayList<ParkingLot> history;
    private NearbyAdapter nearbyAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    RecyclerView rvNearby, rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeToolbar();
        initializeNearbyParkingLots();
        initializeSearchHistory();
    }

    private void initializeSearchHistory() {
        int startIdx = (int) Math.floor(Math.random() * (nearbyParkingLot.size() - 3));
        rvHistory = (RecyclerView) findViewById(R.id.rvSearchParkingHistory);
        history = new ArrayList<>();
        history.add(nearbyParkingLot.get(startIdx++));
        history.add(nearbyParkingLot.get(startIdx++));
        history.add(nearbyParkingLot.get(startIdx));
        searchHistoryAdapter = new SearchHistoryAdapter(history, this);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(searchHistoryAdapter);
    }

    private void initializeNearbyParkingLots() {
        rvNearby = (RecyclerView) findViewById(R.id.rvNearbyParkingLots);
        ArrayList<ParkingLot> temp = ParkingLot.getNearbyParkingLot(this);
        nearbyParkingLot = new ArrayList<>();
        for(int i=0;i<5;i++)
            nearbyParkingLot.add(temp.get(i));
        nearbyAdapter = new NearbyAdapter(nearbyParkingLot, this);
        rvNearby.setLayoutManager(new LinearLayoutManager(this));
        rvNearby.setItemAnimator(new DefaultItemAnimator());
        rvNearby.setAdapter(nearbyAdapter);
    }

    private void initializeToolbar() {
        //ini biar di action bar yang atas ada back
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // ini lanjutan dari back button
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNearbyClicked(int position) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra(DBOpenHelper.ID, nearbyParkingLot.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onSearchClicked(int position) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra(DBOpenHelper.ID, history.get(position).getId());
        startActivity(intent);
    }
}