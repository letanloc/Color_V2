package com.letanloc.color_v2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.arc_Layout.ArcLayout;
import com.arc_Layout.widget.ClipRevealFrame;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener, View.OnClickListener {

    DrawableView drawing;
    private float smallBrush, mediumBrush, largeBrush;/* chieu  lon cua buton*/
    DrawingView_1 drawView;
    ClipRevealFrame FrameDialog;
    ArcLayout arclayout;
    View v, mainlayout;/* khởi tạo giá trị từ v*/
    int x, y;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.appbarr));
        getSupportActionBar().setTitle("");
        MenuItem itm;
        drawing = (DrawableView) findViewById(R.id.drawing);
        Arc();

    }

    public void Arc() {
        this.v = v;
/* gọi cạ giá trị của layout arc*/
        Button fab = (Button) findViewById(R.id.fab);
        FrameDialog = (ClipRevealFrame) findViewById(R.id.menu_layout);
        mainlayout = findViewById(R.id.mainlayout);

        fab.setOnClickListener(this);
    }

    public void arcshow(View v) {
        this.v = v;
        x = (v.getLeft() + v.getRight()) / 2;// tọa độ x= chiều rộng chia 2 và toaj dộ y cũng vậy
        y = (v.getTop() + v.getBottom()) / 2;// láy khoan cach ben trai con vs ben phai thi tưc la no nam o giu day la noi chứa tọa dộ xats hiện
        float radiusOfFab = 1f * v.getWidth() / 2f;
        float radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, mainlayout.getWidth() - x),
                Math.max(y, mainlayout.getHeight() - y));
        if (v.isSelected()) {
            hidemenu(x, y, radiusFromFabToRoot, radiusOfFab);// nếu nó gì dó thì ó xẻ ản giá tri đó đi
        } else {
            showMenu(x, y, radiusOfFab, radiusFromFabToRoot);
        }
        v.setSelected(!v.isSelected());


//        float radiausfad = x * x + 2* x * y + y * y;
 /* noi day chuwa phuong trinh binh phuong tui vaan chua hieu no la gi*/
    }

    /* lays toa foi, diem bac dau v diem ket thuc cho no*/
    public void hidemenu(int x, int y, float starradiaus, float endradiaus) {
        List<Animator> amimaList = new ArrayList<>();
        Animator revealAnim = createCircularReveal(FrameDialog, x, x, starradiaus, endradiaus);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FrameDialog.setVisibility(View.INVISIBLE);
            }
        });

        amimaList.add(revealAnim);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(amimaList);
        animSet.start();

//            Animator animator =createCircularReveal(mMenu, cx, cy, startRadius, endRadius);

    }

    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        FrameDialog.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        Animator revealAnim = createCircularReveal(FrameDialog, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);

        animList.add(revealAnim);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }


    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius, float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem itm = (MenuItem) findViewById(R.id.action_Clear);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_Clear:


                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                arcshow(view);
                break;

        }
    }
}
