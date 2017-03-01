package com.example.cot11.wewear;

import android.app.Application;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.kakao.auth.KakaoSDK;

import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by cot11 on 2017-02-24.
 */

public class GlobalApplication extends Application {

    static{
        // Load native library after(!) OpenCV initialization
        System.loadLibrary("asmlibrary");
        System.loadLibrary("jni-asmlibrary");
    }

    private static final String TAG = "wewear";
    private File mCascadeFile;
    private File mFastCascadeFile;
    private File mModelFile;
    private File mAAMModelFile;
    public CascadeClassifier mJavaCascade;
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        if(OpenCVLoader.initDebug())
            initialize();
    }

    @Override
    public void onTerminate()
    {
        Log.i(TAG, "called onTerminate");
        super.onTerminate();
    }

    public static GlobalApplication getGlobalApplicationContext() {
        return obj;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }
    private void initialize()
    {
        Log.i(TAG, "Application " + this.getClass());

        mModelFile = getSourceFile(R.raw.my68_1d, "my68_1d.amf", "model");
        if(mModelFile != null)
            ASMFit.nativeReadModel(mModelFile.getAbsolutePath());

        mAAMModelFile = getSourceFile(R.raw.my68, "my68.aam", "model");
        if(mAAMModelFile != null)
            ASMFit.nativeReadAAMModel(mAAMModelFile.getAbsolutePath());

        mCascadeFile = getSourceFile(R.raw.haarcascade_frontalface_alt2,
                "haarcascade_frontalface_alt2.xml", "model");
        if(mCascadeFile != null)
        {
            ASMFit.nativeInitCascadeDetector(mCascadeFile.getAbsolutePath());
            mJavaCascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            if (mJavaCascade.empty())
                mJavaCascade = null;
        }

        mFastCascadeFile = getSourceFile(R.raw.lbpcascade_frontalface,
                "lbpcascade_frontalface.xml", "model");
        if(mFastCascadeFile != null)
            ASMFit.nativeInitFastCascadeDetector(mFastCascadeFile.getAbsolutePath());
    }

    private File getSourceFile(int id, String name, String folder)
    {
        File cascadeDir = getDir(folder, Context.MODE_PRIVATE);
        File file = new File(cascadeDir, name);
        boolean existed = true;
        FileInputStream fis = null;
        try {
            fis=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            existed = false;
        } finally{
            try{
                fis.close();
            }catch(Exception e){
            }
        }
        if(existed == true)
            return file;

        try
        {
            InputStream is = getResources().openRawResource(id);
            FileOutputStream os = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        }catch (IOException e)
        {
            e.printStackTrace();
            Log.e(TAG, "Failed to load file " + name + ". Exception thrown: " + e);
        }

        return file;
    }
}

