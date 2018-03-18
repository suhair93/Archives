package com.archives.student;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.archives.R;
import com.archives.admin.Home_admin;
import com.archives.admin.manage_students;

import static java.lang.System.exit;

/**
 * Created by locall on 2/14/2018.
 */

public class Home_student extends AppCompatActivity {
   private Button project_student, report_student,alert_student,file_student;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.home_student);

        //هذا الكود لتغير لون اليدير ليصبح مثل لون التطبيق

        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        project_student = (Button) findViewById(R.id.project_btn_student);
        report_student = (Button)findViewById(R.id.reports_btn_student);
        alert_student =(Button)findViewById(R.id.alert_btn_student);
        file_student =(Button)findViewById(R.id.files_btn_student);


        ImageView logout =(ImageView)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(0);
            }
        });

        project_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_student.this, all_projects_student.class);
                startActivity(i);
            }
        });

        report_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_student.this, recive_reports.class);
                startActivity(i);
            }
        });


        alert_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_student.this, recive_alert.class);
                startActivity(i);
            }
        });
        file_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_student.this, recive_files.class);
                startActivity(i);
            }
        });

    }
}
