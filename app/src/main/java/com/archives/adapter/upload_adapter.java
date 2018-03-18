package com.archives.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.archives.R;
import com.archives.models.Upload;
import com.archives.models.message;

import java.util.List;


public class upload_adapter extends RecyclerView.Adapter<upload_adapter.MyViewHolder>  {

    private Context mContext;
    private List<Upload> uploadList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView file_name;


        public MyViewHolder(View view) {
            super(view);
            file_name = (TextView) view.findViewById(R.id.name_file);


        }
    }


    public upload_adapter(Context mContext, List<Upload> uploadList) {
        this.mContext = mContext;
        this.uploadList = uploadList;
        //this.orig=studentsList;
    }

    @Override
    public upload_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.files_row_item, parent, false);

        return new upload_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final upload_adapter.MyViewHolder holder, final int position) {
        Upload upload = uploadList.get(position);
        holder.file_name.setText(upload.getName());
        holder.file_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload upload = uploadList.get(position);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                mContext.startActivity(intent);
            }
        });



    }



        @Override
        public int getItemCount () {
            return uploadList.size();


        }

}
