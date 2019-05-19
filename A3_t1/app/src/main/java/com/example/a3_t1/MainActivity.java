package com.example.a3_t1;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements nextAct_interface{
    FirebaseAuth mauth;
    EditText email;
    EditText pass;
    String e_message;
    String p_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        email=findViewById(R.id.editText);
        pass=findViewById(R.id.editText2);

        mauth=FirebaseAuth.getInstance();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mauth.getCurrentUser();
        //updateUI(currentUser);
    }
    public Intent setup(Class c1, Class c2){
        Intent main2home=new Intent(this,HomePage.class);
        main2home.putExtra("useremail",e_message);
        main2home.putExtra("userpass",p_message);
        return main2home;

    }
    public void startnext(Intent i){
        startActivity(i);
    }


    public void logintoaccount(View view) {
        e_message=email.getText().toString();
        p_message=pass.getText().toString();
        login L=new login(e_message,p_message,mauth,this);
        L.user_authentication();
    }

    public void registerfornewaccount(View view) {
        Intent main2register=new Intent(this,RegisterActivity.class);
        startnext(main2register);
    }
}
