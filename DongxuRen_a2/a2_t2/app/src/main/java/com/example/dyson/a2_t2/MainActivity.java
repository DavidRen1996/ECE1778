package com.example.dyson.a2_t2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mMessageEmail;
    private EditText mMessagePassword;
    public static final String EXTRA_MESSAGE_email =
            "com.example.android.twoactivities.extra.MESSAGE";
    public static final String EXTRA_MESSAGE_password =
            "com.example.android.twoactivities.extra.MESSAGE";

    private FirebaseAuth mauth;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();
        mMessageEmail=findViewById(R.id.email_text);
        mMessagePassword=findViewById(R.id.password_text);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mauth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void lanuchsecondactivity(View view) {
        Intent i1=new Intent(this,SecondActivity.class);
        //Bundle fb1=new Bundle();

        startActivity(i1);
    }

    public void signinexist(View view) {
        EditText e1=findViewById(R.id.email_text);
        email=e1.getText().toString();
        EditText e2=findViewById(R.id.password_text);
        String password=e2.getText().toString();
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("tag", "signInWithEmail:success");
                    FirebaseUser user = mauth.getCurrentUser();
                    Intent i3=new Intent(MainActivity.this,ThirdActivity.class);
                    i3.putExtra("email address",email);

                    i3.putExtra("activity_name","Mainactivity");
                    startActivity(i3);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("tag", "signInWithEmail:failure", task.getException());
                    failmessage();

                }
            }
        });

    }



    public void failmessage(){
        Toast t= Toast.makeText(this,"failed",Toast.LENGTH_SHORT);
        t.show();
    }
}
