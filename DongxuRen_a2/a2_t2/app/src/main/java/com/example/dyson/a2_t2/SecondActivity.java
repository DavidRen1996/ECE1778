package com.example.dyson.a2_t2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_image =
            "com.example.android.twoactivities.extra.MESSAGE";
    public static final String EXTRA_MESSAGE_name =
            "com.example.android.twoactivities.extra.MESSAG";
    public static final String EXTRA_MESSAGE_bio =
            "com.example.android.twoactivities.extra.MESSA";
    public static final String EXTRA_MESSAGE_intent =
            "com.example.android.twoactivities.extra.MESS";
    public static byte buff[] = new byte[125*250];
    private static final String LOG_TAG = SecondActivity.class.getSimpleName();
    private EditText Name_content;
    private EditText Bio_content;
    private ImageView mImageView;
    private static Bitmap MB;
    private FirebaseAuth mFBauth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    String newuser_name;
    String l1;
    Bitmap b1;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference thumb_ref = storage.getReference();
    private StorageReference thumb=thumb_ref.child("photoes");
    //private StorageReference thumb=thumb_up.child("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mFBauth=FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        Intent Main=getIntent();
        mImageView=findViewById(R.id.imageView);
        Name_content=findViewById(R.id.user_label);

        Bio_content=findViewById(R.id.bio_label);


    }

    public void lanuchCamera(View view) {

        Intent intent_camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent_camera,1);
        }

    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            System.out.print(data);
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            b1=imageBitmap;
            mImageView.setImageBitmap(imageBitmap);
            buff=Bitmap2Bytes(imageBitmap);

        }
    }


    public void lanuchThirdActivity(View view) {
        boolean full=content_detect();
        if (full){
            Log.d("System.out","true");
        }
        else{
            Log.d("System.out","false");
        }

        if (full){
            Bundle b2to3=new Bundle();
            Intent intent_two=new Intent(this,ThirdActivity.class);
            String NAME=Name_content.getText().toString();
            newuser_name=Name_content.getText().toString();
            //Log.d(LOG_TAG,NAME);
            String USER=Bio_content.getText().toString();
            b2to3.putString(EXTRA_MESSAGE_name,NAME);
            b2to3.putString(EXTRA_MESSAGE_bio,USER);
            b2to3.putByteArray(EXTRA_MESSAGE_image,buff);
            EditText eemail=findViewById(R.id.email_second);
            EditText epass=findViewById(R.id.editText2);
            EditText nameT=findViewById(R.id.user_label);
            String uname=nameT.getText().toString();
            String email=eemail.getText().toString();
            String password=epass.getText().toString();
            String self_bio=Bio_content.getText().toString();

            mFBauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("System.out", "createUserWithEmail:success");
                        Toast.makeText(SecondActivity.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("System.out", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SecondActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });

            FirebaseAuth f1=FirebaseAuth.getInstance();
            //FirebaseUser u1=f1.getCurrentUser().getUid();
            String ref_thub=thumb.toString()+"/"+email+"/"+email;
            List<String> thumb_one=new ArrayList<>();
            thumb_one.add(ref_thub);
            Map<String,Object> user_one=new HashMap<>();
            user_one.put("user email",email);
            user_one.put("user password",password);
            user_one.put("user bio",self_bio);
            user_one.put("userthumbnail",thumb_one);
            user_one.put("name",uname);
            DocumentReference df_one=db.document("user_one/"+email);
            df_one.set(user_one).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d("System.out", "storeuserinfo:success");
                    }
                    else{

                        Log.d("System.out", "storeuserinfo:fail");
                    }

                }
            });
            upload_thumb(email);


            //intent_two.putExtra(EXTRA_MESSAGE_intent,b2to3);
            intent_two.putExtra("user thumbnail",ref_thub);
            intent_two.putExtra(EXTRA_MESSAGE_intent,email);
            intent_two.putExtra("activity_name","Secondactivity");
            intent_two.putExtra("user_bio",self_bio);
            intent_two.putExtra("uername",uname);
            startActivity(intent_two);
        }
        else {
            Toast t1 = Toast.makeText(this, "please fill in all blanks", Toast.LENGTH_SHORT);
        }


    }
    public void signinnew_user(){
        EditText e=findViewById(R.id.email_second);
        EditText p=findViewById(R.id.editText2);
        String email=e.getText().toString();
        String password=p.getText().toString();
        mFBauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("tag", "signInWithEmail:success");
                    //FirebaseUser user = auth2.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("tag", "signInWithEmail:failure", task.getException());
                }
            }
        });
        Log.d("tag", "fuck u");
    }
    public void upload_thumb(String user){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference t1=thumb.child("/"+user+"/"+user);
        UploadTask up_thumb=t1.putBytes(data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean content_detect(){
        Toast t2 = Toast.makeText(this, "check", Toast.LENGTH_SHORT);
        t2.show();

        EditText e1=(EditText)findViewById(R.id.email_second);
        EditText e2=(EditText)findViewById(R.id.editText2);
        EditText e3=(EditText)findViewById(R.id.pw_confirm);
        EditText e4=(EditText)findViewById(R.id.bio_label);
        EditText arr[]={e1,e2,e3,e4};
        int mark=0;
        for (int i=0;i<1;i++){
            if (arr[i].getText().toString().length()==0) {
                //Log.d(LOG_TAG,"my name is van");
                System.out.print(arr[i].toString().length());
                Toast t1 = Toast.makeText(this, "fill in all blamks", Toast.LENGTH_SHORT);
                t1.show();
                mark=1;
                Log.d("System.out",mark+"2222");
                break;
            }
            else {
                Log.d("System.out","1111");
                System.out.print(arr[i].toString().length());
            }
        }
        if (mark==1){
            return false;
        }
        else {
            return true;
        }

    }
}
