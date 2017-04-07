package com.example.cot11.wewear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cot11 on 2017-04-03.
 */

public class putAdapter extends RecyclerView.Adapter<putAdapter.ViewHolder> {

    private ArrayList<String> mDataSet;
    private FirebaseStorage mStorage;
    private StorageReference storageRef;
    private String Brandname;
    private Context mContext;

    public putAdapter(String brandname, ArrayList<String> k, Context context) {
        Brandname = brandname;
        mDataSet = k;
        System.out.println("k size : " + k.size());
        mContext = context;
    }

    public void putAdapterAdd(String ClothesName)
    {
        System.out.println("name : " + ClothesName);
        mDataSet.add(ClothesName);
        System.out.println("size : " + mDataSet.size());
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        System.out.println("size change");
        return mDataSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        System.out.println("과연");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.putview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        mStorage= FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://wewear-db78b.appspot.com/");
        String K = Brandname+"/"+"이미지"+"/"+mDataSet.get(position)+".png";
        System.out.println(K);
        storageRef.child(K).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("hi ru:");
                Glide.with(mContext).load(uri).into(holder.imageView);
            }
        });

        /*
        String storagePath = Brandname + "/" + "이미지" + "/" + mDataSet.get(position);
        StorageReference imageRef = mStorageRef.child(storagePath);
        try {
            // Storage 에서 다운받아 저장시킬 임시파일
            final File imageFile = File.createTempFile("images", "jpg");
            imageRef.getFile(imageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Success Case
                    Bitmap bitmapImage = BitmapFactory.decodeFile(imageFile.getPath());
                    holder.imageView.setImageBitmap(bitmapImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail Case
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.imageaa);
        }
    }
}
