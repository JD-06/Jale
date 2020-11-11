package com.jvr.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recomRecyclerAdapter extends RecyclerView.Adapter<recomRecyclerAdapter.ViewHolder>{
    public ArrayList<RecomProfile> recomProfiles;
    public Context context;

    public recomRecyclerAdapter(ArrayList<RecomProfile> recomProfiles, Context context) {
        this.recomProfiles = recomProfiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new recomRecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recomcard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvrecom.setText(recomProfiles.get(position).getRecomm());
        holder.tvname.setText(recomProfiles.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recomProfiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvname, tvrecom;

        ViewHolder(View view){
            super(view);
            tvrecom = view.findViewById(R.id.tvrecom);
            tvname = view.findViewById(R.id.tvnamerecom);
        }
    }
}
