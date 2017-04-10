package com.example.cot11.wewear;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AvartPutAdapter extends RecyclerView.Adapter<AvartPutAdapter.ViewHolder> {

    private ArrayList<Bitmap> mBitSet;
    private ArrayList<String> mDataSet;
    private Context mContext;


    public AvartPutAdapter(Context context, ArrayList arrayList, ArrayList arrayList2) {
        mBitSet = arrayList;
        mDataSet = arrayList2;
        mContext = context;
        System.out.println("HIHI : " + mBitSet.size());
    }


    @Override
    public int getItemCount() {
        return mBitSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int Width = (((AvartaMain) mContext).getWidth() * 2) / 3;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vputview, parent, false);
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.rootLinear1);
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
        holder.imageView.setImageBitmap(mBitSet.get(position));
        holder.textView.setText(mDataSet.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.imageaa1);
            textView = (TextView)v.findViewById(R.id.producttext1);
        }
    }
}
