package com.example.cot11.wewear;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.cot11.wewear.ASMFit;

import javax.net.ssl.HttpsURLConnection;

public class SuccessActivity extends AppCompatActivity {

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
    private DB_Manager db_manager;
    private MatrixImageView mImageView;
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
    private int mScaleFactor = 1;

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

        if(firstviewshow != 1)
        {
            Intent intent = new Intent(SuccessActivity.this, FirstActivity.class);
            startActivity(intent);
        }

        //init
         // class
        db_manager = new DB_Manager();

        // Activity
        unlick = (Button)findViewById(R.id.unlink);
        add = (Button)findViewById(R.id.add);
        logout = (Button)findViewById(R.id.logout);
        mGallaryButton = (Button)findViewById(R.id.gallaryButton);
        mApp = (GlobalApplication)getApplication();
        mImageView = (MatrixImageView)findViewById(R.id.image_view);


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
                requestMe();
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
        db_manager.Userinfo("이언우","email");

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

        mImageView.setImageBitmap(mBitmap);
        //mProgress = ProgressDialog.show(SuccessActivity.this, null, "Loading", true);
        //mProgress.setCancelable(false);

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

                mHandler.obtainMessage(MSG_STATUS, 1, 0).sendToTarget();
                ASMFit.fitting(image, shapes, 30);
                for(int i = 0; i < shapes.rows(); i++){
                    for(int j = 0; j < shapes.row(i).cols()/2; j++){
                        double x = shapes.get(i, 2*j)[0];
                        double y = shapes.get(i, 2*j+1)[0];
                        Point pt = new Point(x, y);

                        Core.circle(image, pt, 3, mCyanColor, 2);
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
    }
}