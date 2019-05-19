package com.example.a3_t1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
//it seems that only the user himself can upload a comment, need to fix this
public class Gloable extends AppCompatActivity {
    String current_sign_in_email;
    RecyclerView r1;
    RVadapter adapter;
//create an instance of firebase cloud database
    FirebaseFirestore fire=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gloable);
        Intent receiver=getIntent();
        final String e=receiver.getStringExtra("useremail");
        current_sign_in_email=e;
        loadImage();

    }
    public void loadImage(){
        //plachholders for image uri and image time path&current user

        fire.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    Log.d("system,out", "Total Documents Number: "+ myListOfDocuments.size());
                    List<String>URL=new ArrayList<>();
                    List<String>time=new ArrayList<>();
                    List<String>master=new ArrayList<>();
                    final String s=current_sign_in_email;
                    r1= (RecyclerView) findViewById(R.id.recycle_gloable);
                    r1.setLayoutManager(new LinearLayoutManager(Gloable.this));
                    r1.setAdapter(adapter = new RVadapter(Gloable.this,URL,time,s,master));
                    //if the rececycler view contains onclick components need to add a listener
                    //but the onclick function is executed in onbindview function in adapter not the listener below
                    //it should be the same if delete the listener onclick part
                    adapter.setOnItemClickListener(new RVadapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, String data) {
                            Intent home2display=new Intent(Gloable.this,Display.class);
                            startActivity(home2display);
                        }
                    });
                    for (int l=0;l<myListOfDocuments.size();l++){
                        final List<String>URL_sizecount=URL;
                        final int URL_size=URL_sizecount.size();
                        Log.d("system,out", "url_size: "+URL_size);
                        DocumentSnapshot doc=myListOfDocuments.get(l);
                        List<String>times=(List<String>)doc.get("time");
                        final String all_email=(String)doc.get("user email");
                        Log.d("system,out", "The"+l+"document has "+times.size()+"photoes");
                        int Length=times.size();
                        for (int i=0;i<Length;i++){
                            final String path=times.get(i);
                            DocumentReference doc_ref=fire.document("users/"+all_email+"/"+path+"/infos");
                            doc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot=task.getResult();
                                        String down=(String)documentSnapshot.get("downloaduri");
                                        adapter.addItem(URL_size,down,path,all_email);
                                    }
                                    else{
                                        Log.d("system,out", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        }
                    }

                }
                else{
                    Log.d("system,out", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
