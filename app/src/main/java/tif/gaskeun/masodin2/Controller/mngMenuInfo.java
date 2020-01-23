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

public class mngMenuInfo extends AppCompatActivity {

    public TextView namaMn,deskpMn,hargaMn,assignKey;
    public Button btnDel,btnEdit;
    public DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);

        assignKey = findViewById(R.id.testkey);
        namaMn = findViewById(R.id.detailNama);
        deskpMn = findViewById(R.id.detailDeskripsi);
        hargaMn = findViewById(R.id.detailHarga);

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

        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mngMenuEdit.class).putExtra("key",key));
            }
        });

        btnDel = findViewById(R.id.btn_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(mngMenuInfo.this).setTitle("Hapus Data").setMessage("Data Akan Di Hapus?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbref.child("DaftarMenu").child(key).removeValue();
                                ;
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Data Berhasil Ditambahkan", Snackbar.LENGTH_LONG).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(getApplicationContext(), mngDashboard.class));
                                    }
                                }, 1000);
                            }
                        })
                        .setNegativeButton("Batal", null).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });
    }
}
