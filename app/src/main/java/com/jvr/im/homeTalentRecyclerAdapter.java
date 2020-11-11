package com.jvr.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class homeTalentRecyclerAdapter extends RecyclerView.Adapter<homeTalentRecyclerAdapter.ViewHolder>{
    public ArrayList<HomeTalentProfile> homeTalentProfiles;
    public Context context;


    public homeTalentRecyclerAdapter(ArrayList<HomeTalentProfile> homeTalentProfiles, Context context) {
        this.homeTalentProfiles = homeTalentProfiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new homeTalentRecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.homecard, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id = homeTalentProfiles.get(position).getId();
        holder.title = homeTalentProfiles.get(position).getTitle();
        holder.name = homeTalentProfiles.get(position).getName();
        holder.date = homeTalentProfiles.get(position).getDate();
        holder.postulados = homeTalentProfiles.get(position).getPostulantes();
        holder.tvtitulohome.setText(homeTalentProfiles.get(position).getTitle());
        holder.tvdescriptionhome.setText(homeTalentProfiles.get(position).getName());
        holder.onClick(context);
    }

    @Override
    public int getItemCount() {
        return homeTalentProfiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private DatabaseReference databaseReference;
        private FirebaseAuth auth;
        CardView btnpostularse;
        TextView tvtitulohome, tvdescriptionhome;
        String name,title,date,id;
        int postulados;
        ViewHolder(View view){
            super(view);
            auth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            tvtitulohome = view.findViewById(R.id.tvtitulohome);
            tvdescriptionhome = view.findViewById(R.id.tvdescriptionhome);
            btnpostularse = view.findViewById(R.id.btnpostularse);




        }
        public void onClick(final Context context)
        {
            Map<String,Object> user = new HashMap<>();
            user.put("id",id);
            user.put("title",title);
            btnpostularse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("postulaciones").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String,Object> dato = new HashMap<>();
                                dato.put("Postulantes",postulados+1);
                                dato.put("title",title);
                                dato.put("name",name);
                                dato.put("id",id);
                                dato.put("date",date);
                                Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                                databaseReference.child("Users").child("publications").child(id).updateChildren(dato);
                            }
                        }
                    });
                }
            });
        }
    }
}
