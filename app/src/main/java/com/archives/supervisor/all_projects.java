package com.archives.supervisor;

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
import com.archives.adapter.project_adapter;
import com.archives.help.Keys;
import com.archives.models.message;
import com.archives.models.project;
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

public class all_projects extends AppCompatActivity {
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    private List<project> projectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private project_adapter mAdapter;
    String Token = "",supervisorID="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_project);


        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        supervisorID = prefs.getString(Keys.KEY_SUPERVIOR, "");

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
                ref.child("project").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    project project = snapshot.getValue(project.class);
                    if(project.getSupervisor_id().equals(supervisorID)){
                    projectList.add(project);
                    mAdapter.notifyDataSetChanged();}
                }

           Collections.reverse(projectList);
                box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_project);
        mAdapter = new project_adapter(all_projects.this, projectList);
        recyclerView.setLayoutManager(new LinearLayoutManager(all_projects.this));
        recyclerView.setAdapter(mAdapter);

        box = new DynamicBox(all_projects.this,recyclerView);
        box.showLoadingLayout();


    }

}

