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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class personRecyclerAdapter extends RecyclerView.Adapter<personRecyclerAdapter.ViewHolder>{

    public ArrayList<PersonProfile> personProfiles;
    public Context context;

    public personRecyclerAdapter(ArrayList<PersonProfile> personProfiles, Context context) {
        this.personProfiles = personProfiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new personRecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.profilecard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id = personProfiles.get(position).getId();
        holder.tvname.setText(personProfiles.get(position).getName());
        Picasso.get().load(personProfiles.get(position).getPicture()).into(holder.imageView);
        holder.onClick(context);
    }

    @Override
    public int getItemCount() {
        return personProfiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView btnperson;
        ImageView imageView;
        String id;
        TextView tvname;

        ViewHolder(View view){
            super(view);
            btnperson = view.findViewById(R.id.btnperfil);
            tvname = view.findViewById(R.id.tvnameperson);
            imageView = view.findViewById(R.id.ivperfilcard);
        }
        public void onClick(final Context context)
        {
            btnperson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileVisitorView.class);
                    intent.putExtra("id",id);
                    ContextCompat.startActivity(context,intent, Bundle.EMPTY);
                }
            });
        }
    }
}
