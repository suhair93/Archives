package com.archives.student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.archives.R;
import com.archives.adapter.comment_adapter;
import com.archives.help.Keys;
import com.archives.models.comment;
import com.archives.models.project;
import com.archives.models.student_in_project;
import com.archives.models.student_project;
import com.archives.supervisor.details_project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by locall on 2/25/2018.
 */

public class details_project_student extends AppCompatActivity {
   TextView  namepro,detalis,referances;
    ListView listView ;
    public Bundle extras;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private List<student_project> student_item = new ArrayList<>();
    private List<comment> searchList = new ArrayList<comment>();
    private List<String> namelist = new ArrayList<>();
    TextView namestudentTitle;
    private Button add_btn;
    private String project = "",Token="",supervisor_id="",student_id="",student_name="";
    private EditText add_comment;
    private TextView namestudent,commentstudent;
    private LinearLayout layout_comments;

    DynamicBox box;
    private List<comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private comment_adapter Adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_details);
        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        supervisor_id=prefs.getString(Keys.KEY_SUPERVIOR, "");
        student_id=prefs.getString(Keys.KEY_STUDENT, "");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
        namepro =(TextView)findViewById(R.id.name_project);
        referances =(TextView)findViewById(R.id.referanc);
        detalis =(TextView)findViewById(R.id.details_project);

        listView =(ListView)findViewById(R.id.list);
        listView.setVisibility(View.GONE);

        namestudentTitle=(TextView)findViewById(R.id.namestudent_textview);
        namestudentTitle.setVisibility(View.GONE);

        add_btn = (Button)findViewById(R.id.comment_btn) ;
        namestudent=(TextView)findViewById(R.id.namestudent_textview);
        commentstudent = (TextView)findViewById(R.id.comments);
        add_comment = (EditText)findViewById(R.id.add_comment);

        layout_comments = (LinearLayout)findViewById(R.id.LinearLayout_comment) ;
        layout_comments.setVisibility(View.VISIBLE);

        extras =  getIntent().getExtras();
        if (extras != null) {
            project= extras.getString("name_project");
        }
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_comment);
        Adapter = new comment_adapter(details_project_student.this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(details_project_student.this));
        recyclerView.setAdapter(Adapter);

        box = new DynamicBox(details_project_student.this,recyclerView);
        box.showLoadingLayout();


        Query fireQuery = ref.child("student_project").orderByChild("nameProject").equalTo(project);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(details_project_student.this, "Not found", Toast.LENGTH_SHORT).show();
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        student_project project1 = snapshot.getValue(student_project.class);
                        namepro.setText(project1.getNameProject());
                        detalis.setText(project1.getDetailsProject());
                        referances.setText(project1.getReferancProject());
                        if((project1.getNumberStudent()).equals(student_id)) {
                            student_name = project1.getNameStudent();
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query comment = ref.child("comment").orderByChild("projectname").equalTo(project);
        comment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(details_project_student.this, "Not found", Toast.LENGTH_SHORT).show();
                    box.hideAll();
                } else {
                    box.hideAll();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        comment c = snapshot.getValue(comment.class);
                        searchList.add(c);
                        Adapter = new comment_adapter(details_project_student.this, searchList);
                        recyclerView.setAdapter(Adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                box.hideAll();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment comment= new comment();
                comment.setProjectname(namepro.getText().toString());
                comment.setComment(add_comment.getText().toString());
                comment.setAdminId(Token);
                comment.setName(student_name);
                comment.setSupervisorId(supervisor_id);
                ref.child("comment").push().setValue(comment);
                searchList.add(comment);
                Toast.makeText(details_project_student.this,"done",Toast.LENGTH_LONG).show();
            }
        });


    }
}
