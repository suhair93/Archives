package com.archives.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.archives.models.message;
import com.archives.models.student;
import com.archives.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class alert_adapter extends RecyclerView.Adapter<alert_adapter.MyViewHolder>  {
    AlertDialog alertDialog;
Context mContext;
    String name="",idNO="",password="",Token="";
  List<message> messageList;
  int pos =0;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title_msg, msg;


        public MyViewHolder(View view) {
            super(view);
            title_msg = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg_content);

        }
    }


    public alert_adapter(Context mContext, List<message> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
        //this.orig=studentsList;
    }

    @Override
    public alert_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_row_item, parent, false);

        return new alert_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final alert_adapter.MyViewHolder holder, final int position) {
        message message = messageList.get(position);
        holder.title_msg.setText(message.getTitle());
        holder.msg.setText(message.getContant());


    }



        @Override
        public int getItemCount () {
            return messageList.size();


        }

}
