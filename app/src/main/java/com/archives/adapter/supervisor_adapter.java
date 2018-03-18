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
import com.archives.models.student;
import com.archives.models.supervisor;
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


public class supervisor_adapter extends RecyclerView.Adapter<supervisor_adapter.MyViewHolder>  {
    AlertDialog alertDialog;
Context mContext;
    String name="",idNO="",password="",Token="";
  List<supervisor> supervisorList;
  int pos =0;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_supervisor, pass_supervisor,id_supervisor,id_project;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            name_supervisor = (TextView) view.findViewById(R.id.name_supervisor);
            id_supervisor = (TextView) view.findViewById(R.id.id_supervisor);
            pass_supervisor = (TextView) view.findViewById(R.id.password_supervisor);
            overflow = (ImageView) view.findViewById(R.id.setting);
        }
    }


    public supervisor_adapter(Context mContext, List<supervisor> supervisorList) {
        this.mContext = mContext;
        this.supervisorList = supervisorList;
        //this.orig=studentsList;
    }

    @Override
    public supervisor_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supervisor_row_item, parent, false);

        return new supervisor_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final supervisor_adapter.MyViewHolder holder, final int position) {
        supervisor supervisor = supervisorList.get(position);
        holder.name_supervisor.setText(supervisor.getName());
        holder.id_supervisor.setText(supervisor.getId());
        holder.pass_supervisor.setText(supervisor.getPassword());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
                pos = position;
                name=holder.name_supervisor.getText().toString();
                idNO=holder.id_supervisor.getText().toString();
                password=holder.pass_supervisor.getText().toString();

            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.contacts, popup.getMenu());
        popup.setOnMenuItemClickListener(new supervisor_adapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.edit:
                   edit_student_dialog();
                    all_supervisor();
                    return true;
                case R.id.delete:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                    builder2.setMessage(" are you sure ?");
                    builder2.setCancelable(true)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = database.getReference();
                                    Query query = ref.child("supervisor").orderByChild("id").equalTo(idNO);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                snapshot.getRef().removeValue();
                                                                                           }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    all_supervisor();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertdialog2 = builder2.create();
                    alertdialog2.show();
                    return true;
                default:
            }
            return false;
        }
    }
        @Override
        public int getItemCount () {
            return supervisorList.size();

    }


    public void edit_student_dialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View alertLayout = inflater.inflate(R.layout.edit_supervisor, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(alertLayout);
       final EditText name_edittext=(EditText)alertLayout.findViewById(R.id.supervisor_username_edit);
        final EditText password_edittext=(EditText)alertLayout.findViewById(R.id.supervisor_password_edit);
        final EditText id_edittext=(EditText)alertLayout.findViewById(R.id.supervisor_id_edit);

        name_edittext.setText(name);
        password_edittext.setText(password);
        id_edittext.setText(idNO);
        TextView title = new TextView(mContext);
        Button b_neg=(Button) alertLayout.findViewById(R.id.btn_Edit_supervisor);;


        b_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = mContext.getSharedPreferences("arch", MODE_PRIVATE);
                Token = prefs.getString(Keys.KEY_TOKEN, "");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference();
                final supervisor supervisor = new supervisor();
                supervisor.setName(name_edittext.getText().toString());
                supervisor.setId(id_edittext.getText().toString());
                supervisor.setPassword(password_edittext.getText().toString());
                supervisor.setToken(Token);
                final user user = new user();
                user.setName(name_edittext.getText().toString());
                user.setId(id_edittext.getText().toString());
                user.setPassword(password_edittext.getText().toString());
                user.setToken(Token);
                final Query query1 = ref.child("supervisor").orderByChild("id").equalTo(idNO);
                final Query query2 = ref.child("user").orderByChild("id").equalTo(idNO);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.getRef().setValue(supervisor);
                            snapshot.getRef().setValue(user);
                            notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            snapshot.getRef().setValue(user);
                            //   notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }

        });
// / You Can Customise your Title here
        title.setText("Edit");
        title.setBackgroundColor(Color.GRAY);
        title.setPadding(15, 15, 15, 15);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        alert.setCustomTitle(title);




        alertDialog = alert.create();
        ///  dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        alertDialog.show();

    }





   public void all_supervisor(){
       final FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference();
       ref.child("supervisor").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               supervisorList.clear();

               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   supervisor supervisor = snapshot.getValue(supervisor.class);
                   SharedPreferences prefs = mContext.getSharedPreferences("arch", MODE_PRIVATE);
                   Token = prefs.getString(Keys.KEY_TOKEN, "");
                   if(supervisor.getToken().equals(Token)){
                       supervisorList.add(supervisor);
                     //  notifyDataSetChanged();
                       }
               }
notifyDataSetChanged();
               Collections.reverse(supervisorList);

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
   }




}
