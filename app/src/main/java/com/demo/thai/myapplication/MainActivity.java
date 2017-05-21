package com.demo.thai.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private ImageView mImageView = null;
    private Bitmap bm;
    /**
     * 螢幕高度
     */
    private int displayHeight;
    /**
     * 螢幕寬度
     */
    private int displayWidth;
    /**
     * 旋轉角度
     */
    protected float rotation = 0;
    /**
     * 圖片初始寬度
     */
    private int imgWidth;
    /**
     * 圖片初始高度
     */
    private int imgHeight;
    /**
     * 圖片改變中的樣式
     */
    private Matrix matrix = new Matrix();
    /**
     * 保存觸碰前的圖片
     */
    private Matrix savedMatrix = new Matrix();
    /**
     * 保存移動中的圖片
     */
    private Matrix matrix1 = new Matrix();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initData();

    }

    private float rotation(MotionEvent event) {
        double delta_x = ((displayWidth / 2) - event.getX());
        double delta_y = ((displayHeight / 2) - event.getY());
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void showImage() {
        imgWidth = bm.getWidth();
        imgHeight = bm.getHeight();
        mImageView.setImageBitmap(bm);
        centerAndRotate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        ImageView imageView = (ImageView) view;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                System.out.println("MotionEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                centerAndRotate();
                System.out.println("MotionEvent--ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                operateMove(event);
                imageView.setImageMatrix(matrix1);
                System.out.println("MotionEvent--ACTION_MOVE");
                break;

        }
        return true;
    }

    public void initData() {
        // TODO Auto-generated method stub
        bm = BitmapFactory.decodeResource(getResources(), R.mipmap.circlephone);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        displayWidth = dm.widthPixels;
        displayHeight = dm.heightPixels;
        mImageView = (ImageView) findViewById(R.id.rotate_view);
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);
        mImageView.setOnTouchListener(this);
        showImage();
    }

    private void operateMove(MotionEvent event) {
        matrix1.set(savedMatrix);
        rotation = rotation(event);
        System.out.println("旋轉角度---" + rotation);
        /** 旋轉角度 */
        matrix1.postRotate(rotation, displayWidth / 2, displayHeight / 2);
    }

    private void centerAndRotate() {
        RectF rect = new RectF(0, 0, imgWidth, imgHeight);
        matrix.mapRect(rect);
        float width = rect.width();
        float height = rect.height();
        float dx = 0;
        float dy = 0;

        if (width < displayWidth) {
            dx = displayWidth / 2 - width / 2 - rect.left;
        } else if (rect.left > 0) {
            dx = -rect.left;
        } else if (rect.right < displayWidth) {
            dx = displayWidth - rect.right;
        }

        if (height < displayHeight) {
            dy = displayHeight / 2 - height / 2 - rect.top;
        } else if (rect.top > 0) {
            dy = -rect.top;
        } else if (rect.bottom < displayHeight) {
            dy = displayHeight - rect.bottom;
        }

        matrix.postTranslate(dx, dy);

        matrix.postRotate(0, displayWidth / 2, displayHeight / 2);
        mImageView.setImageMatrix(matrix);
    }
}
