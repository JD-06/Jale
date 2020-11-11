package com.jvr.im;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileVisitorView extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private TextView tvname, tvage, tvoccupation, tvubi, tvtype;
    private ImageView ivprofperfil, profmenu;
    private RecyclerView rvprof, rvsocial;
    private LinearLayout lnbtnprofile;
    private pictureRecyclerAdapter adapter;
    private socialRecyclerAdapter socialRecyclerAdapter;
    private ArrayList<PictureProfile> pictures;
    private ArrayList<SocialProfile> socialProfiles;
    private String id, namevisit;
    private  PowerMenu powerMenu;

    LinearLayoutManager HorizontalLayout;
    private String name,age,ubi,occupation,type,question = "",description= "",trajectory= "",company = "",picturelink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_visitor_view);
        Intent intent = getIntent();
        id  = intent.getStringExtra("id");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        lnbtnprofile = findViewById(R.id.lnbtnprofile);
        tvname = findViewById(R.id.profname);
        tvage = findViewById(R.id.profage);
        tvoccupation = findViewById(R.id.profileprofesion);
        tvubi = findViewById(R.id.profubi);
        tvtype = findViewById(R.id.proftype);
        ivprofperfil = findViewById(R.id.ivprofperfil);
        rvprof = findViewById(R.id.rvprofile);
        rvsocial = findViewById(R.id.rvsocialprofile);
        profmenu = findViewById(R.id.profmenu);
        HorizontalLayout
                = new LinearLayoutManager(
                ProfileVisitorView.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        rvsocial.setLayoutManager(HorizontalLayout);
        rvprof.setLayoutManager(new GridLayoutManager(ProfileVisitorView.this,3));

        powerMenu = new PowerMenu.Builder(ProfileVisitorView.this)
                .addItem(new PowerMenuItem("Recomendar",R.drawable.ic_baseline_add_24))
                .addItem(new PowerMenuItem("Seguir",R.drawable.ic_baseline_add_24))
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(ProfileVisitorView.this,R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("font/sansjale.otf", Typeface.NORMAL))
                .setMenuColor(Color.WHITE)
                .setIconColor(Color.BLACK)
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setSelectedEffect(false)
                .build();

        CardView btntrajectory = findViewById(R.id.btntrajectory);
        CardView cvprofile = findViewById(R.id.cvfragprofile);
        getDatos();
        getDatosVisit();
        profmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerMenu.showAsDropDown(v);
            }
        });
        cvprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        btntrajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileVisitorView.this, trajectory.class);
                intent.putExtra("id",id);
                intent.putExtra("trajectory",trajectory);
                intent.putExtra("describe",description);
                startActivity(intent);
            }
        });

    }
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            switch (position){
                case 0:
                    showAlertDialogDocs();
                    break;
                case 1:
                    /*
                    intent = new Intent(getApplicationContext(), accountmanager.class);
                    startActivity(intent);

                     */
                    break;


            }
            powerMenu.dismiss();
        }
    };

    private void getDatos(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name = snapshot.child("name").getValue().toString();
                    age = snapshot.child("age").getValue().toString();
                    occupation = snapshot.child("occupation").getValue().toString();
                    ubi = snapshot.child("ubication").getValue().toString();
                    type = snapshot.child("mode").getValue().toString();
                    picturelink = snapshot.child("picture").getValue().toString();
                    tvname.setText(name);
                    tvage.setText(age);
                    tvubi.setText(ubi);
                    tvtype.setText(type);
                    Picasso.get().load(picturelink).into(ivprofperfil);
                    switch (type){
                        case "Talento":
                            tvoccupation.setText(occupation);
                            description = snapshot.child("question").getValue().toString();
                            trajectory = snapshot.child("trajectory").getValue().toString();
                            lnbtnprofile.setVisibility(View.VISIBLE);
                            break;
                        case "Buscador":
                            trajectory = snapshot.child("trajectory").getValue().toString();
                            description = snapshot.child("describe").getValue().toString();
                            tvoccupation.setText(occupation+", "+snapshot.child("company").getValue().toString());
                            lnbtnprofile.setVisibility(View.VISIBLE);
                            break;
                        case "Espectador":
                            tvoccupation.setText(occupation);
                            lnbtnprofile.setVisibility(View.INVISIBLE);
                            break;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReferencerv = firebaseDatabase.getReference("Users").child(id+"/publications");
        databaseReferencerv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pictures = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    PictureProfile pictureProfile = dataSnapshot1.getValue(PictureProfile.class);
                    pictures.add(pictureProfile);
                }
                adapter = new pictureRecyclerAdapter(pictures,getApplicationContext());
                rvprof.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReferencesocial = firebaseDatabase.getReference("Users").child(id+"/social");
        databaseReferencesocial.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                socialProfiles = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    SocialProfile socialProfile = dataSnapshot1.getValue(SocialProfile.class);
                    socialProfiles.add(socialProfile);
                }
                socialRecyclerAdapter = new socialRecyclerAdapter(socialProfiles,getApplicationContext());
                rvsocial.setAdapter(socialRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showAlertDialogDocs() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileVisitorView.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.recommendation, null);
        builder.setView(customLayout);
        final EditText etrecom = customLayout.findViewById(R.id.etrecommendation);
        CardView btnok = customLayout.findViewById(R.id.btnokrecom);

        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReferenceupload = firebaseDatabase.getReference("Users").child(id+"/recommendations").child(auth.getCurrentUser().getUid());
                Map<String,Object> user = new HashMap<>();
                user.put("recomm",etrecom.getText().toString());
                user.put("name",namevisit);
                databaseReferenceupload.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileVisitorView.this, "Exito", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void getDatosVisit() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    namevisit = snapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}