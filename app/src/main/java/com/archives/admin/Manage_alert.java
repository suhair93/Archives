package com.archives.admin;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.archives.R;
import com.archives.help.Keys;
import com.archives.models.message;
import com.archives.models.student;
import com.archives.supervisor.Home_supervisor;
import com.archives.supervisor.add_student_in_project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by locall on 2/17/2018.
 */

public class Manage_alert  extends AppCompatActivity {
    SearchableSpinner spinner_student ;
    ArrayAdapter<String>  studentArrayAdapter ;
    EditText msg,title_msg;
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<student> studentList = new ArrayList<>();
    String namestudent[] , idstudent[];
    Button send ;
    ImageView back;
    String Token = "",selected_student = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.manage_alert);

        spinner_student =(SearchableSpinner)findViewById(R.id.spinner_student);
        msg = (EditText)findViewById(R.id.msg);
        title_msg = (EditText)findViewById(R.id.title_msg);
        send = (Button)findViewById(R.id.send);
        back = (ImageView)findViewById(R.id.back);


        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");

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

        //////////////////////////////////
send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        message message = new message();
        message.setTitle(title_msg.getText().toString());
        message.setContant(msg.getText().toString());
        message.setStudent_id(selected_student);
        message.setAdmin_id(Token);
        ref.child("message").push().setValue(message);
        Toast.makeText(Manage_alert.this,"done",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Manage_alert.this,Home_supervisor.class);
        startActivity(i);
        finish();
    }
});


    }



    public void spinner(){
        // add all student user in list
        ref.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    student student = snapshot.getValue(student.class);
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(Manage_alert.this, "Not found", Toast.LENGTH_SHORT).show();
                    }else if(student.getToken().equals(Token) ) {
                        studentList.add(student);
                        //  mAdapter.notifyDataSetChanged();}
                    }

                }
                namestudent =new String[studentList.size()];
                idstudent =new String[studentList.size()];
                for(int i=0;i<studentList.size();i++){
                    // student student =studentList.get(i);
                    namestudent[i] = studentList.get(i).getName();
                    idstudent[i] =studentList.get(i).getId();

                }

                studentArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.tv, namestudent);
                //AutoCompleteTextView.setAdapter(adapter );
                spinner_student.setAdapter(studentArrayAdapter);
                spinner_student.setPositiveButton("Done");
                spinner_student.setTitle("choose student ");
                spinner_student.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        for(int j=0;j<idstudent.length;j++) {
                            if (studentArrayAdapter.getItem(i) == namestudent[j]){
                                selected_student = idstudent[j];
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
