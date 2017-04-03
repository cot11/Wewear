package com.example.cot11.wewear;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;


/**
 * Created by cot11 on 2017-03-22.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>
{

    private Context mContext;
    private ArrayList<Brandlist> Brandlist1;


    public BrandAdapter(ArrayList<Brandlist> brandlist1, Context context)
    {
        Brandlist1 = brandlist1;
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
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(myViewHolder.imageView1);
        Glide.with(mContext).load(Brandlist1.get(position).getRogo()).thumbnail(0.1f).into(imageViewTarget);


        myViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).brandSet(Brandlist1.get(position).getName(), true);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return Brandlist1.size();

    }

    protected static class BrandViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1;

        public BrandViewHolder(View itemView) {
            super(itemView);
            imageView1 = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}




