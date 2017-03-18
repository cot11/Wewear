package com.example.cot11.wewear;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;



public class Shopping extends Fragment {

    private RecyclerView mRecyclerView;
    private StorageReference mStorage;

    public Shopping() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String folderName = "Logo";
        String imageName = String.format("face.jpg");

        // Storage 이미지 다운로드 경로
        String storagePath = folderName + "/" + imageName;
        mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = mStorage.child(storagePath);

        // firebase Storage 읽기
        try
        {
            final File imageFile = File.createTempFile("images", "jpeg");
            imageRef.getFile(imageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("성공");
                    Toast.makeText(getActivity(), "Success !!", Toast.LENGTH_LONG).show();
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


        View v =  inflater.inflate(R.layout.shopping_itemlist, container, false);
        ButterKnife.bind(v);
        String[] dataSet = null;
        try {
            dataSet = getActivity().getAssets().list("demo-pictures");
        } catch (IOException e) {
            e.printStackTrace();
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

        //ListView listview = (ListView) v.findViewById(R.id.shopping_list);
        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;

    }
}
