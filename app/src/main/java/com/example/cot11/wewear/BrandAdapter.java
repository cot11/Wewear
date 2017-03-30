package com.example.cot11.wewear;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;


/**
 * Created by cot11 on 2017-03-22.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>
{
    private String[] mDataSet1;
    private String[] mLinkSet1;
    private String[] mDataSet2;
    private String[] mLinkSet2;
    private Context mContext;

    private FirebaseStorage mStorage;
    private StorageReference storageRef;
    private Uri url;
    private  File localFile;


    public BrandAdapter(String[] linkSet, String[] dataSet,String[] linkSet2, String[] dataSet2, Context context)
    {
        mLinkSet1 = linkSet;
        mDataSet1 = dataSet;
        mLinkSet2 = linkSet2;
        mDataSet2 = dataSet2;
        mContext = context;

    }
    @Override
    public BrandAdapter.BrandViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brand_itemlist, viewGroup, false);
        return new BrandViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final BrandAdapter.BrandViewHolder myViewHolder, final int position)
    {
        mStorage= FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://wewear-db78b.appspot.com/");

        storageRef.child("Logo/" + mDataSet1[position]+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).thumbnail(0.1f).into(myViewHolder.imageView1);

            }
        });

        if(position < mDataSet2.length)
        {
            storageRef.child("Logo/" + mDataSet2[position]+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).thumbnail(0.1f).into(myViewHolder.imageView2);
                    //   ((AvartaMain) mContext).ProgressStop();
                }
            });
        }

        myViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).brandSet(mDataSet1[position], true);
            }
        });

        myViewHolder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < mDataSet2.length)
                {
                    ((AvartaMain) mContext).brandSet(mDataSet2[position], true);
                }
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return mDataSet1.length;

    }

    protected static class BrandViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1;
        ImageView imageView2;

        public BrandViewHolder(View itemView) {
            super(itemView);
            imageView1 = (ImageView)itemView.findViewById(R.id.image);
            imageView2 = (ImageView)itemView.findViewById(R.id.image2);
        }
    }
}




