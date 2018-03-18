package com.archives.supervisor;

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
import com.archives.admin.manage_students;

import static java.lang.System.exit;

/**
 * Created by locall on 2/14/2018.
 */

public class Home_supervisor extends AppCompatActivity {
    Button view_project, send_report,add_student_in_project,add_new_project;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.home_supervisor);

        //هذا الكود لتغير لون اليدير ليصبح مثل لون التطبيق

        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        view_project = (Button) findViewById(R.id.all_project_btn);
        add_new_project = (Button)findViewById(R.id.new_project_btn);
        add_student_in_project = (Button)findViewById(R.id.add_student_project_btn);
        send_report = (Button)findViewById(R.id.report_btn);

        ImageView logout =(ImageView)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit(0);
            }
        });

        add_new_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_supervisor.this, new_project.class);
                startActivity(i);

            }
        });

        add_student_in_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_supervisor.this, add_student_in_project.class);
                startActivity(i);

            }
        });
        view_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_supervisor.this, all_projects.class);
                startActivity(i);
            }
        });

        send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_supervisor.this, send_report.class);
                startActivity(i);

            }
        });


    }
}
