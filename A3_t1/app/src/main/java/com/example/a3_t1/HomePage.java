package com.example.a3_t1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    FirebaseFirestore store_zero=FirebaseFirestore.getInstance();
    ImageView bioimage;
    RecyclerView mRecyclerView;
    RVadapter adapter;
    String Email;
    FirebaseAuth fire1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bioimage =findViewById(R.id.imageView2);
        Intent receiveRegister=getIntent();
        final String useremail=receiveRegister.getStringExtra("useremail");
        Email=receiveRegister.getStringExtra("useremail");
        loadimage(store_zero,bioimage,useremail);
        Log.d("system.out","oncreate is executed");
        fire1=FirebaseAuth.getInstance();//instance for firebase auth

    }
    //every time this activity is called it completes a full process of oncreate,start,resume
    //so if return from another activity it is oncreate that first be executed, not onresume
    @Override
    protected void onStart(){
        super.onStart();

    }
    /*@Override
    protected void onResume(){
        super.onResume();
        Intent resume=getIntent();
        String newuri=resume.getStringExtra("newphotouri");
        //int k=adapter.getItemCount();
        //adapter.addItem(k,newuri);
        Log.d("system.out","onresume is executed");

    }*/
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }
    public void loadimage(FirebaseFirestore ff,final ImageView bioimage,final String useremail){
        DocumentReference d=store_zero.document("users/"+useremail);
        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    String n=(String)documentSnapshot.get("user password");
                    Log.d("system.out","password is:"+n);
                    List<String>time=(List<String>)documentSnapshot.get("time");
                    String uname=(String)documentSnapshot.get("name");
                    String ubio=(String)documentSnapshot.get("user bio");
                    TextView username=findViewById(R.id.textView3);
                    TextView userbio=findViewById(R.id.textView4);
                    username.setText(uname);
                    userbio.setText(ubio);
                    int k=time.size();
                    Log.d("system.out","time size is:"+k);
                    List<String>download_uri=new ArrayList<>();
                    List<String>o=new ArrayList<>();
                    List<String>time_list=new ArrayList<>();
                    mRecyclerView= (RecyclerView) findViewById(R.id.recycleview);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(HomePage.this,3));
                    mRecyclerView.setAdapter(adapter = new RVadapter(HomePage.this,download_uri,time_list,useremail,o));
                    adapter.setOnItemClickListener(new RVadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, String data) {
                            Intent home2display=new Intent(HomePage.this,Display.class);
                            //String uri=datas.get(position);
                            //home2display.putExtra("photouri",uri);
                            startActivity(home2display);
                        }
                    });
                    if (k==1){
                        String times=time.get(0);
                        DocumentReference d2=store_zero.document("users/"+useremail+"/"+times+"/infos");
                        d2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String photo_uri=(String)documentSnapshot.get("downloaduri");
                                    Log.d("system.out","receive uri is:"+photo_uri);
                                    Picasso.get().load(photo_uri).into(bioimage);
                                }
                            }
                        });
                    }
                    else{
                        /*mRecyclerView= (RecyclerView) findViewById(R.id.recycleview);
                        mRecyclerView.setLayoutManager(new GridLayoutManager(HomePage.this,3));
                        mRecyclerView.setAdapter(adapter = new RVadapter(HomePage.this,download_uri));*/
                        String ts=time.get(0);
                        DocumentReference d1=store_zero.document("users/"+useremail+"/"+ts+"/infos");
                        d1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String photo_uri=(String)documentSnapshot.get("downloaduri");
                                    Log.d("system.out","receive uri is:"+photo_uri);
                                    Picasso.get().load(photo_uri).into(bioimage);
                                }
                            }
                        });
                        for (int i=1;i<k;i++){
                            final String times=time.get(i);
                            final List<String>count_size=download_uri;
                            final int length_uri=count_size.size();
                            Log.d("data size:",length_uri+"");
                            DocumentReference d2=store_zero.document("users/"+useremail+"/"+times+"/infos");
                            d2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        String photo_uri=(String)documentSnapshot.get("downloaduri");
                                        Log.d("retrived directory",times);
                                        adapter.addItem(length_uri,photo_uri,times,useremail);
                                    }
                                }
                            });
                        }
                    }




                    //Log.d("system.out","time is:"+time);
                }
            }
        });
    }


    public void takephoto(View view) {
        Intent intent_camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_camera.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent_camera,1);
            //cr.startActivity();startActivityForResult(intent_camera,1);
        }
        else{
            Toast.makeText(this,"Camera Failed",Toast.LENGTH_SHORT).show();
        }
    }
    //notice onactivityresult can only be accessed within an activity class
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            byte[] b= Bitmap2Bytes(imageBitmap);
            Intent home2context=new Intent(this,Context.class);
            home2context.putExtra("imagebyte",b);
            home2context.putExtra("email",Email);
            startActivity(home2context);
        }
        else{
            Toast.makeText(this,"Camera Failed",Toast.LENGTH_SHORT).show();
        }
    }

    public void jump2globle(View view) {
        Intent home2globle=new Intent(this,Gloable.class);
        //pass current sign in user email
        home2globle.putExtra("useremail",Email);
        startActivity(home2globle);
    }

    public void signout_current_account(View view) {
        fire1.signOut();
        returnReply();
    }
    public void returnReply() {
        /*get the text of the EditText as a string:*/
        Intent intent_reply=new Intent(this,MainActivity.class);/*don't reuse the Intent object that you received from the original request.*/
        /*Add the reply string from the EditText to the new intent as an Intent extra. Because extras are key/value pairs, here the key is EXTRA_REPLY, and the value is the reply:*/
        startActivity(intent_reply);


    }
}
