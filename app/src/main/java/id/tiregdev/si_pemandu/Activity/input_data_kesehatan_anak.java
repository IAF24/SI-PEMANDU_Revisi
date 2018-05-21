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

public class input_data_kesehatan_anak extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog pDialog;
    private Button save;
    private TextView nama_anak;
    private TextView umur;
    private EditText penyakit;
    private EditText tindakan;
    private EditText ket;
    private SQLiteHandler db;
    private static final String TAG = input_data_kesehatan_anak.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_kesehatan_anak);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
    }
    private void findViews() {
        nama_anak = (TextView) findViewById(R.id.namaAnak);
        umur = (TextView) findViewById(R.id.umur);
        penyakit = (EditText) findViewById(R.id.penyakit);
        tindakan = (EditText) findViewById(R.id.tindakan);
        ket = (EditText) findViewById(R.id.ket);
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

        String ageS = ageyears +" Tahun "+ agemonth+" Bulan "+ agedays+" Hari";

        return ageS;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                input_data_kesehatan_anak.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendData(){


        final String _nama_anak = nama_anak.getText().toString().trim();
        final String _umur = umur.getText().toString().trim();
        final String _penyakit = penyakit.getText().toString().trim();
        final String _tindakan = tindakan.getText().toString().trim();
        final String _ket = ket.getText().toString().trim();

        String tag_string_req = "req_data_kesehatan_anak";

        pDialog.setMessage("Mengirim Permintaan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REG_TAMBAH_KESEHATAN_ANAK , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
//                        String uid = jObj.getString("uid");
//
//                        JSONObject user = jObj.getJSONObject("user");
//                        String name = user.getString("name");
//                        String email = user.getString("email");
//                        String alamat = user.getString("alamat");
//                        String no_telp = user.getString("no_telp");
//                        String tanggal_lahir = user.getString("tanggal_lahir");
//                        String bio = user.getString("bio");
//                        String foto_user = user.getString("foto");
//
//                        // Inserting row in users table
//                        db.updateUser(name, email, uid, alamat, no_telp, tanggal_lahir, bio, foto_user);

                        Toast.makeText(getBaseContext(), "Data berhasil terkirim!", Toast.LENGTH_LONG).show();
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
                params.put("penyakit", _penyakit);
                params.put("tindakan", _tindakan);
                params.put("keterangan", _ket);
                return params;
            }

        };

// Adding request to request queue
        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public void onClick(View v) {
        if ( v == save ) {
            if (penyakit.getText().toString().isEmpty() || tindakan.getText().toString().isEmpty() ||
                    ket.getText().toString().isEmpty() ) {
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
