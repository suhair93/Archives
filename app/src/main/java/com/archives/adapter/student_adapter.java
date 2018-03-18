package com.archives.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.archives.R;
import com.archives.help.Keys;
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


public class student_adapter extends RecyclerView.Adapter<student_adapter.MyViewHolder>  {
    AlertDialog alertDialog;
Context mContext;
    String name="",idNO="",password="",Token="";
  List<student> studentsList;
  int pos =0;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_student, pass_student,id_student,id_project;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            name_student = (TextView) view.findViewById(R.id.name_student);
            id_student = (TextView) view.findViewById(R.id.id_student);
            pass_student = (TextView) view.findViewById(R.id.password_student);
            overflow = (ImageView) view.findViewById(R.id.setting);
        }
    }


    public student_adapter(Context mContext, List<student> studentsList) {
        this.mContext = mContext;
        this.studentsList = studentsList;
        //this.orig=studentsList;
    }

    @Override
    public student_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_row_item, parent, false);

        return new student_adapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final student_adapter.MyViewHolder holder, final int position) {
        student student = studentsList.get(position);
        holder.name_student.setText(student.getName());
        holder.id_student.setText(student.getId());
        holder.pass_student.setText(student.getPassword());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
                pos = position;
                name=holder.name_student.getText().toString();
                idNO=holder.id_student.getText().toString();
                password=holder.pass_student.getText().toString();

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
        popup.setOnMenuItemClickListener(new student_adapter.MyMenuItemClickListener());
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
                    all_student();
                    return true;
                case R.id.delete:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                    builder2.setMessage(" are you sure ?");
                    builder2.setCancelable(true)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = database.getReference();
                                    Query query = ref.child("students").orderByChild("id").equalTo(idNO);

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

                                    /////////////////////////////////////
                                    Query query1 = ref.child("user").orderByChild("id").equalTo(idNO);

                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
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

                                    all_student();

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
            return studentsList.size();

    }


    public void edit_student_dialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View alertLayout = inflater.inflate(R.layout.edit_student, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setView(alertLayout);
       final EditText name_edittext=(EditText)alertLayout.findViewById(R.id.student_username_edit);
        final EditText password_edittext=(EditText)alertLayout.findViewById(R.id.student_password_edit);
        final EditText id_edittext=(EditText)alertLayout.findViewById(R.id.student_id_edit);

        name_edittext.setText(name);
        password_edittext.setText(password);
        id_edittext.setText(idNO);
        TextView title = new TextView(mContext);
        Button b_neg=(Button) alertLayout.findViewById(R.id.btn_Edit_student);;


        b_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = mContext.getSharedPreferences("arch", MODE_PRIVATE);
                Token = prefs.getString(Keys.KEY_TOKEN, "");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference();
                final student student = new student();
                student.setName(name_edittext.getText().toString());
                student.setId(id_edittext.getText().toString());
                student.setPassword(password_edittext.getText().toString());
                student.setToken(Token);
                final user user = new user();
                user.setName(name_edittext.getText().toString());
                user.setId(id_edittext.getText().toString());
                user.setPassword(password_edittext.getText().toString());
                user.setToken(Token);
                final Query query1 = ref.child("students").orderByChild("id").equalTo(idNO);
                final Query query2 = ref.child("user").orderByChild("id").equalTo(idNO);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.getRef().setValue(student);
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





   public void all_student(){
       final FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference ref = database.getReference();
       ref.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               studentsList.clear();

               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   student student = snapshot.getValue(student.class);
                   SharedPreferences prefs = mContext.getSharedPreferences("arch", MODE_PRIVATE);
                   Token = prefs.getString(Keys.KEY_TOKEN, "");
                   if(student.getToken().equals(Token)){
                       studentsList.add(student);
                     //  notifyDataSetChanged();
                       }
               }
                  notifyDataSetChanged();
               Collections.reverse(studentsList);

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
   }




}
