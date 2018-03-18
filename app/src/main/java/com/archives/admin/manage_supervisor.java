package com.archives.admin;

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
import com.archives.adapter.student_adapter;
import com.archives.adapter.supervisor_adapter;
import com.archives.help.Keys;
import com.archives.models.student;
import com.archives.models.supervisor;
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

public class manage_supervisor extends AppCompatActivity {
    FloatingActionButton add_supervisor_btn;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    DynamicBox box;
    private List<supervisor> supervisorList = new ArrayList<>();
    private RecyclerView recyclerView;
    private supervisor_adapter mAdapter;
    String Token = "";
    private SearchView searchView;
    String name_supervisor="",password_supervisor="",id_supervisor="";
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_supervisor_account);
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
        ref.child("supervisor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supervisorList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    supervisor supervisor = snapshot.getValue(supervisor.class);
                    if(supervisor.getToken().equals(Token)){
                        supervisorList.add(supervisor);
                    mAdapter.notifyDataSetChanged();}
                }

                Collections.reverse(supervisorList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview_supervisor);
        mAdapter = new supervisor_adapter(manage_supervisor.this, supervisorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(manage_supervisor.this));
        recyclerView.setAdapter(mAdapter);


        add_supervisor_btn=(FloatingActionButton) findViewById(R.id.add_supervisor_by_admin);
        add_supervisor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_supervisor_AlertDialog();

            }
        });

        searchView = (SearchView)findViewById(R.id.search_supervisor_by_admin);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query fireQuery = ref.child("supervisor").orderByChild("name").equalTo(query);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(manage_supervisor.this, "Not found", Toast.LENGTH_SHORT).show();
                        } else {
                            List<supervisor> searchList = new ArrayList<supervisor>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                supervisor supervisor = snapshot.getValue(supervisor.class);
                                searchList.add(supervisor);
                                mAdapter = new supervisor_adapter(manage_supervisor.this, searchList);
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
                mAdapter = new supervisor_adapter(manage_supervisor.this, supervisorList);
                recyclerView.setAdapter(mAdapter);
                return false;
            }
        });

    }


    public void add_supervisor_AlertDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.add_supervisor, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        TextView title = new TextView(this);

        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        final EditText name=(EditText)alertLayout.findViewById(R.id.supervisor_username);
        final EditText password=(EditText)alertLayout.findViewById(R.id.supervisor_password);
        final EditText id=(EditText)alertLayout.findViewById(R.id.supervisor_id);

          name_supervisor = name.getText().toString().trim();
          password_supervisor = password.getText().toString().trim();
         id_supervisor = id.getText().toString().trim();



        Button b_neg;
        b_neg = (Button) alertLayout.findViewById(R.id.btn_add_supervisor);
        b_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query fireQuery = ref.child("supervisor").orderByChild("id").equalTo(id.getText().toString());
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            //create user
                            String typeuser = "supervisor";
                            user user1 = new user();
                            user1.setName(name.getText().toString());
                            user1.setPassword(password.getText().toString());
                            user1.setTypeUser(typeuser);
                            user1.setId(id.getText().toString());
                            user1.setToken(Token);
                            ref.child("user").push().setValue(user1);

                            supervisor supervisor = new supervisor();
                            supervisor.setId(id.getText().toString());
                            supervisor.setName(name.getText().toString());
                            supervisor.setToken(Token);
                            supervisor.setPassword(password.getText().toString());
                            ref.child("supervisor").push().setValue(supervisor);
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        } else {

                            Toast.makeText(getApplicationContext(), "This supervisor account already exists", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });
// / You Can Customise your Title here
        title.setText("add supervisor");
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

