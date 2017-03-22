package com.example.cot11.wewear;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cot11 on 2017-03-22.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>
{
    private String[] mDataSet1;
    private String[] mLinkSet1;
    private String[] mDataSet2;
    private String[] mLinkSet2;
    private Context mContext;

    private FirebaseStorage mStorage;
    private StorageReference storageRef;
    private Uri url;


    public ProductAdapter(String[] linkSet, String[] dataSet,String[] linkSet2, String[] dataSet2, Context context)
    {
        mLinkSet1 = linkSet;
        mDataSet1 = dataSet;
        mLinkSet2 = linkSet2;
        mDataSet2 = dataSet2;
        mContext = context;

        System.out.println("data : " + mDataSet1[0]);
        System.out.println("data : " + mDataSet2[0]);

    }
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list, viewGroup, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ProductViewHolder myViewHolder, int position)
    {
        mStorage= FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://wewear-db78b.appspot.com/");
        storageRef.child("소녀나라/" + mDataSet1[position]+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(myViewHolder.imageView1);
                System.out.println(myViewHolder.imageView1.getHeight());
                System.out.println(myViewHolder.imageView1.getWidth());
            }
        });


        if(position < mDataSet2.length)
        {
            storageRef.child("소녀나라/" + mDataSet2[position]+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).into(myViewHolder.imageView2);
                }
            });
        }
    }


    @Override
    public int getItemCount()
    {
        return mDataSet1.length;
    }

    protected static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1;
        ImageView imageView2;
        Button save_1;
        Button size_1;
        Button like_1;
        Button save_2;
        Button size_2;
        Button like_2;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView1 = (ImageView)itemView.findViewById(R.id.image);
            imageView2 = (ImageView)itemView.findViewById(R.id.image2);
        }
    }
}




