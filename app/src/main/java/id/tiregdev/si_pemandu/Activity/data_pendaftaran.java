package id.tiregdev.si_pemandu.Activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.tiregdev.si_pemandu.R;
import id.tiregdev.si_pemandu.utils.AppConfig;
import id.tiregdev.si_pemandu.utils.AppControl;

public class data_pendaftaran extends AppCompatActivity {

    private TextView  tgl;
    private TextView  tpa;
    private TextView  tl;
    private TextView  tp;
    private TextView  tudk;
    private TextView  tudb;
    private TextView  tudv;
    private TextView  tudi;
    private TextView  tudka;
    private static final String TAG = data_pendaftaran.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendaftaran);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        displayData();
    }
    private void displayData(){

        final String tgl_ = (getIntent().getExtras().getString("tgls"));
        String tag_string_req = "show_data_pendaftaran";
//        Toast.makeText(this, no_nfcs, Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.DATA_PENDAFTARAN + "?tgl=" + tgl_, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
                        JSONObject jsonobject = jObj.getJSONObject("info");

                        String tpas = jsonobject.getString("total_pendaftaran");
                        String tls = jsonobject.getString("total_lk");
                        String tps = jsonobject.getString("total_perempuan");
                        String tudks = jsonobject.getString("total_update_kms");
                        String tudbs = jsonobject.getString("total_update_kbbl");
                        String tudvs = jsonobject.getString("total_update_vita");
                        String tudis = jsonobject.getString("total_update_imunisasi");
                        String tudkas = jsonobject.getString("total_kesehatan_anak");
                        tpa.setText(tpas);
                        tl.setText(tls);
                        tp.setText(tps);
                        tudk.setText(tudks);
                        tudb.setText(tudbs);
                        tudv.setText(tudvs);
                        tudi.setText(tudis);
                        tudka.setText(tudkas);


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
                params.put("tgl", tgl_);
                return params;
            }

        };

        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void findViews() {
        tgl = (TextView)findViewById( R.id.tgl );
        tpa = (TextView)findViewById( R.id.totalPendaftaranAnak );
        tl = (TextView)findViewById( R.id.totalLaki );
        tp = (TextView) findViewById( R.id.totalCewe );
        tudk = (TextView)findViewById( R.id.dataKMS );
        tudb = (TextView)findViewById( R.id.dataKBBL );
        tudv = (TextView)findViewById( R.id.dataVitA );
        tudi = (TextView)findViewById( R.id.dataImunisasi );
        tudka = (TextView)findViewById( R.id.dataKesAnak );
        tgl.setText(getIntent().getExtras().getString("tgls"));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                data_pendaftaran.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
