package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

public class TambahPenyewaActivity extends AppCompatActivity {

    DataHelper dbHelper;
    Button btn1, btn2;
    EditText txt1, txt2, txt3, txt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_penyewa);

        getSupportActionBar().setTitle("Tambah Data Penyewa");

        dbHelper = new DataHelper(this);

        txt1 = findViewById(R.id.et1);
        txt2 = findViewById(R.id.et2);
        txt3 = findViewById(R.id.et3);
        txt4 = findViewById(R.id.et4);


        btn1 = findViewById(R.id.btnA);
        btn2 = findViewById(R.id.btnB);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(txt2.getText().toString())|| isEmpty(txt3.getText().toString())|| isEmpty(txt4.getText().toString())) {
                    Toast.makeText(TambahPenyewaActivity.this, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
                } else if (txt1.length() < 5 ){
                    Toast.makeText(TambahPenyewaActivity.this, "ID Penyewa harus diisi 5 Karakter Angka!", Toast.LENGTH_SHORT).show();
                }else{
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT INTO penyewa(id,nama, alamat, no_hp) VALUES ('" +
                            txt1.getText().toString() + "','" +
                            txt2.getText().toString() + "','" +
                            txt3.getText().toString() + "','" +
                            txt4.getText().toString() + "')");
                    Toast.makeText(getApplicationContext(), "Data Berhasil dimasukan", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}