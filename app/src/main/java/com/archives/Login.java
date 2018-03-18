package com.archives;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.archives.adapter.student_adapter;
import com.archives.admin.Home_admin;
import com.archives.admin.manage_students;
import com.archives.help.Keys;
import com.archives.models.student;
import com.archives.models.user;
import com.archives.student.Home_student;
import com.archives.supervisor.Home_supervisor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by locall on 2/14/2018.
 */

public class Login  extends AppCompatActivity {
    EditText inputUsername;
    EditText inputPassword;
    TextView signup;
    ImageButton mCbShowPwd;
    String name = "", password = "";
    ProgressDialog dialog;
    Button login;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
     List<user> userlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.login);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        //هذا الكود لتغير لون اليدير ليصبح مثل لون التطبيق

        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
  // للانتظار قبل الذهاب للرئيسية
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // تعريف المتغيرات وربطها بالواجهات
        login = (Button) findViewById(R.id.btn_login);
        mCbShowPwd = (ImageButton) findViewById(R.id.show_pass);
        inputUsername = (EditText) findViewById(R.id.usen_name);
        inputPassword = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup1);


// to show password
        mCbShowPwd.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                }
                return true;
            }
        });

        userlist= new ArrayList<>();


        //عند الضغط علي التسجيل حساب جديد
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
                finish();
            }
        });
        // عند الضغط على زر اللوجين
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputUsername.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                //authenticate user
 // نفس الداله المستخدمه بتسجيل حساب جديد

                Query fireQuery = ref.child("user").orderByChild("id").equalTo(email);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ازا الحساب غير موجوديظهر مسج
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(Login.this, "Not found", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                        } else {
                            List<user> searchList = new ArrayList<user>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                user user = snapshot.getValue(user.class);
                                searchList.add(user);

                            }
                         // لوب ليقوم بالبحث عن الحساب
                            for(int i=0;i<searchList.size();i++){
                         // ازا الايميل والباسورد صحيحة
                                if(searchList.get(i).getId().equals(email) && searchList.get(i).getPassword().equals(password)) {

                                  //الاوبجكت هذا خاص بنقل البيانات من كلاس لكلاس اخر
                                    SharedPreferences.Editor editor = getSharedPreferences("arch", MODE_PRIVATE).edit();
                                    // شرط ازا كان نوع المستخدم ادمن
                                    if (searchList.get(i).getTypeUser().equals("admin")) {
                                        // Keys.KEY_TOKEN خزن الايميل بال
                                        //لاعتماده كid لحساب
                                        editor.putString(Keys.KEY_TOKEN, email);
                                        editor.apply();
                                        dialog.dismiss();
                                        // الانتقال لواجهة الرئيسية اللادمن
                                        Intent intent = new Intent(Login.this, Home_admin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    // ازا نوع المتسخدم طالب
                                    if (searchList.get(i).getTypeUser().equals("student") ) {
                                        // خزن الايميل هنا
                                        editor.putString(Keys.KEY_STUDENT, email);
                                        editor.apply();
                                        dialog.dismiss();
                                        // الانتقال لواجة الالرئيسية للطالب
                                        Intent intent = new Intent(Login.this, Home_student.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }
                                    // ازا كان مشرف
                                    if (searchList.get(i).getTypeUser().equals("supervisor") ) {
                                        editor.putString(Keys.KEY_SUPERVIOR, email);
                                        editor.apply();
                                        dialog.dismiss();
                                        Intent intent = new Intent(Login.this, Home_supervisor.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    // غير ذلك ازا كان خطأ بكلمة المرور او اسم المستخدم
                                }else{ Toast.makeText(Login.this, "invalid user name or password", Toast.LENGTH_SHORT).show();}

                            }


                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                             }





        });


    }



}
