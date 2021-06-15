package tiffanytiph.com.parkit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import tiffanytiph.com.parkit.adapter.SpinnerAdapter;
import tiffanytiph.com.parkit.model.Floor;
import tiffanytiph.com.parkit.model.ParkingLot;
import tiffanytiph.com.parkit.util.DBOpenHelper;

public class BookingActivity extends AppCompatActivity {
    private ParkingLot parkingLot;
    private ArrayList<Floor> carFloors, motorFloors;
    private ArrayList<String> carFloorNames, motorFloorNames;
    private SpinnerAdapter carFloorAdapter, motorFloorAdapter;
    Spinner typeSpinner, floorSpinner;
    TextView tvAvailableSlots;
    Button bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getParkingLot();
        initialize();
        initializeToolbar();
        initializeTypeSpinner();
        initializeFloorSpinner();
        settingSpinner();
        settingButton();
    }

    private void settingButton() {
        bookBtn.setOnClickListener(x -> {
            String type = (typeSpinner.getSelectedItemPosition() == 0)? "Car" : "Motorcycle";
            int floorId = (typeSpinner.getSelectedItemPosition() == 0)?
                    carFloors.get(floorSpinner.getSelectedItemPosition()).getId() :
                    motorFloors.get(floorSpinner.getSelectedItemPosition()).getId();
            Intent intent = new Intent(this, BookingSlotActivity.class);
            intent.putExtra(DBOpenHelper.VEHICLE_TYPE, type);
            intent.putExtra(DBOpenHelper.FLOOR_ID, floorId);
            startActivity(intent);
        });
    }

    private void settingSpinner() {
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    floorSpinner.setAdapter(carFloorAdapter);
                    String slot = carFloors.get(0).getCarSlot() + " Available Slots";
                    tvAvailableSlots.setText(slot);
                }else{
                    if(motorFloorAdapter == null){
                        motorFloors = new ArrayList<>();
                        motorFloorNames = new ArrayList<>();
                        getFloors("motorcycle", motorFloors, motorFloorNames);
                        motorFloorAdapter = new SpinnerAdapter(BookingActivity.this, R.layout.custom_spinner_item, motorFloorNames);
                    }
                    String slot = motorFloors.get(0).getMotorcycleSlot() + " Available Slots";
                    tvAvailableSlots.setText(slot);
                    floorSpinner.setAdapter(motorFloorAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(typeSpinner.getSelectedItemPosition() == 0){
//                    car
                    String slot = carFloors.get(position).getCarSlot() + " Available Slots";
                    tvAvailableSlots.setText(slot);
                }else{
//                    motorcycle
                    String slot = motorFloors.get(position).getMotorcycleSlot() + " Available Slots";
                    tvAvailableSlots.setText(slot);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeFloorSpinner() {
        floorSpinner = (Spinner) findViewById(R.id.spinnerFloor);
        carFloors = new ArrayList<>();
        carFloorNames = new ArrayList<>();
        getFloors("car", carFloors, carFloorNames);
        carFloorAdapter = new SpinnerAdapter(this, R.layout.custom_spinner_item, carFloorNames);
        floorSpinner.setAdapter(carFloorAdapter);
    }

    private void getFloors(String type, ArrayList<Floor> floors, ArrayList<String> floorNames) {
        ArrayList<Floor> temp = parkingLot.getFloors();
        for (Floor f: temp) {
            if(type.equals("car") && f.getCarSlot() > 0){
                floors.add(f);
                floorNames.add(f.getFloorName());
            }else if(type.equals("motorcycle") && f.getMotorcycleSlot() > 0){
                floors.add(f);
                floorNames.add(f.getFloorName());
            }
        }
    }

    private void initializeTypeSpinner() {
        typeSpinner = (Spinner) findViewById(R.id.spinnerVehicleType);
        ArrayList<String> types = new ArrayList<>();
        types.add("Car");
        types.add("Motorcycle");
        SpinnerAdapter customAdapter = new SpinnerAdapter(this, R.layout.custom_spinner_item, types);
        typeSpinner.setAdapter(customAdapter);
    }

    private void initializeToolbar() {
        //ini biar di action bar yang atas ada back
        ActionBar actionBar = getSupportActionBar();
        String title = parkingLot.getParkingName();
        assert actionBar != null;
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initialize() {
        tvAvailableSlots = (TextView) findViewById(R.id.tvBookingAvailableSlots);
        bookBtn = (Button) findViewById(R.id.btnBook);
    }

    private void getParkingLot() {
        int id = getIntent().getIntExtra(DBOpenHelper.ID, -1);
        parkingLot = ParkingLot.getOneParkingLot(this, id);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}