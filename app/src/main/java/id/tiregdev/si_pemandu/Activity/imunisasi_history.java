package id.tiregdev.si_pemandu.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class imunisasi_history extends AppCompatActivity {


    private TextView tgl;
    private TextView namaanak;
    private TextView usia;
    private TextView vaksin;

    private static final String TAG = imunisasi_history.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imunisasi_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        displayData();
        findViews();
        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(imunisasi_history.this, input_imunisasi.class);
                i.putExtra("idanak", getIntent().getExtras().getString("idankimun"));
                i.putExtra("nama_", getIntent().getExtras().getString("namaimun"));
                i.putExtra("tl_", getIntent().getExtras().getString("tlm"));
                i.putExtra("jkl", getIntent().getExtras().getString("jklmi"));
                i.putExtra("namaayah", getIntent().getExtras().getString("ayhi"));
                i.putExtra("namaibu", getIntent().getExtras().getString("ibui"));
                startActivity(i);
            }
        });
    }
    private void displayData(){

        String tag_string_req = "show_data_imuniasasi";
//        Toast.makeText(this, no_nfcs, Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig. IMUNISASI + "?id_anak=" + getIntent().getExtras().getString("idankimun"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {

                    String tgls = jObj.getString("tgl");
                    String nama_anaks = jObj.getString("nama_anak");
                    String usias = jObj.getString("umur");
                    String nama_kapsuls = jObj.getString("vaksin");
                    tgl.setText(tgls);
                    namaanak.setText(nama_anaks);
                    usia.setText(usias);
                    vaksin.setText(nama_kapsuls);





//                    } else {
//
//                        // Error occurred in registration. Get the error
//                        // message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "Failed with error msg:\t" + error.getMessage());
                Log.d(TAG, "Error StackTrace: \t" + error.getStackTrace());
                // edited here
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e(TAG, new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anaks", getIntent().getExtras().getString("idankimun"));
                return params;
            }

        };

        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void  findViews() {
        tgl = (TextView) findViewById(R.id.tgl);
        namaanak = (TextView) findViewById(R.id.namaAnak);
        usia = (TextView) findViewById(R.id.usia);
        vaksin = (TextView) findViewById(R.id.vaksin);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                imunisasi_history.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
