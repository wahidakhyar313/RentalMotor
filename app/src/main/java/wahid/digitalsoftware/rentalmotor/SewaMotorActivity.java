package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SewaMotorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView lama;
    RadioGroup promo;
    RadioButton weekday, weekend;
    Button sewa, tambah, kurang;

    String sPenyewa, sMotor, sLama, idmotor, idpenyewa, tanggal, sMot, sPromo;

    private Spinner spinner, spinner1;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewa_motor);

        getSupportActionBar().setTitle("Sewa Motor");

        dbHelper = new DataHelper(this);

        spinner = findViewById(R.id.spinner);
        spinner1 = findViewById(R.id.spinner1);
        sewa = findViewById(R.id.btnSewa);
        promo = findViewById(R.id.promoGroup);
        weekday = findViewById(R.id.rbWeekDay);
        weekend = findViewById(R.id.rbWeekEnd);
        lama = findViewById(R.id.lmsw);
        tambah = findViewById(R.id.bttambah);
        kurang = findViewById(R.id.btkurang);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.valueOf(lama.getText().toString());
                a++;
                lama.setText(String.valueOf(a));
            }
        });
        kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int b = Integer.valueOf(lama.getText().toString());
                b--;
                lama.setText(String.valueOf(b));
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        tanggal = df.format(c.getTime());

        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);

        loadSpinnerDataMotor();
        loadSpinnerDataPenyewa();


        sewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.valueOf(lama.getText().toString()) < 1) {
                    Toast.makeText(SewaMotorActivity.this, "Lama Sewa harus lebih dari 0", Toast.LENGTH_SHORT).show();
                } else {
                    sLama = lama.getText().toString();

                    if (weekday.isChecked()) {
                        sPromo = "0.25";
                    } else if (weekend.isChecked()) {
                        sPromo = "0.1";
                    }

                    Intent sw = new Intent(getApplicationContext(), CetakSewaActivity.class);
                    sw.putExtra("idp", idpenyewa);
                    sw.putExtra("idm", idmotor);
                    sw.putExtra("tgl", tanggal);
                    sw.putExtra("po", sPromo);
                    sw.putExtra("m", sLama);
                    startActivity(sw);
                }
            }
        });
    }

    private void loadSpinnerDataMotor() {
        DataHelper db = new DataHelper(getApplicationContext());
        List<String> categories = db.semuaMotor();

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sMotor = parent.getItemAtPosition(position).toString();
                sMot = sMotor.substring(8);

                idmotor = sMotor.substring(0, 6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void loadSpinnerDataPenyewa() {
        DataHelper db = new DataHelper(getApplicationContext());
        List<String> categories = db.semuaPenyewa();

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPenyewa = parent.getItemAtPosition(position).toString();
                idpenyewa = sPenyewa.substring(0, 6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}