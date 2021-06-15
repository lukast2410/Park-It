package tiffanytiph.com.parkit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import tiffanytiph.com.parkit.util.DBOpenHelper;

public class ParkingLot {
    private int id;
    private String parkingName;
    private double distance;
    private int carPricePerHour;
    private int motorPricePerHour;
    private ArrayList<Floor> floors;
    private int totalCarSlots;
    private int totalMotorSlots;

    public ParkingLot(int id, String parkingName, double distance, int carPricePerHour, int motorPricePerHour, ArrayList<Floor> floors) {
        this.id = id;
        this.parkingName = parkingName;
        this.distance = distance;
        this.carPricePerHour = carPricePerHour;
        this.motorPricePerHour = motorPricePerHour;
        this.floors = floors;
        calculateSlots();
    }

    public ParkingLot(String parkingName, double distance, int carPricePerHour, int motorPricePerHour) {
        this.parkingName = parkingName;
        this.distance = distance;
        this.carPricePerHour = carPricePerHour;
        this.motorPricePerHour = motorPricePerHour;
    }

    private void calculateSlots() {
        totalCarSlots = totalMotorSlots = 0;
        for (Floor f : floors) {
            totalCarSlots += f.getCarSlot();
            totalMotorSlots += f.getMotorcycleSlot();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getCarPricePerHour() {
        return carPricePerHour;
    }

    public void setCarPricePerHour(int carPricePerHour) {
        this.carPricePerHour = carPricePerHour;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public void setFloors(ArrayList<Floor> floors) {
        this.floors = floors;
    }

    public int getTotalCarSlots() {
        return totalCarSlots;
    }

    public int getTotalMotorSlots() {
        return totalMotorSlots;
    }

    public int getMotorPricePerHour() {
        return motorPricePerHour;
    }

    public void setMotorPricePerHour(int motorPricePerHour) {
        this.motorPricePerHour = motorPricePerHour;
    }

    public static long insertParkingLot(Context context, ParkingLot parkingLot){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.PARKING_NAME, parkingLot.getParkingName());
        cv.put(DBOpenHelper.DISTANCE, parkingLot.getDistance());
        cv.put(DBOpenHelper.CAR_PRICE_PER_HOUR, parkingLot.getCarPricePerHour());
        cv.put(DBOpenHelper.MOTORCYCLE_PRICE_PER_HOUR, parkingLot.getMotorPricePerHour());

        return db.insert(DBOpenHelper.PARKING_LOTS, null, cv);
    }

    public static ArrayList<ParkingLot> getNearbyParkingLot(Context context){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
        String sort = DBOpenHelper.DISTANCE + " ASC";

        Cursor cursor = db.query(
                DBOpenHelper.PARKING_LOTS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.PARKING_NAME,
                        DBOpenHelper.DISTANCE,
                        DBOpenHelper.CAR_PRICE_PER_HOUR,
                        DBOpenHelper.MOTORCYCLE_PRICE_PER_HOUR
                },
                null,
                null,
                null,
                null,
                sort
        );

        if(cursor.getCount() == 0){
            return null;
        }else{
            cursor.moveToFirst();
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID));
                parkingLots.add(new ParkingLot(
                        id,
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DBOpenHelper.DISTANCE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_PRICE_PER_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTORCYCLE_PRICE_PER_HOUR)),
                        Floor.getParkingLotFloors(context, id)
                ));
            }while(cursor.moveToNext());
            return parkingLots;
        }
    }

    public static ParkingLot getOneParkingLot(Context context, int id){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { id + "" };

        Cursor cursor = db.query(
                DBOpenHelper.PARKING_LOTS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.PARKING_NAME,
                        DBOpenHelper.DISTANCE,
                        DBOpenHelper.CAR_PRICE_PER_HOUR,
                        DBOpenHelper.MOTORCYCLE_PRICE_PER_HOUR
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0){
            return null;
        }else{
            cursor.moveToFirst();
            return new ParkingLot(
                    id,
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DBOpenHelper.DISTANCE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_PRICE_PER_HOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTORCYCLE_PRICE_PER_HOUR)),
                    Floor.getParkingLotFloors(context, id)
            );
        }
    }
}
