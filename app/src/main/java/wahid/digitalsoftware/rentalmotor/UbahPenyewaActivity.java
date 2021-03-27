package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

public class UbahPenyewaActivity extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    Button btn1, btn2;
    EditText txt1, txt2, txt3, txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_penyewa);

        getSupportActionBar().setTitle("Ubah Data Penyewa");

        dbHelper =new DataHelper(this);
        txt1 = findViewById(R.id.etA);
        txt2 = findViewById(R.id.etB);
        txt3 = findViewById(R.id.etC);
        txt4 = findViewById(R.id.etD);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM penyewa WHERE id = '" +
                getIntent().getStringExtra("nama") + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            txt1.setText(cursor.getString(0).toString());
            txt2.setText(cursor.getString(1).toString());
            txt3.setText(cursor.getString(2).toString());
            txt4.setText(cursor.getString(3).toString());
        }
        btn1 = findViewById(R.id.btnA1);
        btn2 = findViewById(R.id.btnA2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(txt2.getText().toString())|| isEmpty(txt3.getText().toString())|| isEmpty(txt4.getText().toString())) {
                    Toast.makeText(UbahPenyewaActivity.this, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
                } else if (txt1.length() < 5 ){
                    Toast.makeText(UbahPenyewaActivity.this, "ID Penyewa harus diisi 5 Karakter Angka!", Toast.LENGTH_SHORT).show();
                }else{
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE penyewa SET nama ='"+
                        txt2.getText().toString() + "', alamat='" +
                        txt3.getText().toString() + "', no_hp='" +
                        txt4.getText().toString() + "' WHERE  id='" +
                        txt1.getText().toString() + "'");
                Toast.makeText(getApplicationContext(),"Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                Penyewa1Activity.ma.RefreshList1();
                finish();
            }
        }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}