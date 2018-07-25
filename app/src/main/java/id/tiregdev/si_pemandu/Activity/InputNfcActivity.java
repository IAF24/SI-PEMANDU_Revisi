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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.tiregdev.si_pemandu.R;
import id.tiregdev.si_pemandu.utils.AppConfig;
import id.tiregdev.si_pemandu.utils.AppControl;

public class InputNfcActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_nfc);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();

    }

    private TextView no_nfc;
    private TextView nik;
    private Button save;
    private ProgressDialog pDialog;
    private static final String TAG = InputNfcActivity.class.getSimpleName();

    private void findViews() {
        no_nfc = (TextView) findViewById( R.id.noNFC );
        nik = (TextView)findViewById( R.id.nik );
        save = (Button) findViewById( R.id.savenfc );
        pDialog = new ProgressDialog(this);
        save.setOnClickListener( this );
        no_nfc.setText(getIntent().getExtras().getString("reversedhex"));


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                InputNfcActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendData(){

        final String nomor_nfc = no_nfc.getText().toString().trim();
        final String _nik = nik.getText().toString().trim();


        String tag_string_req = "req_data_nfc";

        pDialog.setMessage("Mengirim Permintaan ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REG_TAMBAH_NFC, new Response.Listener<String>() {

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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_nfc", nomor_nfc);
                params.put("nik", _nik);
                return params;
            }

        };

// Adding request to request queue
        AppControl.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-10-17 13:34:08 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    public void onClick(View v) {

            if (nik.getText().toString().isEmpty()) {
                Toast.makeText(this, "Error,, harap isi data nik!", Toast.LENGTH_SHORT).show();
            } else {
                sendData();
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


