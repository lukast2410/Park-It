package tiffanytiph.com.parkit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;
import tiffanytiph.com.parkit.util.DBOpenHelper;

public class CheckoutActivity extends AppCompatActivity {
    private Transaction transaction;
    private ParkingLot parkingLot;
    TextView tvVehicleType, tvEnterHour, tvLeaveHour, tvTotalHour, tvPricePerHour, tvPaymentMethod, tvTotalPrice;
    Button closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getData();
        initialize();
        initializeToolbar();
        settingComponent();
        settingButton();
    }

    private void settingButton() {
        closeBtn.setOnClickListener(x -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void settingComponent() {
        tvVehicleType.setText(transaction.getVehicleType());
        tvEnterHour.setText(Transaction.convertHourToString(transaction.getEnterHour()));
        tvLeaveHour.setText(Transaction.convertHourToString(transaction.getLeaveHour()));
        int totalHour = calculateTotalHour();
        String totalHourText = totalHour + (totalHour <= 1 ? " hour" : " hours");
        tvTotalHour.setText(totalHourText);
        int pricePerHour = transaction.getVehicleType().equals(Transaction.CAR) ?
                parkingLot.getCarPricePerHour() : parkingLot.getMotorPricePerHour();
        String pricePerHourText = "Rp. " + pricePerHour + ",00";
        tvPricePerHour.setText(pricePerHourText);
        tvPaymentMethod.setText(transaction.getPaymentMethod());
        String totalPrice = "Rp. " + (totalHour * pricePerHour) + ",00";
        tvTotalPrice.setText(totalPrice);
    }

    private int calculateTotalHour() {
        int between = transaction.getLeaveHour() - transaction.getEnterHour();
        if (transaction.getLeaveHour() < transaction.getEnterHour()){
            between = (24 * 60) - transaction.getEnterHour() + transaction.getLeaveHour();
        }
        int hour = (int) Math.ceil(between / 60.0);
        return hour;
    }

    private void initializeToolbar() {
        //ini biar di action bar yang atas ada back
        ActionBar actionBar = getSupportActionBar();
        String title = "Payment";
        assert actionBar != null;
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initialize() {
        tvVehicleType = (TextView) findViewById(R.id.tvVehicleTypeCO);
        tvEnterHour = (TextView) findViewById(R.id.tvEnterHourCO);
        tvLeaveHour = (TextView) findViewById(R.id.tvLeaveHourCO);
        tvTotalHour = (TextView) findViewById(R.id.tvTotalHourCO);
        tvPricePerHour = (TextView) findViewById(R.id.tvPricePerHourCO);
        tvPaymentMethod = (TextView) findViewById(R.id.tvPaymentMethodCO);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPriceCO);
        closeBtn = (Button) findViewById(R.id.btnClose);
    }

    private void getData() {
        String id = getIntent().getStringExtra(DBOpenHelper.ID);
        if(id == null || id.isEmpty()){
            finish();
            return;
        }
        transaction = Transaction.getOneTransaction(this, id);
        assert transaction != null;
        parkingLot = ParkingLot.getOneParkingLot(this, transaction.getParkingLotId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}