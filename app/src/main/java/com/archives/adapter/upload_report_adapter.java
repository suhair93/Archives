package com.archives.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archives.R;
import com.archives.models.Report;
import com.archives.models.Upload;

import java.util.List;


public class upload_report_adapter extends RecyclerView.Adapter<upload_report_adapter.MyViewHolder>  {

 private   Context mContext;
 private List<Report> reportList;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView report_name;


        public MyViewHolder(View view) {
            super(view);
           report_name = (TextView) view.findViewById(R.id.name_report);


        }
    }


    public upload_report_adapter(Context mContext, List<Report> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;
        //this.orig=studentsList;
    }

    @Override
    public upload_report_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_row_item, parent, false);

        return new upload_report_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final upload_report_adapter.MyViewHolder holder, final int position) {
        Report report = reportList.get(position);
        holder.report_name.setText(report.getName());
        holder.report_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Report Report = reportList.get(position);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Report.getUrl()));
                mContext.startActivity(intent);
            }
        });



    }



        @Override
        public int getItemCount () {
            return reportList.size();


        }

}
