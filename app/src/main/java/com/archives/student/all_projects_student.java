package com.archives.student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.archives.R;
import com.archives.adapter.project_adapter;
import com.archives.adapter.project_student_adapter;
import com.archives.adapter.student_adapter;
import com.archives.admin.manage_students;
import com.archives.help.Keys;
import com.archives.models.project;
import com.archives.models.student;
import com.archives.models.student_in_project;
import com.archives.models.student_project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by locall on 2/14/2018.
 */

public class all_projects_student extends AppCompatActivity {
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    List<student_project> searchList = new ArrayList<student_project>();
    project project;
    private RecyclerView recyclerView;
    private project_student_adapter mAdapter;
    String Token = "",supervisorID="",studentId="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_project);


        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        studentId = prefs.getString(Keys.KEY_STUDENT, "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });



            Query fireQuery = ref.child("student_project").orderByChild("numberStudent").equalTo(studentId);
            fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(all_projects_student.this, "Not found", Toast.LENGTH_SHORT).show();
                        box.hideAll();
                    } else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            student_project p = snapshot.getValue(student_project.class);
                              searchList.add(p);
                            mAdapter.notifyDataSetChanged();
                        }
                        Collections.reverse(searchList);
                        box.hideAll();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_project);
        mAdapter = new project_student_adapter(all_projects_student.this,searchList );
        recyclerView.setLayoutManager(new LinearLayoutManager(all_projects_student.this));
        recyclerView.setAdapter(mAdapter);

        box = new DynamicBox(all_projects_student.this,recyclerView);
        box.showLoadingLayout();


    }

}

