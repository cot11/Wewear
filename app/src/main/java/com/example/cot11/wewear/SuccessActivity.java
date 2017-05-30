package com.example.cot11.wewear;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.senab.photoview.PhotoViewAttacher;

public class SuccessActivity extends Activity {

    static {
        System.loadLibrary("opencv_java");
    }
    // final
    private static final int SELECT_PICTURE = 1;
    private static final int MSG_SUCCESS = 0;
    private static final int MSG_FAILURE = 1;
    private static final int MSG_PROGRESS = 2;
    private static final int MSG_STATUS = 3;
    private static final Scalar mRedColor = new Scalar(255, 0, 0);
    private static final Scalar 	mCyanColor = new Scalar(0, 255, 255);


    // class
    private UserProfile myprofile;
    private ImageView mImageView;
    private GlobalApplication	mApp;

    // Activity
    private Button mGallaryButton = null;
    private Button logout = null;
    private Button add = null;
    private Button unlick = null;
    private ProgressDialog mProgress;

    // etc
    private String mImageFileName = "";
    private boolean mFittingDone = false;
    private Bitmap mBitmap = null;
    private Bitmap tempcanvas;
    private Bitmap temp2canvas;
    private Bitmap bitmap2 = null;
    private int mScaleFactor = 1;
    private Path path;      //페스
    private String mode = "none";
    private float downx = 0, downy = 0, upx = 0, upy = 0;
    private  Canvas canvas;
    private Canvas canvas2;
    private ArrayList<Point> arrayList;
    private ArrayList<Point> pointsList;
    private ArrayList<Float> floatsList;
    private boolean setCircle = false;
    private RelativeLayout linearLayout;
    private PathMeasure pathMeasure;

    private Paint paint;
    private Paint paint1;
    private Paint paint2;
    private Paint temppaint2;
    private Bitmap temp;
    private Bitmap temp2;
    private PhotoViewAttacher attacher;

    private Context context;


    // Thread & Handler


    // Action
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {
            switch(msg.what) {
                case MSG_SUCCESS:
                    mFittingDone = true;
                    //mProgress.dismiss();
                    mImageView.setImageBitmap((Bitmap) msg.obj);
                    Toast.makeText(getApplication(), "Fitting Done", Toast.LENGTH_LONG).show();
                    break;
                case MSG_FAILURE:
                    mFittingDone = true;
                    //mProgress.dismiss();
                    Toast.makeText(getApplication(), "Canot detect any face", Toast.LENGTH_LONG).show();
                    break;
                case MSG_STATUS:
                    //mProgress.setMessage(msg.arg1 == 0 ? "Detecting" : "Alignment");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        SharedPreferences preferences = getSharedPreferences("A",MODE_PRIVATE);
        int firstviewshow = preferences.getInt("First",0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent_get = getIntent();
        String userinfo = intent_get.getStringExtra("userprofile");
        context = this;


        if(userinfo == null)
        {
            userinfo = "1";
        }
        if(firstviewshow != 1 && userinfo.equals("1")) // 처음 접속시 -> 모델 없을 경우
        {
            Intent intent = new Intent(SuccessActivity.this, FirstActivity.class);
            startActivity(intent);
        }
        else if(firstviewshow == 1 && userinfo.equals("1")) // 모델이 있을 경우 -> main page로 이동
        {
            Intent intent = new Intent(SuccessActivity.this, AvartaMain.class);
            startActivity(intent);
            finish();
        }
        else
        {
            System.out.println("아바타 만들기");
        }


        //init
        // class
        // Activity
        unlick = (Button)findViewById(R.id.unlink);
        add = (Button)findViewById(R.id.add);
        logout = (Button)findViewById(R.id.logout);
        mGallaryButton = (Button)findViewById(R.id.gallaryButton);
        mApp = (GlobalApplication)getApplication();
        path = new Path();


        // Action
        mGallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FromAlbum();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        System.out.println("로그아웃되었습니다.");
                    }
                });
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestMe();
                ChatData chatData = new ChatData("eonu","hihi");
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("Message").push().setValue(chatData);

            }
        });
        unlick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUnlink();
            }
        });

    }


    private void onClickUnlink() {
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }
                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    private void requestUpdateProfile() {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("nickname", "이언우");
        properties.put("email", "cot11@naver.com");

        UserManagement.requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                myprofile.updateUserProfile(properties);
                if (myprofile != null) {
                    myprofile.saveUserToCache();
                }
                Logger.d("succeeded to update user profile" + myprofile);
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.e(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

        }, properties);
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                myprofile = userProfile;
                requestUpdateProfile();
            }

            @Override
            public void onNotSignedUp() {
                //showSignup();
            }
        });
    }
    private void FromAlbum()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri uri = data.getData();
            if( uri == null ) {
                return;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            String path = null;
            if( cursor != null ){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if(cursor.moveToFirst())
                    path = cursor.getString(column_index);
                cursor.close();
            }
            if(path == null)
                path = uri.getPath();

            fittingOnStaticImageAsyn(path);
        }
    }

    private void facec()
    {

    }

    private void fittingOnStaticImageAsyn(String imgName){
        mImageFileName = imgName;
        System.out.println("mImageFileName : " + mImageFileName);
        mFittingDone = false;
        if(mBitmap != null)
            mBitmap.recycle();
        System.gc();

        int []factors = {1, 2, 4, 8, 16};
        BitmapFactory.Options opt = new BitmapFactory.Options();
        for(int i = 0; i < factors.length; i++)
        {
            boolean ok = true;
            try
            {
                if(i == 0)
                    mBitmap = BitmapFactory.decodeFile(imgName);
                else
                {
                    opt.inSampleSize = factors[i];
                    mBitmap = BitmapFactory.decodeFile(imgName, opt);
                }
            }
            catch(OutOfMemoryError e)
            {
                ok = false;
            }

            if(ok == true)
            {
                mScaleFactor = factors[i];
                break;
            }
        }

        if(mBitmap == null)
        {
            //Toast.makeText(mApp.getBaseContext(), "Cannot open image file " + imgName, Toast.LENGTH_LONG).show();
            System.out.println("이미지를 열 수 없습니다.");
            return;
        }

        linearLayout = (RelativeLayout)findViewById(R.id.KKK);
        mImageView = (ImageView) findViewById(R.id.GGGG);

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3F);
        paint1 = new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint1.setARGB(255, 255, 255, 0);
        paint1.setStrokeWidth(5F);
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setFilterBitmap(false);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));


        bitmap2 = Bitmap.createScaledBitmap(mBitmap, linearLayout.getWidth(), linearLayout.getHeight(), false);
        temp = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
        temp2 = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getHeight(), Bitmap.Config.ARGB_8888);

        canvas = new Canvas(temp);
        canvas.drawBitmap(bitmap2, 0, 0, null); // 전체화면의 배경을 그림
        canvas2 = new Canvas(temp2);
        mImageView.setImageBitmap(temp);
        attacher = new PhotoViewAttacher(mImageView);

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float[] value = new float[9];
                mImageView.getImageMatrix().getValues(value);
                int dx = (int)(((event.getX())/attacher.getScale())+(Math.abs(value[2]/attacher.getScale())));
                int dy = (int)((event.getY()/attacher.getScale())+(Math.abs(value[5])/attacher.getScale()));

                int action = event.getAction();
                int pointerCount = event.getPointerCount();
                if(pointerCount >= 2)
                {
                    attacher.onTouch(v, event);
                }
                else
                {
                    if(!setCircle)
                    {
                        switch (action)
                        {
                            case MotionEvent.ACTION_POINTER_1_DOWN:
                                mode = "zoom";
                                break;

                            case MotionEvent.ACTION_POINTER_2_DOWN:
                                mode = "zoom";
                                break;

                            case MotionEvent.ACTION_DOWN:
                                temp = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
                                temp2 = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
                                canvas = new Canvas(temp);
                                canvas.drawBitmap(bitmap2, 0, 0, null); // 전체화면의 배경을 그림
                                canvas2 = new Canvas(temp2);
                                mImageView.setImageBitmap(temp);
                                path.reset();
                                path.moveTo(dx, dy);
                                arrayList = new ArrayList<Point>();
                                arrayList.add(new Point(dx,dy));
                                break;
                            case MotionEvent.ACTION_UP:

                                PathMeasure pathMeasure2 = new PathMeasure(path,false);
                                float line_lenght = pathMeasure2.getLength();
                                float line_lenght2 = 0;

                                if(arrayList.size() > 30)
                                {
                                    if(arrayList.get(0).x != arrayList.get(arrayList.size()-1).x)
                                    {
                                        double m =  ((arrayList.get(arrayList.size()-1).y - arrayList.get(0).y) / (arrayList.get(arrayList.size()-1).x - arrayList.get(0).x));
                                        int bb = (int)(arrayList.get(0).y - (m * arrayList.get(0).x));
                                        int coco = (int)Math.abs(arrayList.get(0).x) - (int)Math.abs(arrayList.get(arrayList.size()-1).x);
                                        coco = Math.abs(coco);
                                        System.out.println("기울기 :  " +arrayList.get(0).x + " y : "  + arrayList.get(0).y);
                                        System.out.println("기울기 :  " +arrayList.get(arrayList.size()-1).x + " y : " + arrayList.get(arrayList.size()-1).y);
                                        System.out.println("기울기 : bb " +bb);
                                        System.out.println("기울기 : m " + m);
                                        System.out.println("기울기 : coco " + coco);
                                        System.out.println("기울기 : " + (arrayList.get(0).x * m));

                                        if(arrayList.get(0).x > arrayList.get(arrayList.size()-1).x)
                                        {
                                            for(float i = (float)arrayList.get(0).x; i > (int)arrayList.get(arrayList.size()-1).x; i--)
                                            {
                                                float k = (float) ((i * m) + bb);
                                                path.lineTo(i,k);
                                                System.out.println("value x "+ i + "y : " + k);
                                            }
                                        }
                                        else
                                        {
                                            for(float i = (float)arrayList.get(0).x; i < (int)arrayList.get(arrayList.size()-1).x; i++)
                                            {
                                                float k = (float) ((i * m) + bb);
                                                path.lineTo(i,k);
                                                System.out.println("value x "+ i + "y : " + k);
                                            }
                                        }
                                        canvas2.drawPath(path, paint1);
                                        canvas.drawBitmap(temp2,0,0,null);

                                    /*
                                    int width   = linearLayout.getWidth();
                                    int height  = linearLayout.getHeight();
                                    //배경 이미지를 그린다.
                                    canvas.drawBitmap(bitmap2, 0, 0, null);

                                    canvas.save();
                                    // 가져올 부분만 사각형으로 가져온다.
                                    canvas.clipPath(path, Region.Op.DIFFERENCE);
                                    // 나머지 부분의 그림은 없앤다.
                                    canvas.clipRect(0, 0, width, height);
                                    canvas.drawColor(Color.BLACK, PorterDuff.Mode.DST);
                                    canvas.restore();
                                    */


                                        pathMeasure = new PathMeasure(path, false);
                                        float[] pos = new float[2];
                                        float[] tan = new float[2];
                                        float distance = 0;
                                        line_lenght2 = pathMeasure.getLength();
                                        float different_line = line_lenght2 - line_lenght;
                                        System.out.println("lenght : " + pathMeasure.getLength());
                                        int path_count = (int)(line_lenght / 20);
                                        int path_count2 = (int)different_line / path_count;
                                        System.out.println("lenght size : " + path_count2);
                                        System.out.println("different_line size : " + different_line);

                                        pointsList = new ArrayList<Point>();
                                        floatsList = new ArrayList<Float>();

                                        temppaint2 = new Paint();
                                        temppaint2.setColor(Color.RED);
                                        temppaint2.setStyle(Paint.Style.FILL);
                                        //temppaint2.setStrokeWidth(10F);
                                        temppaint2.setAntiAlias(true);

                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);

                                        for(int i = 0; i < 20; i++)
                                        {
                                            floatsList.add(distance);
                                            pathMeasure.getPosTan(distance, pos, tan);
                                            canvas2.drawCircle(pos[0],pos[1],10,temppaint2);
                                            canvas.drawBitmap(temp2,0,0,null);
                                            Point point = new Point(pos[0],pos[1]);
                                            pointsList.add(point);

                                            //System.out.println("pos x : " + pos[0] + " pos y : " + pos[1]);
                                            distance = distance+path_count;
                                        }



                                        for(int i = 0; i < (path_count2/2 + 1); i++)
                                        {
                                            floatsList.add(distance);
                                            pathMeasure.getPosTan(distance, pos, tan);
                                            canvas2.drawCircle(pos[0],pos[1],10,temppaint2);
                                            canvas.drawBitmap(temp2,0,0,null);
                                            Point point = new Point(pos[0],pos[1]);
                                            pointsList.add(point);
                                            System.out.println("pos x : " + pos[0] + " pos y : " + pos[1]);
                                            distance = distance+path_count;
                                        }
                                        System.out.println("floatsList : " + floatsList.size());
                                        setCircle = true;
                                        mImageView.invalidate();

                                    }
                                }
                                else
                                {

                                }


                            /*
                            tempcanvas = Bitmap.createBitmap(bitmap2,0,0,linearLayout.getWidth(),linearLayout.getHeight());
                            temp2canvas = Bitmap.createBitmap(temp2,0,0,linearLayout.getWidth(),linearLayout.getHeight());
                            canvas = new Canvas(tempcanvas);
                            canvas2 = new Canvas(temp2canvas);
                            canvas2.drawPath(path,paint2);
                            canvas2.drawCircle((float)pointsList.get(1).x,(float)pointsList.get(1).y,1,paint2);
                            canvas.drawBitmap(temp2canvas,0,0,null);
                            mImageView.setImageBitmap(tempcanvas);
                            */

                                break;
                            case MotionEvent.ACTION_MOVE:
                                upx = event.getX();
                                upy = event.getY();

                                //줌 인 아웃이 아닐때, 손가락 드레그 선 그리기
                                if((upx>=4 || upy>=4) && mode.equalsIgnoreCase("none"))
                                {
                                    path.lineTo(dx, dy);
                                    arrayList.add(new Point(dx,dy));
                                    canvas2.drawPath(path, paint1);
                                    canvas.drawBitmap(temp2,0,0,null);
                                    mImageView.setImageBitmap(temp);
                                }

                                break;
                        }
                    }
                    else
                    {
                        Point selectPoint;
                        switch (action)
                        {

                            case MotionEvent.ACTION_DOWN:
                                System.out.println("x : " + dx);
                                System.out.println("y : " + dy);
                                double distance = Math.sqrt((dx*dx) + (dy*dy));

                                for(int i = 0; i < pointsList.size(); i++)
                                {
                                    double x = dx - pointsList.get(i).x;
                                    double y = dy - pointsList.get(i).y;
                                    double dis = Math.sqrt((x*x)+(y*y));
                                    if(dis < 10)
                                    {
                                        tempcanvas = Bitmap.createBitmap(bitmap2,0,0,linearLayout.getWidth(),linearLayout.getHeight());
                                        temp2canvas = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
                                        canvas = new Canvas(tempcanvas);
                                        canvas2 = new Canvas(temp2canvas);

                                        for(int j =0; j < pathMeasure.getLength(); j++)
                                        {
                                            float[] pos = new float[2];
                                            float[] tan = new float[2];
                                            pathMeasure.getPosTan(j, pos, tan);
                                            canvas2.drawCircle(pos[0],pos[1],0.3f,paint1);
                                        }

                                        for(int j = 0; j < pointsList.size(); j++)
                                        {
                                            canvas2.drawCircle((float)pointsList.get(j).x,(float)pointsList.get(j).y,5 ,temppaint2);
                                        }

                                        int pre,next;
                                        if(i == 0)
                                        {
                                            pre = pointsList.size()-1;
                                            next = i+1;
                                            System.out.println("that : " + i);
                                        }
                                        else if(i == pointsList.size()-1)
                                        {
                                            pre = i-1;
                                            next = 0;
                                            System.out.println("that : " + i);
                                        }
                                        else
                                        {
                                            pre = i - 1;
                                            next = i + 1;
                                        }

                                        for(float j = floatsList.get(pre); j < floatsList.get(i); j++)
                                        {
                                            float[] pos = new float[2];
                                            float[] tan = new float[2];
                                            pathMeasure.getPosTan(j, pos, tan);
                                            canvas2.drawCircle(pos[0],pos[1],3,paint2);
                                        }
                                        for(float j = floatsList.get(i); j < floatsList.get(next); j++)
                                        {
                                            float[] pos = new float[2];
                                            float[] tan = new float[2];
                                            pathMeasure.getPosTan(j, pos, tan);
                                            canvas2.drawCircle(pos[0],pos[1],3,paint2);
                                        }

                                        canvas2.drawCircle((float)pointsList.get(pre).x,(float)pointsList.get(pre).y,5 ,temppaint2);
                                        canvas2.drawCircle((float)pointsList.get(next).x,(float)pointsList.get(next).y,5 ,temppaint2);


                                        System.out.println("that : " + pre + "," + next);
                                        System.out.println("that : " +pointsList.get(pre).x + "," + pointsList.get(pre).y );
                                        System.out.println("that : " +pointsList.get(next).x + "," + pointsList.get(next).y );
                                        

                                        double m =  ((pointsList.get(pre).y - pointsList.get(i).y) / (pointsList.get(pre).x - pointsList.get(i).x));
                                        int bb = (int)(arrayList.get(0).y - (m * arrayList.get(0).x));
                                        int coco = (int)Math.abs(arrayList.get(0).x) - (int)Math.abs(arrayList.get(arrayList.size()-1).x);
                                        canvas.drawBitmap(temp2canvas,0,0,null);
                                        mImageView.setImageBitmap(tempcanvas);
                                        break;
                                    }
                                }




                                //System.out.println("distance : " + distance);
                                /*
                                for(int i = 0; i < pointsList.size(); i++)
                                {
                                    double x = dx - pointsList.get(i).x;
                                    double y = dy - pointsList.get(i).y;
                                    double dis = Math.sqrt((x*x)+(y*y));
                                    if(dis < 10)
                                    {
                                        selectPoint = new Point(pointsList.get(i).x,pointsList.get(i).y);
                                        System.out.println("dis : " + dis);
                                        System.out.println("dis count : "+ pathMeasure.getLength());
                                        tempcanvas = Bitmap.createBitmap(bitmap2,0,0,linearLayout.getWidth(),linearLayout.getHeight());
                                        temp2canvas = Bitmap.createBitmap(temp2,0,0,linearLayout.getWidth(),linearLayout.getHeight());
                                        canvas = new Canvas(tempcanvas);
                                        canvas2 = new Canvas(temp2canvas);
                                        canvas2.drawCircle((float)pointsList.get(i).x,(float)pointsList.get(i).y,10,paint2);
                                        canvas2.drawCircle((float)pointsList.get(i).x,(float)pointsList.get(i).y,10,paint2);
                                        canvas.drawBitmap(temp2canvas,0,0,null);
                                        mImageView.setImageBitmap(tempcanvas);
                                        break;
                                    }
                                }
                                */


                        }
                    }

                }
                return true;
            }
        });





        //mProgress = ProgressDialog.show(SuccessActivity.this, null, "Loading", true);
        //mProgress.setCancelable(false);

        /*

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!mFittingDone) {
                    try {
                        i += 4;
                        int j = i % 200;
                        if(j >= 100) j = 200 - j;
                        mHandler.obtainMessage(MSG_PROGRESS, j, 0).sendToTarget();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Mat image = Highgui.imread(mImageFileName, Highgui.IMREAD_COLOR);
                Mat shapes = new Mat();
                mHandler.obtainMessage(MSG_STATUS, 0, 0).sendToTarget();
                boolean flag = ASMFit.detectAll(image, shapes);

                if(flag == false){
                    mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
                    return;
                }
                int count = 0;
                Point[] pp = new Point[30];
                mHandler.obtainMessage(MSG_STATUS, 1, 0).sendToTarget();
                ASMFit.fitting(image, shapes, 15);
                for(int i = 0; i < shapes.rows(); i++){
                    for(int j = 0; j < (shapes.row(i).cols()/2)/2; j++){
                        double x = shapes.get(i, 2*j)[0];
                        double y = shapes.get(i, 2*j+1)[0];
                        Point pt = new Point(x, y);
                        if(j < 15)
                        {
                            Core.circle(image, pt, 3, mCyanColor, 2);
                            pp[count] = pt;
                            if(count >= 1)
                            {
                                Core.line(image,pp[count],pp[count-1],new Scalar(0,0,0));
                            }
                            count++;
                        }
                        else
                        {
                            Core.circle(image, pt, 3, mRedColor, 2);
                        }
                    }
                }
                Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2BGR);
                if(mScaleFactor == 1)
                    Utils.matToBitmap(image, mBitmap);
                else
                {
                    Mat image2 = new Mat(mBitmap.getHeight(), mBitmap.getWidth(), image.type());
                    Imgproc.resize(image, image2, image2.size());
                    Utils.matToBitmap(image2, mBitmap);
                    image2.release();
                }
                image.release();
                shapes.release();
                mHandler.obtainMessage(MSG_SUCCESS, mBitmap).sendToTarget();
            }
        }).start();

        */

    }

}