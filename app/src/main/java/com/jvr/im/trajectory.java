package com.jvr.im;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class trajectory extends AppCompatActivity {

    private ArrayList<RecomProfile> recomProfiles;
    private recomRecyclerAdapter recyclerAdapter;
    private ArrayList<DocsProfile> docsProfiles;
    private docsRecyclerAdapter adapter;
    private TextView tvtrajectory, tvdescription;
    private RecyclerView trajectoryDocs, trajectoryRecomendations;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String trajectory = intent.getStringExtra("trajectory");
        String describe = intent.getStringExtra("describe");
        trajectoryRecomendations = findViewById(R.id.trajectoryRecomendations);
        trajectoryRecomendations.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        trajectoryDocs = findViewById(R.id.trajectoryDocs);
        trajectoryDocs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tvdescription = findViewById(R.id.descriptiontrajectory);
        tvtrajectory = findViewById(R.id.trajectoryperson);
        tvtrajectory.setText(trajectory);
        tvdescription.setText(describe);
        getDatosFirebase(id);
        getDatos2(id);
    }

    private void getDatosFirebase(String id){
        DatabaseReference databaseReferencerv = firebaseDatabase.getReference("Users").child(id+"/docs");
        databaseReferencerv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                docsProfiles = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    DocsProfile docsProfileadapter = dataSnapshot1.getValue(DocsProfile.class);
                    docsProfiles.add(docsProfileadapter);
                }
                adapter = new docsRecyclerAdapter(docsProfiles,getApplicationContext());
                trajectoryDocs.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDatos2(String id){
        DatabaseReference databaseReferencerv = firebaseDatabase.getReference("Users").child(id+"/recommendations");
        databaseReferencerv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recomProfiles = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    RecomProfile recomProfile = dataSnapshot1.getValue(RecomProfile.class);
                    recomProfiles.add(recomProfile);
                }
                recyclerAdapter = new recomRecyclerAdapter(recomProfiles,getApplicationContext());
                trajectoryRecomendations.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}