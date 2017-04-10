package com.example.cot11.wewear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SquaringDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class putAdapter extends RecyclerView.Adapter<putAdapter.ViewHolder> {

    private ArrayList<String> mDataSet;
    private ArrayList<Bitmap> mBitSet;
    private Context mContext;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String BrandName;
    private int Mode = 0;

    public putAdapter(Context context, String brandName) {
        mDataSet = new ArrayList<>();
        mContext = context;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://wewear-db78b.appspot.com");
        BrandName = brandName;
        Mode = 0;
    }
    public putAdapter(Context context, String brandName, ArrayList StringArray, ArrayList BitmapArray) {
        mDataSet = StringArray;
        mBitSet = BitmapArray;
        mContext = context;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://wewear-db78b.appspot.com");
        BrandName = brandName;
        Mode = 1;
    }


    public ArrayList Add(String item)
    {
        for(int i = 0; i < mDataSet.size(); i++)
        {
            if(mDataSet.get(i).equals(item))
            {
                System.out.println("이미 추가된 상품입니다.");
                return mDataSet;
            }
        }
        mDataSet.add(item);
        notifyDataSetChanged();
        return mDataSet;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int Width = (((AvartaMain) mContext).getWidth() * 2) / 3;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.putview, parent, false);
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.rootLinear);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Width,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        linearLayout.setLayoutParams(params);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(Mode == 1)
        {
            holder.imageView.setImageBitmap(mBitSet.get(position));
            holder.textView.setText(mDataSet.get(position));
            if(position == mDataSet.size()-1)
            {
                Mode = 0;
            }
        }
        else
        {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(BrandName+"/"+"이미지/" + mDataSet.get(position) + ".png");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            holder.imageView.setImageBitmap(resource);
                            holder.textView.setText(mDataSet.get(position));
                            ((AvartaMain) mContext).setPutBitmap(resource);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.imageaa);
            textView = (TextView)v.findViewById(R.id.producttext);
        }
    }
}
