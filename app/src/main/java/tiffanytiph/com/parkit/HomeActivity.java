package tiffanytiph.com.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import tiffanytiph.com.parkit.adapter.BookedAdapter;
import tiffanytiph.com.parkit.adapter.CurrentAdapter;
import tiffanytiph.com.parkit.adapter.NearbyAdapter;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;
import tiffanytiph.com.parkit.util.DBOpenHelper;
import tiffanytiph.com.parkit.util.Helper;

public class HomeActivity extends AppCompatActivity implements NearbyAdapter.NearbyClickListener, BookedAdapter.BookedClickListener, CurrentAdapter.CurrentClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<ParkingLot> nearbyParkingLot;
    private ArrayList<Transaction> bookedTransactions, currTransactions;
    private NearbyAdapter nearbyAdapter;
    private BookedAdapter bookedAdapter;
    private CurrentAdapter currentAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fbSearch;
    RecyclerView rvNearby, rvCurrent, rvBooked;
    LinearLayout llBooked, llCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeToolbar();
        initializeNearbyParkingLots();
        initializeBookedTransactions();
        initializeCurrentTransactions();
        settingFloatingButton();
    }

    private void initializeCurrentTransactions() {
        rvCurrent = (RecyclerView) findViewById(R.id.rvCurrentParking);
        llCurrent = (LinearLayout) findViewById(R.id.llCurrentParking);
        currTransactions = Transaction.getMyCurrentTransaction(this);
        if(currTransactions == null){
            llCurrent.setVisibility(View.GONE);
            return;
        }

        llCurrent.setVisibility(View.VISIBLE);
        currentAdapter = new CurrentAdapter(currTransactions, this);
        rvCurrent.setLayoutManager(new LinearLayoutManager(this));
        rvCurrent.setItemAnimator(new DefaultItemAnimator());
        rvCurrent.setAdapter(currentAdapter);
    }

    private void initializeBookedTransactions() {
        rvBooked = (RecyclerView) findViewById(R.id.rvBookedParking);
        llBooked = (LinearLayout) findViewById(R.id.llBookedParking);
        bookedTransactions = Transaction.getMyBookedTransaction(this);
        if(bookedTransactions == null){
            llBooked.setVisibility(View.GONE);
            return;
        }

        llBooked.setVisibility(View.VISIBLE);
        bookedAdapter = new BookedAdapter(bookedTransactions, this);
        rvBooked.setLayoutManager(new LinearLayoutManager(this));
        rvBooked.setItemAnimator(new DefaultItemAnimator());
        rvBooked.setAdapter(bookedAdapter);
    }

    private void initializeNearbyParkingLots() {
        rvNearby = (RecyclerView) findViewById(R.id.rvNearbyParkingLots);
        nearbyParkingLot = ParkingLot.getNearbyParkingLot(this);
        nearbyAdapter = new NearbyAdapter(nearbyParkingLot, this);
        rvNearby.setLayoutManager(new LinearLayoutManager(this));
        rvNearby.setItemAnimator(new DefaultItemAnimator());
        rvNearby.setAdapter(nearbyAdapter);
    }

    private void settingFloatingButton() {
        // ini untuk floating button
        fbSearch = findViewById(R.id.fbSearch);
        fbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent ke page search
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeToolbar() {
        //nama aplikasi di tengah
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.activity_home);

//        set listener
        navigationView = (NavigationView) findViewById(R.id.navViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        // ini untuk navigation bar
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onNearbyClicked(int position) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra(DBOpenHelper.ID, nearbyParkingLot.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onBookedClicked(int position) {
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra(DBOpenHelper.ID, bookedTransactions.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onCurrentClicked(int position) {
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra(DBOpenHelper.ID, currTransactions.get(position).getId());
        startActivity(intent);
    }

    // ini untuk navigation bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.actionAdd:
                //TODO: go to notification page
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToActivity(String act) {
        if(act.equals("home")) return;

        Intent intent = new Intent(this, AccountActivity.class);
        if(act.equals("history")){
            intent.setClass(this, ParkingHistoryActivity.class);
        }else if(act.equals("logout")){
            Helper.getInstance().setCurrentUser(null);
            intent.setClass(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    // ini untuk notifikasi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homepage:
                goToActivity("home");
                break;
            case R.id.accountpage:
                goToActivity("profile");
                break;
            case R.id.parkingHistoryPage:
                goToActivity("history");
                break;
            case R.id.nav_logout:
                goToActivity("logout");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}