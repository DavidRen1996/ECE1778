package com.example.a3_t1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login {
    private String login_email;
    private String login_password;
    private FirebaseAuth myauth;
    private Context context;
    //define variables that need user input
    int auth=0;
    //for auth use
    public login(String email,String password,FirebaseAuth au_one,Context c0){
        //build function for class
        this.login_email=email;
        this.login_password=password;
        this.myauth=au_one;
        this.context=c0;

    }
    public void user_authentication(){
        if (myauth!=null) {
            //sign in existing account
            myauth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                //overide firebaseauth function oncomplete, return boolean value to tell if sign in is success
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast t0 = Toast.makeText(context, "Sign In Success", Toast.LENGTH_SHORT);
                        t0.show();
                        auth = 1;
                        Intent main2home=new Intent(context,HomePage.class);
                        main2home.putExtra("useremail",login_email);
                        context.startActivity(main2home);
                    } else {
                        //make tost in non-activity class need context from activity
                        Toast t1 = Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT);
                        t1.show();
                        auth = 0;
                    }
                }
            });
        }

    }
}
