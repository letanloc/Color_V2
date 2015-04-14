package com.letanloc.color_v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by letanloc on 13/04/2015.
 */
public class DrawableView extends View {
    private ScaleGestureDetector scaleGestureDetector;
    private Path drawPath;/* daw pat này giống như đầu bút tưởng tượng vậy */
    private Paint drawPaint, canvasPaint;/* daw pain laf sown tức là màu sơn lấy giá trị màu sơn canvás theo kiểu ve hoặc là bức tranh*/
    private int Paintcolor = 0xFF660000;/* pain là màu sơn tức là đây là màu sơn cua nó ở vị trí đàu tiên*/
    private int paintAlpha = 255;/*max của nó là 255 và đây là giá trị của độ trong suốt của nét vẻ*/
    private Canvas drawCanvas;/*lấy gí trị cho bức tranh hay trang view đó*/
    private Bitmap canvasBitrap;/*  ddây là biến nó theo kiểu dạng bitmap giống như trong  một tắm hình*/
    private float brushsize, lastbrushSize;/* size ở đây là chỉ kích thước đầu tiên khi khởi tao giá trị cho dawpath và last tức là giá trị sau khi khởi tạo giá trị cho noS*/
    private boolean erase = false;/*erase  đây là nơi chứa giá trị save cuả nó */
    private int sizebrush = 50;/* size tối đa hay mặc  đinhj dành cho  nét bút*/





    //    private  GestureScaler  a;
    public DrawableView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setupDraw();
    }

    /* phần cài đặc chho màu hiện tại*/
    public void setupDraw() {
        brushsize = getResources().getInteger(R.integer.small_size);
        lastbrushSize = brushsize;
        drawPath = new Path();/* khởi taọ */
        drawPaint = new Paint();/*kkhowir tạo*/
        /*sét gía trị màu đầu tiên cho nó*/
        drawPaint.setColor(Paintcolor);
//        thiết lập chống Bí danh vaf macwj định giá trị của nó là true
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushsize);/* sets gia tri dau tien cho nó */
        drawPaint.setStyle(Paint.Style.STROKE);/* nơi đây set style của nó theo kiểu stroke*/
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);/* nơi đay sét một nét gì đó của nó nhưng  tôi vaant chưa biết được*/
        canvasPaint = new Paint(Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);


    }

    /* sestt gía trọ bằng chiều dà và chiều rộng của cái view chúng ta sử sụng vá sủ dụng mauf theo kiểu  ...*/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitrap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);/* sét giá trik mauuf cho phần chane*/
        drawCanvas = new Canvas(canvasBitrap);/*lấy giá trj của canvasbit trap */

    }

    /* phuuowng phap gi đè lên trên view thì phải sets giá trị show lên màng hinhf*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitrap, 0, 0, canvasPaint);/* can vas vẻ theo bit và lấy giá trj dau tư canvasbitrap va mau tư can vaspain*/
            /* nơi ver dương ra*/
        canvas.drawPath(drawPath, drawPaint);

    }

    /* nơi đây sét sự kiện  chạm vào magnf hình*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchx = event.getX();/* giá trị float cho  lấy từ giá trị x của sự kiện  lấy gía triij x */
        float touchy = event.getY();/*lấy giá trị từ sự kiện chạm vào màng hinh , màng hình chamk ở đâu thì lấy giá trị y từ đó*/

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               /* neeus như nó có sự kiện là xóa thì    */
                drawPath.moveTo(touchx, touchy);/* */
                break;
            case MotionEvent.ACTION_MOVE:
                /* néu  hoạt động của nó là duy chiên thì */
                drawPath.lineTo(touchx, touchy);
                /*  để dòng đó ngay tại vị trí bạn đăc tay lên tức là khi đăc xuống rồi thì khi duy chuyển nó tới đây thì màu nó bám vào tới đó theo tọa độ x và  y*/
                break;
            case MotionEvent.ACTION_UP:
                    /* khi no duy chuyeenr len thì */
                /*1*/
                /* nét vẻ hay đườn butts giử giá trị màu  đương line ngay tại đó*/
                drawPath.lineTo(touchx, touchy);
                /*2*/
                drawCanvas.drawPath(drawPath, drawPaint);/* bứt tranh sẻ chứa giá trị từ màu và nét vẻ đo*/
                drawPath.reset();/* vaf nét vẻ quay lại vị trí ban đầu tức là vị trí crủa nó khi chưa hoạt động*/
                break;
                default:
                    return false;/* không thì giá trị hiện tại của nó ser là như thế này*/
        }
//        vô  hiềuj hóa ?

        invalidate();
        return true;
    }

    public void undo() {
        if (canvasBitrap != null) {
            invalidate();
        }
    }


    public void setcolor(String newcolor) {
        /* noi day nhan gais trij mau*/
        invalidate();
        //   làm mất hiệu lực

        /* dầu tiên phải sét giá trị màu nhận được*/
        if (newcolor.startsWith("#")) {
            /* ở đây tức là khi gía trị theo kiể chuổi khi lấy và là giá trị đứng đâu là một dấu thăng thì nó sẻ*/
            Paintcolor = Color.parseColor(newcolor);
            /*là sơn vet*/
            /* sơn của bút sẻ lấy gaistrij của màu hienj  mới*/
            drawPaint.setColor(Paintcolor);
            drawPaint.setShader(null);/* và ó ko chưa một đói tượng vật lý nào cả*/
        }
    }

    /* cài đặc sizde cho nét cọ*/
    public void setuprushsize(float newsize) {
        float PixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newsize, getResources().getDisplayMetrics());
//        *  kieur giá trị lấy theo kiểu dip láy giá  ttri mới láy gía trị ccura số liệu hiển thị*/
        brushsize = PixelAmount;/* size của ó se bang thangg nay*/
        drawPaint.setStrokeWidth(brushsize);/* và nkcish thovws  của ngoi se băn cai nay*/
    }

    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void setLastBrushSize(float lastSize) {
        lastbrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return lastbrushSize;
    }


    public int getrussize() {

        return sizebrush;
    }
}
