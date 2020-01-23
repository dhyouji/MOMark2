package tif.gaskeun.masodin2.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tif.gaskeun.masodin2.Model.Menu;
import tif.gaskeun.masodin2.R;

public class ordMenuInfo extends AppCompatActivity {

    public TextView namaMn,deskpMn,hargaMn,assignKey;
    public Button btnBack;
    public DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormenu_info);

        assignKey = findViewById(R.id.ordtestkey);
        namaMn = findViewById(R.id.orddetailNama);
        deskpMn = findViewById(R.id.orddetailDeskripsi);
        hargaMn = findViewById(R.id.orddetailHarga);

        final String key = getIntent().getStringExtra("key");
        assignKey.setText(key);

        dbref = FirebaseDatabase.getInstance().getReference("DataResto");
        dbref.child("DaftarMenu").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    namaMn.setText(menu.getNama());
                    deskpMn.setText(menu.getDeskripsi());
                    hargaMn.setText(menu.getHarga());
                }
                ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                startActivity(new Intent(getApplicationContext(), ordMenuList.class));
            }
        });
    }
}
