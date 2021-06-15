package tiffanytiph.com.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import tiffanytiph.com.parkit.model.Floor;
import tiffanytiph.com.parkit.model.ParkingLot;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initializeData();

        final int splashTimeOut = 2500;

        Thread splashThread = new Thread() {
            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < splashTimeOut) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                } finally {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        };
        splashThread.start();
    }

    private void initializeData() {
        ArrayList<ParkingLot> parkingLots = ParkingLot.getNearbyParkingLot(this);
        if (parkingLots == null) {
//            TODO: insert parking lot and floors
            int id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Neo Soho Mall", 0.3, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "B1", 0, 200));
                Floor.insertFloor(this, new Floor(0, id, "B2", 0, 200));
                Floor.insertFloor(this, new Floor(0, id, "P1", 50, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 50, 0));
                Floor.insertFloor(this, new Floor(0, id, "P3", 50, 0));
                Floor.insertFloor(this, new Floor(0, id, "P4", 50, 0));
                Floor.insertFloor(this, new Floor(0, id, "P5", 50, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Central Park Mall", 0.6, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "B1", 0, 300));
                Floor.insertFloor(this, new Floor(0, id, "B2", 0, 300));
                Floor.insertFloor(this, new Floor(0, id, "P1", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P3", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P4", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P5", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P6", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P7", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P8", 75, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Taman Anggrek Mall", 1.1, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "B1", 0, 250));
                Floor.insertFloor(this, new Floor(0, id, "B2", 0, 250));
                Floor.insertFloor(this, new Floor(0, id, "P1", 60, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 60, 0));
                Floor.insertFloor(this, new Floor(0, id, "P3", 60, 0));
                Floor.insertFloor(this, new Floor(0, id, "P4", 60, 0));
                Floor.insertFloor(this, new Floor(0, id, "P5", 60, 0));
                Floor.insertFloor(this, new Floor(0, id, "P6", 60, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Hotel Bulevar", 1.2, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "Basement", 0, 200));
                Floor.insertFloor(this, new Floor(0, id, "P1", 60, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 60, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Supernova Esports iCafe", 1.5, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "First Floor", 10, 100));
                Floor.insertFloor(this, new Floor(0, id, "Basement", 40, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Grand Indonesia", 5.2, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "B1", 0, 250));
                Floor.insertFloor(this, new Floor(0, id, "B2", 0, 250));
                Floor.insertFloor(this, new Floor(0, id, "P1", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P3", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P4", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P5", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P6", 75, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Gajah Mada Plaza", 6.5, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "B1", 50, 150));
                Floor.insertFloor(this, new Floor(0, id, "B2", 50, 150));
                Floor.insertFloor(this, new Floor(0, id, "P1", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P3", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P4", 75, 0));
            }

            id = (int) ParkingLot.insertParkingLot(this, new ParkingLot("Paragon Mall", 7.2, 5000, 2000));
            if(id > 0){
                Floor.insertFloor(this, new Floor(0, id, "Basement", 50, 200));
                Floor.insertFloor(this, new Floor(0, id, "P1", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P2", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P3", 75, 0));
                Floor.insertFloor(this, new Floor(0, id, "P4", 75, 0));
            }
        }else Log.d("checkpark", parkingLots.size() + " slot");
    }
}