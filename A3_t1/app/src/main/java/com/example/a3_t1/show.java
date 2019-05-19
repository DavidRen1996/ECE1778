package com.example.a3_t1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class show {
    private String user;
    private FirebaseFirestore store_one;
    private FirebaseStorage storage_one;
    private Context c0;
    DocumentReference Dsub;
    private String sub_address;
    String photo_uri;
    public show(String s,FirebaseStorage storage_one,FirebaseFirestore store_one,Context c0){
        this.user=s;
        this.store_one=store_one;
        this.storage_one=storage_one;
        this.c0=c0;
    }
    public void get_subcollection(final String s,final ImageView im){
        DocumentReference d=store_one.document("users/"+s);
        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    String time=(String)documentSnapshot.get("time");
                    DocumentReference d2=store_one.document("users/"+s+"/"+time+"/infos");
                    d2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String photo_uri=(String)documentSnapshot.get("downloaduri");
                                Log.d("system.out","receive uri is:"+photo_uri);
                                Picasso.get().load(photo_uri).into(im);
                            }
                        }
                    });
                    //Log.d("system.out","time is:"+time);
                }
            }
        });
    }
    /*public void get_subcollection(final String s,final ImageView im){
        DocumentReference d=store_one.document("users/"+s);
        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    sub_address=(String)documentSnapshot.get("time");
                    Dsub=store_one.document("users/"+s+"/"+sub_address+"/infos");
                    Log.d("system.out","users/"+s+"/"+sub_address+"/infos");
                    geturi(s,Dsub,im);
                    //receive_thumb = (List<String>) documentSnapshot.get("userthumbnail");
                }
                else{
                    Log.d("system,out","get_subcollection fail");
                }
            }
        });
    }*/
    /*public void geturi(String s, DocumentReference d, final ImageView im){
        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    photo_uri=(String)documentSnapshot.get("downloaduri");
                    if (photo_uri!=null){
                        Log.d("system.out",photo_uri);
                    }
                    else{
                        Log.d("system,out","null!");
                    }

                    Picasso.get().load(photo_uri).into(im);
                    //receive_thumb = (List<String>) documentSnapshot.get("userthumbnail");
                }
            }
        });
    }*/

}
