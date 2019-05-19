package com.example.dyson.a1_backup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText mMessageEmail;
    private EditText mMessagePassword;
    public static final String EXTRA_MESSAGE_email =
            "com.example.android.twoactivities.extra.MESSAGE";
    public static final String EXTRA_MESSAGE_password =
            "com.example.android.twoactivities.extra.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageEmail=findViewById(R.id.email_text);
        mMessagePassword=findViewById(R.id.password_text);
    }

    public void lanuchsecondactivity(View view) {
        Intent intent_one=new Intent(this,SecondActivity.class);
        String email_address=mMessageEmail.getText().toString();
        String password_message=mMessagePassword.getText().toString();
        intent_one.putExtra(EXTRA_MESSAGE_password,password_message);
        intent_one.putExtra(EXTRA_MESSAGE_email,email_address);
        startActivity(intent_one);

    }
}
