package com.example.a3_t1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class create_user {
    private String EMAIL;
    private String PASSWORD;
    private String USERNAME;
    private String BIO;
    private FirebaseFirestore store_one;
    private StorageReference base_reference;
    private String thumbnails;
    public List<String>http=new ArrayList<>();
    private List<String>comments=new ArrayList<>();
    private FirebaseAuth aone;
    private Bitmap B;

    public Uri down;
    Context c1;
    StorageReference bio_address;
    private String path;
    //get calender date tt
    Calendar cc = Calendar.getInstance();
    Date date = cc.getTime();
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    String tt = format2.format(date).toString();
    private List<String>timestamp=new ArrayList<>();
    create_user(){}//for other classes to extend but not used
    //below is the build function for class create_user to initialize it
    public create_user(String EMAIL,String PASSWORD,String USERNAME,String BIO,FirebaseFirestore store_one,StorageReference base_reference,FirebaseAuth aone,Context c1,Bitmap B){
        this.EMAIL=EMAIL;
        this.PASSWORD=PASSWORD;
        this.USERNAME=USERNAME;
        this.BIO=BIO;
        this.store_one=store_one;
        this.base_reference=base_reference;
        this.aone=aone;
        this.c1=c1;
        this.B=B;
        timestamp.add(tt);
    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }
    public Bitmap reshape(Bitmap b){
        int width = b.getWidth();
        int height = b.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = 1024;
            height = (int) (width / bitmapRatio);
        } else {
            height = 1024;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(b, width, height, true);
    }
    public void getdownloaduri(Uri u){
        down=u;
    }

    public void upload_storage(StorageReference s1,Bitmap b){

        Log.d("system.out",tt);
        bio_address=s1.child("/"+EMAIL+"/"+tt);
        byte[] b1=Bitmap2Bytes(b);
        //upload file to storage
        UploadTask up=bio_address.putBytes(b1);
        Task<Uri> urltask=up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return bio_address.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    final String downloaduri=downloadUri.toString();
                    if (timestamp!=null){
                        Log.d("system.out","timestamp size is:"+timestamp.size());
                        upload_database(downloaduri);
                    }
                    else{
                        Log.d("system.out","timestamp is null!");
                    }

                    Log.d("system.out","Uri is:"+downloadUri.toString());
                } else {
                    Log.d("system.out","get download uri failed");
                }
            }
        });


        path=s1.toString()+"/"+EMAIL+"/"+tt;
        thumbnails=path;


    }
    public void upload_database(final String uri){
        Log.d("system,out","http size is:"+http.size());
        //create user
        aone.createUserWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(c1,"user creation success",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(c1,"user creation fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
        FirebaseUser uu=FirebaseAuth.getInstance().getCurrentUser();
        String uid=uu.getUid();
        Log.d("system.out","timestamp size is:"+timestamp.size());
        //create a hash map and add contents to it by using put method
        Map<String,Object> user_one=new HashMap<>();
        user_one.put("user email",EMAIL);
        user_one.put("user password",PASSWORD);
        user_one.put("user bio",BIO);
        user_one.put("time",timestamp);
        user_one.put("name",USERNAME);
        user_one.put("UID",uid);
        Log.d("system,out","http upload size is:"+http.size());
        DocumentReference df_one=store_one.document("users/"+EMAIL);
        //remember a document is not create unless some content is put inside, simply reference to it cannot finish the creation
        //also notice that if all content inside the document is deleted then the document will not disappear
        //another is document and collection must come in pairs cannot just reference the document without collection
        df_one.set(user_one).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    String caption="Profile Photo";
                    List<String>usernames=new ArrayList<>();
                    List<String>profile_uri=new ArrayList<>();
                    Map<String,Object> photoInfo=new HashMap<>();
                    photoInfo.put("userthumbnail",thumbnails);
                    photoInfo.put("usercomments",comments);
                    photoInfo.put("downloaduri",uri);
                    photoInfo.put("comment_usernames",usernames);
                    photoInfo.put("profile_photos",profile_uri);
                    photoInfo.put("caption",caption);
                    DocumentReference photoset_ref=store_one.document("users/"+EMAIL+"/"+tt+"/infos");
                    photoset_ref.set(photoInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Log.d("system,out","upload database success");
                                Intent intentR2home=new Intent(c1,HomePage.class);
                                intentR2home.putExtra("useremail",EMAIL);
                                //use java context to send intents and toast in non-activity classes
                                c1.startActivity(intentR2home);
                            }
                            else{
                                Log.d("system,out","upload database fail");
                            }
                        }
                    });
                    Log.d("system,out","upload database success");
                }
                else{
                    Log.d("system,out","upload database fail");
                }
            }
        });

    }

}
