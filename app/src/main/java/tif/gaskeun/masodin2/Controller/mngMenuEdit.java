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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tif.gaskeun.masodin2.Model.Menu;
import tif.gaskeun.masodin2.R;

public class mngMenuEdit extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbref;
    public EditText etNama,etDeskripsi,etHarga,etKatg;
    public Button btnSubmit;
    String key,keyItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_edit);
        mAuth = FirebaseAuth.getInstance();
        key = mAuth.getUid();
        keyItem = getIntent().getStringExtra("key");
        etNama = findViewById(R.id.et_nmMenu);
        etDeskripsi = findViewById(R.id.et_descMenu);
        etHarga = findViewById(R.id.et_prcMenu);
        etKatg = findViewById(R.id.et_katgMenu);
        btnSubmit = findViewById(R.id.btn_submit);

        dbref = FirebaseDatabase.getInstance().getReference(key);
        dbref.child("DaftarMenu").child(keyItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    etNama.setText(menu.getNama());
                    etDeskripsi.setText(menu.getDeskripsi());
                    etHarga.setText(menu.getHarga());
                    etKatg.setText(menu.getKategori());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !isEmpty(etNama.getText().toString()) && !isEmpty(etDeskripsi.getText().toString()) && !isEmpty(etHarga.getText().toString()) && !isEmpty(etKatg.getText().toString()))
                    Submit(new Menu((etNama.getText().toString()), (etDeskripsi.getText().toString()), (etHarga.getText().toString()),(etKatg.getText().toString())));
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
        dbref.child("DaftarMenu").child(keyItem).setValue(menu).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etNama.setText("");
                etDeskripsi.setText("");
                etHarga.setText("");
                etKatg.setText("");
                Snackbar.make(findViewById(R.id.btn_submit),"Data Berhasil Di Perbaharui", Snackbar.LENGTH_LONG).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), mngDashboard.class));
                        finish();
                    }
                }, 1000);
            }
        });
    }
}
