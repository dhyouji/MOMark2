package tif.gaskeun.masodin2.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tif.gaskeun.masodin2.R;

public class mngCreateAcc extends AppCompatActivity {

    private Button btDaftar;
    private EditText etEmail,etNama,etPasswd;
    FirebaseAuth mAuth;
    DatabaseReference dbref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signup);

        etEmail = findViewById(R.id.et_addemail);
        etPasswd = findViewById(R.id.et_addpswd);
        btDaftar = findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference();

        btDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String passwd = etPasswd.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is Required");
                    return;
                }
                if (passwd.length() < 6){
                    etPasswd.setError("password Must be >= 6 Charaters");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String key = mAuth.getUid();
                            startActivity(new Intent(getApplicationContext(),mngInfoRest.class).putExtra("Key",key));
                            finish();
                        }
                    }
                });
            }
        });


    }
}