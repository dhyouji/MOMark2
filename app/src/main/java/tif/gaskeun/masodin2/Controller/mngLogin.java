package tif.gaskeun.masodin2.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import tif.gaskeun.masodin2.R;

public class mngLogin extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText et_uname, et_passwd;
    public Button sign,signup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

        et_uname = findViewById(R.id.et_uname);
        et_passwd = findViewById(R.id.et_pswd);
        sign = findViewById(R.id.btn_signin);
        signup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_uname.getText().toString().trim();
                String passwd = et_passwd.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    et_uname.setError("Please Fill The Email");
                    return;
                }
                if (passwd.length() < 6) {
                    et_passwd.setError("Minimum Password Length is 6 Characters");
                }

                mAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mngLogin.this, "SignIn Success", Toast.LENGTH_SHORT).show();
                            String key = mAuth.getUid();
                            startActivity(new Intent(getApplicationContext(), mngDashboard.class).putExtra("Key",key));
                        } else {
                            Toast.makeText(mngLogin.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),mngCreateAcc.class));
                finish();
            }
        });
    }
}
