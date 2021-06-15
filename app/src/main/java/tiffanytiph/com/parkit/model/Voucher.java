package tiffanytiph.com.parkit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tiffanytiph.com.parkit.util.DBOpenHelper;
import tiffanytiph.com.parkit.util.Helper;

public class Voucher {
    public static final String EXPIRED = "Expired";
    public static final String AVAILABLE = "Available";
    private int id;
    private int userId;
    private String vehicleType;
    private String expiredDate;
    private int discount;
    private String status;

    public Voucher(int id, int userId, String vehicleType, String expiredDate, int discount, String status) {
        this.id = id;
        this.userId = userId;
        this.vehicleType = vehicleType;
        this.expiredDate = expiredDate;
        this.discount = discount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void insertVoucher(Context context, Voucher voucher){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.USER_ID, voucher.getUserId());
        cv.put(DBOpenHelper.VEHICLE_TYPE, voucher.getVehicleType());
        cv.put(DBOpenHelper.EXPIRED_DATE, voucher.getExpiredDate());
        cv.put(DBOpenHelper.DISCOUNT, voucher.getDiscount());
        cv.put(DBOpenHelper.STATUS, voucher.getStatus());

        db.insert(DBOpenHelper.VOUCHERS, null, cv);
    }

    public static ArrayList<Voucher> getMyVoucher(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return null;
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.USER_ID + " = ? AND " + DBOpenHelper.STATUS + " = ?";
        String[] selectionArgs = {Helper.getInstance().getCurrentUser().getId() + "", AVAILABLE};
        String sort = DBOpenHelper.ID + " ASC";
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");

        Cursor cursor = db.query(
                DBOpenHelper.VOUCHERS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USER_ID,
                        DBOpenHelper.VEHICLE_TYPE,
                        DBOpenHelper.EXPIRED_DATE,
                        DBOpenHelper.DISCOUNT,
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
            ArrayList<Voucher> vouchers = new ArrayList<>();
            cursor.moveToFirst();
            do{
                String expired = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.EXPIRED_DATE));
                try {
                    Date expiredDate = df.parse(expired);
                    Date now = Calendar.getInstance().getTime();
                    long different = expiredDate.getTime() - now.getTime();
                    if (different <= 0) continue;
                    else {
                        vouchers.add(new Voucher(
                                cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.USER_ID)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.VEHICLE_TYPE)),
                                expired,
                                cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.DISCOUNT)),
                                cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.STATUS))
                        ));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());
            return vouchers;
        }
    }

    public static int updateVoucher(Context context, Voucher voucher){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.STATUS, voucher.getStatus());

        String selection = DBOpenHelper.ID + " LIKE ?";
        String[] selectionArgs = { voucher.getId() + "" };

        int count = db.update(
                DBOpenHelper.VOUCHERS,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }
}
