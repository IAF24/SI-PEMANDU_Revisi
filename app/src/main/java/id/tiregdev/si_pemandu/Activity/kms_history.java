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

public class kms_history extends AppCompatActivity {


    private TextView tgl;
    private TextView namaanak;
    private TextView usia;
    private TextView bp;
    private TextView tinggi;
    private TextView bb;
    private TextView sbb;
    private TextView s_asi;

    private static final String TAG = kms_history.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kms_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        displayData();
        findViews();
        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(kms_history.this, kms_input.class);
                i.putExtra("idanak", getIntent().getExtras().getString("idankms"));
                i.putExtra("nama_", getIntent().getExtras().getString("nama"));
                i.putExtra("tl_", getIntent().getExtras().getString("tll"));
                i.putExtra("bb_",bb.getText().toString());
                startActivity(i);
            }
        });
    }
    private void displayData(){

        String tag_string_req = "show_data_kms";
//        Toast.makeText(this, no_nfcs, Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig. KMS + "?id_anak=" + getIntent().getExtras().getString("idankms"), new Response.Listener<String>() {

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
                    String bps = jObj.getString("bln_penimbangan");
                    String tinggis = jObj.getString("tinggi");
                    String bbs = jObj.getString("bb");
                    String sbbs = jObj.getString("status_bb");
                    String s_asis = jObj.getString("status_asi");
                    tgl.setText(tgls);
                    namaanak.setText(nama_anaks);
                    usia.setText(usias);
                    bp.setText(bps);
                    tinggi.setText(tinggis);
                    bb.setText(bbs);
                    sbb.setText(sbbs);
                    s_asi.setText(s_asis);




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
                params.put("id_anaks", getIntent().getExtras().getString("idankms"));
                return params;
            }

        };

        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void  findViews(){
        tgl = (TextView)findViewById( R.id.tgl );
        namaanak = (TextView)findViewById( R.id.namaAnak );
        usia = (TextView)findViewById( R.id.usia );
        bp = (TextView)findViewById( R.id.blnPenimbang );
        tinggi = (TextView)findViewById( R.id.tinggi );
        bb = (TextView)findViewById( R.id.berat );
        sbb = (TextView)findViewById( R.id.statusBB );
        s_asi = (TextView)findViewById( R.id.statusASI );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                kms_history.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
