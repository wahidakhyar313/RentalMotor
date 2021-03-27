package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DataMotorActivity extends AppCompatActivity {

    String[] daftar;
    ListView ls1;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static DataMotorActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_motor);

        getSupportActionBar().setTitle("Data Motor");

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM motor WHERE status = 'y'", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            daftar[i] = cursor.getString(1);
        }
        ls1 = findViewById(R.id.ls1);
        ls1.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ls1.setSelected(true);
        ls1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                Intent i = new Intent(DataMotorActivity.this, DetailMotorActivity.class);
                i.putExtra("namo", selection);
                startActivity(i);
            }
        });

        ((ArrayAdapter) ls1.getAdapter()).notifyDataSetInvalidated();
    }
}