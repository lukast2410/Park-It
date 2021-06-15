package tiffanytiph.com.parkit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import tiffanytiph.com.parkit.util.DBOpenHelper;

public class Floor {
    private int id;
    private int parkingLotId;
    private String floorName;
    private int carSlot;
    private int motorcycleSlot;

    public Floor(int id, int parkingLotId, String floorName, int carSlot, int motorcycleSlot) {
        this.id = id;
        this.parkingLotId = parkingLotId;
        this.floorName = floorName;
        this.carSlot = carSlot;
        this.motorcycleSlot = motorcycleSlot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public int getCarSlot() {
        return carSlot;
    }

    public void setCarSlot(int carSlot) {
        this.carSlot = carSlot;
    }

    public int getMotorcycleSlot() {
        return motorcycleSlot;
    }

    public void setMotorcycleSlot(int motorcycleSlot) {
        this.motorcycleSlot = motorcycleSlot;
    }

    public static void insertFloor(Context context, Floor floor){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.PARKING_LOTS_ID, floor.getParkingLotId());
        cv.put(DBOpenHelper.FLOOR_NAME, floor.getFloorName());
        cv.put(DBOpenHelper.CAR_SLOTS, floor.getCarSlot());
        cv.put(DBOpenHelper.MOTORCYCLE_SLOTS, floor.getMotorcycleSlot());

        db.insert(DBOpenHelper.FLOORS, null, cv);
    }

    public static ArrayList<Floor> getParkingLotFloors(Context context, int parkingLotId){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.PARKING_LOTS_ID + " = ?";
        String[] selectionArgs = {parkingLotId + ""};
        String sort = DBOpenHelper.ID + " ASC";

        Cursor cursor = db.query(
                DBOpenHelper.FLOORS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_NAME,
                        DBOpenHelper.CAR_SLOTS,
                        DBOpenHelper.MOTORCYCLE_SLOTS
                },
                selection,
                selectionArgs,
                null,
                null,
                sort
        );

        if(cursor.getCount() == 0){
            return null;
        }else{
            ArrayList<Floor> floors = new ArrayList<>();
            cursor.moveToFirst();
            do{
                floors.add(new Floor(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_SLOTS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTORCYCLE_SLOTS))
                ));
            }while(cursor.moveToNext());
            return floors;
        }
    }

    public static Floor getOneFloor(Context context, int id){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { id + "" };

        Cursor cursor = db.query(
                DBOpenHelper.FLOORS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_NAME,
                        DBOpenHelper.CAR_SLOTS,
                        DBOpenHelper.MOTORCYCLE_SLOTS
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
            return new Floor(
                    id,
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_SLOTS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTORCYCLE_SLOTS))
            );
        }
    }

    public static int updateFloorSlot(Context context, int id, boolean isIncrement, boolean isCar){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { id + "" };

        Cursor cursor = db.query(
                DBOpenHelper.FLOORS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_NAME,
                        DBOpenHelper.CAR_SLOTS,
                        DBOpenHelper.MOTORCYCLE_SLOTS
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0) return 0;
        cursor.moveToFirst();
        int carSlot = cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_SLOTS));
        int motorSlot = cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTORCYCLE_SLOTS));

        db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(isCar) cv.put(DBOpenHelper.CAR_SLOTS, carSlot + (isIncrement ? 1 : -1));
        else cv.put(DBOpenHelper.MOTORCYCLE_SLOTS, motorSlot + (isIncrement ? 1 : -1));

        int count = db.update(
                DBOpenHelper.FLOORS,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }
}
