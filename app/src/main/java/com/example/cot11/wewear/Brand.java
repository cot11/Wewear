package com.example.cot11.wewear;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by 이언우 on 2017-03-20.
 */

public class Brand extends Fragment {

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    public Brand() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Storage 이미지 다운로드 경로

        mDatabase = FirebaseDatabase.getInstance().getReference();
        View v =  inflater.inflate(R.layout.brand, container, false);

        mDatabase.child("Brand").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        for(DataSnapshot post : dataSnapshot.getChildren() ){
                            String key = post.getKey();
                            try
                            {
                                String imageName = String.format(key +".jpg");
                                String storagePath = "Logo/"  + imageName;
                                mStorage = FirebaseStorage.getInstance().getReference();
                                StorageReference imageRef = mStorage.child(storagePath);
                                final File imageFile = File.createTempFile("images", "jpeg");
                                imageRef.getFile(imageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                       System.out.println("성공");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Failed !!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                        }

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("DDD", "getUser:onCancelled", databaseError.toException());
                    }
                });

        // firebase Storage 읽기


        return v;
    }
}
