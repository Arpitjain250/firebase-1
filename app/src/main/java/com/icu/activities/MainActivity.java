package com.icu.activities;

import android.animation.Animator;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.icu.R;
import com.icu.utils.SharedPreferenceManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean isFabOpen = false;
    private Button btnLogin;
    private EditText userName, password;
    private boolean isUsername, isPassword;
    private CardView loginPanel, registerPanel;
    private FloatingActionButton fab;
    private Animation plusAnimation, crossAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        loginPanel = (CardView) findViewById(R.id.loginPanel);
        registerPanel = (CardView) findViewById(R.id.registerPanel);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        TextView forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feature coming soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 5) {
                    isPassword = true;
                    if (isUsername)
                        enableButton();
                } else {
                    isPassword = false;
                    disableButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 5) {
                    isUsername = true;
                    if (isPassword)
                        enableButton();
                } else {
                    isUsername = false;
                    disableButton();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        plusAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_plus);
        crossAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_cross);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                animateButton();
                break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_history) {
            startActivity(new Intent(this, MessageListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableButton() {
        btnLogin.setEnabled(true);
        btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.button_enabled));
        btnLogin.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void disableButton() {
        btnLogin.setEnabled(false);
        btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.button_disabled));
        btnLogin.setTextColor(ContextCompat.getColor(this, R.color.colorDisabled));
    }

    private void animateCard() {
        int cx = (loginPanel.getLeft() + loginPanel.getRight()) / 2;
        int cy = (loginPanel.getTop() + loginPanel.getBottom()) / 2;

        int dx = Math.max(cx, loginPanel.getWidth() - cx);
        int dy = Math.max(cy, loginPanel.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);


        Animator animator =
                ViewAnimationUtils.createCircularReveal(registerPanel, loginPanel.getRight(), loginPanel.getTop(), 0, finalRadius + 500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(600);
        registerPanel.setVisibility(View.VISIBLE);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                fab.layout(fab.getLeft(),
                        fab.getTop(),
                        fab.getRight(),
                        fab.getBottom());
                fab.invalidate();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void animateCardReverse() {
        int cx = (loginPanel.getLeft() + loginPanel.getRight()) / 2;
        int cy = (loginPanel.getTop() + loginPanel.getBottom()) / 2;

        int dx = Math.max(cx, loginPanel.getWidth() - cx);
        int dy = Math.max(cy, loginPanel.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);


        Animator animator =
                ViewAnimationUtils.createCircularReveal(registerPanel, loginPanel.getRight(), loginPanel.getTop(), finalRadius + 500, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(600);

        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                registerPanel.setVisibility(View.GONE);
                fab.layout(0, 0, 0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void animateFAB() {

        if (isFabOpen) {
            animateCardReverse();
            fab.startAnimation(crossAnimation);
            fab.setElevation(0);
            fab.setTranslationZ(0);
            isFabOpen = false;
        } else {
            animateCard();
            fab.startAnimation(plusAnimation);
            fab.setElevation(20);
            isFabOpen = true;

        }
    }

    private void setupUsername(String userId) {
        SharedPreferenceManager.getInstance(this).saveData("userid", userId);
    }

    private void animateButton() {
        int cx = btnLogin.getWidth() / 2;
        int cy = btnLogin.getHeight() / 2;

        int dx = Math.max(cx, btnLogin.getWidth() - cx);
        int dy = Math.max(cy, btnLogin.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);


        Animator animator =
                ViewAnimationUtils.createCircularReveal(btnLogin, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        btnLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                btnLogin.setTextColor(getResources().getColor(R.color.white));
                setupUsername(userName.getText().toString());
                startActivity(new Intent(MainActivity.this, MapActivity.class));

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
    }
}
