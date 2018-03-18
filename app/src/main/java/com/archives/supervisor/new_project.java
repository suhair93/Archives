package com.archives.supervisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.archives.R;
import com.archives.help.Keys;
import com.archives.models.project;
import com.archives.models.student;
import com.archives.models.student_in_project;
import com.archives.models.student_project;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by locall on 2/22/2018.
 */

public class new_project extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Button create ;

    private EditText name_project,details_project,referanc_project;
    private String Token = "",selected_student = "",supervisor_id="";
    //firebase objects
    private StorageReference storageReference ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.new_project);

        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        name_project=(EditText)findViewById(R.id.newProjectName);
        details_project=(EditText)findViewById(R.id.details);
        referanc_project=(EditText)findViewById(R.id.referanc);
        create =(Button)findViewById(R.id.create);

        final SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        supervisor_id=prefs.getString(Keys.KEY_SUPERVIOR, "");


        storageReference  = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project project = new project();
                project.setName(name_project.getText().toString());
                project.setDetails(details_project.getText().toString());
                project.setReferanc(referanc_project.getText().toString());
                project.setAdmin_id(Token);
                project.setSupervisor_id(supervisor_id);
                List<student_project> list =new ArrayList<>();
                student_project s =new student_project();
                s.setNumberStudent(" ");
                s.setNameStudent("");
                list.add(s);
                project.setStudents(list);
                ref.child("project").push().setValue(project);
                Toast.makeText(new_project.this,"done",Toast.LENGTH_LONG).show();
                Intent i = new Intent(new_project.this,Home_supervisor.class);
                startActivity(i);
                finish();
            }
        });




}}
