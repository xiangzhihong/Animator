package com.xzh.androiddemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.xzh.androiddemo.utils.MyInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.username_layout)
    LinearLayout usernameLayout;
    @Bind(R.id.password_layout)
    LinearLayout passwordLayout;
    @Bind(R.id.account_layout)
    View accountLayout;
    @Bind(R.id.progress_layout)
    View progressLayout;

    private int viewWidth,viewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.main_btn_login)
    public void onClick() {
        usernameLayout.setVisibility(View.INVISIBLE);
        passwordLayout.setVisibility(View.INVISIBLE);
        
        initAnim();
    }

    private void initAnim() {
        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, viewWidth);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) accountLayout
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                accountLayout.setLayoutParams(params);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(accountLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, objectAnimator);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                progressLayout.setVisibility(View.VISIBLE);
                accountLayout.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressAnimator();
                        MainActivity.open(LoginActivity.this);
                    }
                },3000);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void progressAnimator() {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(progressLayout,
                animator, animator2);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new MyInterpolator());
        objectAnimator.start();

    }
}
