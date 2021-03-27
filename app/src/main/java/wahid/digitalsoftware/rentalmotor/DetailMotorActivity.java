package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailMotorActivity extends AppCompatActivity {

    protected Cursor cursor;
    String sMotor, sHarga, sGambar, sNopol;
    DataHelper dbHelper;
    TextView tMotor, tHarga, tNopol;
    ImageView iGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor);

        getSupportActionBar().setTitle("Detail Motor");

        Bundle terima = getIntent().getExtras();
        String motor = terima.getString("namo");

        dbHelper = new DataHelper(this);
        tMotor = findViewById(R.id.Tmotor);
        tHarga = findViewById(R.id.Tharga);
        tNopol = findViewById(R.id.Tnopol);
        iGambar = findViewById(R.id.Imotor);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from motor where namo = '" + motor + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            sMotor = cursor.getString(1);
            sHarga = cursor.getString(2);
            sNopol = cursor.getString(3);
        }

        if (sMotor.equals("Supra X 125")) {
            sGambar = "supra";
        } else if (sMotor.equals("Vario 150 FI")) {
            sGambar = "vario";
        } else if (sMotor.equals("Genio")) {
            sGambar = "genio";
        } else if (sMotor.equals("Beat")) {
            sGambar = "beat";
        } else if (sMotor.equals("Mio")) {
            sGambar = "mio";
        }else if (sMotor.equals("Xabre")) {
            sGambar = "xabre";
        }else if (sMotor.equals("Sonic")) {
            sGambar = "sonic";
        }else if (sMotor.equals("Mega Pro")) {
            sGambar = "megapro";
        }else if (sMotor.equals("Piagio")) {
            sGambar = "piagio";
        }else if (sMotor.equals("Revo")) {
            sGambar = "revo";
        }

        tMotor.setText(sMotor);
        tNopol.setText(sNopol);
        iGambar.setImageResource(getResources().getIdentifier(sGambar, "drawable", getPackageName()));
        tHarga.setText("Rp. " + sHarga);

    }
}