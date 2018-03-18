package com.archives.supervisor;

import android.annotation.SuppressLint;
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
import com.archives.adapter.student_adapter;
import com.archives.admin.manage_students;
import com.archives.help.Keys;
import com.archives.models.comment;
import com.archives.models.project;
import com.archives.models.student;
import com.archives.models.student_in_project;
import com.archives.models.student_project;
import com.archives.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by locall on 2/25/2018.
 */

public class details_project  extends AppCompatActivity {
   TextView  namepro,detalis,referances;
    ListView listView ;
    public Bundle extras;
    DynamicBox box;
    private List<comment> commentList = new ArrayList<>();
   private List<comment> searchList = new ArrayList<comment>();
    private RecyclerView recyclerView;
    private comment_adapter Adapter;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private List<student_project> student_item = new ArrayList<>();
    private List<String> namelist = new ArrayList<>();
    private Button add_btn;
    private String project = "",Token="",supervisor_id="",supervisor_name="";
    private EditText add_comment;
    private TextView namestudent,commentstudent;
    LinearLayout layout_comments;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_details);
        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        supervisor_id=prefs.getString(Keys.KEY_SUPERVIOR, "");

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
        add_btn = (Button)findViewById(R.id.comment_btn) ;
        add_comment = (EditText)findViewById(R.id.add_comment);
        namestudent=(TextView)findViewById(R.id.namestudent_textview);
        commentstudent = (TextView)findViewById(R.id.comments);
        layout_comments = (LinearLayout)findViewById(R.id.LinearLayout_comment) ;
        extras =  getIntent().getExtras();
        if (extras != null) {
            project= extras.getString("name_project");
        }

        namestudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
                layout_comments.setVisibility(View.GONE);
                namestudent.setTextColor(getResources().getColor(R.color.baj));
                commentstudent.setTextColor(getResources().getColor(R.color.white));


            }
        });
        commentstudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listView.setVisibility(View.GONE);
                layout_comments.setVisibility(View.VISIBLE);
                namestudent.setTextColor(getResources().getColor(R.color.white));
                commentstudent.setTextColor(getResources().getColor(R.color.baj));


            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_comment);
        Adapter = new comment_adapter(details_project.this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(details_project.this));
        recyclerView.setAdapter(Adapter);

        box = new DynamicBox(details_project.this,recyclerView);
        box.showLoadingLayout();

        Query nameuser = ref.child("user").orderByChild("id").equalTo(supervisor_id);
        nameuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(details_project.this, "Not found", Toast.LENGTH_SHORT).show();
                } else {
                    // List<comment> searchList = new ArrayList<comment>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user user = snapshot.getValue(user.class);
                        supervisor_name = user.getName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query projectname = ref.child("project").orderByChild("name").equalTo(project);
        projectname.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(details_project.this, "Not found", Toast.LENGTH_SHORT).show();
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        project project1 = snapshot.getValue(project.class);
                        namepro.setText(project1.getName());
                        detalis.setText(project1.getDetails());
                        referances.setText(project1.getReferanc());

                        student_item.addAll(project1.getStudents());
                        for (int i = 0;i<student_item.size();i++){
                            student_project s = new student_project();
                            s= project1.getStudents().get(i);
                            namelist.add(s.getNameStudent().toString());
                        }




                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (details_project.this, android.R.layout.simple_list_item_1, namelist);

                    listView.setAdapter(arrayAdapter);
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
                    Toast.makeText(details_project.this, "Not found", Toast.LENGTH_SHORT).show();
                    box.hideAll();
                } else {
                    box.hideAll();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        comment c = snapshot.getValue(comment.class);
                        searchList.add(c);
                        Adapter = new comment_adapter(details_project.this, searchList);
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
                comment.setName(supervisor_name);
                comment.setSupervisorId(supervisor_id);
                ref.child("comment").push().setValue(comment);
                searchList.add(comment);
                Toast.makeText(details_project.this,"done",Toast.LENGTH_LONG).show();
            }
        });





    }
}
