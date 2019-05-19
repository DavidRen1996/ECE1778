package com.example.dyson.a2_t2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ThirdActivity extends AppCompatActivity {
    private FirebaseStorage fs1=FirebaseStorage.getInstance();
    private ImageView image_photo;
    private static final String LOG_TAG = ThirdActivity.class.getSimpleName();
    private TextView t1;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private RecyclerView mRecyclerView;
    public List<Bitmap> mDatas;
    private MyAdapter myAdapter;
    DocumentReference d3f;
    private List<String> vans;
    private String thumbnail_example;
    private FirebaseAuth fire1;
    byte[] bio_byte;
    ImageView bio_image;
    List<String>receive_thumb=new ArrayList<>();
    String eid;
    String address;
    String x;
    String y;
    private View v0;
    int p1;
    Bitmap b1;
    Button ib;
    Button ishow;
    String nail;
    String photoplace;
    int size=0;


    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    public void fetch_user() {
        d3f.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String bio_add = documentSnapshot.getString("user bio");
                    t1.setText(bio_add);
                }
            }
        });
    }



    public void fetch_userbiophoto() {
        d3f.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    receive_thumb = (List<String>) documentSnapshot.get("userthumbnail");
                    //Log.d("the truth is ",receive_thumb.size()+"this number");
                    String name1=(String)documentSnapshot.get("name");
                    String bio_one=(String)documentSnapshot.get("user bio");
                    TextView t0=findViewById(R.id.bio_third);
                    TextView t1=findViewById(R.id.name_third);
                    t0.setText(bio_one);
                    t1.setText(name1);
                }

            }
        });

    }
    public void returnReply() {
        /*get the text of the EditText as a string:*/
        Intent intent_reply=new Intent(this,MainActivity.class);/*don't reuse the Intent object that you received from the original request.*/
        /*Add the reply string from the EditText to the new intent as an Intent extra. Because extras are key/value pairs, here the key is EXTRA_REPLY, and the value is the reply:*/
        startActivity(intent_reply);


    }

    public void signoutaccount(View view) {
        fire1.signOut();
        returnReply();

    }
    public void getdata() {

        Log.d("system.out","getdata function is called");
        fetch_userbiophoto();
        int k=receive_thumb.size();
        //Log.d("fffff",k+"what is ");
        for (size=0;size<k;size++){
            String fake=receive_thumb.get(size);
            //Log.d("fffff",size+"count");
            StorageReference photo=fs1.getReferenceFromUrl(fake);
            final long ONE_MEGABYTE = 1024 * 1024;
            photo.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    //get photo and set it on bio
                    bio_byte=bytes;
                    Bitmap stitchBmp = Bytes2Bimap(bio_byte);

                    Log.d("system.out","getdata function inner class is called");

                    mDatas.add(stitchBmp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        if (mDatas.size()>0){
            Bitmap bb=mDatas.get(0);
            if(bb!=null){
                bio_image.setImageBitmap(bb);
            }
        }

    }


    public void startadapter(View view) {

        getdata();
        Log.d("system out","mdata size is"+mDatas.size()+"before showing");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(myAdapter = new MyAdapter(this,mDatas));
        send();
    }
    public void send(){
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Bitmap data) {

                //Toast t=Toast.makeText(ThirdActivity.this,"vans"+position,Toast.LENGTH_SHORT);
                //startActivity(i4);
                v0=view;
                p1=position;
                b1=data;

            }
        });
    }


    public void click(View view,int position, Bitmap data) {
        Toast t1=Toast.makeText(this,"I click",Toast.LENGTH_SHORT);
        t1.show();
        Intent intent_4=new Intent(this,FourthActivity.class);
        byte[] byte_one=Bitmap2Bytes(data);
        intent_4.putExtra("position",position);
        intent_4.putExtra("photo",byte_one);

        startActivity(intent_4);

    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

    public void dooooo(View view) {
        getdata();
    }

    public void addCamera(View view) {
        Intent intent_camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent_camera,1);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Bitmap newb;
        if (requestCode == 1 && resultCode == RESULT_OK){
            /*int a_size=mDatas.size();
            Log.d("system out",a_size+"size");
            if (a_size>0){
                Log.d("system out","mdatas oversize again");
                int m=(a_size-1)/2;
                Iterator i1=mDatas.iterator();
                int count=0;
                List<Bitmap>md;
                md=new ArrayList<>();
                while (count<m+1){
                    if (count==m){
                        md.add(mDatas.get(a_size-1));
                    }
                    else{
                        md.add(mDatas.get(count));
                    }

                    count=count+1;
                }
                mDatas=md;
                Log.d("system out","mdatas size is:"+mDatas.size()+"before update storeage");

            }*/
            Bundle extras = data.getExtras();
            System.out.print(data);
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            newb=imageBitmap;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            add_itemtoview(newb);
            byte[] byte_upload = baos.toByteArray();

            upload2firebase(byte_upload);
            Log.d("system out","mdatas size is:"+mDatas.size()+"before update database");
            updatedatabase();
            Log.d("system out","mdatas size is:"+mDatas.size()+"after taking a photo");
            //mImageView.setImageBitmap(imageBitmap);
            //byte[] byte_upload=Bitmap2Bytes(imageBitmap);


        }
    }
    public void upload2firebase(byte[] b1){
        //upload new photo to storage
        Calendar cc = Calendar.getInstance();
        Date date = cc.getTime();
        // SimpleDateFormat format1 = new SimpleDateFormat("dd MMM");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = format2.format(date).toString();
        photoplace=timestamp;
        String time= com.google.firebase.firestore.FieldValue.serverTimestamp().toString();
        StorageReference news1=fs1.getReference();
        StorageReference s1place;
        if (x.equals(y)){
            s1place=news1.child("/photoes/"+address+"/"+timestamp);
        }
        else{
            s1place=news1.child("/photoes/"+address+"/"+timestamp);
        }

        UploadTask up_thumb=s1place.putBytes(b1);
    }
    public void updatedatabase(){
        //String time= com.google.firebase.firestore.FieldValue.serverTimestamp().toString();
        Calendar cc = Calendar.getInstance();
        Date date = cc.getTime();
        // SimpleDateFormat format1 = new SimpleDateFormat("dd MMM");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //photoplace = format2.format(date).toString();
        StorageReference news1=fs1.getReference();
        String p=news1.toString()+"photoes/"+address+"/"+photoplace;
        receive_thumb.add(p);
        DocumentReference newDoc_updata=db.document("user_one/"+address);
        newDoc_updata.update("userthumbnail",receive_thumb).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("sss", "DocumentSnapshot successfully updated!");
                }
                else {
                    Log.d("sss", "DocumentSnapshot failed updated!");
                }
            }
        });


    }
    public void add_itemtoview(Bitmap b0){
        myAdapter.addItem(1,b0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        //initData();
        mDatas = new ArrayList<>();
        fire1=FirebaseAuth.getInstance();
        bio_image=findViewById(R.id.imageView3);
        Intent intent_three = getIntent();
        x=intent_three.getStringExtra("activity_name");
        y="Secondactivity";
        //Log.d("System.out",x);
        ib=findViewById(R.id.button6);
        ishow=findViewById(R.id.button5);
        //send();


        if (x.equals(y)){
            String current_uid=intent_three.getStringExtra(SecondActivity.EXTRA_MESSAGE_intent);
            d3f=db.document("user_one/"+current_uid);
            //can be added as function
            address=intent_three.getStringExtra(SecondActivity.EXTRA_MESSAGE_intent);
            nail=intent_three.getStringExtra("user thumbnail");
            //Log.d("fffff",nail);
            StorageReference storageRef = fs1.getReference();
            String name=intent_three.getStringExtra("uername");
            String bios=intent_three.getStringExtra("user_bio");
            TextView t1=findViewById(R.id.name_third);
            TextView t2=findViewById(R.id.bio_third);
            t1.setText(name);
            t2.setText(bios);


        }
        else {
            address=intent_three.getStringExtra("email address");
            eid=intent_three.getStringExtra("email address");
            d3f=db.document("user_one/"+eid);
            String u1=fire1.getCurrentUser().getUid();

            Log.d("system out","current user is:"+u1);
            StorageReference storageRef = fs1.getReference();


        }
        //String current_uid=intent_three.getStringExtra(SecondActivity.EXTRA_MESSAGE_intent);




        //t1=findViewById(R.id.textView3);
        //fetch_user();


    }
    /*@Override
    public void onStart(){
        super.onStart();
        if(x.equals(y)){
            StorageReference photo=fs1.getReferenceFromUrl(nail);
            //StorageReference photo=fs1.getReferenceFromUrl("gs://a2t2-b185b.appspot.com/photoes/user/123456@189.com");
            final long ONE_MEGABYTE = 1024 * 1024;
            photo.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    //get photo and set it on bio
                    bio_byte=bytes;
                    Bitmap stitchBmp = Bytes2Bimap(bio_byte);
                    bio_image.setImageBitmap(stitchBmp);
                    mDatas.add(stitchBmp);
                    Log.d("fffff",mDatas.size()+"mData size onstart");
                    //int k=mDatas.size();
                    // Data for "images/island.jpg" is returns, use this as needed
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("dddddd","failed getting user bio_photo");
                }
            });
        }

        int k=0;
        while((k<10)&&(mDatas.size()==0)){
            k=k+1;
            ib.performClick();
            Log.d("ddddf","click button for once");
        }
        ishow.performClick();
    }*/
}


















