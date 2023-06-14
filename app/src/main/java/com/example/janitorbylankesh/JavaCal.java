package com.example.janitorbylankesh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JavaCal extends AppCompatActivity {
    private Button add;
    private EditText et1, et2, et3, et4, et5, et6, et7;
    private TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_cal);


        et1 = (EditText)findViewById(R.id.editTextNumber6);
        et2 = (EditText)findViewById(R.id.editTextNumber7);
        et3 = (EditText)findViewById(R.id.editTextNumber8);
        et4 = (EditText)findViewById(R.id.editTextNumber9);
        et5 = (EditText)findViewById(R.id.editTextNumber10);
        et6 = (EditText)findViewById(R.id.editTextNumber11);
        et7 = (EditText)findViewById(R.id.editTextNumber12);
        result = (TextView)findViewById(R.id.editTextNumber13);
        add = (Button) findViewById(R.id.addfunc);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int n1 = Integer.parseInt(et1.getText().toString());
                int n2 = Integer.parseInt(et2.getText().toString());
                int n3 = Integer.parseInt(et3.getText().toString());
                int n4 = Integer.parseInt(et4.getText().toString());
                int n5 = Integer.parseInt(et5.getText().toString());
                int n6 = Integer.parseInt(et6.getText().toString());
                int n7 = Integer.parseInt(et7.getText().toString());

                int abc = (n1*600)+(n2*400)+(n3*500)+(n4*750)+(n5*800)+(n6*500)+(n7*400);
                result.setText("Total Fee: "+ String.valueOf(abc)+ "/=");

                result.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);



            }
        });


    }
}