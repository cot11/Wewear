package com.example.cot11.wewear;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by cot11 on 2017-03-22.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>
{
    private Context mContext;
    private String BrandName;
    private ArrayList<productList> productAdapter1;
    private FirebaseStorage mStorage;
    private StorageReference storageRef;

    private int Height_image = 0;
    private int Width_image = 0;

    public void Clear(ArrayList<productList> arrayList)
    {
        productAdapter1 = arrayList;
        notifyDataSetChanged();
    }


    public ProductAdapter(String brandName, ArrayList<productList> adapter1, Context context)
    {
        BrandName = brandName;
        productAdapter1 = adapter1;
        mContext = context;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list, viewGroup, false);
        Height_image = ((AvartaMain) mContext).getHeight();
        Width_image = ((AvartaMain) mContext).getWidth();
        CardView cardView = (CardView)v.findViewById(R.id.cardview);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Height_image,
                1
        );
        params.setMargins(6,6,6,6);
        cardView.setLayoutParams(params);

        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ProductViewHolder myViewHolder, final int position)
    {
        System.out.println("HI");
        mStorage= FirebaseStorage.getInstance();
        Glide.with(mContext).load(productAdapter1.get(position).getImg()).override(Width_image,Height_image).into(myViewHolder.imageView1);

        myViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).openWeb(productAdapter1.get(position).getLink(),BrandName);

            }
        });

        myViewHolder.try_on1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).putAdditem(productAdapter1.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return productAdapter1.size();
    }

    protected static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1;
        Button try_on1;
        Button like_1;

        public ProductViewHolder(View itemView) {
            super(itemView);
            try_on1 = (Button)itemView.findViewById(R.id.try_on);
            imageView1 = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}




