package com.jvr.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class docsRecyclerAdapter extends RecyclerView.Adapter<docsRecyclerAdapter.ViewHolder>{
    public ArrayList<DocsProfile> docsProfiles;
    public Context context;

    public docsRecyclerAdapter(ArrayList<DocsProfile> docsProfiles, Context context) {
        this.docsProfiles = docsProfiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new docsRecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.docscard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvtittle.setText(docsProfiles.get(position).getTitle());
        holder.url = docsProfiles.get(position).getUrl();
        holder.onClick(context);
    }

    @Override
    public int getItemCount() {
        return docsProfiles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        CardView btndocsview;
        TextView tvtittle;
        String url;

        ViewHolder(View view){
            super(view);
            tvtittle = view.findViewById(R.id.tvfilename);
            btndocsview = view.findViewById(R.id.btndocsview);
        }
        public void onClick(final Context context)
        {
            btndocsview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, webview.class);
                    intent.putExtra("url",url);
                    ContextCompat.startActivity(context,intent, Bundle.EMPTY);
                }
            });
        }
    }
}