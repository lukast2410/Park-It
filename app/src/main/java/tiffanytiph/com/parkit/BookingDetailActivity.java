package tiffanytiph.com.parkit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Calendar;

import tiffanytiph.com.parkit.model.Floor;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.model.Transaction;
import tiffanytiph.com.parkit.util.DBOpenHelper;

public class BookingDetailActivity extends AppCompatActivity {
    private Transaction transaction;
    private ParkingLot parkingLot;
    private Floor floor;
    ImageView ivBarcode;
    LinearLayout llEnterHour, llSecretBtn;
    TextView tvParkingName, tvFloorName, tvVehicleType, tvEnterHour;
    Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        getData();
        initialize();
        initializeToolbar();
        settingComponent();
        validateComponent();
        settingButton();
    }

    private void settingButton() {
        llSecretBtn.setOnClickListener(x -> {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int time = hour * 60 + minute;
            transaction.setEnterHour(time);
            transaction.setStatus(Transaction.STARTED);
            Transaction.updateTransaction(this, transaction);
            validateComponent();
        });

        checkoutBtn.setOnClickListener(x -> {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int time = hour * 60 + minute;
            transaction.setLeaveHour(time);
            transaction.setStatus(Transaction.DONE);
            Transaction.updateTransaction(this, transaction);

            if (transaction.getVehicleType().equals(Transaction.CAR)) floor.setCarSlot(floor.getCarSlot() + 1);
            else floor.setMotorcycleSlot(floor.getMotorcycleSlot() + 1);
            Floor.updateFloorSlot(this, floor.getId(), true, transaction.getVehicleType().equals(Transaction.CAR));

//            TODO: go to payment
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(DBOpenHelper.ID, transaction.getId());
            startActivity(intent);
        });
    }

    private void settingComponent() {
        generateQRCode();
        tvParkingName.setText(parkingLot.getParkingName());
        tvFloorName.setText(floor.getFloorName());
        tvVehicleType.setText(transaction.getVehicleType());
    }

    private void generateQRCode() {
//        TODO: get id for QR Code
        String id = transaction.getId();
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(id, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            ivBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void validateComponent() {
        if(transaction.getEnterHour() < 0){
            checkoutBtn.setVisibility(View.GONE);
            llEnterHour.setVisibility(View.GONE);
            llSecretBtn.setVisibility(View.VISIBLE);
        }else{
            tvEnterHour.setText(Transaction.convertHourToString(transaction.getEnterHour()));
            checkoutBtn.setVisibility(View.VISIBLE);
            llEnterHour.setVisibility(View.VISIBLE);
            llSecretBtn.setVisibility(View.GONE);
        }
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
        ivBarcode = (ImageView) findViewById(R.id.ivBarcodeOutput);
        llEnterHour = (LinearLayout) findViewById(R.id.llEnterHourBD);
        llSecretBtn = (LinearLayout) findViewById(R.id.llSecretBtn);
        tvParkingName = (TextView) findViewById(R.id.tvMallNameBD);
        tvFloorName = (TextView) findViewById(R.id.tvFloorName);
        tvVehicleType = (TextView) findViewById(R.id.tvVehicleTypeBD);
        tvEnterHour = (TextView) findViewById(R.id.tvEnterHourBD);
        checkoutBtn = (Button) findViewById(R.id.btnCheckOut);
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
        floor = Floor.getOneFloor(this, transaction.getFloorId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}