package com.archives;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.archives.adapter.student_adapter;
import com.archives.admin.manage_students;
import com.archives.models.student;
import com.archives.models.user;
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

public class Signup extends AppCompatActivity {
    ProgressDialog dialog;
    EditText inputEmail;
    EditText inputPassword;
    EditText rewrite_password;
    Button signup_btn;
    ImageView back;

    // بتعريف الاوبجكت الخاصه بالفيربيس
    FirebaseDatabase database;
    DatabaseReference ref;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.signup);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        //هذا الكود لتغير لون اليدير ليصبح مثل لون التطبيق

        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        // للانتظار قبل الذهاب للواجهة التالية
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        // ربط العناصر بالواجهة
        inputEmail = (EditText)findViewById(R.id.email);
        inputPassword = (EditText)findViewById(R.id.password);
        rewrite_password = (EditText)findViewById(R.id.rewrite_password);
        signup_btn = (Button) findViewById(R.id.signup);
        back = (ImageView) findViewById(R.id.back);

        // زر الرجوع
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signup.this, Login.class);
                startActivity(i);
                finish();
            }
        });
        // زر تسجيل حساب جديد والذي يحدث فيه عمليه التسجيل وحفظ البيانات بالفير بيس
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // تخزين الايميل والباسورد في متغيرات من نوع استرنج
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
// شروط عندما لا يدخل المستخدم الايميل او الباسورد
                if (TextUtils.isEmpty(email)) {
                    // الرسالة التى تظهر للمستخدم
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // شرط ان لا يكون الباسورد اقل من 6 ارقام او حروف
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(!password.equals(rewrite_password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password is not identical", Toast.LENGTH_SHORT).show();

                }
                // ظهور علامة التحميل
                dialog.show();
// هذه الدالة خاصه بالبحث في الفيربيس للتأكد من ان الايميل الذي تم ادخاله غير موجود بالداتا بيس
                //user اسم الجدول الذي يتم تخزين فيه كل حسابات المسخدمين
                //id هو العمود الي يتم تخزين فيه الايميل
                // email وهو متغير الستلانج اللي خزنت فيه ليتم مقارنته بالاوبجكت الموجود
                Query fireQuery = ref.child("user").orderByChild("id").equalTo(email);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ازا غير موجود قم بتخزينه
                        if (dataSnapshot.getValue() == null) {

                           // تم تخزينه من نوع ادمن
                            String typeuser = "admin";
                            // اوبجكت من نوع يوزر لتخزين بيانات الادمن الجديد
                            user user = new user();
                            user.setId(email);
                            user.setPassword(password);
                            user.setTypeUser(typeuser);
                            // حفظه ك اوبجكت في جدول اليوزر بالفيربيس
                            ref.child("user").push().setValue(user);
                            // رسالة عند الانتهاء
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                            // الانتقال اللي تسجيل الدخول
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        } else {
                            // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "This account already exists", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dialog.dismiss();
                        // رساله خطأ
                        Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        dialog.dismiss();
    }
}
