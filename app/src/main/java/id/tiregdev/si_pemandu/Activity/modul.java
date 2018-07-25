package id.tiregdev.si_pemandu.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import id.tiregdev.si_pemandu.R;

public class modul extends AppCompatActivity {

    LinearLayout kms, vita, imunisasi,  gizi, kbbl, kesAnak, kesimpulan;

    String id_ank;
    String nam;
    String tl;
    String naayh;
    String naibu;
    String jkl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modul);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id_ank  = (getIntent().getExtras().getString("idanak"));
        nam = (getIntent().getExtras().getString("na"));
        tl = (getIntent().getExtras().getString("tgla"));
        naayh = (getIntent().getExtras().getString("nayh"));
        naibu = (getIntent().getExtras().getString("nibu"));
        jkl = (getIntent().getExtras().getString("jk"));
        findID();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                modul.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findID(){
        kms = findViewById(R.id.kms);
        vita = findViewById(R.id.vita);
        imunisasi = findViewById(R.id.imunisasi);
//        gizi = findViewById(R.id.gizi);
        kbbl = findViewById(R.id.kbbl);
        kesAnak = findViewById(R.id.kesehatanAnak);
        kesimpulan = findViewById(R.id.kesimpulan);

        kms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(modul.this, kms_history.class);
                i.putExtra("idankms", id_ank);
                i.putExtra("nama", nam);
                i.putExtra("tll", tl);
                i.putExtra("ayh", naayh);
                i.putExtra("ibu", naibu);
                i.putExtra("jklm", jkl);
                startActivity(i);
            }
        });

        vita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(modul.this, vita_history.class);
                i.putExtra("idankvita", id_ank);
                i.putExtra("namavita", nam);
                i.putExtra("tlv", tl);
                i.putExtra("ayhv", naayh);
                i.putExtra("ibuv", naibu);
                i.putExtra("jklmv", jkl);
                startActivity(i);
            }
        });

        imunisasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(modul.this, imunisasi_history.class);
                i.putExtra("idankimun", id_ank);
                i.putExtra("namaimun", nam);
                i.putExtra("tlm", tl);
                i.putExtra("ayhi", naayh);
                i.putExtra("ibui", naibu);
                i.putExtra("jklmi", jkl);
                startActivity(i);
            }
        });


        kbbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(modul.this, kbbl_history.class);
                i.putExtra("idankbl", id_ank);
                i.putExtra("namabl", nam);
                i.putExtra("tlk", tl);
                i.putExtra("ayhk", naayh);
                i.putExtra("ibuk", naibu);
                i.putExtra("jklmk", jkl);
                startActivity(i);
            }
        });

        kesAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(modul.this, data_kesehatan_anak.class);
                i.putExtra("idanka", id_ank);
                i.putExtra("namaka", nam);
                i.putExtra("tla", tl);
                i.putExtra("ayhka", naayh);
                i.putExtra("ibuka", naibu);
                i.putExtra("jklmka", jkl);
                startActivity(i);
            }
        });

        kesimpulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(modul.this, kesimpulan.class);
                i.putExtra("idankp", id_ank);
                startActivity(i);
            }
        });
    }
}
