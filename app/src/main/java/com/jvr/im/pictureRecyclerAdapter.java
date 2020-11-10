package com.jvr.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class pictureRecyclerAdapter extends RecyclerView.Adapter<pictureRecyclerAdapter.ViewHolder>{
    public ArrayList<PictureProfile> pictures;
    public Context context;

    public pictureRecyclerAdapter(ArrayList<PictureProfile> pictures, Context context) {
        this.pictures = pictures;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.profilepicturescard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(pictures.get(position).getPicturelink()).into(holder.picture);
        holder.title = pictures.get(position).getTitle();
        holder.name = pictures.get(position).getName();
        holder.id = pictures.get(position).getId();
        holder.date = pictures.get(position).getDate();
        holder.likes = pictures.get(position).getLikes();
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView picture;
        String name,title,date,id;
        int likes;
        ViewHolder(View view){
            super(view);
            picture = view.findViewById(R.id.ivpicturecard);
        }
    }
}
