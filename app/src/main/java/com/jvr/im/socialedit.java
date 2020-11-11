package com.jvr.im;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tiper.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class socialedit extends Fragment {

    private ArrayList<SocialProfile> socialProfiles;
    private socialRecyclerAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private RecyclerView rvsocialsedit;
    private String socialn;

    public socialedit() {
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
        View view = inflater.inflate(R.layout.fragment_socialedit, container, false);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ImageView socialaddedit = view.findViewById(R.id.socialaddedit);
        rvsocialsedit = view.findViewById(R.id.rvsocialsedit);
        rvsocialsedit.setLayoutManager(new GridLayoutManager(getContext(),4));
        getDatosFirebase();

        socialaddedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogDocs();
            }
        });

        return view;
    }

    private void showAlertDialogDocs() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.add_social, null);
        builder.setView(customLayout);
        final EditText name = customLayout.findViewById(R.id.eturlsocial);
        CardView btnok = customLayout.findViewById(R.id.btnoksocial);
        final MaterialSpinner materialSpinner = customLayout.findViewById(R.id.spinnersocial);
        String[] tipos={"WhatsApp","Youtube","Twitter","Tumblr","SoundCloud","Pinterest","LinkedIn","Instagram","GitHub","Facebook"};
        //---------------------------

        //---------------------------
        ArrayAdapter<String> listatipusu=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,tipos);
        listatipusu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(listatipusu);
        materialSpinner.setOnItemClickListener(new MaterialSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(MaterialSpinner materialSpinner, View view, int i, long l) {
                Toast.makeText(getContext(), materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                switch (i){
                    case 0:
                        socialn = "wpp";
                        break;
                    case 1:
                        socialn = "yt";
                        break;
                    case 2:
                        socialn = "tw";
                        break;
                    case 3:
                        socialn = "tblr";
                        break;
                    case 4:
                        socialn = "sc";
                        break;
                    case 5:
                        socialn = "pin";
                        break;
                    case 6:
                        socialn = "in";
                        break;
                    case 7:
                        socialn = "ig";
                        break;
                    case 8:
                        socialn = "git";
                        break;
                    case 9:
                        socialn = "fb";
                        break;

                }
            }
        });
        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    DatabaseReference databaseReferenceupload = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()+"/social").child(socialn);
                    Map<String,Object> user = new HashMap<>();
                    user.put("Social",socialn);
                    user.put("Url",name.getText().toString());
                    databaseReferenceupload.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Exito", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });

            }
        });
    }

    private void getDatosFirebase(){
        DatabaseReference databaseReferencerv = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()+"/social");
        databaseReferencerv.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                socialProfiles = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    SocialProfile socialProfile = dataSnapshot1.getValue(SocialProfile.class);
                    socialProfiles.add(socialProfile);
                }
                adapter = new socialRecyclerAdapter(socialProfiles,getContext());
                rvsocialsedit.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}