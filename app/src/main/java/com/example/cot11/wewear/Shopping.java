package com.example.cot11.wewear;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.kakao.usermgmt.response.model.User;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;



public class Shopping extends Fragment {

    private RecyclerView mRecyclerView;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private String[] dataSet;
    private View v;

    public Shopping() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Storage 이미지 다운로드 경로
        mStorage = FirebaseStorage.getInstance().getReference();


        mDatabase.child("Clothes").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        dataSet = new String[(int)dataSnapshot.getChildrenCount()];
                        int i = 0;
                        for(DataSnapshot post : dataSnapshot.getChildren() ){
                            String[] name = post.getKey().split("!");
                            dataSet[i] = name[1];
                            System.out.println(dataSet[i]);
                            // firebase Storage 읽기

                            /*
                            try
                            {
                                String folderName = "소녀나라";
                                String imageName = String.format(name[1]+".jpg");
                                String storagePath = folderName + "/" + imageName;
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
                                        Toast.makeText(getActivity(), "Failed !!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            i++;
                            */
                        }

                        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
                        PhotoAdapter adapter = new PhotoAdapter(dataSet, getActivity());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });
                        mRecyclerView.setAdapter(adapter);


                        // ...
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("DDD", "getUser:onCancelled", databaseError.toException());
                    }
                });


        /*
        try {
            dataSet = getActivity().getAssets().list("demo-pictures");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        v =  inflater.inflate(R.layout.shopping_itemlist, container, false);
        ButterKnife.bind(v);

        //ListView listview = (ListView) v.findViewById(R.id.shopping_list);
        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;

    }
}
