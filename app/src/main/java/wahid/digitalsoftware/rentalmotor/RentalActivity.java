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
import android.widget.ListView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class RentalActivity extends AppCompatActivity {

    String[] daftar;
    ListView ls3;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static RentalActivity mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        getSupportActionBar().setTitle("Data Rental");

        mi = this;
        dbcenter = new DataHelper(this);
        RefreshList2();

    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public void RefreshList2() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT motor.id_motor, sewa.id_sewa, sewa.tgl, penyewa.nama, motor.namo, sewa.promo, sewa.lama, sewa.total FROM sewa, penyewa, motor WHERE sewa.id = penyewa.id AND sewa.id_motor = motor.id_motor ORDER BY id_sewa DESC", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = "Kode Sewa :\t"+cursor.getString(0).toString() +""+cursor.getString(1).toString() + "\nTanggal\t:\t" + cursor.getString(2).toString()+ "\n" + cursor.getString(3).toString()+
                    " Menyewa Motor "+cursor.getString(4).toString() + "\nselama " + cursor.getString(6).toString()+ " hari, dengan potongan Harga sebanyak " + cursor.getString(5).toString()+ "%\nTotal Pembayaran\t:\t" + cursor.getString(7).toString()+"\n";

            ls3 = findViewById(R.id.ls3);
            ls3.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
            ls3.setSelected(true);
            ls3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                    final String selection = daftar[arg2];
                    final String id = selection.substring(17,27);
                    final String idm = selection.substring(12,17);
                    final CharSequence[] dialogitem = {"Motor Kembali", "Hapus Data"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(RentalActivity.this);
                    builder.setTitle("Pilihan");
                    builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                SQLiteDatabase dba = dbcenter.getWritableDatabase();
                                dba.execSQL("UPDATE motor SET status = 'y' WHERE id_motor = '"+idm+"'");
                                RefreshList2();
                                Toast.makeText(getApplicationContext(), "Motor Telah Dikembalikan", Toast.LENGTH_SHORT).show();
                                break;

                                case 1:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("DELETE FROM sewa WHERE id_sewa = '" + id + "'");
                                RefreshList2();
                                Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    });
                    builder.create().show();
                }
            });
            ((ArrayAdapter) ls3.getAdapter()).notifyDataSetInvalidated();
        }
    }
}