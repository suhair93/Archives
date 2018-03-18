package com.archives.student;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.archives.R;
import com.archives.adapter.upload_adapter;
import com.archives.adapter.upload_report_adapter;
import com.archives.help.Keys;
import com.archives.models.Report;
import com.archives.models.Upload;
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

public class recive_reports extends AppCompatActivity {
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private upload_report_adapter mAdapter;
    String Token = "",studentID="",superviorID="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recive_reports);


        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        studentID = prefs.getString(Keys.KEY_STUDENT, "");
        superviorID =prefs.getString(Keys.KEY_SUPERVIOR, "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_report);
        mAdapter = new upload_report_adapter(recive_reports.this, reportList);
        recyclerView.setLayoutManager(new LinearLayoutManager(recive_reports.this));
        recyclerView.setAdapter(mAdapter);

        box = new DynamicBox(recive_reports.this,recyclerView);
        box.showLoadingLayout();

// add all student user in list
        ref.child("uploadFileSupervisor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Report report = snapshot.getValue(Report.class);
                    if( report.getStudent_id().equals(studentID)){
                        reportList.add(report);
                        mAdapter.notifyDataSetChanged();}
//                    else{
//                        Toast.makeText(recive_reports.this,"no report",Toast.LENGTH_LONG).show();
//                    }
                }
                Collections.reverse(reportList);
                box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getPDF();

    }
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }
    }
}

