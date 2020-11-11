package com.jvr.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class socialRecyclerAdapter extends RecyclerView.Adapter<socialRecyclerAdapter.ViewHolder>{
    public ArrayList<SocialProfile> socialProfiles;
    public Context context;

    public socialRecyclerAdapter(ArrayList<SocialProfile> socialProfiles, Context context) {
        this.socialProfiles = socialProfiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new socialRecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.socialcard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.url = socialProfiles.get(position).getUrl();
        holder.social = socialProfiles.get(position).getSocial();
        holder.setImageinCard(context);
        holder.onClick(context);
    }

    @Override
    public int getItemCount() {
        return socialProfiles.size();
    }
    //

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView picture;
        CardView btnsocialweb;
        String url, social;
        ViewHolder(View view){
            super(view);
            picture = view.findViewById(R.id.socialpicture);
            btnsocialweb = view.findViewById(R.id.btnsocialweb);
        }
        public void setImageinCard(Context context){
            switch (social){
                case "wpp":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__07_whatsapp));
                    break;
                case "yt":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__02_youtube));
                    break;
                case "git":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__39_github));
                    break;
                case "tw":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__14_twitter));
                    break;
                case "tblr":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__16_tumblr));
                    break;
                case "sc":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__20_soundcloud));
                    break;
                case "pin":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__26_pinterest));
                    break;
                case "in":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__31_linkedin));
                    break;
                case "ig":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__34_instagram));
                    break;
                case "fb":
                    picture.setImageDrawable(context.getDrawable(R.drawable.ic__45_facebook));
                    break;
            }
        }
        public void onClick(final Context context) {
            btnsocialweb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uriUrl = Uri.parse(url);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    ContextCompat.startActivity(context, launchBrowser, Bundle.EMPTY);
                }
            });
        }
    }
}
