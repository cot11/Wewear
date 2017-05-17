package com.example.cot11.wewear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 이언우 on 2017-05-17.
 */

class MyView extends ImageView {
    PhotoViewAttacher attacher;
    Paint paint = new Paint();
    Path path  = new Path();    // 자취를 저장할 객체
    private Drawable mPlaceholder, mImage;
    private Bitmap KK = null;
    public MyView(Context context) {
        super(context);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
    }
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
    }

    public MyView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
        paint.setStrokeWidth(10f); // 선의 굵기 지정
    }

    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 메서드
        if(KK != null)
        {
            canvas.drawBitmap(KK,0,0,paint);
        }
        canvas.drawPath(path, paint); // 저장된 path 를 그려라
    }

    public void setmImage(Bitmap bitmap)
    {
        setImageBitmap(bitmap);
        KK = bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int pointerCount = event.getPointerCount();
        if(pointerCount >= 2)
        {
            System.out.println("하극상");
        }
        else
        {
            switch(event.getAction()) {

                case MotionEvent.ACTION_DOWN :
                    path.moveTo(x, y); // 자취에 그리지 말고 위치만 이동해라
                    break;
                case MotionEvent.ACTION_MOVE :
                    path.lineTo(x, y); // 자취에 선을 그려라
                    break;
                case MotionEvent.ACTION_UP :
                    break;
            }
        }
        invalidate(); // 화면을 다시그려라
        return true;
    }

}