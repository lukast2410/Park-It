package tiffanytiph.com.parkit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tiffanytiph.com.parkit.util.DBOpenHelper;
import tiffanytiph.com.parkit.util.Helper;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private int CarBonusCount;
    private int MotorBonusCount;

    public User(int id, String username, String email, String password, String phone, int carBonusCount, int motorBonusCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        CarBonusCount = carBonusCount;
        MotorBonusCount = motorBonusCount;
    }

    public User(String username, String email, String password, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCarBonusCount() {
        return CarBonusCount;
    }

    public void setCarBonusCount(int carBonusCount) {
        CarBonusCount = carBonusCount;
    }

    public int getMotorBonusCount() {
        return MotorBonusCount;
    }

    public void setMotorBonusCount(int motorBonusCount) {
        MotorBonusCount = motorBonusCount;
    }

    public static void insertUser(Context context, User user){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.USERNAME, user.getUsername());
        cv.put(DBOpenHelper.EMAIL, user.getEmail());
        cv.put(DBOpenHelper.PASSWORD, user.getPassword());
        cv.put(DBOpenHelper.PHONE, user.getPhone());
        cv.put(DBOpenHelper.CAR_BONUS_COUNT, 0);
        cv.put(DBOpenHelper.MOTOR_BONUS_COUNT, 0);

        db.insert(DBOpenHelper.USERS, null, cv);
    }

    public static User userLogin(Context context, String email, String password){
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.EMAIL + " LIKE ? AND " + DBOpenHelper.PASSWORD + " LIKE ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(
                DBOpenHelper.USERS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USERNAME,
                        DBOpenHelper.EMAIL,
                        DBOpenHelper.PASSWORD,
                        DBOpenHelper.PHONE,
                        DBOpenHelper.CAR_BONUS_COUNT,
                        DBOpenHelper.MOTOR_BONUS_COUNT
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0 || cursor.getCount() > 1){
            return null;
        }else{
            cursor.moveToFirst();
            return new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PHONE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_BONUS_COUNT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTOR_BONUS_COUNT))
            );
        }
    }

    public static int updateUserBonusCount(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return 0;
        User user = Helper.getInstance().getCurrentUser();
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getWritableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { user.getId() + "" };

        ContentValues cv = new ContentValues();
        cv.put(DBOpenHelper.CAR_BONUS_COUNT, user.getCarBonusCount());
        cv.put(DBOpenHelper.MOTOR_BONUS_COUNT, user.getMotorBonusCount());

        int count = db.update(
                DBOpenHelper.USERS,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }

    public static void refreshUser(Context context){
        if(Helper.getInstance().getCurrentUser() == null)
            return;
        User user = Helper.getInstance().getCurrentUser();
        SQLiteDatabase db = DBOpenHelper.getInstance(context).getReadableDatabase();
        String selection = DBOpenHelper.ID + " = ?";
        String[] selectionArgs = { user.getId() + "" };

        Cursor cursor = db.query(
                DBOpenHelper.USERS,
                new String[]{
                        DBOpenHelper.ID,
                        DBOpenHelper.USERNAME,
                        DBOpenHelper.EMAIL,
                        DBOpenHelper.PASSWORD,
                        DBOpenHelper.PHONE,
                        DBOpenHelper.CAR_BONUS_COUNT,
                        DBOpenHelper.MOTOR_BONUS_COUNT
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0 || cursor.getCount() > 1){
            return;
        }else{
            cursor.moveToFirst();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PASSWORD)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.PHONE)));
            user.setCarBonusCount(cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.CAR_BONUS_COUNT)));
            user.setMotorBonusCount(cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.MOTOR_BONUS_COUNT)));
        }
    }
}
