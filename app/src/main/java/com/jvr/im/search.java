package com.jvr.im;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.ArrayList;


public class search extends Fragment {
    private DatabaseReference ref;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private ArrayList<HomeTalentProfile> list;
    private ArrayList<PersonProfile> listperson;
    private ImageView menusearch;
    private PowerMenu powerMenu;
    public search() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.rv);

        searchView = view.findViewById(R.id.search_view);
        menusearch = view.findViewById(R.id.menusearch);
        powerMenu = new PowerMenu.Builder(getContext())
                .addItem(new PowerMenuItem(getString(R.string.strprofile)))
                .addItem(new PowerMenuItem("Jale"))
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getContext(),R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("font/sansjale.otf", Typeface.NORMAL))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColorResource(R.color.yellow_a200)
                .setSelectedTextColor(Color.BLACK)
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setSelectedEffect(true)
                .build();
        powerMenu.setSelectedPosition(0);

        menusearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                powerMenu.showAsDropDown(v);
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        perfiles();

    }
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            switch (position){
                case 0:
                    perfiles();
                    break;
                case 1:
                    publis();
                    break;

            }
            powerMenu.setSelectedPosition(position);
            powerMenu.dismiss();
        }
    };

    private void search(String sr) {
        ArrayList<HomeTalentProfile> mylist = new ArrayList<>();
        for(HomeTalentProfile object : list){
            if(object.getName().toLowerCase().contains(sr.toLowerCase())){
                mylist.add(object);
            }
        }
        homeTalentRecyclerAdapter adapter = new homeTalentRecyclerAdapter(mylist,getContext());
        recyclerView.setAdapter(adapter);
    }
    private void searchpeson(String sr) {
        ArrayList<PersonProfile> mylist = new ArrayList<>();
        for(PersonProfile object : listperson){
            if(object.getName().toLowerCase().contains(sr.toLowerCase())){
                mylist.add(object);
            }
        }
        personRecyclerAdapter adapter = new personRecyclerAdapter(mylist,getContext());
        recyclerView.setAdapter(adapter);
    }
    public void publis(){
        recyclerView.setAdapter(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ref = FirebaseDatabase.getInstance().getReference("publications");
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<HomeTalentProfile>();
                        //Deal pp = dataSnapshot.getValue(Deal.class);
                        //list.add(pp);
                        for(DataSnapshot ds : dataSnapshot.getChildren() ){
                            HomeTalentProfile dl = ds.getValue(HomeTalentProfile.class);
                                list.add(dl);

                        }
                        homeTalentRecyclerAdapter Adaptor= new homeTalentRecyclerAdapter(list,getContext());
                        recyclerView.setAdapter(Adaptor);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }
    public void perfiles(){
        recyclerView.setAdapter(null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ref = FirebaseDatabase.getInstance().getReference("Users");
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listperson = new ArrayList<PersonProfile>();
                        for(DataSnapshot ds : dataSnapshot.getChildren() ){
                            PersonProfile dl = ds.getValue(PersonProfile.class);
                            if(!dl.getId().equals(auth.getCurrentUser().getUid())){
                                listperson.add(dl);
                            }
                        }
                        personRecyclerAdapter Adaptor= new personRecyclerAdapter(listperson,getContext());
                        recyclerView.setAdapter(Adaptor);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchpeson(newText);
                    return true;
                }
            });
        }
    }

}