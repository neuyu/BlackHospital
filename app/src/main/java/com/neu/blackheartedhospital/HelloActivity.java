package com.neu.blackheartedhospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 引导页
 * Created by neu on 16/5/4.
 */
public class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView imageView = (ImageView) findViewById(R.id.de_img_backgroud);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        imageView.startAnimation(animation);




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeDialog();
            }


        });
    }

    private void showMeDialog() {
        new SweetAlertDialog(this)
                .setTitleText("Thank to neuyu!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        gotoMain();
                    }
                })
                .show();

    }

    private void gotoMain() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
