package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import android.content.DialogInterface;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CallActivity extends Activity{
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.call_layout);
        Intent intent = getIntent();
        Button callButton = findViewById(R.id.do_call);
        String phone = intent.getStringExtra("phone");

        callButton.setOnClickListener(view -> {
            Intent call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:"+phone));
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(call);
            } else {
                // Demande la permission si elle n'est pas accordée
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            }
            //Toast.makeText(this,phone,Toast.LENGTH_SHORT).show();
        });
    }
}
