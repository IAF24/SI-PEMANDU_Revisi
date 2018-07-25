package id.tiregdev.si_pemandu;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.tiregdev.si_pemandu.R;
import id.tiregdev.si_pemandu.utils.AppConfig;
import id.tiregdev.si_pemandu.utils.AppControl;
import id.tiregdev.si_pemandu.utils.SQLiteHandler;

public class notifikasi extends AppCompatActivity implements View.OnClickListener {
    private SQLiteHandler db;
    private EditText catatan;
    private ProgressDialog pDialog;
    private static final String TAG = notifikasi.class.getSimpleName();
    private Button save;
    Spinner namamodul;
    String[] cities = {
            "KMS",
            "VIT A",
            "IMUNISASI",
            "KA",
            "KBBL"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,cities);

        namamodul.setAdapter(adapter);

        namamodul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid= namamodul.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "Modul yang dipilih : " + cities[sid],
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void findViews() {

        namamodul = (Spinner) findViewById(R.id.namamodul);
        catatan = (EditText) findViewById(R.id.bio);
        save = (Button) findViewById(R.id.save);
        pDialog = new ProgressDialog(this);
        save.setOnClickListener(this);
        db = new SQLiteHandler(getBaseContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                notifikasi.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void sendData(){
        final String _namamodul = namamodul.getSelectedItem().toString();
        final String _catatan = catatan.getText().toString().trim();

        String tag_string_req = "req_notifikasi";

        pDialog.setMessage("Mengirim Permintaan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REG_NOTIFIKASI , new Response.Listener<String>() {

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
                params.put("id_kader", db.getUserDetails().get("id_kader"));
                params.put("nama_modul", _namamodul);
                params.put("catatan", _catatan);
                return params;
            }

        };

// Adding request to request queue
        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public void onClick(View v) {
        if ( v == save ) {
            if (catatan.getContext().toString().isEmpty()) {
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