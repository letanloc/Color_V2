package com.letanloc.color_v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by letanloc on 10/04/2015.
 */
public class DrawingView_1 extends View implements View.OnTouchListener {
    private ScaleGestureDetector scaleGestureDetector;
    private ShapeDrawable shapeDrawable;
//    private GestureScaler gestureScaler;
    private Path drawPath;
    Shape shaper;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;/*gia trj mau dau tien*/
    //canvas
    private int paintAlpha = 255;/*gia tri mo*/
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //brush sizes
    private float brushSize, lastBrushSize;
    //erase flag
    private boolean erase = false;







    public DrawingView_1(Context context, AttributeSet attributes) {
        super(context, attributes);
        setupDrawing();
//        gestureScaler = new GestureScaler(new ScalerListener());
//        scaleGestureDetector =
//                new ScaleGestureDetector(getContext(), new GestureScaleListener(gestureScaler));
    }

    //setup drawing

    /* cài đặc mau mặc đinh cho  lần xuất hiện*/
    private void setupDrawing() {

        //prepare for drawing and setup paint stroke properties
        brushSize = getResources().getInteger(R.integer.small_size);
        lastBrushSize = brushSize;/*size cura nets*/
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        //        gestureScaler= new GestureScaler(getSt);

//        drawPaint.setColorFilter();
//        drawPaint.setFakeBoldText(true);
//        drawPa    int.setDither(true);/* gia tri run len/*/

        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        /* drawPaint.setStrokeCap(Paint.Cap.BUTT); xt keiur hienj cua no la hinh tron hay sao */
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
//        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
            /*can vas pan laf phan ve*/
    }

    //size assigned to view /*Chúng tôi cần phải ghi đè lên một vài phương pháp
    // để thực hiện các tùy chỉnh chức năng Xem như một bản vẽ View. Đầu tiên,
    // vẫn còn ở trong DrawingView lớp, ghi đè lên các
    // onSizeChanged phương pháp,Bên trong phương pháp này, trước tiên hãy gọi phương thức lớp cha:
    // mà sẽ được gọi khi tùy chỉnh View được chỉ định một kích thước:
    // nhanh chóng khung vẽ và bitmap sử dụng các giá trị chiều rộng và chiều
    // */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*noii day xet chieu rong cho cai view*/
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        drawCanvas = new Canvas(canvasBitmap);
    }

    //draw the view - will be called after touch event
    @Override
        /*Để cho phép các lớp có chức năng như một bản vẽ tùy chỉnh View,
        chúng ta cũng cần phải ghi đè lên các OnDraw phương pháp, do đó, thêm nó vào lớp ngay bây giờ:*/
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

    }

    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //        gestureScaler= new GestureScaler();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;

    }

    public void undo() {
        if (canvasBitmap != null) {
            invalidate();
        }
    }

    /* cập nhaapt mau*/
    //update color
    public void setColor(String newColor) {
        invalidate();

        /*
        * xets gia tri do la mau hay la mau
        *
        *
        * */
        if (newColor.startsWith("#")) {
// neesu set color bat dau la mau thi
            paintColor = Color.parseColor(newColor);
            drawPaint.setColor(paintColor);/* hình thức chạm vaò*/
            drawPaint.setShader(null);
        }


        else {
            int patternID = getResources().getIdentifier(
                    newColor, "drawable", "com.letanloc.android_study_paint");
            Bitmap patternBMP = BitmapFactory.decodeResource(getResources(), patternID);
            BitmapShader patternBMPshader = new BitmapShader(patternBMP,
                    Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

            drawPaint.setColor(0xFFaa00aa);
            drawPaint.setShader(patternBMPshader);
        }

    }

    /*  setss chiều dài cho nó*/
    //    /set brush size chiều rồng
    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());/*  kieur giá trị lấy theo kiểu dip láy giá  ttri mới láy gía trị ccura số liệu hiển thị*/
        brushSize = pixelAmount;
            drawPaint.setStrokeWidth(brushSize);
    }

    //get and set last brush size/*nhận và thiết lập kích thước brush cuối cùng*/
    public void setLastBrushSize(float lastSize) {
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }

    //set erase true or false/*bộ Xoá bỏ đúng hay sai*/
    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    //start new drawing
    public void startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    // sư kiện
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        //        gestureDetector.onTouchEvent(event);
        //        gestureCreator.onTouchEvent(event);
        invalidate();
        return true;
    }


    //set alpha
    /*
     * nhận giá trị từ draw paint là giá trj của
      *  nhânj giá trị từ get imaget
      *
      * */
    public void setPaintAlpha(int newAlpha) {
        paintAlpha = Math.round((float) newAlpha / 100 * 255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }

    public int getPaintAlpha() {

        return Math.round((float) paintAlpha / 255 * 100);/*100 là của seek bar giá trị của nó là giá trị của 2.55*/
    }
}
