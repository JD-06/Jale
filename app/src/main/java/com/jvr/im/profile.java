package com.jvr.im;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class profile extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private TextView tvname, tvage, tvoccupation, tvubi, tvtype;
    private ImageView ivprofperfil, profmenu;
    private RecyclerView rvprof;
    private pictureRecyclerAdapter adapter;
    private ArrayList<PictureProfile> pictures;
    private  PowerMenu powerMenu;
    private String name,age,ubi,occupation,type,question = "",description= "",trajectory= "",company = "",picturelink;
    public profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tvname = view.findViewById(R.id.profname);
        tvage = view.findViewById(R.id.profage);
        tvoccupation = view.findViewById(R.id.profileprofesion);
        tvubi = view.findViewById(R.id.profubi);
        tvtype = view.findViewById(R.id.proftype);
        ivprofperfil = view.findViewById(R.id.ivprofperfil);
        rvprof = view.findViewById(R.id.rvprofile);
        profmenu = view.findViewById(R.id.profmenu);
        rvprof.setLayoutManager(new GridLayoutManager(getContext(),3));

        powerMenu = new PowerMenu.Builder(getContext())
                .addItem(new PowerMenuItem(getString(R.string.strconfig)))
                .addItem(new PowerMenuItem(getString(R.string.strabout)))
                .addItem(new PowerMenuItem(getString(R.string.strexit)))
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getContext(),R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("font/sansjale.otf", Typeface.NORMAL))
                .setMenuColor(Color.WHITE)
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setSelectedEffect(false)
                .build();

        CardView cvprofile = view.findViewById(R.id.cvfragprofile);
        getDatos();
        cvprofile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        profmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                powerMenu.showAsDropDown(v);

            }
        });
        return view;
    }

    private void getDatos(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid());
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
                            question = snapshot.child("question").getValue().toString();
                            trajectory = snapshot.child("trajectory").getValue().toString();
                            break;
                        case "Buscador":
                            trajectory = snapshot.child("trajectory").getValue().toString();
                            description = snapshot.child("describe").getValue().toString();
                            tvoccupation.setText(occupation+", "+snapshot.child("company").getValue().toString());
                            break;
                        case "Espectador":
                            tvoccupation.setText(occupation);
                            break;

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReferencerv = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()+"/publications");
        databaseReferencerv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pictures = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    PictureProfile pictureProfile = dataSnapshot1.getValue(PictureProfile.class);
                    pictures.add(pictureProfile);
                }
                adapter = new pictureRecyclerAdapter(pictures,getContext());
                rvprof.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            Intent intent;
            switch (position){
                case 0:
                    intent = new Intent(getContext(), accountmanager.class);
                    if(!type.equals("")){
                        intent.putExtra("name",name);
                        intent.putExtra("age",age);
                        intent.putExtra("ubi",ubi);
                        intent.putExtra("type",type);
                        intent.putExtra("occupation",occupation);
                        intent.putExtra("trajectory",trajectory);
                        intent.putExtra("question",question);
                        intent.putExtra("describe",description);
                        intent.putExtra("copany",company);
                        intent.putExtra("picture",picturelink);
                        startActivity(intent);
                    }
                    break;
                case 1:
                    /*
                    intent = new Intent(getApplicationContext(), accountmanager.class);
                    startActivity(intent);

                     */
                    break;
                case 2:
                    auth.signOut();
                    startActivity(new Intent(getContext(),loginscreen.class));
                    getActivity().finish();
            }
            powerMenu.dismiss();
        }
    };
}