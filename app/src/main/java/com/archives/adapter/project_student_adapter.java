package com.archives.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.archives.R;
import com.archives.models.project;
import com.archives.models.student_project;
import com.archives.student.details_project_student;
import com.archives.supervisor.details_project;

import java.util.List;


public class project_student_adapter extends RecyclerView.Adapter<project_student_adapter.MyViewHolder>  {
    AlertDialog alertDialog;
Context mContext;
    String name="";
  List<student_project> projectList;
  int pos =0;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_project;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            name_project = (TextView) view.findViewById(R.id.name_project);


        }
    }


    public project_student_adapter(Context mContext, List<student_project> projectList) {
        this.mContext = mContext;
        this.projectList = projectList;
        //this.orig=studentsList;
    }

    @Override
    public project_student_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_row_item, parent, false);

        return new project_student_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final project_student_adapter.MyViewHolder holder, final int position) {
        student_project project = projectList.get(position);
        holder.name_project.setText(project.getNameProject());
        holder.name_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_project  project = projectList.get(position);
                 String name = holder.name_project.getText().toString();
              Intent intent =new Intent(mContext,details_project_student.class);
                intent.putExtra("name_project",name);
                intent.putExtra("details_project",project.getDetailsProject());
                intent.putExtra("referance_project",project.getReferancProject());
                intent.putExtra("student_project_name", String.valueOf(project.getNameProject()));
                intent.putExtra("student_project_number", String.valueOf(project.getNumberStudent()));
                mContext.startActivity(intent);
            }
        });




    }



        @Override
        public int getItemCount () {
            return projectList.size();

    }




}
