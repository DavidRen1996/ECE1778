package com.example.a3_t1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    ImageView bio_photo;
    EditText em;
    EditText pass_w;
    EditText confirm_pw;
    EditText user;
    EditText bio;
    Bitmap photo_upload;
    FirebaseAuth auth_one;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference thumb_ref = storage.getReference();
    private StorageReference thumb=thumb_ref.child("photoes");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bio_photo=findViewById(R.id.imageView);
        em=findViewById(R.id.editText3);
        pass_w=findViewById(R.id.editText4);
        confirm_pw=findViewById(R.id.editText5);
        user=findViewById(R.id.editText6);
        bio=findViewById(R.id.editText7);
        auth_one=FirebaseAuth.getInstance();
    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

    public void take_homepage_photo(View view) {
        takephoto();
    }
    public void takephoto(){
        Intent intent_camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent_camera,1);
            //cr.startActivity();startActivityForResult(intent_camera,1);
        }
        else{
            Toast.makeText(this,"Camera Failed",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            bio_photo.setImageBitmap(imageBitmap);
            photo_upload=imageBitmap;
        }
        else{
            Toast.makeText(this,"Camera Failed",Toast.LENGTH_SHORT).show();
        }
    }

    public void signup_account(View view) {
        String email=em.getText().toString();
        String password=pass_w.getText().toString();
        String username=user.getText().toString();
        String bio_info=bio.getText().toString();
        create_user user_one=new create_user(email,password,username,bio_info,db,thumb,auth_one,this,photo_upload);
        user_one.upload_storage(thumb,photo_upload);
        //remember android execute in async order(not serial order,line by line like scripts) everything follows has to added in oncomplete or await!!!!!!!!
        //otherwise if send intent like below lines, the next activity will be launched without finishing creation of the user!!!!
        //user_one.upload_database();
        //Intent intentR2home=new Intent(this,HomePage.class);
        //intentR2home.putExtra("useremail",email);
        //startActivity(intentR2home);
    }
}
