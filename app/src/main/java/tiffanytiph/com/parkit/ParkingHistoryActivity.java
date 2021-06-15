package tiffanytiph.com.parkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import tiffanytiph.com.parkit.parking_history_fragment.All;
import tiffanytiph.com.parkit.parking_history_fragment.Cars;
import tiffanytiph.com.parkit.parking_history_fragment.Motorcycles;
import tiffanytiph.com.parkit.util.DBOpenHelper;
import tiffanytiph.com.parkit.util.Helper;

//tabLayout : https://youtu.be/021Z1rUjhNU

public class ParkingHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TabLayout tabLayout;
    ViewPager viewPager;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);
        initializeToolbar();
        initializeViewPager();
    }

    private void initializeViewPager() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new MainAdapter(getSupportFragmentManager());
        adapter.AddFragment(All.newInstance(), "    All    ");
        adapter.AddFragment(new Cars(), "    Cars    ");
        adapter.AddFragment(new Motorcycles(), "   Motor   ");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private class MainAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

        public void AddFragment(Fragment fragment, String s){
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return  stringArrayList.get(position);
        }
    }

    private void initializeToolbar() {
        navigationView = (NavigationView) findViewById(R.id.navViewDrawer);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
        if(act.equals("history")) return;

        Intent intent = new Intent(this, AccountActivity.class);
        if(act.equals("home")){
            intent.setClass(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }else if(act.equals("logout")){
            Helper.getInstance().setCurrentUser(null);
            intent.setClass(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

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