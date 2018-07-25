package id.tiregdev.si_pemandu.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.tiregdev.si_pemandu.R;
import id.tiregdev.si_pemandu.utils.AppConfig;
import id.tiregdev.si_pemandu.utils.AppControl;
import id.tiregdev.si_pemandu.utils.SQLiteHandler;

public class kms_input extends AppCompatActivity implements View.OnClickListener {



    private ProgressDialog pDialog;
    private Button save;
    private TextView nama_anak;
    private TextView umur;
    //private EditText bln_penimbangan;
    private EditText bb;
    private EditText tinggi;
    private RadioGroup asi;
    private SQLiteHandler db;
    int tahun;
    int bulan;
    int hari;
    String kesimpulan;
    String status_bb;
    String status_anak;
    private static final String TAG = kms_input.class.getSimpleName();

    Spinner bln_penimbangan;
    String[] cities = {
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kms_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,cities);

        bln_penimbangan.setAdapter(adapter);

        bln_penimbangan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid=bln_penimbangan.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "Bulan Penimbangan : " + cities[sid],
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void findViews() {
        nama_anak = (TextView) findViewById( R.id.namaAnak );
        umur = (TextView) findViewById( R.id.umur);
        bln_penimbangan = (Spinner) findViewById( R.id.blnPenimbang);
        bb = (EditText) findViewById( R.id.bb);
        tinggi = (EditText) findViewById( R.id.tinggi);
        asi = (RadioGroup) findViewById( R.id.asi );
        save = (Button) findViewById(R.id.save);
        pDialog = new ProgressDialog(this);
        save.setOnClickListener(this);
        nama_anak.setText(getIntent().getExtras().getString("nama_"));
        db = new SQLiteHandler(getBaseContext());



        String dtStart = (getIntent().getExtras().getString("tl_"));
        String[] sourceSplit= dtStart.split("-");

        int year= Integer.parseInt(sourceSplit[0]);
        int month= Integer.parseInt(sourceSplit[1]);
        int day= Integer.parseInt(sourceSplit[2]);

        String[] nyawa = getAge(year,month,day).split(",");
        tahun= Integer.parseInt(nyawa[0]);
        bulan= Integer.parseInt(nyawa[1]);
        hari= Integer.parseInt(nyawa[2]);
        umur.setText(tahun+ " Tahun " + bulan + " Bulan "+ hari+ " Hari");
    }

   private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int ageyears = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        int agemonth = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        int agedays = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

       if (agemonth < 0) {
           ageyears--;
           agemonth = (12 + agemonth);
       }
       if (today.get(Calendar.DAY_OF_MONTH)< dob.get(Calendar.DAY_OF_MONTH)) {
           agemonth--;
           agedays = 30 + agedays;
       }

       String ageS = ageyears +","+ agemonth+","+ agedays;
        return ageS;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                kms_input.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

     private void sendData(){


        final String _nama_anak = nama_anak.getText().toString().trim();
        final String _umur = umur.getText().toString().trim();
        final String _bln_penimbangan = bln_penimbangan.getSelectedItem().toString();
        final String _bb = bb.getText().toString().trim();
        final String _tinggi = tinggi.getText().toString().trim();
        final String _asi = ((RadioButton)findViewById(asi.getCheckedRadioButtonId())).getText().toString();
        final String kbm = getIntent().getExtras().getString("bb_");

        if( Double.valueOf(kbm)== 0 && Double.valueOf(_bb)>= 0) {
          status_bb ="Belum Ada";
        } else if(Double.valueOf(kbm) >= Double.valueOf(_bb) ){
                status_bb = "Tidak Naik";
        } else if (Double.valueOf(kbm) < Double.valueOf(_bb)){
                status_bb = "Naik";
            }

        if (tahun >= 1){
            status_anak = "Balita";
        } else if (tahun == 0 && bulan >= 1){
            status_anak = "Bayi";
        } else if (tahun == 0 && bulan == 0 && hari >= 1){
            status_anak ="Neonatus";
        }

        if (tahun == 0 && bulan == 0 && Double.valueOf(_bb) <= 2 ){
                kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 0 && Double.valueOf(_bb) >= 2.1 && Double.valueOf(_bb) <= 2.4){
                kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 0 && Double.valueOf(_bb) >= 2.5 && Double.valueOf(_bb) <= 4.4){
                kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 0 && Double.valueOf(_bb) >= 4.5){
                kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 1 && Double.valueOf(_bb) <= 2.8 ){
             kesimpulan = "Gizi Buruk";
         } else if(tahun == 0 && bulan == 1 && Double.valueOf(_bb) >= 2.9 && Double.valueOf(_bb) <= 3.3){
             kesimpulan = "Gizi Kurang";
         } else if(tahun == 0 && bulan == 1 && Double.valueOf(_bb) >= 3.4 && Double.valueOf(_bb) <= 5.8){
             kesimpulan = "Gizi Baik";
         } else if(tahun == 0 && bulan == 1 && Double.valueOf(_bb) >= 5.9) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 2 && Double.valueOf(_bb) <= 3.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 2 && Double.valueOf(_bb) >= 3.8 && Double.valueOf(_bb) <= 4.2){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 2 && Double.valueOf(_bb) >= 4.3 && Double.valueOf(_bb) <= 7.1){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 2 && Double.valueOf(_bb) >= 7.2) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 3 && Double.valueOf(_bb) <= 4.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 3 && Double.valueOf(_bb) >= 4.4 && Double.valueOf(_bb) <= 4.9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 3 && Double.valueOf(_bb) >= 5 && Double.valueOf(_bb) <= 8){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 3 && Double.valueOf(_bb) >= 8.1) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 4 && Double.valueOf(_bb) <= 4.8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 4 && Double.valueOf(_bb) >= 4.9 && Double.valueOf(_bb) <= 5.5){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 4 && Double.valueOf(_bb) >= 5.6 && Double.valueOf(_bb) <= 8.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 4 && Double.valueOf(_bb) >= 8.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 5 && Double.valueOf(_bb) <= 5.2 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 5 && Double.valueOf(_bb) >= 5.3 && Double.valueOf(_bb) <= 5.9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 5 && Double.valueOf(_bb) >= 6 && Double.valueOf(_bb) <= 9.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 5 && Double.valueOf(_bb) >= 9.4) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 6 && Double.valueOf(_bb) <= 5.6 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 6 && Double.valueOf(_bb) >= 5.7 && Double.valueOf(_bb) <= 6.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 6 && Double.valueOf(_bb) >= 6.4 && Double.valueOf(_bb) <= 9.8){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 6 && Double.valueOf(_bb) >= 9.9) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 7 && Double.valueOf(_bb) <= 5.8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 7 && Double.valueOf(_bb) >= 5.9 && Double.valueOf(_bb) <= 6.6){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 7 && Double.valueOf(_bb) >= 6.7 && Double.valueOf(_bb) <= 10.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 7 && Double.valueOf(_bb) >= 10.4) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 8 && Double.valueOf(_bb) <= 6.1 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 8 && Double.valueOf(_bb) >= 6.2 && Double.valueOf(_bb) <= 6.8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 8 && Double.valueOf(_bb) >= 6.9 && Double.valueOf(_bb) <= 10.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 8 && Double.valueOf(_bb) >= 10.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 9 && Double.valueOf(_bb) <= 6.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 9 && Double.valueOf(_bb) >= 6.4 && Double.valueOf(_bb) <= 7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 9 && Double.valueOf(_bb) >= 7.1 && Double.valueOf(_bb) <= 11){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 9 && Double.valueOf(_bb) >= 11.1) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 10 && Double.valueOf(_bb) <= 6.5 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 10 && Double.valueOf(_bb) >= 6.7  && Double.valueOf(_bb) <= 7.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 10 && Double.valueOf(_bb) >= 7.4 && Double.valueOf(_bb) <= 11.4){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 10 && Double.valueOf(_bb) >= 11.5) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 0 && bulan == 11 && Double.valueOf(_bb) <= 6.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 0 && bulan == 11 && Double.valueOf(_bb) >= 6.8 && Double.valueOf(_bb) <= 7.5){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 0 && bulan == 11 && Double.valueOf(_bb) >= 7.6 && Double.valueOf(_bb) <= 11.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 0 && bulan == 11 && Double.valueOf(_bb) >= 11.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 0 && Double.valueOf(_bb) <= 6.8 ) {
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 0 && Double.valueOf(_bb) >= 6.9 && Double.valueOf(_bb) <= 7.6){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 0 && Double.valueOf(_bb) >= 7.7 && Double.valueOf(_bb) <= 12){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 0 && Double.valueOf(_bb) >= 12.1){
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 1 && Double.valueOf(_bb) <= 7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 1 && Double.valueOf(_bb) >= 7.1 && Double.valueOf(_bb) <= 7.8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 1 && Double.valueOf(_bb) >= 7.9 && Double.valueOf(_bb) <= 12.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 1 && Double.valueOf(_bb) >= 12.4) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 2 && Double.valueOf(_bb) <= 7.1 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 2 && Double.valueOf(_bb) >= 7.2 && Double.valueOf(_bb) <= 8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 2 && Double.valueOf(_bb) >= 8.1 && Double.valueOf(_bb) <= 12.6){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 2 && Double.valueOf(_bb) >= 12.7) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 3 && Double.valueOf(_bb) <= 7.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 3 && Double.valueOf(_bb) >= 7.4 && Double.valueOf(_bb) <= 8.2){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 3 && Double.valueOf(_bb) >= 8.3 && Double.valueOf(_bb) <= 12.8){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 3 && Double.valueOf(_bb) >= 12.9) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 4 && Double.valueOf(_bb) <= 7.4 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 4 && Double.valueOf(_bb) >= 7.5 && Double.valueOf(_bb) <= 8.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 4 && Double.valueOf(_bb) >= 8.4 && Double.valueOf(_bb) <= 13.1){
            kesimpulan = "Gizi baik";
        } else if(tahun == 1 && bulan == 4 && Double.valueOf(_bb) >= 13.2) {
            kesimpulan = "Gizi lebih";
        } else if (tahun == 1 && bulan == 5 && Double.valueOf(_bb) <= 7.6 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 5 && Double.valueOf(_bb) >= 7.7 && Double.valueOf(_bb) <= 8.5){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 5 && Double.valueOf(_bb) >= 8.6 && Double.valueOf(_bb) <= 13.4){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 5 && Double.valueOf(_bb) >= 13.5) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 6 && Double.valueOf(_bb) <= 7.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 6 && Double.valueOf(_bb) >= 7.8 && Double.valueOf(_bb) <= 8.7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 6 && Double.valueOf(_bb) >= 8.8 && Double.valueOf(_bb) <= 13.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 6 && Double.valueOf(_bb) >= 13.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 7 && Double.valueOf(_bb) <= 7.9 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 7 && Double.valueOf(_bb) >= 8 && Double.valueOf(_bb) <= 8.8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 7 && Double.valueOf(_bb) >= 8.9 && Double.valueOf(_bb) <= 13.9){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 7 && Double.valueOf(_bb) >= 14) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 8 && Double.valueOf(_bb) <= 8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 8 && Double.valueOf(_bb) >= 8. && Double.valueOf(_bb) <= 9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 8 && Double.valueOf(_bb) >= 9.1 && Double.valueOf(_bb) <= 14.2){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 8 && Double.valueOf(_bb) >= 14.3) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 9 && Double.valueOf(_bb) <= 8.1 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 9 && Double.valueOf(_bb) >= 8.2 && Double.valueOf(_bb) <= 9.1){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 9 && Double.valueOf(_bb) >= 9.2 && Double.valueOf(_bb) <= 14.5){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 9 && Double.valueOf(_bb) >= 14.6) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 10 && Double.valueOf(_bb) <= 8.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 10 && Double.valueOf(_bb) >= 8.4  && Double.valueOf(_bb) <= 9.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 10 && Double.valueOf(_bb) >= 9.4 && Double.valueOf(_bb) <= 14.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 10 && Double.valueOf(_bb) >= 14.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 1 && bulan == 11 && Double.valueOf(_bb) <= 8.4 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 1 && bulan == 11 && Double.valueOf(_bb) >= 8.5 && Double.valueOf(_bb) <= 9.4){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 1 && bulan == 11 && Double.valueOf(_bb) >= 9.5 && Double.valueOf(_bb) <= 15){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 1 && bulan == 11 && Double.valueOf(_bb) >= 15.1) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 0 && Double.valueOf(_bb) <= 8.5 ) {
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 0 && Double.valueOf(_bb) >= 8.6 && Double.valueOf(_bb) <= 9.6){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 0 && Double.valueOf(_bb) >= 9.7 && Double.valueOf(_bb) <= 15.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 0 && Double.valueOf(_bb) >= 15.4){
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 1 && Double.valueOf(_bb) <= 8.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 1 && Double.valueOf(_bb) >= 8.8 && Double.valueOf(_bb) <= 9.7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 1 && Double.valueOf(_bb) >= 9.8 && Double.valueOf(_bb) <= 15.5){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 1 && Double.valueOf(_bb) >= 15.6) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 2 && Double.valueOf(_bb) <= 8.8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 2 && Double.valueOf(_bb) >= 8.9 && Double.valueOf(_bb) <= 9.9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 2 && Double.valueOf(_bb) >= 10 && Double.valueOf(_bb) <= 15.8){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 2 && Double.valueOf(_bb) >= 15.9) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 3 && Double.valueOf(_bb) <= 8.9 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 3 && Double.valueOf(_bb) >= 9 && Double.valueOf(_bb) <= 10){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 3 && Double.valueOf(_bb) >= 10.1 && Double.valueOf(_bb) <= 16.1){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 3 && Double.valueOf(_bb) >= 16.2) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 4 && Double.valueOf(_bb) <= 9 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 4 && Double.valueOf(_bb) >= 9.1 && Double.valueOf(_bb) <= 10.1){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 4 && Double.valueOf(_bb) >= 10.2 && Double.valueOf(_bb) <= 16.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 4 && Double.valueOf(_bb) >= 16.4) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 5 && Double.valueOf(_bb) <= 9.1 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 5 && Double.valueOf(_bb) >= 9.2 && Double.valueOf(_bb) <= 10.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 5 && Double.valueOf(_bb) >= 10.4 && Double.valueOf(_bb) <= 16.6){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 5 && Double.valueOf(_bb) >= 16.7) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 6 && Double.valueOf(_bb) <= 9.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 6 && Double.valueOf(_bb) >= 9.4 && Double.valueOf(_bb) <= 10.4){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 6 && Double.valueOf(_bb) >= 10.5 && Double.valueOf(_bb) <= 16.9){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 6 && Double.valueOf(_bb) >= 17) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 7 && Double.valueOf(_bb) <= 9.4 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 7 && Double.valueOf(_bb) >= 9.5 && Double.valueOf(_bb) <= 10.6){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 7 && Double.valueOf(_bb) >= 10.7 && Double.valueOf(_bb) <= 17.1){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 7 && Double.valueOf(_bb) >= 17.2) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 8 && Double.valueOf(_bb) <= 9.5 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 8 && Double.valueOf(_bb) >= 9.6 && Double.valueOf(_bb) <= 10.7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 8 && Double.valueOf(_bb) >= 10.8 && Double.valueOf(_bb) <= 17.4){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 8 && Double.valueOf(_bb) >= 17.5) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 9 && Double.valueOf(_bb) <= 9.6 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 9 && Double.valueOf(_bb) >= 9.7 && Double.valueOf(_bb) <= 10.8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 9 && Double.valueOf(_bb) >= 10.9 && Double.valueOf(_bb) <= 17.6){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 9 && Double.valueOf(_bb) >= 17.7) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 10 && Double.valueOf(_bb) <= 9.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 10 && Double.valueOf(_bb) >= 9.8  && Double.valueOf(_bb) <= 10.9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 10 && Double.valueOf(_bb) >= 11 && Double.valueOf(_bb) <= 17.8){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 10 && Double.valueOf(_bb) >= 17.9) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 2 && bulan == 11 && Double.valueOf(_bb) <= 9.8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 2 && bulan == 11 && Double.valueOf(_bb) >= 9.9 && Double.valueOf(_bb) <= 11.1){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 2 && bulan == 11 && Double.valueOf(_bb) >= 11.2 && Double.valueOf(_bb) <= 18.1){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 2 && bulan == 11 && Double.valueOf(_bb) >= 18.2) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 0 && Double.valueOf(_bb) <= 9.9 ) {
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 0 && Double.valueOf(_bb) >= 10 && Double.valueOf(_bb) <= 11.2){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 0 && Double.valueOf(_bb) >= 11.3 && Double.valueOf(_bb) <= 18.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 0 && Double.valueOf(_bb) >= 18.4){
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 1 && Double.valueOf(_bb) <= 10 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 1 && Double.valueOf(_bb) >= 10.1 && Double.valueOf(_bb) <= 11.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 1 && Double.valueOf(_bb) >= 11.4 && Double.valueOf(_bb) <= 18.6){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 1 && Double.valueOf(_bb) >= 18.7) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 2 && Double.valueOf(_bb) <= 10.1 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 2 && Double.valueOf(_bb) >= 10.2 && Double.valueOf(_bb) <= 11.4){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 2 && Double.valueOf(_bb) >= 11.5 && Double.valueOf(_bb) <= 18.8){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 2 && Double.valueOf(_bb) >= 18.9) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 3 && Double.valueOf(_bb) <= 10.2 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 3 && Double.valueOf(_bb) >= 10.3 && Double.valueOf(_bb) <= 11.5){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 3 && Double.valueOf(_bb) >= 11.6 && Double.valueOf(_bb) <= 19){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 3 && Double.valueOf(_bb) >= 19.1) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 4 && Double.valueOf(_bb) <= 10.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 4 && Double.valueOf(_bb) >= 10.4 && Double.valueOf(_bb) <= 11.7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 4 && Double.valueOf(_bb) >= 11.8 && Double.valueOf(_bb) <= 19.3){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 4 && Double.valueOf(_bb) >= 19.4) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 5 && Double.valueOf(_bb) <= 10.4 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 5 && Double.valueOf(_bb) >= 10.5 && Double.valueOf(_bb) <= 11.8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 5 && Double.valueOf(_bb) >= 11.9 && Double.valueOf(_bb) <= 19.5){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 5 && Double.valueOf(_bb) >= 19.6) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 6 && Double.valueOf(_bb) <=10.5 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 6 && Double.valueOf(_bb) >= 10.6 && Double.valueOf(_bb) <= 11.9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 6 && Double.valueOf(_bb) >= 12 && Double.valueOf(_bb) <= 19.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 6 && Double.valueOf(_bb) >= 19.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 7 && Double.valueOf(_bb) <= 10.6 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 7 && Double.valueOf(_bb) >= 10.7 && Double.valueOf(_bb) <= 12){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 7 && Double.valueOf(_bb) >= 12.1 && Double.valueOf(_bb) <= 20){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 7 && Double.valueOf(_bb) >= 20.1) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 8 && Double.valueOf(_bb) <= 10.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 8 && Double.valueOf(_bb) >= 10.8 && Double.valueOf(_bb) <= 12.1){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 8 && Double.valueOf(_bb) >= 12.2 && Double.valueOf(_bb) <= 20.2){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 8 && Double.valueOf(_bb) >= 20.3) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 9 && Double.valueOf(_bb) <= 10.8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 9 && Double.valueOf(_bb) >= 10.9 && Double.valueOf(_bb) <= 12.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 9 && Double.valueOf(_bb) >= 12.4 && Double.valueOf(_bb) <= 20.5){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 9 && Double.valueOf(_bb) >= 20.6) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 10 && Double.valueOf(_bb) <= 10.9 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 10 && Double.valueOf(_bb) >= 11  && Double.valueOf(_bb) <= 12.4){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 10 && Double.valueOf(_bb) >= 12.5 && Double.valueOf(_bb) <= 20.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 10 && Double.valueOf(_bb) >= 20.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 3 && bulan == 11 && Double.valueOf(_bb) <= 11 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 3 && bulan == 11 && Double.valueOf(_bb) >= 11.1 && Double.valueOf(_bb) <= 12.5){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 3 && bulan == 11 && Double.valueOf(_bb) >= 12.6 && Double.valueOf(_bb) <= 20.9){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 3 && bulan == 11 && Double.valueOf(_bb) >= 21) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 0 && Double.valueOf(_bb) <= 11.1 ) {
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 0 && Double.valueOf(_bb) >= 11.2 && Double.valueOf(_bb) <= 12.6){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 0 && Double.valueOf(_bb) >= 12.7 && Double.valueOf(_bb) <= 21.2){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 0 && Double.valueOf(_bb) >= 21.3){
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 1 && Double.valueOf(_bb) <= 11.2 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 1 && Double.valueOf(_bb) >= 11.3 && Double.valueOf(_bb) <= 12.7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 1 && Double.valueOf(_bb) >= 12.8 && Double.valueOf(_bb) <= 21.4){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 1 && Double.valueOf(_bb) >= 21.5 ) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 2 && Double.valueOf(_bb) <= 11.3 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 2 && Double.valueOf(_bb) >= 11.4 && Double.valueOf(_bb) <= 12.8){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 2 && Double.valueOf(_bb) >= 12.9 && Double.valueOf(_bb) <= 21.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 2 && Double.valueOf(_bb) >= 21.8 ) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 3 && Double.valueOf(_bb) <= 11.4 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 3 && Double.valueOf(_bb) >= 11.5 && Double.valueOf(_bb) <= 13){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 3 && Double.valueOf(_bb) >= 13.1 && Double.valueOf(_bb) <= 21.9){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 3 && Double.valueOf(_bb) >= 22) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 4 && Double.valueOf(_bb) <= 11.5 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 4 && Double.valueOf(_bb) >= 11.6 && Double.valueOf(_bb) <= 13.1){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 4 && Double.valueOf(_bb) >= 13.2 && Double.valueOf(_bb) <= 22.2){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 4 && Double.valueOf(_bb) >= 22.3) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 5 && Double.valueOf(_bb) <= 11.6 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 5 && Double.valueOf(_bb) >= 11.7 && Double.valueOf(_bb) <= 13.2){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 5 && Double.valueOf(_bb) >= 13.3 && Double.valueOf(_bb) <= 22.4){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 5 && Double.valueOf(_bb) >= 22.5) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 6 && Double.valueOf(_bb) <= 11.7 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 6 && Double.valueOf(_bb) >= 11.8 && Double.valueOf(_bb) <= 13.3){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 6 && Double.valueOf(_bb) >= 13.4 && Double.valueOf(_bb) <= 22.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 6 && Double.valueOf(_bb) >= 22.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 7 && Double.valueOf(_bb) <= 11.8 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 7 && Double.valueOf(_bb) >= 11.9 && Double.valueOf(_bb) <= 13.4){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 7 && Double.valueOf(_bb) >= 13.5 && Double.valueOf(_bb) <= 22.9){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 7 && Double.valueOf(_bb) >= 23) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 8 && Double.valueOf(_bb) <= 11.9 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 8 && Double.valueOf(_bb) >= 12 && Double.valueOf(_bb) <= 13.5){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 8 && Double.valueOf(_bb) >= 13.6 && Double.valueOf(_bb) <= 23.2){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 8 && Double.valueOf(_bb) >= 23.4) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 9 && Double.valueOf(_bb) <= 12 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 9 && Double.valueOf(_bb) >= 12.1 && Double.valueOf(_bb) <= 13.6){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 9 && Double.valueOf(_bb) >= 13.7 && Double.valueOf(_bb) <= 23.4){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 9 && Double.valueOf(_bb) >= 23.5) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 10 && Double.valueOf(_bb) <= 12.1 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 10 && Double.valueOf(_bb) >= 12.2  && Double.valueOf(_bb) <= 13.7){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 10 && Double.valueOf(_bb) >= 13.8 && Double.valueOf(_bb) <= 23.7){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 10 && Double.valueOf(_bb) >= 23.8) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 4 && bulan == 11 && Double.valueOf(_bb) <= 12.2 ){
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 4 && bulan == 11 && Double.valueOf(_bb) >= 12.3 && Double.valueOf(_bb) <= 13.9){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 4 && bulan == 11 && Double.valueOf(_bb) >= 14 && Double.valueOf(_bb) <= 23.9){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 4 && bulan == 11 && Double.valueOf(_bb) >= 24) {
            kesimpulan = "Gizi Lebih";
        } else if (tahun == 5 && bulan == 0 && Double.valueOf(_bb) <= 12.3 ) {
            kesimpulan = "Gizi Buruk";
        } else if(tahun == 5 && bulan == 0 && Double.valueOf(_bb) >= 12.4 && Double.valueOf(_bb) <= 14){
            kesimpulan = "Gizi Kurang";
        } else if(tahun == 5 && bulan == 0 && Double.valueOf(_bb) >= 14.1 && Double.valueOf(_bb) <= 24.2){
            kesimpulan = "Gizi Baik";
        } else if(tahun == 5 && bulan == 0 && Double.valueOf(_bb) >= 24.3){
            kesimpulan = "Gizi Lebih";
        } else if(tahun >= 5 && bulan >= 1){
            kesimpulan = "Status Gizi Khusus Balita";
        }



        String tag_string_req = "req_data_kms";

        pDialog.setMessage("Mengirim Permintaan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REG_TAMBAH_KMS , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Data berhasil terkirim!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Data gagal terkirim ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Failed with error msg:\t" + error.getMessage());
                Log.d(TAG, "Error StackTrace: \t" + error.getStackTrace());
                // edited here
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e(TAG, new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anak", getIntent().getExtras().getString("idanak"));
                params.put("nama_anak", _nama_anak);
                params.put("id_kader", db.getUserDetails().get("id_kader"));
                params.put("umur", _umur);
                params.put("bln_penimbangan", _bln_penimbangan);
                params.put("bb", _bb);
                params.put("tinggi", _tinggi);
                params.put("status_asi", _asi);
                params.put("status_anak", status_anak);
                params.put("kesimpulan_kms", kesimpulan);
                params.put("status_bb",status_bb );
                params.put("nama_ayah", getIntent().getExtras().getString("namaayah"));
                params.put("nama_ibu", getIntent().getExtras().getString("namaibu"));
                params.put("jenis_kelamin", getIntent().getExtras().getString("jkl"));
                params.put("ttl", getIntent().getExtras().getString("tl_"));
                return params;
            }

        };

// Adding request to request queue
        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View v) {
        if ( v == save ) {
            if (bln_penimbangan.getContext().toString().isEmpty() || bb.getText().toString().isEmpty() ||
                    tinggi.getText().toString().isEmpty() || asi.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Error,, harap isi semua data!", Toast.LENGTH_SHORT).show();
            } else {
                sendData();
            }

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
