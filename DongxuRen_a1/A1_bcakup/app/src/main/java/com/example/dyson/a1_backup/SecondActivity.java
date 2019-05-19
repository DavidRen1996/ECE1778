package com.example.dyson.a1_backup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");
        setSupportActionBar(toolbar);
        Intent Main=getIntent();
        mImageView=findViewById(R.id.imageView);
        Name_content=findViewById(R.id.user_label);
        Bio_content=findViewById(R.id.bio_label);


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
            mImageView.setImageBitmap(imageBitmap);
            buff=Bitmap2Bytes(imageBitmap);

        }
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
        /*if (e1.getText().toString().length()==0){//remember to get text before tostring
            return  false;

        }
        else{
            return true;
        }*/
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
        Log.d("System.out",mark+"aaaaaaa");
        if (mark==1){
            return false;
        }
        else {
            return true;
        }

    }
    public void lanuchThirdActivity(View view) {
        boolean full=content_detect();
        //System.out.print("my name is van");

        if (full){
            Log.d("System.out","true");
        }
        else{
            Log.d("System.out","false");
        }
        //Log.d(LOG_TAG,full.toString());
        if (full){
            Bundle b2to3=new Bundle();
            Intent intent_two=new Intent(this,ThirdActivity.class);
            String NAME=Name_content.getText().toString();
            //Log.d(LOG_TAG,NAME);
            String USER=Bio_content.getText().toString();
       /* intent_two.putExtra(EXTRA_MESSAGE_name,NAME);
        intent_two.putExtra(EXTRA_MESSAGE_bio,USER);
        intent_two.putExtra(EXTRA_MESSAGE_image,buff);*/
            b2to3.putString(EXTRA_MESSAGE_name,NAME);
            b2to3.putString(EXTRA_MESSAGE_bio,USER);
            b2to3.putByteArray(EXTRA_MESSAGE_image,buff);
            intent_two.putExtra(EXTRA_MESSAGE_intent,b2to3);
            startActivity(intent_two);
        }
        else {
            Toast t1 = Toast.makeText(this, "please fill in all blanks", Toast.LENGTH_SHORT);
        }


    }
}
