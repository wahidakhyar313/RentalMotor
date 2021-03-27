package wahid.digitalsoftware.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CetakSewaActivity extends AppCompatActivity {

    TextView idpenyewa, nama, notelp, alamat, idmotor, namo, lama, promo, total, tgl, harga, prtotal;
    Button cetak;
    String sTot, sTot1, sHarga,sHarga1, sPromo, sLama, sprTot;
    int iLama, iHarga,  iSub, iPromo;
    double dPromo, dTotal, dsubP, dLama, dHarga;
    protected Cursor cursor, cursor1;
    DataHelper dbHelper;

    Bitmap bitmap, scaleBitmap;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_sewa);

        idpenyewa = findViewById(R.id.tvidPenyewa);
        nama = findViewById(R.id.tvnmPenyewa);
        notelp = findViewById(R.id.tvnoPenyewa);
        alamat = findViewById(R.id.tvalPenyewa);
        idmotor = findViewById(R.id.tvidMotor);
        namo = findViewById(R.id.tvMotor);
        lama = findViewById(R.id.tvLama);
        promo = findViewById(R.id.tvPromo);
        prtotal = findViewById(R.id.tvPrTotal);
        total = findViewById(R.id.tvTotal);
        tgl = findViewById(R.id.tvTgl);
        harga = findViewById(R.id.tvHarga);
        cetak = findViewById(R.id.btnPrint);

        tgl.setText(getIntent().getStringExtra("tgl"));
        idpenyewa.setText(getIntent().getStringExtra("idp"));
        idmotor.setText(getIntent().getStringExtra("idm"));
        lama.setText(getIntent().getStringExtra("m")+" Hari");
        sPromo = getIntent().getStringExtra("po");

        getSupportActionBar().setTitle("Cetak Nota Pembayaran");

        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM motor WHERE id_motor = '" + getIntent().getStringExtra("idm") +"'", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            cursor.moveToPosition(0);
            namo.setText(cursor.getString(1).toString());
            sHarga = cursor.getString(2).toString();
            sHarga1 = formatRupiah(Double.parseDouble(sHarga));
            harga.setText(sHarga1);

        }

        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        cursor1 = db1.rawQuery("SELECT * FROM penyewa WHERE id = '" + getIntent().getStringExtra("idp") +"'", null);
        cursor1.moveToFirst();
        if (cursor1.getCount()>0){
            cursor1.moveToPosition(0);
            nama.setText(cursor1.getString(1).toString());
            alamat.setText(cursor1.getString(2).toString());
            notelp.setText(cursor1.getString(3).toString());
        }

//        sHarga = harga.getText().toString();
        iHarga =Integer.parseInt(sHarga.replaceAll("[\\D]", ""));
        dHarga = Double.parseDouble(sHarga.replaceAll("[\\D]", ""));
        sLama = lama.getText().toString();
        iLama = Integer.parseInt(sLama.replaceAll("[\\D]", ""));
        dLama = Double.parseDouble(sLama.replaceAll("[\\D]", ""));
        iSub = iHarga * iLama;
        dPromo = Double.parseDouble(sPromo);
        dsubP = dPromo * dHarga *dLama;
        dTotal = iSub - dsubP;
        sprTot = formatRupiah(dsubP);
        sTot1 = String.valueOf(dTotal);
        sTot = formatRupiah(dTotal);
        prtotal.setText(sprTot);
        total.setText(sTot);
        iPromo =  (int) (dPromo * 100);
        sPromo = String.valueOf(iPromo);
        promo.setText(sPromo+"%");


        //cover header
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
        scaleBitmap = Bitmap.createScaledBitmap(bitmap, 1200, 518, false);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();
    }

    private void createPDF() {
        cetak.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                dateTime = new Date();

                //get input
                if (idmotor.getText().toString().length() == 0 ||
                        idpenyewa.getText().toString().length() == 0 ||
                        lama.getText().toString().length() == 0 ||
                        promo.getText().toString().length() == 0) {
                    Toast.makeText(CetakSewaActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo pageInfo
                        = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                canvas.drawBitmap(scaleBitmap, 0, 0, paint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setColor(Color.WHITE);
                titlePaint.setTextSize(70);
                canvas.drawText("Nota Pembayaran", pageWidth / 2, 500, titlePaint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.BLACK);
                paint.setTextSize(35f);
                canvas.drawText("Nama Penyewa: " + nama.getText(), 20, 590, paint);
                canvas.drawText("Nomor Tlp: " + notelp.getText(), 20, 640, paint);

                dateFormat = new SimpleDateFormat("yyMMddHHmm");
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("No. Pesanan: " + dateFormat.format(dateTime), pageWidth - 20, 590, paint);

                dateFormat = new SimpleDateFormat("dd/MM/yy");
                canvas.drawText("Tanggal: " + dateFormat.format(dateTime), pageWidth - 20, 640, paint);

                dateFormat = new SimpleDateFormat("HH:mm:ss");
                canvas.drawText("Pukul: " + dateFormat.format(dateTime), pageWidth - 20, 690, paint);

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                canvas.drawRect(20, 780, pageWidth - 20, 860, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText("ID Motor.", 40, 830, paint);
                canvas.drawText("Motor", 200, 830, paint);
                canvas.drawText("Harga", 700, 830, paint);
                canvas.drawText("Lama", 900, 830, paint);
                canvas.drawText("Total", 1050, 830, paint);

                canvas.drawLine(180, 790, 180, 840, paint);
                canvas.drawLine(680, 790, 680, 840, paint);
                canvas.drawLine(880, 790, 880, 840, paint);
                canvas.drawLine(1030, 790, 1030, 840, paint);


                canvas.drawText("" + idmotor.getText(), 40, 950, paint);
                canvas.drawText("" + namo.getText(), 200, 950, paint);
                canvas.drawText(String.valueOf(harga.getText()), 700, 950, paint);
                canvas.drawText(String.valueOf(iLama), 900, 950, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(formatRupiah(Double.parseDouble(String.valueOf(iSub))), pageWidth - 40, 950, paint);
                paint.setTextAlign(Paint.Align.LEFT);

                canvas.drawLine(400, 1200, pageWidth - 20, 1200, paint);
                canvas.drawText("Sub Total", 700, 1250, paint);
                canvas.drawText(":", 900, 1250, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(formatRupiah(Double.parseDouble(String.valueOf(iSub))), pageWidth - 40, 1250, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("PPN (" + sPromo + "%)", 700, 1300, paint);
                canvas.drawText(":", 900, 1300, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(""+prtotal.getText().toString(), pageWidth - 40, 1300, paint);
                paint.setTextAlign(Paint.Align.LEFT);

                paint.setColor(Color.rgb(247, 147, 30));
                canvas.drawRect(680, 1350, pageWidth - 20, 1450, paint);

                paint.setColor(Color.BLACK);
                paint.setTextSize(50f);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Total", 700, 1415, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.valueOf(sTot), pageWidth - 40, 1415, paint);

                pdfDocument.finishPage(page);

                dateFormat = new SimpleDateFormat("yyMMddHHmm");
                File file = new File(Environment.getExternalStorageDirectory(), "/"+ dateFormat.format(dateTime)+" Rental Motor.pdf");
                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                pdfDocument.close();
                SQLiteDatabase dbH = dbHelper.getWritableDatabase();
                dbH.execSQL("INSERT INTO sewa (id_sewa, tgl, id, id_motor, promo, lama, total) VALUES ('" +
                        dateFormat.format(dateTime) + "','" +
                        tgl.getText().toString() + "','" +
                        getIntent().getStringExtra("idp") + "','" +
                        getIntent().getStringExtra("idm") + "','" +
                        sPromo + "','" +
                        iLama + "','" +
                        sTot + "');");
                dbH.execSQL("UPDATE motor SET status = 'n' WHERE id_motor ='"+getIntent().getStringExtra("idm")+"';");
                RentalActivity.mi.RefreshList2();
                Toast.makeText(getApplicationContext(), "Penyewaan Motor Berhasil, dan Nota Pembayaran sudah dibuat", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CetakSewaActivity.this, MainActivity.class));
            }
            }
        });

    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}