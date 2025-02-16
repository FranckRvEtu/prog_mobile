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


public class ConfirmActivity extends Activity{
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.second_activity);
        Intent intent = getIntent();
        if (intent != null) {
            String firstName = intent.getStringExtra("fst_name");
            String secondName = intent.getStringExtra("snd_name");
            String age = intent.getStringExtra("age");
            String skill = intent.getStringExtra("skill");
            String phone = intent.getStringExtra("phone");
            // Affichage pour debug
            //Toast.makeText(this, "Nom: " + firstName + ", Âge: " + age, Toast.LENGTH_LONG).show();
            RelativeLayout parent = findViewById(R.id.second_layout);
            /*for (int i=0; i < parent.getChildCount(); i++){
                View child = parent.getChildAt(i);
                if(child instanceof TextView){
                    String txt = ((TextView) child).getText().toString() + ;
                    ((TextView) child).setText(txt+firstName);
                }*/
            TextView firstNameText = findViewById(R.id.firstname);
            TextView secondNameText = findViewById(R.id.name_text);
            TextView ageText = findViewById(R.id.age);
            TextView skillsText = findViewById(R.id.skills);
            TextView phoneText = findViewById(R.id.phone);

            firstNameText.setText(firstNameText.getText().toString() + firstName);
            secondNameText.setText(secondNameText.getText().toString() + secondName);
            ageText.setText(ageText.getText().toString() + age);
            skillsText.setText(skillsText.getText().toString()+ skill);
            phoneText.setText(phoneText.getText().toString() + phone);

            Button callButton = findViewById(R.id.go_call);
            Button cancel = findViewById(R.id.no_call);

            callButton.setOnClickListener(view -> {
                Intent go_next = new Intent(this,CallActivity.class);
                go_next.putExtra("phone",phone);
                startActivity(go_next);
            });
        }
    }
}
