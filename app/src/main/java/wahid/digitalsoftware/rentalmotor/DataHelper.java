package wahid.digitalsoftware.rentalmotor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rentalmotor.db";
    private static final int DATABASE_VERSION = 1;
    private String KEY_NAME = "NAMA";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        String sqlpenyewa = "create table penyewa (id integer primary key, nama text null, alamat text null, no_hp text null);";
        Log.d("Data", "onCreate: "+ sqlpenyewa);
        db.execSQL(sqlpenyewa);
        String sqlmotor = "create table motor(id_motor integer primary key, namo text null, harga integer null, nopol text null , status text null);";
        Log.d("Data", "onCreate: "+ sqlmotor);
        db.execSQL(sqlmotor);
        String sqlsewa = "create table sewa(id_sewa integer primary key, tgl text null, id integer null, id_motor integer null, promo integer null, lama integer null , total double null, foreign key(id) references penyewa (id), foreign key(id_motor) references motor (id_motor));";
        Log.d("Data", "onCreate: "+ sqlsewa);
        db.execSQL(sqlsewa);

        db.execSQL("insert into motor values (" +
                "'10001'," +
                "'Supra X 125'," +
                "100000," +
                "'D 6127 JJ'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10002'," +
                "'Vario 150 FI'," +
                "90000," +
                "'D 6128 JK'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10003'," +
                "'Genio'," +
                "80000," +
                "'D 6129 JL'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10004'," +
                "'Beat'," +
                "80000," +
                "'D 6130 JM'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10005'," +
                "'Mio'," +
                "80000," +
                "'D 6131 JN'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10006'," +
                "'Revo'," +
                "90000," +
                "'D 6132 JO'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10007'," +
                "'Xabre'," +
                "120000," +
                "'D 6133 JP'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10008'," +
                "'Piagio'," +
                "100000," +
                "'D 6134 JQ'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10009'," +
                "'Mega Pro'," +
                "110000," +
                "'D 6134 JR'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into motor values (" +
                "'10010'," +
                "'Sonic'," +
                "100000," +
                "'D 6135 JS'," +
                "'y'" +
                ");" +
                "");
        db.execSQL("insert into penyewa values (" +
                "'20001'," +
                "'Wahid'," +
                "'Sadang Serang'," +
                "'08112321354'" +
                ");" +
                "");
    }

    public List<String> semuaMotor() {
        List<String> motors = new ArrayList<String>();
        String selectQuery = "select * from motor where status = 'y'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                motors.add(cursor.getString(0) +" - "+cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return motors;
    }

    public List<String> semuaPenyewa() {
        List<String> penyewas = new ArrayList<String>();
        String selectQuery = "select * from penyewa";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                penyewas.add(cursor.getString(0) +" - "+ cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return penyewas;
    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
