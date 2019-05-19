package com.example.a3_t1;

import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class delete {
    String timestamp;
    String owner_email;
    FirebaseFirestore fire=FirebaseFirestore.getInstance();
    FirebaseStorage store=FirebaseStorage.getInstance();

    public delete(String time, String email){
        this.timestamp=time;
        this.owner_email=email;
    }
    public void delete_database(){
        DocumentReference doc=fire.document("users/"+owner_email);
        //delete a single line from array using update and arrayremove
        doc.update("time", FieldValue.arrayRemove(timestamp));
        fire.collection("users/"+owner_email+"/"+timestamp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    //get all documents under a collection
                    for(int i=0;i<myListOfDocuments.size();i++){
                        DocumentSnapshot temp=myListOfDocuments.get(i);
                        //get reference to document by documentsnapshot
                        DocumentReference doc_temp=temp.getReference();
                        //delete this document by reference
                        doc_temp.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("delete.class",timestamp);
                                Log.d("delete.class","deleted one item");
                            }
                        });
                    }

                }
                else{
                    Log.d("delete.class","fail getting documentsnaps");
                }
            }
        });

    }
    public void delete_storage(){
        StorageReference dir_ref=store.getReference();
        StorageReference image_ref=dir_ref.child("photoes/"+owner_email+"/"+timestamp);
        //delete storage
        image_ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d("delete.class","delete storage finish");
                }
                else{
                    Log.d("delete.class","delete storage fail");
                }

            }
        });

    }
}
