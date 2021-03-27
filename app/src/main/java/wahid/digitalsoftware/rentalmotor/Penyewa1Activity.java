package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class Penyewa1Activity extends AppCompatActivity {

    String[] daftar;
    ListView ls2;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static Penyewa1Activity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyewa1);

        getSupportActionBar().setTitle("Data Penyewa");

        ImageButton fab = findViewById(R.id.btnA2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kj = new Intent(Penyewa1Activity.this, TambahPenyewaActivity.class);
                startActivity(kj);
            }
        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList1();
    }

    public void RefreshList1() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM penyewa", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(0).toString()+" - "+cursor.getString(1).toString();
        }
        ls2 = findViewById(R.id.ls2);
        ls2.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ls2.setSelected(true);
        ls2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final String id = selection.substring(0,5);
                final CharSequence[] dialogitem = {"Lihat Data Penyewa", "Ubah Data Penyewa", "Hapus Data Penyewa"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Penyewa1Activity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), LihatPenyewaActivity.class);
                                i.putExtra("nama", id);
                                startActivity(i);
                                break;
                            case 1:
                                Intent b = new Intent(getApplicationContext(), UbahPenyewaActivity.class);
                                b.putExtra("nama", id);
                                startActivity(b);
                                break;
                            case 2:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("DELETE FROM penyewa WHERE id = '" + id + "'");
                                RefreshList1();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ls2.getAdapter()).notifyDataSetInvalidated();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}