package com.example.a3_t1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Context extends AppCompatActivity {
    TextView t;
    FirebaseFirestore fstore=FirebaseFirestore.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String Email;
    TextView t0;
    Bitmap newphoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        t0=findViewById(R.id.textView5);
        Intent recevier_intent=getIntent();
        byte[] b_recceive=recevier_intent.getByteArrayExtra("imagebyte");
        Email=recevier_intent.getStringExtra("email");
        newphoto= BitmapFactory.decodeByteArray(b_recceive, 0,
                b_recceive.length);
        ImageView context_photo=findViewById(R.id.imageView4);
        context_photo.setImageBitmap(newphoto);
        t=findViewById(R.id.editText8);
        final Switch sv=findViewById(R.id.switch2);
        //define a switch component and initialize it to be not checked
        sv.setChecked(false);
        //this listener listen to change of the switch
        sv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //see if the switch is checked
                    FirebaseVisionLabelDetector detector = FirebaseVision.getInstance().getVisionLabelDetector();
                    //firebase ml kit to detect labels for image
                    FirebaseVisionImage img = FirebaseVisionImage.fromBitmap(newphoto);
                    detector.detectInImage(img).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionLabel>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<FirebaseVisionLabel>> task) {
                            List<FirebaseVisionLabel>labels=task.getResult();
                            String all_labels="";
                            int k=labels.size();
                            for (int i=0;i<k;i++){
                                String temp=labels.get(i).toString();
                                String entityId = labels.get(i).getLabel();
                                // get the label for the image and keep four of them
                                if (i<4){
                                    all_labels=all_labels+entityId+",";
                                }


                            }
                            Log.d("system,out",""+all_labels);

                            t0.setText(all_labels);
                        }
                    });
                    /*FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(newphoto);
                    FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                            .getOnDeviceImageLabeler();*/
                }
                else{
                    Log.d("system,out","switch is turned off");
                }
            }
        });

    }

    public void cancel_post(View view) {
        Intent cancel=new Intent(this,HomePage.class);
        cancel .putExtra("useremail",Email);
        startActivity(cancel);
    }

    public void post_photo(View view) {
        final String tags=t0.getText().toString();
        post p1=new post(this,tags);
        StorageReference s= FirebaseStorage.getInstance().getReference();
        final String semail=Email;
        final String caption=t.getText().toString();
        final String com="my profile image";
        final String u="myself";
        final String downloaduri="";
        final String time="";
        final String request="post";

        p1.upload_storage(s, newphoto,semail,caption,com,u,downloaduri, time, request);
    }


}
