package com.archives.student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.archives.R;
import com.archives.adapter.alert_adapter;
import com.archives.adapter.upload_adapter;
import com.archives.help.Keys;
import com.archives.models.Upload;
import com.archives.models.message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by locall on 2/14/2018.
 */

public class recive_files extends AppCompatActivity {
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    private List<Upload> uploadList = new ArrayList<>();
    private RecyclerView recyclerView;
    private upload_adapter mAdapter;
    String Token = "",studentID="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recive_file);


        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        studentID = prefs.getString(Keys.KEY_STUDENT, "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
// add all student user in list
                ref.child("uploadFileAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uploadList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Upload upload = snapshot.getValue(Upload.class);
                    if(upload.getAdmin_id().equals(Token) && upload.getStudent_id().equals(studentID)){
                    uploadList.add(upload);
                    mAdapter.notifyDataSetChanged();}
                }
                Collections.reverse(uploadList);
                box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_files);
        mAdapter = new upload_adapter(recive_files.this, uploadList);
        recyclerView.setLayoutManager(new LinearLayoutManager(recive_files.this));
        recyclerView.setAdapter(mAdapter);

        box = new DynamicBox(recive_files.this,recyclerView);
        box.showLoadingLayout();


    }

}

