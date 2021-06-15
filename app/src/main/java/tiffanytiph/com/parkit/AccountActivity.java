package tiffanytiph.com.parkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tiffanytiph.com.parkit.adapter.VoucherAdapter;
import tiffanytiph.com.parkit.model.Transaction;
import tiffanytiph.com.parkit.model.User;
import tiffanytiph.com.parkit.model.Voucher;
import tiffanytiph.com.parkit.util.Helper;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, VoucherAdapter.VoucherClickListener {
    private User user;
    private ArrayList<Voucher> vouchers;
    private VoucherAdapter voucherAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout llCarBonus, llMotorBonus;
    TextView tvUsername;
    RecyclerView rvVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initialize();
        initializeToolbar();
        refreshUserData();
        initializeVoucherCount();
        settingComponent();
        initializeVouchers();
    }

    private void initializeVouchers() {
        rvVoucher = (RecyclerView) findViewById(R.id.rvVoucher);
        vouchers = Voucher.getMyVoucher(this);
        voucherAdapter = new VoucherAdapter(vouchers, this);
        rvVoucher.setLayoutManager(new LinearLayoutManager(this));
        rvVoucher.setItemAnimator(new DefaultItemAnimator());
        rvVoucher.setAdapter(voucherAdapter);
    }

    private void settingComponent() {
        tvUsername.setText(user.getUsername());
    }

    private void initializeVoucherCount() {
        int carBonus = user.getCarBonusCount();
        for(int i=0;i<10;i++){
            TextView textView = new TextView(this, null, 0, R.style.BonusParkingFee);
            textView.setTextColor(getColor(R.color.black));
            String text = i+1+"";
            textView.setText(text);
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (24 * scale + 0.5f);
            textView.setWidth(pixels);
            textView.setHeight(pixels);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(10);
            textView.setLayoutParams(params);
            if (i < carBonus)
                textView.setBackgroundResource(R.drawable.circle_green);
            llCarBonus.addView(textView);
        }

        int motorcycleBonus = user.getMotorBonusCount();
        for(int i=0;i<10;i++){
            TextView textView = new TextView(this, null, 0, R.style.BonusParkingFee);
            textView.setTextColor(getColor(R.color.black));
            String text = i+1+"";
            textView.setText(text);
            final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (24 * scale + 0.5f);
            textView.setWidth(pixels);
            textView.setHeight(pixels);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(10);
            textView.setLayoutParams(params);
            if (i < motorcycleBonus)
                textView.setBackgroundResource(R.drawable.circle_green);
            llMotorBonus.addView(textView);
        }
    }

    private void refreshUserData() {
        User.refreshUser(this);
        user = Helper.getInstance().getCurrentUser();
        int carVoucher = user.getCarBonusCount() / 10;
        int motorVoucher = user.getMotorBonusCount() / 10;
        if (carVoucher == 0 && motorVoucher == 0) return;

        Calendar c = Calendar.getInstance();
        int add = (int) Math.floor(Math.random() * 3 + 2);
        c.add(Calendar.DATE, add);
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String dateNow = df.format(c.getTime());

        for(int i=0;i<carVoucher;i++){
            Voucher.insertVoucher(this, new Voucher(0, user.getId(), Transaction.CAR, dateNow, 3000, Voucher.AVAILABLE));
        }
        for(int i=0;i<motorVoucher;i++){
            Voucher.insertVoucher(this, new Voucher(0, user.getId(), Transaction.MOTORCYCLE, dateNow, 3000, Voucher.AVAILABLE));
        }

        int carBonusCount = user.getCarBonusCount() % 10;
        int motorBonusCount = user.getMotorBonusCount() % 10;
        user.setCarBonusCount(carBonusCount);
        user.setMotorBonusCount(motorBonusCount);
        User.updateUserBonusCount(this);
    }

    private void initialize() {
        llCarBonus = (LinearLayout) findViewById(R.id.llCarBonusFee);
        llMotorBonus = (LinearLayout) findViewById(R.id.llMotorcycleBonusFee);
        tvUsername = (TextView) findViewById(R.id.tvUsernameACC);
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
        if(act.equals("profile")) return;

        Intent intent = new Intent(this, AccountActivity.class);
        if(act.equals("home")){
            intent.setClass(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }else if(act.equals("history")){
            intent.setClass(this, ParkingHistoryActivity.class);
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

    @Override
    public void onVoucherClicked(int position) {

    }
}