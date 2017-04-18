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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ArrayList<Bitmap> mDataSet_bitmap;
    private Context mContext;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String BrandName;
    private int lastPosition = -1;
    private Animation animation;
    private ArrayList<Thread> ThreadQueue;

    public putAdapter(Context context) {
        mDataSet = new ArrayList<>();
        mDataSet_bitmap = new ArrayList<>();
        ThreadQueue = new ArrayList<>();
        mContext = context;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://wewear-db78b.appspot.com");
        animation = AnimationUtils.loadAnimation(mContext, R.anim.wiggle_shake);
    }

    public void Add(String item, int split)
    {
        for(int i = 0; i < mDataSet.size(); i++)
        {
            if(mDataSet.get(i).equals(item))
            {
                System.out.println("sizecc : 이미 추가된 상품입니다.");
                return;
            }
        }

        PutThread putthread = new PutThread(item,split);
        putthread.start();
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

        holder.textView.setText(mDataSet.get(position));
        holder.imageView.setImageBitmap(mDataSet_bitmap.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).itemApply(position);
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.imageView.startAnimation(animation);
                holder.remove.setVisibility(View.VISIBLE);
                System.out.println("ee : " + mDataSet.get(position));
                return false;
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).removePutBitmap(position);
                mDataSet.remove(position);
                mDataSet_bitmap.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataSet.size());
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button remove;
        public ViewHolder(View v) {
            super(v);
            remove = (Button)v.findViewById(R.id.remove_button);
            imageView = (ImageView)v.findViewById(R.id.imageaa);
            textView = (TextView)v.findViewById(R.id.producttext);
            remove.setVisibility(View.INVISIBLE);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            lastPosition = position;
        }
    }

    class PutThread extends Thread
    {
        private String item;
        private int split;
        private int count;
        private ArrayList<Bitmap> temp_bitmap;
        public PutThread(String item, int split)
        {
            this.item = item;
            this.split = split;
        }
        @Override
        public void run() {
            super.run();
            final String[] split_string = item.split("_");
            temp_bitmap = new ArrayList<>();
            System.out.println("sizecc init size : " + temp_bitmap.size());
            for(int i = 1; i <= split; i++)
            {
                StorageReference storageRefk = FirebaseStorage.getInstance().getReference().child(split_string[0]+"/"+"이미지/" + split_string[1] + i + ".png");
                storageRefk.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mContext).load(uri).asBitmap().override(35,80).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                temp_bitmap.add(resource);
                                System.out.println("sizesize pic : " + resource.getWidth());
                                System.out.println("sizesize pic : " + resource.getHeight());
                                count++;
                                if(count == split)
                                {
                                    mDataSet_bitmap.add(((AvartaMain) mContext).setPutBitmap(temp_bitmap));
                                    mDataSet.add(item);
                                    temp_bitmap.clear();
                                    notifyDataSetChanged();
                                    System.out.println("sizecc : Done");
                                    System.out.println("sizecc done size : " + temp_bitmap.size());
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                        // Handle any errors
                    }
                });
            }
        }
    }
}
