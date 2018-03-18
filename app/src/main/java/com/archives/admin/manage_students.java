package com.archives.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.archives.Login;
import com.archives.R;
import com.archives.Signup;
import com.archives.adapter.student_adapter;
import com.archives.help.Keys;
import com.archives.models.student;
import com.archives.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by locall on 2/14/2018.
 */

public class manage_students extends AppCompatActivity {
    FloatingActionButton add_student_btn;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    private List<student> studentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private student_adapter mAdapter;
    private SearchView searchView ;
    String Token = "";
    String name_student="",password_student="",id_student="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_student_account);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("waiting...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");

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
                ref.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    student student = snapshot.getValue(student.class);
                    if(student.getToken().equals(Token)){
                    studentList.add(student);
                    mAdapter.notifyDataSetChanged();}
                }

                Collections.reverse(studentList);
                box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_student);
        mAdapter = new student_adapter(manage_students.this, studentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(manage_students.this));
        recyclerView.setAdapter(mAdapter);

        box = new DynamicBox(manage_students.this,recyclerView);
        box.showLoadingLayout();
        add_student_btn=(FloatingActionButton) findViewById(R.id.add_student_by_admin);
        add_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_student_AlertDialog();

            }
        });

        searchView = (SearchView)findViewById(R.id.search_student_by_admin);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query fireQuery = ref.child("students").orderByChild("name").equalTo(query);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(manage_students.this, "Not found", Toast.LENGTH_SHORT).show();
                        } else {
                            List<student> searchList = new ArrayList<student>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                student student = snapshot.getValue(student.class);
                                searchList.add(student);
                                mAdapter = new student_adapter(manage_students.this, searchList);
                                recyclerView.setAdapter(mAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mAdapter = new student_adapter(manage_students.this, studentList);
                recyclerView.setAdapter(mAdapter);
                return false;
            }
        });



    }


    public void add_student_AlertDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.add_student, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        TextView title = new TextView(this);

        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");

        database = FirebaseDatabase
                .getInstance();
        ref = database.getReference();

        final EditText name=(EditText)alertLayout.findViewById(R.id.student_username);
        final EditText password=(EditText)alertLayout.findViewById(R.id.student_password);
        final EditText id=(EditText)alertLayout.findViewById(R.id.student_id);

        Button b_neg;
        b_neg = (Button) alertLayout.findViewById(R.id.btn_add_student);
        b_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query fireQuery = ref.child("students").orderByChild("id").equalTo(id.getText().toString());
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            //create user

                            String typeuser = "student";
                            user user1 = new user();
                            user1.setName(name.getText().toString());
                            user1.setPassword(password.getText().toString());
                            user1.setId(id.getText().toString());
                            user1.setTypeUser(typeuser);
                            user1.setToken(Token);
                            ref.child("user").push().setValue(user1);


                            student student = new student();
                            student.setId(id.getText().toString());
                            student.setName(name.getText().toString());
                            student.setToken(Token);
                            student.setPassword(password.getText().toString());
                            ref.child("students").push().setValue(student);
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        } else {

                            Toast.makeText(getApplicationContext(), "This student account already exists", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();

                    }
                });



               // finish();
            }
        });
// / You Can Customise your Title here
        title.setText("add student");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        title.setPadding(15, 15, 15, 15);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setTextSize(20);
        alert.setCustomTitle(title);
        alert.setView(alertLayout);
        alertDialog = alert.create();
        alertDialog.show();



    }
}

