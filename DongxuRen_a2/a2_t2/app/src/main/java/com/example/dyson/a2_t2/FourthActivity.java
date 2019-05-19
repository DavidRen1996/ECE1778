package com.example.dyson.a2_t2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        Intent ithree=getIntent();
        ImageView i1=findViewById(R.id.imageView4);
        byte[] b2=ithree.getByteArrayExtra("photo");
        Bitmap B_one=Bytes2Bimap(b2);
        i1.setImageBitmap(B_one);

    }
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
