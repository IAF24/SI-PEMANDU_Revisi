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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.tiregdev.si_pemandu.R;
import id.tiregdev.si_pemandu.utils.AppConfig;
import id.tiregdev.si_pemandu.utils.AppControl;
import id.tiregdev.si_pemandu.utils.SQLiteHandler;

public class data_anak extends AppCompatActivity  {


    private TextView nik;
    private TextView nama_ibu;
    private TextView nama_ayah;
    private TextView kelurahan;
    private TextView rw;
    private TextView rt;
    private TextView nama_anak;
    private TextView jenis_kelamin;
    private TextView no_nfc;
    private ProgressDialog pDialog;
    private Button ok;
    private SQLiteHandler db;
    String ids;
    String ttl;
    private static final String TAG = data_anak.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_anak);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        final String A = (getIntent().getExtras().getString("_niks"));
       // final String B = (getIntent().getExtras().getString("id_anak"));
        if (A != null ){
           displayDataNik();
         } else if (A == null ){
           displayData();
         }

       /* Button ok = findViewById(R.id.oke);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(data_anak.this, modul.class);
                startActivity(i);
            }
        });*/
        Button ok = findViewById(R.id.oke);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(data_anak.this, modul.class);
                i.putExtra("idanak",ids);
                i.putExtra("tgla",ttl);
                i.putExtra("na",nama_anak.getText().toString());
                i.putExtra("nayh",nama_ayah.getText().toString());
                i.putExtra("nibu",nama_ibu.getText().toString());
                i.putExtra("jk",jenis_kelamin.getText().toString());
                sendData();
                startActivity(i);
            }});
    }


    private void displayData(){

        final String id_anak = (getIntent().getExtras().getString("id_anak"));
        String tag_string_req = "show_data_anak";
//        Toast.makeText(this, no_nfcs, Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.DATA_ANAK + "?id_anak=" + id_anak, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {

                    ids = jObj.getString("id_anak");
                    ttl = jObj.getString("ttl");
                    String niks = jObj.getString("nik");
                    String nama_ibus = jObj.getString("nama_ibu");
                    String nama_ayahs = jObj.getString("nama_ayah");
                    String kelurahans = jObj.getString("kelurahan");
                    String rws = jObj.getString("rw");
                    String rts = jObj.getString("rt");
                    String nama_anaks  = jObj.getString("nama_anak");
                    String jenis_kelamins  = jObj.getString("jenis_kelamin");
                    nik.setText(niks);
                    nama_ibu.setText(nama_ibus);
                    nama_ayah.setText(nama_ayahs);
                    kelurahan.setText(kelurahans);
                    rw.setText(rws);
                    rt.setText(rts);
                    nama_anak.setText(nama_anaks);
                    jenis_kelamin.setText(jenis_kelamins);


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
                params.put("id_anak", id_anak);
                return params;
            }

        };

        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void displayDataNik(){

        final String niks = (getIntent().getExtras().getString("_niks"));
        String tag_string_req = "show_data_anak";
//        Toast.makeText(this, no_nfcs, Toast.LENGTH_SHORT).show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.DATA_ANAK + "?nik=" + niks, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {

                    ids = jObj.getString("id_anak");
                    ttl = jObj.getString("ttl");
                    String niks = jObj.getString("nik");
                    String nama_ibus = jObj.getString("nama_ibu");
                    String nama_ayahs = jObj.getString("nama_ayah");
                    String kelurahans = jObj.getString("kelurahan");
                    String rws = jObj.getString("rw");
                    String rts = jObj.getString("rt");
                    String nama_anaks  = jObj.getString("nama_anak");
                    String jenis_kelamins  = jObj.getString("jenis_kelamin");
                    nik.setText(niks);
                    nama_ibu.setText(nama_ibus);
                    nama_ayah.setText(nama_ayahs);
                    kelurahan.setText(kelurahans);
                    rw.setText(rws);
                    rt.setText(rts);
                    nama_anak.setText(nama_anaks);
                    jenis_kelamin.setText(jenis_kelamins);



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
                params.put("nik", niks);
                return params;
            }

        };

        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void findViews() {
        nik = (TextView)findViewById( R.id.nik );
        nama_ibu = (TextView)findViewById( R.id.ibu );
        nama_ayah = (TextView)findViewById( R.id.ayah );
        kelurahan = (TextView)findViewById( R.id.kelurahan );
        rw = (TextView)findViewById( R.id.rw );
        rt = (TextView)findViewById( R.id.rt );
        ok = (Button) findViewById( R.id.oke );
        nama_anak = (TextView)findViewById( R.id.namaAnak );
        jenis_kelamin = (TextView)findViewById( R.id.jk );
        db = new SQLiteHandler(getBaseContext());
        pDialog = new ProgressDialog(this);

//        nik.setText(getIntent().getExtras().getString("nik"));
//        nama_ibu.setText(getIntent().getExtras().getString("nama_ibu"));
//        nama_ayah.setText(getIntent().getExtras().getString("nama_ayah"));
//        alamat.setText(getIntent().getExtras().getString("alamat"));

    }
    private void sendData(){

        final String jenis_kelamins  = jenis_kelamin.getText().toString().trim();
        String tag_string_req = "req_data_pendaftar";

      //  pDialog.setMessage("Mengirim Permintaan ...");
       // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REG_DATA_ANAK , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
              //  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Data berhasil terkirim!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(getBaseContext(),
                                data_anak.class);
                        startActivity(intent);
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
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_anak",ids);
                params.put("jenis_kelamin", jenis_kelamins);
                params.put("id_kader", db.getUserDetails().get("id_kader"));
                return params;
            }

        };

// Adding request to request queue
        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    //private void showDialog() {
       // if (!pDialog.isShowing())
  //          pDialog.show();
   // }

   // private void hideDialog() {
     //   if (pDialog.isShowing())
     //       pDialog.dismiss();
  //  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                data_anak.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
