package tiffanytiph.com.parkit.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper  extends SQLiteOpenHelper {
    public final static String DB_NAME = "ParkIt.db";
    public final static int DB_VERSION = 1;
    public final static String USERS = "Users";
    public final static String ID = "ID";
    public final static String USERNAME = "Username";
    public final static String EMAIL = "Email";
    public final static String PASSWORD = "Password";
    public final static String PHONE = "Phone";
    public final static String CAR_BONUS_COUNT = "CarBonusCount";
    public final static String MOTOR_BONUS_COUNT = "MotorBonusCount";

    public final static String PARKING_LOTS = "ParkingLots";
    public final static String PARKING_NAME = "ParkingName";
    public final static String DISTANCE = "Distance";
    public final static String CAR_PRICE_PER_HOUR = "CarPricePerHour";
    public final static String MOTORCYCLE_PRICE_PER_HOUR = "MotorcyclePricePerHour";

    public final static String FLOORS = "Floors";
    public final static String PARKING_LOTS_ID = "ParkingLotsID";
    public final static String FLOOR_NAME = "FloorName";
    public final static String CAR_SLOTS = "CarSlot";
    public final static String MOTORCYCLE_SLOTS = "MotorcycleSlot";

    public final static String TRANSACTIONS = "Transactions";
    public final static String USER_ID = "UserId";
//    public final static String PARKING_LOTS_ID = "ParkingLotsID";
    public final static String FLOOR_ID = "FloorID";
    public final static String VEHICLE_TYPE = "VehicleType";
    public final static String TRANSACTION_DATE = "TransactionDate";
    public final static String ENTER_HOUR = "EnterHour";
    public final static String LEAVE_HOUR = "LeaveHour";
    public final static String PAYMENT_METHOD = "PaymentMethod";
    public final static String STATUS = "Status";

    public final static String VOUCHERS = "Vouchers";
//    public final static String USER_ID = "UserId";
//    public final static String VEHICLE_TYPE = "VehicleType";
    public final static String EXPIRED_DATE = "ExpiredDate";
    public final static String DISCOUNT = "Discount";

    private static DBOpenHelper db = null;

    private DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBOpenHelper getInstance(Context context){
        return db = (db == null)? new DBOpenHelper(context) : db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERNAME + " TEXT NOT NULL," +
                EMAIL + " TEXT NOT NULL," +
                PASSWORD + " TEXT NOT NULL," +
                PHONE + " TEXT NOT NULL," +
                CAR_BONUS_COUNT + " INTEGER NOT NULL," +
                MOTOR_BONUS_COUNT + " INTEGER NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE " + PARKING_LOTS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PARKING_NAME + " TEXT NOT NULL," +
                DISTANCE + " REAL NOT NULL," +
                CAR_PRICE_PER_HOUR + " INTEGER NOT NULL," +
                MOTORCYCLE_PRICE_PER_HOUR + " INTEGER NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE " + FLOORS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PARKING_LOTS_ID + " INTEGER NOT NULL," +
                FLOOR_NAME + " TEXT NOT NULL," +
                CAR_SLOTS + " INTEGER NOT NULL," +
                MOTORCYCLE_SLOTS + " INTEGER NOT NULL," +
                " FOREIGN KEY("+ PARKING_LOTS_ID +") REFERENCES "+ PARKING_LOTS +"("+ ID +")" +
                ")");
        db.execSQL("CREATE TABLE " + TRANSACTIONS + "(" +
                ID + " TEXT NOT NULL PRIMARY KEY," +
                USER_ID + " INTEGER NOT NULL," +
                PARKING_LOTS_ID + " INTEGER NOT NULL," +
                FLOOR_ID + " INTEGER NOT NULL," +
                VEHICLE_TYPE + " TEXT NOT NULL," +
                TRANSACTION_DATE + " TEXT NOT NULL," +
                ENTER_HOUR + " INTEGER," +
                LEAVE_HOUR + " INTEGER," +
                PAYMENT_METHOD + " TEXT NOT NULL," +
                STATUS + " TEXT NOT NULL," +
                " FOREIGN KEY("+ USER_ID +") REFERENCES "+ USERS +"("+ ID +")," +
                " FOREIGN KEY("+ PARKING_LOTS_ID +") REFERENCES "+ PARKING_LOTS +"("+ ID +")," +
                " FOREIGN KEY("+ FLOOR_ID +") REFERENCES "+ FLOORS +"("+ ID +")" +
                ")");
        db.execSQL("CREATE TABLE " + VOUCHERS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER NOT NULL," +
                VEHICLE_TYPE + " TEXT NOT NULL," +
                EXPIRED_DATE + " TEXT NOT NULL," +
                DISCOUNT + " INTEGER NOT NULL," +
                STATUS + " TEXT NOT NULL," +
                " FOREIGN KEY("+ USER_ID +") REFERENCES "+ USERS +"("+ ID +")" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
