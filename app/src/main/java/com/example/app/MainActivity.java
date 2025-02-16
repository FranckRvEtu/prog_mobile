package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import android.content.DialogInterface;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        Button submitButton = findViewById(R.id.valid_button);
        RelativeLayout parent  = findViewById(R.id.main_layout);

        submitButton.setOnClickListener(view -> {

            ArrayList<String> parametres = new ArrayList<>();
            boolean flag = true;

            for (int i=0; i<parent.getChildCount();i++){
                View child = parent.getChildAt(i);
                if (child instanceof EditText){
                    if (((EditText) child).getText().toString().isEmpty()&&flag){
                        Toast.makeText(this,"Au moins un champ est vide !",Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                    if (child.getId()==R.id.AgeField){
                        parametres.add(((EditText) child).getText().toString()+" ans");
                    }
                    else{
                        parametres.add(((EditText) child).getText().toString());
                    }
                }
            }
            if(flag){
                showDialog(parametres);
            }
        });
    }
    private void showDialog(ArrayList<String> parametres){
        StringBuilder message = new StringBuilder();
        parametres.forEach(s -> {
            message.append(s);
            message.append("\n");
        });
        Intent intent = new Intent(this, ConfirmActivity.class);
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setMessage(message)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                intent.putExtra("fst_name",parametres.get(0));
                                intent.putExtra("snd_name",parametres.get(1));
                                intent.putExtra("age",parametres.get(2));
                                intent.putExtra("skill",parametres.get(3));
                                intent.putExtra("phone",parametres.get(4));
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton(R.string.negative_button,null)
                .show();
    }
}
