package com.archives.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.archives.R;
import com.archives.help.Keys;
import com.archives.models.Report;
import com.archives.models.project;
import com.archives.models.student;
import com.archives.models.user;
import com.archives.supervisor.details_project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class project_adapter extends RecyclerView.Adapter<project_adapter.MyViewHolder>  {
    AlertDialog alertDialog;
Context mContext;
    String name="";
  List<project> projectList;
  int pos =0;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_project;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            name_project = (TextView) view.findViewById(R.id.name_project);


        }
    }


    public project_adapter(Context mContext, List<project> projectList) {
        this.mContext = mContext;
        this.projectList = projectList;
        //this.orig=studentsList;
    }

    @Override
    public project_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_row_item, parent, false);

        return new project_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final project_adapter.MyViewHolder holder, final int position) {
        project project = projectList.get(position);
        holder.name_project.setText(project.getName());
        holder.name_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project  project = projectList.get(position);
                 String name = holder.name_project.getText().toString();
              Intent intent =new Intent(mContext,details_project.class);
                intent.putExtra("name_project",name);
                intent.putExtra("details_project",project.getDetails());
                intent.putExtra("referance_project",project.getReferanc());
                intent.putExtra("student_project", String.valueOf(project.getStudents()));
                mContext.startActivity(intent);
            }
        });




    }



        @Override
        public int getItemCount () {
            return projectList.size();

    }




}
