package com.example.dyson.a1_backup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    private ImageView image_photo;
    private static final String LOG_TAG = ThirdActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Intent intent_three = getIntent();
        Bundle b3receive2=new Bundle();
        b3receive2=intent_three.getBundleExtra(SecondActivity.EXTRA_MESSAGE_intent);
        //String user_name = intent_three.getStringExtra(SecondActivity.EXTRA_MESSAGE_name);
        //String user_name="vans";
        String user_name=b3receive2.getString(SecondActivity.EXTRA_MESSAGE_name);
        String bio_text=b3receive2.getString(SecondActivity.EXTRA_MESSAGE_bio);
       // Log.d(LOG_TAG,user_name);
        //String bio_text = intent_three.getStringExtra(SecondActivity.EXTRA_MESSAGE_bio);
        //byte b3[] = intent_three.getByteArrayExtra(SecondActivity.EXTRA_MESSAGE_image);
        byte b3[]=b3receive2.getByteArray(SecondActivity.EXTRA_MESSAGE_image);
        Bitmap stitchBmp = Bytes2Bimap(b3);
        image_photo = findViewById(R.id.imageView3);

        TextView name_text = findViewById(R.id.name_third);
        name_text.setText(user_name);
        TextView bio_txt = findViewById(R.id.bio_third);
        bio_txt.setText(bio_text);
        image_photo.setImageBitmap(stitchBmp);


    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
