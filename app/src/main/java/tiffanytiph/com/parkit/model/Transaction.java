package tiffanytiph.com.parkit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import tiffanytiph.com.parkit.util.DBOpenHelper;
import tiffanytiph.com.parkit.util.Helper;

public class Transaction {
    public static final String BOOKED = "Booked";
    public static final String STARTED = "Started";
    public static final String DONE = "Done";
    public static final String CAR = "Car";
    public static final String MOTORCYCLE = "Motorcycle";
    private String id;
    private int userId;
    private int parkingLotId;
    private int floorId;
    private String vehicleType;
    private String transactionDate;
    private int enterHour;
    private int leaveHour;
    private String paymentMethod;
    private String status;

    public Transaction(String id, int userId, int parkingLotId, int floorId, String vehicleType, String transactionDate, int enterHour, int leaveHour, String paymentMethod, String status) {
        this.id = id;
        this.userId = userId;
        this.parkingLotId = parkingLotId;
        this.floorId = floorId;
        this.vehicleType = vehicleType;
        this.transactionDate = transactionDate;
        this.enterHour = enterHour;
        this.leaveHour = leaveHour;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getEnterHour() {
        return enterHour;
    }

    public void setEnterHour(int enterHour) {
        this.enterHour = enterHour;
    }

    public int getLeaveHour() {
        return leaveHour;
    }

    public void setLeaveHour(int leaveHour) {
        this.leaveHour = leaveHour;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String convertHourToString(int hour) {
        if (hour < 0) return "Not yet!";
        int hours = hour/60;
        int minutes = hour%60;
        boolean isMorning = hours < 12;
        hours %= 12;
        String hourText = (hours < 10 ? "0" : "") + hours;
        String minuteText = (minutes < 10 ? "0" : "") + minutes;
        return hourText + "." + minuteText + " " + (isMorning ? "AM" : "PM");
    }

    public static void insertTransaction(Context context, Transaction transaction){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.ID, transaction.getId());
        cv.put(DBOpenHelper.USER_ID, transaction.getUserId());
        cv.put(DBOpenHelper.PARKING_LOTS_ID, transaction.getParkingLotId());
        cv.put(DBOpenHelper.FLOOR_ID, transaction.getFloorId());
        cv.put(DBOpenHelper.VEHICLE_TYPE, transaction.getVehicleType());
        cv.put(DBOpenHelper.TRANSACTION_DATE, transaction.getTransactionDate());
        cv.put(DBOpenHelper.ENTER_HOUR, transaction.getEnterHour());
        cv.put(DBOpenHelper.LEAVE_HOUR, transaction.getLeaveHour());
        cv.put(DBOpenHelper.PAYMENT_METHOD, transaction.getPaymentMethod());
        cv.put(DBOpenHelper.STATUS, transaction.getStatus());

        db.insert(DBOpenHelper.TRANSACTIONS, null, cv);
    }

    public static ArrayList<Transaction> getMyCurrentTransaction(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ? AND " + DBOpenHelper.STATUS + " = ?";
        String[] selectionArgs = {Helper.getInstance().getCurrentUser().getId() + "", Transaction.STARTED};
        String sort = DBOpenHelper.ID + " DESC";

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.TRANSACTION_DATE,
                        DBOpenHelper.ENTER_HOUR,
                        DBOpenHelper.LEAVE_HOUR,
                        DBOpenHelper.PAYMENT_METHOD,
                        DBOpenHelper.STATUS
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
            ArrayList<Transaction> transactions = new ArrayList<>();
            cursor.moveToFirst();
            do{
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ENTER_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.LEAVE_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
                ));
            }while(cursor.moveToNext());
            return transactions;
        }
    }

    public static ArrayList<Transaction> getMyBookedTransaction(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ? AND " + DBOpenHelper.STATUS + " = ?";
        String[] selectionArgs = {Helper.getInstance().getCurrentUser().getId() + "", Transaction.BOOKED};
        String sort = DBOpenHelper.ID + " DESC";

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.TRANSACTION_DATE,
                        DBOpenHelper.ENTER_HOUR,
                        DBOpenHelper.LEAVE_HOUR,
                        DBOpenHelper.PAYMENT_METHOD,
                        DBOpenHelper.STATUS
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
            ArrayList<Transaction> transactions = new ArrayList<>();
            cursor.moveToFirst();
            do{
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ENTER_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.LEAVE_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
                ));
            }while(cursor.moveToNext());
            return transactions;
        }
    }

    public static ArrayList<Transaction> getMyTransactionHistory(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ? AND " + DBOpenHelper.STATUS + " = ?";
        String[] selectionArgs = {Helper.getInstance().getCurrentUser().getId() + "", Transaction.DONE};
        String sort = DBOpenHelper.ID + " DESC";

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.TRANSACTION_DATE,
                        DBOpenHelper.ENTER_HOUR,
                        DBOpenHelper.LEAVE_HOUR,
                        DBOpenHelper.PAYMENT_METHOD,
                        DBOpenHelper.STATUS
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
            ArrayList<Transaction> transactions = new ArrayList<>();
            cursor.moveToFirst();
            do{
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ENTER_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.LEAVE_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
                ));
            }while(cursor.moveToNext());
            return transactions;
        }
    }

    public static ArrayList<Transaction> getMyCarTransactionHistory(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ? AND " + DBOpenHelper.STATUS + " = ? AND " +
                DBOpenHelper.VEHICLE_TYPE + " = ?";
        String[] selectionArgs = {Helper.getInstance().getCurrentUser().getId() + "", Transaction.DONE, Transaction.CAR};
        String sort = DBOpenHelper.ID + " DESC";

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.TRANSACTION_DATE,
                        DBOpenHelper.ENTER_HOUR,
                        DBOpenHelper.LEAVE_HOUR,
                        DBOpenHelper.PAYMENT_METHOD,
                        DBOpenHelper.STATUS
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
            ArrayList<Transaction> transactions = new ArrayList<>();
            cursor.moveToFirst();
            do{
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ENTER_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.LEAVE_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
                ));
            }while(cursor.moveToNext());
            return transactions;
        }
    }

    public static ArrayList<Transaction> getMyMotorcycleTransactionHistory(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ? AND " + DBOpenHelper.STATUS + " = ? AND " +
                DBOpenHelper.VEHICLE_TYPE + " = ?";
        String[] selectionArgs = {Helper.getInstance().getCurrentUser().getId() + "", Transaction.DONE,
                Transaction.MOTORCYCLE};
        String sort = DBOpenHelper.ID + " DESC";

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.TRANSACTION_DATE,
                        DBOpenHelper.ENTER_HOUR,
                        DBOpenHelper.LEAVE_HOUR,
                        DBOpenHelper.PAYMENT_METHOD,
                        DBOpenHelper.STATUS
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
            ArrayList<Transaction> transactions = new ArrayList<>();
            cursor.moveToFirst();
            do{
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ENTER_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.LEAVE_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
                ));
            }while(cursor.moveToNext());
            return transactions;
        }
    }

    public static Transaction getOneTransaction(Context context, String id){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(
                DBOpenHelper.TRANSACTIONS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.PARKING_LOTS_ID,
                        DBOpenHelper.FLOOR_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.TRANSACTION_DATE,
                        DBOpenHelper.ENTER_HOUR,
                        DBOpenHelper.LEAVE_HOUR,
                        DBOpenHelper.PAYMENT_METHOD,
                        DBOpenHelper.STATUS
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
            return new Transaction(
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.PARKING_LOTS_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.FLOOR_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TRANSACTION_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ENTER_HOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.LEAVE_HOUR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PAYMENT_METHOD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
            );
        }
    }

    public static int updateTransaction(Context context, Transaction transaction){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.ENTER_HOUR, transaction.getEnterHour());
        cv.put(DBOpenHelper.LEAVE_HOUR, transaction.getLeaveHour());
        cv.put(DBOpenHelper.STATUS, transaction.getStatus());

        String selection = DBOpenHelper.ID + " LIKE ?";
        String[] selectionArgs = { transaction.getId() + "" };

        int count = db.update(
                DBOpenHelper.TRANSACTIONS,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }
}
