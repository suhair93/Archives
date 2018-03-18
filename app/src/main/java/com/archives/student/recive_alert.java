package com.archives.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.archives.R;
import com.archives.adapter.alert_adapter;
import com.archives.adapter.student_adapter;
import com.archives.help.Keys;
import com.archives.models.message;
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

public class recive_alert extends AppCompatActivity {
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    private List<message> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private alert_adapter mAdapter;
    String Token = "",studentID="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recive_alert);


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
                ref.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    message message = snapshot.getValue(message.class);
                    if(message.getAdmin_id().equals(Token) && message.getStudent_id().equals(studentID)){
                    messageList.add(message);
                    mAdapter.notifyDataSetChanged();}
                }

           Collections.reverse(messageList);
                box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_alerts);
        mAdapter = new alert_adapter(recive_alert.this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(recive_alert.this));
        recyclerView.setAdapter(mAdapter);

        box = new DynamicBox(recive_alert.this,recyclerView);
        box.showLoadingLayout();


    }

}

