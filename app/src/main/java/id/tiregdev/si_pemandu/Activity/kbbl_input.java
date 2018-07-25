package id.tiregdev.si_pemandu.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.tiregdev.si_pemandu.R;
import id.tiregdev.si_pemandu.utils.AppConfig;
import id.tiregdev.si_pemandu.utils.AppControl;
import id.tiregdev.si_pemandu.utils.SQLiteHandler;

public class kbbl_input extends AppCompatActivity implements View.OnClickListener {


    private ProgressDialog pDialog;
    private Button save;
    private TextView nama_anak;
    private TextView umur;
    private EditText bb;
    private EditText pb;
    private EditText tl;
    private EditText cp;
    private SQLiteHandler db;
    private static final String TAG = kbbl_input.class.getSimpleName();
    String kesimpulan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kbbl_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
    }

    private void findViews() {
        nama_anak = (TextView) findViewById(R.id.namaAnak);
        umur = (TextView) findViewById(R.id.umur);
        bb = (EditText) findViewById(R.id.bb);
        pb = (EditText) findViewById(R.id.pb);
        tl = (EditText) findViewById(R.id.tempatlahir);
        cp = (EditText) findViewById(R.id.cara);
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
        umur.setText(getAge(year,month,day));

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

        String ageS = ageyears +" tahun "+ agemonth+" bulan "+ agedays+" hari";

        return ageS;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                kbbl_input.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void sendData(){


        final String _nama_anak = nama_anak.getText().toString().trim();
        final String _umur = umur.getText().toString().trim();
        final String _bb = bb.getText().toString().trim();
        final String _pb = pb.getText().toString().trim();
        final String _tl = tl.getText().toString().trim();
        final String _cp = cp.getText().toString().trim();

        if(Double.valueOf(_bb) >= 2.4 && Double.valueOf(_bb) <= 4.4 && Double.valueOf(_pb) >= 4.3 && Double.valueOf(_pb) <=5.4 ){
            kesimpulan = "Berat lahir dan panjang badan normal";
        } else if(Double.valueOf(_bb) < 2.4 && Double.valueOf(_pb) >= 4.3 && Double.valueOf(_pb) <=5.4){
            kesimpulan = "Berat lahir tidak normal dan panjang badan normal";
        } else if(Double.valueOf(_bb) >= 2.4 && Double.valueOf(_bb) <= 4.4 && Double.valueOf(_pb) < 4.3){
            kesimpulan = "Berat lahir normal dan panjang badan tidak normal";
        } else if (Double.valueOf(_bb) < 2.4 && Double.valueOf(_pb) < 4.3){
            kesimpulan = "Berat lahir dan panjang badan tidak normal";
        } else if (Double.valueOf(_bb) > 4.4 && Double.valueOf(_pb) > 5.4){
            kesimpulan = "Berat lahir dan panjang badan diatas normal";
        } else if(Double.valueOf(_bb) >= 2.4 && Double.valueOf(_bb) <= 4.4 && Double.valueOf(_pb) > 5.4){
            kesimpulan = "Berat lahir normal dan panjang badan diatas normal";
        } else if(Double.valueOf(_bb) > 4.4 && Double.valueOf(_pb) >= 4.3 && Double.valueOf(_pb) <=5.4){
            kesimpulan = "Berat lahir diatas normal dan panjang badan normal";
        } else if(Double.valueOf(_bb) < 2.4 && Double.valueOf(_pb) > 5.4){
            kesimpulan = "Berat lahir tidak normal dan panjang badan diatas normal";
        } else if (Double.valueOf(_bb) > 4.4 && Double.valueOf(_pb) < 4.3){
            kesimpulan = "Berat lahir diatas normal dan panjang badan tidak normal";
        }

        String tag_string_req = "req_data_kbbl";

        pDialog.setMessage("Mengirim Permintaan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REG_TAMBAH_KBBL , new Response.Listener<String>() {

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
                params.put("berat_badan", _bb);
                params.put("panjang_badan", _pb);
                params.put("tempat_lahir", _tl);
                params.put("cara_persalinan", _cp);
                params.put("kesimpulan_kbbl",kesimpulan);
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
            {
                if (bb.getText().toString().isEmpty() || pb.getText().toString().isEmpty() ||
                        tl.getText().toString().isEmpty() || cp.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Error,, harap isi semua data!", Toast.LENGTH_SHORT).show();
                } else {
                    sendData();
                }

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
