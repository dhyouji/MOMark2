package tif.gaskeun.masodin2.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tif.gaskeun.masodin2.Model.Menu;
import tif.gaskeun.masodin2.R;

public class mngMenuAdd extends AppCompatActivity {

    private DatabaseReference dbref;
    public EditText etNama,etDeskripsi,etHarga;
    public Spinner spnKategori;
    public Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_add);

        etNama = findViewById(R.id.et_nmMenu);
        etDeskripsi = findViewById(R.id.et_descMenu);
        etHarga = findViewById(R.id.et_prcMenu);
        spnKategori = findViewById(R.id.spn_ctgMenu);
        btnSubmit = findViewById(R.id.btn_submit);
        dbref = FirebaseDatabase.getInstance().getReference("");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !isEmpty(etNama.getText().toString()) && !isEmpty(etDeskripsi.getText().toString()) && !isEmpty(etHarga.getText().toString()) && !isEmpty(spnKategori.getSelectedItem().toString()))
                    Submit(new Menu((etNama.getText().toString()), (etDeskripsi.getText().toString()), (etHarga.getText().toString()),(spnKategori.getSelectedItem().toString())));
                else Snackbar.make(findViewById(R.id.btn_submit),"Data Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        etNama.getWindowToken(), 0);
            }
        });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void Submit(Menu menu){
        dbref.child("DataResto").child("DaftarMenu").push().setValue(menu).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etNama.setText("");
                etDeskripsi.setText("");
                etHarga.setText("");
                spnKategori.getSelectedItemPosition();
                Snackbar.make(findViewById(R.id.btn_submit),"Data Berhasil Ditambahkan", Snackbar.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), mngDashboard.class));
                    }
                }, 1000);
            }
        });
    }
}
