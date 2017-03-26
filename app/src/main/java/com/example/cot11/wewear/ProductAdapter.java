package com.example.cot11.wewear;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
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
    private ArrayList<productList> productAdapter2;

    private FirebaseStorage mStorage;
    private StorageReference storageRef;
    private Uri url;


    public ProductAdapter(String brandName, ArrayList<productList> adapter1, ArrayList<productList> adapter2, Context context)
    {
        BrandName = brandName;
        productAdapter1 = adapter1;
        productAdapter2 = adapter2;
        mContext = context;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list, viewGroup, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ProductViewHolder myViewHolder, final int position)
    {
        mStorage= FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://wewear-db78b.appspot.com/");
        storageRef.child(BrandName+"/" + productAdapter1.get(position).getName()+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(myViewHolder.imageView1);
            }
        });

        if(position < productAdapter2.size())
        {
            storageRef.child(BrandName+"/" + productAdapter2.get(position).getName()+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).into(myViewHolder.imageView2);
                    ((AvartaMain) mContext).ProgressStop();
                }
            });
        }

        myViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AvartaMain) mContext).openWeb(productAdapter1.get(position).getLink(),BrandName);

            }
        });
        myViewHolder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < productAdapter2.size())
                {
                    ((AvartaMain) mContext).openWeb(productAdapter2.get(position).getLink(),BrandName);
                }
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




