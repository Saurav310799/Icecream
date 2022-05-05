package com.example.icecreambp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class IceAdapter extends RecyclerView.Adapter<IceAdapter.ViewHolder> {

    Activity activity;
    ArrayList<IceData> iceDataArrayList;

    public IceAdapter(Activity activity, ArrayList<IceData> iceDataArrayList) {
        this.activity = activity;
        this.iceDataArrayList = iceDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listofic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IceData iceData = iceDataArrayList.get(position);
        holder.flavour.setText(iceData.getIcecreamflavour());
        holder.price.setText(iceData.getPrice());
        Glide.with(activity).load(iceData.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageice);
    }

    @Override
    public int getItemCount() {
        return iceDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView flavour, price;
        ImageView imageice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flavour = itemView.findViewById(R.id.icecreamflavour);
            price = itemView.findViewById(R.id.price);
            imageice = itemView.findViewById(R.id.image);
        }
    }
}