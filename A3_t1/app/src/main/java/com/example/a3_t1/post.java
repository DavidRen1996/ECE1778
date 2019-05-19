package com.example.a3_t1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.content.Context;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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

/*public class post extends create_user{

    public post(){

    }
    @Override
    public void upload_database(final String uri){

    }
}*/
public class post{
    private Context context;
    StorageReference bio_address;
    Calendar cc = Calendar.getInstance();
    Date date = cc.getTime();
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    final String tt = format2.format(date).toString();
    FirebaseFirestore f=FirebaseFirestore.getInstance();
    final String Tag;

    public post(Context c,final String Tag){
        this.context=c;
        this.Tag=Tag;
    }
    public void updata_database(final String t,final String time,final String request, final String Caption,final String Comments,final String username,final String downloaduri,final String downloadUTL,final String current_signin_email){
        String timestamp;
        if (request.equals("comment")){
            timestamp =time;
            //use comment to tell if it wants an upload or an update on database
            //add comment use existing path
        }
        else{
             timestamp=tt;
             //post new photo
        }
        DocumentReference doc_ref=f.document("users/"+t+"/"+timestamp+"/infos");
        if (request.equals("comment")){
            final DocumentReference comment_ref=f.document("users/"+t+"/"+timestamp+"/"+current_signin_email);
            comment_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        //already exist
                        DocumentSnapshot documentSnapshot=task.getResult();
                        String name=(String)documentSnapshot.get("comment_usernames");
                        if (name==null){
                            Log.d("post","No existing path");
                            List<String>comment_list=new ArrayList<>();
                            comment_list.add(Comments);
                            Map<String,Object> comment_user_one=new HashMap<>();
                            comment_user_one.put("profile_photos",downloaduri);
                            comment_user_one.put("usercomments",comment_list);
                            comment_user_one.put("comment_usernames",username);
                            comment_ref.set(comment_user_one).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("post","path creation complete");
                                }
                            });
                        }
                        else{
                            comment_ref.update("usercomments", FieldValue.arrayUnion(Comments));
                            Log.d("post","update existing path");
                        }

                    }
                    else{

                    }
                }
            });





            // add comment to an existing post
            /*Log.d("post","execute update order"+t);
            Log.d("post","update directory:"+"users/"+t+"/"+timestamp+"/infos");
            Log.d("post","update content 1:"+Comments);
            Log.d("post","update content 2:"+username);
            Log.d("post","update content 3:"+downloaduri);
            doc_ref.update("usercomments", FieldValue.arrayUnion(Comments));
            doc_ref.update("comment_usernames", FieldValue.arrayUnion(username));
            doc_ref.update("profile_photos", FieldValue.arrayUnion(downloaduri));*/

        }
        else{
            //create a new post photo
            DocumentReference D=f.document("users/"+t);
            D.update("time",FieldValue.arrayUnion(timestamp));
            //add new content to array but it can only add not existing content!!
            Map<String,Object>post_info=new HashMap<>();
            List<String>usernames=new ArrayList<>();
            List<String>profile_uri=new ArrayList<>();
            List<String>comments=new ArrayList<>();
            post_info.put("userthumbnail",downloadUTL);
            post_info.put("usercomments",comments);
            post_info.put("downloaduri",downloadUTL);
            post_info.put("comment_usernames",usernames);
            post_info.put("profile_photos",profile_uri);
            post_info.put("caption",Caption);
            post_info.put("Tags",Tag);
            doc_ref.set(post_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d("system,out","upload database success");
                        Intent context2home=new Intent(context,HomePage.class);
                        context2home.putExtra("useremail",t);
                        //context2home.putExtra("newphotouri",downloadUTL);
                        context.startActivity(context2home);
                    }
                    else{
                        Log.d("system,out","upload database fail");
                    }
                }
            });
        }
    }
    private byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }
    public void upload_storage(StorageReference s1, Bitmap b,final String email,final String Caption,final String Comments,final String username,final String downloaduri,final String time,final String request){
        bio_address=s1.child("photoes"+"/"+email+"/"+tt);
        byte[] b1=Bitmap2Bytes(b);
        UploadTask up=bio_address.putBytes(b1);
        Task<Uri> uritask=up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    final String downloadUTL=downloadUri.toString();
                    //uri for the newly taken photo,downloaduri is the user profile photo uri
                    updata_database(email,time,request,Caption,Comments,username,downloaduri,downloadUTL,email);
                }
                else{
                    Log.d("system.out","get download uri failed");
                }
            }
        });
    }
}