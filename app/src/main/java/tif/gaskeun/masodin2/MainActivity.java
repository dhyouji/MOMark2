package tif.gaskeun.masodin2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import tif.gaskeun.masodin2.Controller.IFirebaseLoadDone;
import tif.gaskeun.masodin2.Controller.mngLogin;
import tif.gaskeun.masodin2.Controller.ordMenuList;
import tif.gaskeun.masodin2.Model.Informasi;


public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    public Button btnOk;
    public SearchableSpinner spnRest;
    public TextView login;
    DatabaseReference dbref;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Informasi> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iFirebaseLoadDone = this;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.child("Informasi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Informasi> infox = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    infox.add(dataSnapshot.getValue(Informasi.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(infox);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spnRest = findViewById(R.id.spnResto);
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "bpBsoBLZKmd3UxOYoPYmLsdsxwi1";
                startActivity(new Intent(getApplicationContext(), ordMenuList.class).putExtra("Key",key));
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), mngLogin.class));
            }
        });

    }

    @Override
    public void onFirebaseLoadSuccess(List<Informasi> informasi) {
        info = informasi;
        List<String> resto = new ArrayList<>();
        for(Informasi informasi1:informasi)
            resto.add(informasi1.getRestoran());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,resto);
        spnRest.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailed(String Message) {

    }
}
