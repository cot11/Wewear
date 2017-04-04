package com.example.cot11.wewear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cot11 on 2017-04-03.
 */

public class CodeApdater extends RecyclerView.Adapter<CodeApdater.ViewHolder> {

    private List<String> mDataSet;
    private Context mContext;
    private int currentitem_num = 1;
    private ArrayList<productList> productAdapter1;

    public CodeApdater(List<String> myDataset, Context context, ArrayList<productList> adapter1) {
        mDataSet = myDataset;
        mContext = context;
        productAdapter1 = adapter1;
        ((AvartaMain) mContext).setCurrent_Code(currentitem_num);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button v = (Button) LayoutInflater.from(parent.getContext()).inflate(R.layout.codeview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet.get(position).toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Button mTextView;
        public ViewHolder(Button v) {
            super(v);
            mTextView = v;
            mTextView.setOnClickListener(this);
            mTextView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            add(getAdapterPosition(), mDataSet.get(getAdapterPosition()));
            return true;
        }

        public void delete(int position) {
            try {
                //mDataSet.remove(position);
                //notifyItemRemoved(position);
            } catch(IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }

        public void add(int position, String infoData) {
            //mDataSet.add(position, infoData);
            //notifyItemInserted(position);
        }
    }
}
