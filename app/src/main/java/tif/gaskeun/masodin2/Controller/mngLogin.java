package tif.gaskeun.masodin2.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tif.gaskeun.masodin2.R;

public class mngLogin extends AppCompatActivity {

    public Button sign;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign = findViewById(R.id.btn_signin);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),mngDashboard.class));
            }
        });

    }
}
