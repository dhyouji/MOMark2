package tif.gaskeun.masodin2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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


public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone{

    public Button btnOk;
    public SearchableSpinner spnRest;
    public TextView login;
    DatabaseReference dbref;
    IFirebaseLoadDone iFirebaseLoadDone;
    FirebaseAuth mAuth;
    List<Informasi> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        iFirebaseLoadDone = this;
        dbref = FirebaseDatabase.getInstance().getReference();

        spnRest = findViewById(R.id.spnResto);

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Informasi> info = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    info.add(snapshot.getValue(Informasi.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });

        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = spnRest.getSelectedItem().toString();
                Query query = dbref.orderByChild("restoran").equalTo(name);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = "";
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            key = snapshot.getKey().toString();
                            startActivity(new Intent(getApplicationContext(), ordMenuList.class).putExtra("Key",key));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
    public void onFirebaseLoadSuccess(List<Informasi> infox) {
        info = infox;
        List<String> namarest = new ArrayList<>();
        for(Informasi informasi:infox)
            namarest.add(informasi.getRestoran());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,namarest);
            spnRest.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailed(String Message) {

    }
}
