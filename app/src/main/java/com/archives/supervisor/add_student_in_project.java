package com.archives.supervisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.archives.R;
import com.archives.adapter.student_adapter;
import com.archives.admin.manage_students;
import com.archives.help.Keys;
import com.archives.models.message;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by locall on 2/17/2018.
 */

public class add_student_in_project extends AppCompatActivity {
    SearchableSpinner spinner_student ;
    ArrayAdapter<String>  projectArrayAdapter ;
    EditText student_number,student_name;
    FirebaseDatabase database;
    DatabaseReference ref;
    student_project s = new student_project();
    private List<project> projectList = new ArrayList<>();
   private List<student_project> students = new ArrayList<>() ;
    String nameproject[] , idproject[];
    Button add ;
    ImageView back;
    String Token = "",selected_project = "",supervisorID=" ";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.add_student_in_project);

        spinner_student =(SearchableSpinner)findViewById(R.id.spinner_project);
        student_number = (EditText)findViewById(R.id.student_number);
        student_name = (EditText)findViewById(R.id.student_name);
        add = (Button)findViewById(R.id.addStudentBtn);
        back = (ImageView)findViewById(R.id.back);


        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        supervisorID = prefs.getString(Keys.KEY_SUPERVIOR, "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        ///////////////////////////////////////
        spinner();
        ref.child("project").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(add_student_in_project.this, "Not found", Toast.LENGTH_SHORT).show();
                    } else {
                    project project = snapshot.getValue(project.class);
                    if(project.getSupervisor_id().equals(supervisorID)){
                        projectList.add(project);

                       }}
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //////////////////////////////////
        add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

          students.clear();
        for(int i=0;i< projectList.size();i++){
            if(projectList.get(i).getName().equals(selected_project)){

                    students.addAll(projectList.get(i).getStudents());

                    s.setNameStudent(student_name.getText().toString());
                    s.setNumberStudent(student_number.getText().toString());
                    students.add(s);



            }}



        final project p = new project();

        for(int i=0;i< projectList.size();i++){
            if(projectList.get(i).getName().equals(selected_project)){

                //p.setStudent(student_number.getText()+"");
                p.setName(projectList.get(i).getName());
                p.setDetails(projectList.get(i).getDetails());
                p.setReferanc(projectList.get(i).getReferanc());
                p.setSupervisor_id(projectList.get(i).getSupervisor_id());
                p.setAdmin_id(projectList.get(i).getAdmin_id());
                p.setStudents(students);
                s.setNameProject(projectList.get(i).getName());
                s.setDetailsProject(projectList.get(i).getDetails());
                s.setReferancProject(projectList.get(i).getReferanc());
                s.setSupervisorId(projectList.get(i).getSupervisor_id());
                s.setAdminId(projectList.get(i).getAdmin_id());
                ref.child("student_project").push().setValue(s);



            }
        }

        final Query query1 = ref.child("project").orderByChild("name").equalTo(selected_project);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    snapshot.getRef().setValue(p);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(add_student_in_project.this,"Done add student",Toast.LENGTH_LONG).show();
         Intent i = new Intent(add_student_in_project.this,Home_supervisor.class);

        startActivity(i);
        finish();


    }
});


    }



    public void spinner(){
        // add all student user in list
        ref.child("project").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    project project = snapshot.getValue(project.class);
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(add_student_in_project.this, "Not found", Toast.LENGTH_SHORT).show();
                    }else if(project.getSupervisor_id().equals(supervisorID) ) {
                        projectList.add(project);
                        //  mAdapter.notifyDataSetChanged();}
                    }

                }
                nameproject =new String[projectList.size()];
                idproject =new String[projectList.size()];
                for(int i=0;i<projectList.size();i++){
                    // student student =studentList.get(i);
                    nameproject[i] = projectList.get(i).getName();
                  //  idproject[i] =projectList.get(i).getId();

                }

                projectArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.tv, nameproject);
                //AutoCompleteTextView.setAdapter(adapter );
                spinner_student.setAdapter(projectArrayAdapter);
                spinner_student.setPositiveButton("Done");
                spinner_student.setTitle("choose project ");
                spinner_student.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        for(int j=0;j<nameproject.length;j++) {
                            if (projectArrayAdapter.getItem(i) == nameproject[j]){
                                selected_project = nameproject[j];
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            @Override
            public void onCancelled (DatabaseError databaseError){

            }

        });

    }


}
