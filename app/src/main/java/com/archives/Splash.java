package com.archives;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class Splash extends AppCompatActivity {
  //هنا يتم تعريف العناصر السمسخدمه في الواجهة

    ProgressDialog dialog;
    public AppCompatImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط الكود بالواجهة
        setContentView(R.layout.splash_layout);

        // هذا الكود لتغير لون الترويسه للهاتف نفسه اللي بيظهر فيه الساعه وغيره
        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

        //****************************//
       // هنا يتم ربط العناصر بالواجهة من خلال ال id الخاص بكل عنصر
        logo = findViewById(R.id.logo);

        // شكل التحميل هند الدخول
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("جاري تسجيل الدخول . يرجى الإنتظار ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);


        // الحركة التي تحدث للصورة
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.hyperspace_jump);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        //ربط الحركه بالصورة
        logo.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(an2);
            // عند الانتهاء من الحركه ينتقل الي واجهة تسجيل الدخول
                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        } );
    }
}
