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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Display extends AppCompatActivity {
    private FirebaseFirestore firebase=FirebaseFirestore.getInstance();
    private RecyclerView r1;
    private display_adapter DA;
    String master;
    String EMAIL;
    String TIME;
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent receiver=getIntent();
        //shown image download uri
        String puri=receiver.getStringExtra("photouri");
        //directory for shown image in database
        String t_direct=receiver.getStringExtra("time2direct");
        String master_email=receiver.getStringExtra("owner");
        master=master_email;
        Log.d("system.out","master email is:"+master_email);
        //email address for owner of image
        String em=receiver.getStringExtra("e");
        ed=findViewById(R.id.editText9);
        EMAIL=em;
        TIME=t_direct;
        ImageView i=findViewById(R.id.imageView5);
        Picasso.get().load(puri).into(i);
        //picasso can set an image view by providing a download url and a target image view
        load_comments(em,t_direct,master_email);


    }
    public void load_comments(String email,String direct,String memail){
        //the email here has a problem
        final TextView cap=findViewById(R.id.textView6);
        //notice all variables used inside an inner class have to be defined as final
        final TextView tg=findViewById(R.id.textView9);
        firebase.collection("users/"+memail+"/"+direct).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    //get all documents in collection
                    Log.d("system,out", "Total Documents Number: " + myListOfDocuments.size());
                    if (myListOfDocuments.size()==1){
                        List<String>uries=new ArrayList<>();
                        List<String>u=new ArrayList<>();
                        List<String>content=new ArrayList<>();
                        r1= (RecyclerView) findViewById(R.id.recyclerView2);
                        r1.setLayoutManager(new LinearLayoutManager(Display.this));
                        r1.setAdapter(DA=new display_adapter(Display.this,uries,u,content));
                        //set adapter view, it is ok to initialize the adapter with empty lists or stings
                        //use additem to add additional contents to adapter
                        Log.d("system.out","load comment complete");
                    }
                    else{
                        List<String>uries=new ArrayList<>();
                        List<String>u=new ArrayList<>();
                        for (int l=0;l<myListOfDocuments.size()-1;l++){
                            //get document by using get(int)
                            DocumentSnapshot doc=myListOfDocuments.get(l);
                            List<String>content=(List<String>)doc.get("usercomments");
                            String uri=(String)doc.get("profile_photos");
                            String user=(String)doc.get("comment_usernames");
                            for(int i=0;i<content.size();i++){
                                //add components to list
                                uries.add(uri);
                                u.add(user);
                            }

                            r1= (RecyclerView) findViewById(R.id.recyclerView2);
                            r1.setLayoutManager(new LinearLayoutManager(Display.this));
                            r1.setAdapter(DA=new display_adapter(Display.this,uries,u,content));
                            Log.d("system.out","load comment complete");
                        }
                    }

                }
            }
        });
        DocumentReference d=firebase.document("users/"+memail+"/"+direct+"/infos");
        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Log.d("system.out","task succeed");
                    //load recycler view and caption text
                    DocumentSnapshot documentSnapshot=task.getResult();
                    String caption=(String)documentSnapshot.get("caption");
                    String tag=(String)documentSnapshot.get("Tags");
                    if (tag!=null){
                        tg.setText(tag);
                    }
                    else{
                        String unable="Hashtag not available";
                        tg.setText(unable);
                    }
                    cap.setText(caption);
                }
                else{
                    Log.d("system.out","load comment failed");
                }
            }
        });

    }

    public void add_comment(View view) {
        final String owner_email=master;
        final String email=EMAIL;
        Log.d("system.out","owner is:"+owner_email);
        final String time=TIME;
        final String Caption="";
        final String com=ed.getText().toString();
        DocumentReference d=firebase.document("users/"+email);
        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    final String uname=(String) documentSnapshot.get("name");
                    final List<String> times=(List<String>) documentSnapshot.get("time");
                    final String sub_collection=times.get(0);
                    DocumentReference dr=firebase.document("users/"+email+"/"+sub_collection+"/infos");
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot=task.getResult();
                                final String profile_uri=(String)documentSnapshot.get("downloaduri");
                                final String tag=(String)documentSnapshot.get("Tags");
                                if (tag!=null){
                                    post comment_post=new post(Display.this,tag);
                                    final String downloadUTL="";
                                    Log.d("system.out",owner_email);
                                    comment_post.updata_database(owner_email,time,"comment",Caption,com,uname,profile_uri,downloadUTL,email);
                                    Log.d("system.out","commenter profile uri:"+profile_uri);
                                    Log.d("system.out","commenter username:"+uname);
                                    Log.d("system.out","comment content"+com);
                                    DA.addItem(0,uname,com,profile_uri);
                                }
                                else{
                                    final String t="";
                                    Log.d("system.out",owner_email);
                                    post comment_post=new post(Display.this,t);
                                    final String downloadUTL="";
                                    comment_post.updata_database(owner_email,time,"comment",Caption,com,uname,profile_uri,downloadUTL,email);
                                    Log.d("system.out","commenter profile uri:"+profile_uri);
                                    Log.d("system.out","commenter username:"+uname);
                                    Log.d("system.out","comment content"+com);
                                    DA.addItem(0,uname,com,profile_uri);
                                }


                            }
                        }
                    });
                }
            }
        });


    }

    public void delete_post(View view) {
        if (master.equals(EMAIL)){
            delete delete_thisimage=new delete(TIME,EMAIL);
            delete_thisimage.delete_database();
            delete_thisimage.delete_storage();

        }
        else{
            Toast warn=Toast.makeText(this,"This post cannot be deleted by current user",Toast.LENGTH_SHORT);
            warn.show();
        }

    }
}
