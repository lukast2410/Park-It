package tiffanytiph.com.parkit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import tiffanytiph.com.parkit.model.Floor;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;
import tiffanytiph.com.parkit.model.User;
import tiffanytiph.com.parkit.util.DBOpenHelper;
import tiffanytiph.com.parkit.util.Helper;

public class BookingSlotActivity extends AppCompatActivity {
    private String vehicleType;
    private ParkingLot parkingLot;
    private Floor floor;
    RadioGroup rgPayment;
    ImageView ivVehicleType;
    TextView tvParkingName, tvFloorName, tvAvailableSlot, tvPricePerHour, tvTotalPrice, tvVehicleType, tvBookingFee;
    Button bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_slot);
        getData();
        initialize();
        initializeToolbar();
        settingComponent();
        settingButton();
    }

    private void settingButton() {
        bookBtn.setOnClickListener(x -> {
            String paymentMethod = getSelectedPayment();
            String id = UUID.randomUUID().toString();

//            TODO: insert transaction
            Transaction.insertTransaction(this, new Transaction(
                    id,
                    Helper.getInstance().getCurrentUser().getId(),
                    parkingLot.getId(),
                    floor.getId(),
                    vehicleType,
                    getDateNow(),
                    -1,
                    -1,
                    paymentMethod,
                    Transaction.BOOKED
            ));

//            TODO: update slot
            if (vehicleType.equals(Transaction.CAR)) floor.setCarSlot(floor.getCarSlot() - 1);
            else floor.setMotorcycleSlot(floor.getMotorcycleSlot() - 1);
            Floor.updateFloorSlot(this, floor.getId(), false, vehicleType.equals(Transaction.CAR));

//            TODO: update user bonus
            User.refreshUser(this);
            User user = Helper.getInstance().getCurrentUser();
            if(vehicleType.equals(Transaction.CAR)) user.setCarBonusCount(user.getCarBonusCount()+1);
            else user.setMotorBonusCount(user.getMotorBonusCount()+1);
            User.updateUserBonusCount(this);

            Intent intent = new Intent(this, BookingDetailActivity.class);
            intent.putExtra(DBOpenHelper.ID, id);
            startActivity(intent);
        });
    }

    private String getDateNow(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        return df.format(c.getTime());
    }

    private void settingComponent() {
        ivVehicleType.setImageResource(vehicleType.equals("Car") ?
                R.drawable.ic_baseline_directions_car_24:
                R.drawable.ic_baseline_directions_bike_24);
        String name = parkingLot.getParkingName();
        tvParkingName.setText(name);
        String floorName = floor.getFloorName() + " Floor";
        tvFloorName.setText(floorName);
        String slot = (vehicleType.equals("Car") ? floor.getCarSlot() : floor.getMotorcycleSlot()) + " Available";
        tvAvailableSlot.setText(slot);
        String price = "Rp. " + (vehicleType.equals("Car") ? parkingLot.getCarPricePerHour() :
                parkingLot.getMotorPricePerHour()) + " / hour";
        tvPricePerHour.setText(price);
        tvVehicleType.setText(vehicleType);
        String bookingFee = "Rp. 3000,00";
        tvBookingFee.setText(bookingFee);
        tvTotalPrice.setText(bookingFee);
    }

    private String getSelectedPayment(){
        int selectedId = rgPayment.getCheckedRadioButtonId();
        RadioButton rbSelectedPayment = (RadioButton) findViewById(selectedId);
        return rbSelectedPayment.getText().toString();
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
        rgPayment = (RadioGroup) findViewById(R.id.rgPaymentMethod);
        ivVehicleType = (ImageView) findViewById(R.id.ivVehicleType);
        tvParkingName = (TextView) findViewById(R.id.tvMallNameBS);
        tvFloorName = (TextView) findViewById(R.id.tvBookingFloor);
        tvAvailableSlot = (TextView) findViewById(R.id.tvBookingAvailableSlots);
        tvPricePerHour = (TextView) findViewById(R.id.tvPricePerHour);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        tvVehicleType = (TextView) findViewById(R.id.tvVehicleTypeBS);
        tvBookingFee = (TextView) findViewById(R.id.tvBookingFee);
        bookBtn = (Button) findViewById(R.id.btnBook);
    }

    private void getData() {
        vehicleType = getIntent().getStringExtra(DBOpenHelper.VEHICLE_TYPE);
        int floorId = getIntent().getIntExtra(DBOpenHelper.FLOOR_ID, -1);
        if(floorId < 0){
            finish();
            return;
        }

        floor = Floor.getOneFloor(this, floorId);
        parkingLot = ParkingLot.getOneParkingLot(this, floor.getParkingLotId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}