package com.archives.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.archives.Login;
import com.archives.R;
import com.archives.Signup;

import static java.lang.System.exit;

/**
 * Created by locall on 2/14/2018.
 */

public class Home_admin  extends AppCompatActivity {
    // تعريف العتصاصر
    Button manage_student_account, manage_supervisor_account, manage_notifcation,send_important_files;
    ImageView logout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.home_admin);

        //هذا الكود لتغير لون اليدير ليصبح مثل لون التطبيق

        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        // ربط العناصر بالواجهات
       manage_student_account = (Button) findViewById(R.id.student);
        manage_supervisor_account = (Button)findViewById(R.id.supervisor);
        manage_notifcation = (Button)findViewById(R.id.notification);
        send_important_files = (Button)findViewById(R.id.files);
        logout =(ImageView)findViewById(R.id.logout);

        // عند الضغط علة ايقوم تسجيل الخروج
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(0);
            }
        });

        // زر ادارة جسابات الطلاب
        manage_student_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // انتقال اللي واجهة ادارة الحسابات
                Intent i = new Intent(Home_admin.this, manage_students.class);
                startActivity(i);

            }
        });

        // زر ادارة حسابات المشرفين
        manage_supervisor_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_admin.this, manage_supervisor.class);
                startActivity(i);

            }
        });
        // زر ادارة التبيهات للطلاب
        manage_notifcation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_admin.this, Manage_alert.class);
                startActivity(i);

            }
        });

        //زي ارسال الملفات المهمة 
        send_important_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_admin.this, Manage_files.class);
                startActivity(i);
            }
        });

    }
}
