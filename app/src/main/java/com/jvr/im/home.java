package com.jvr.im;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class home extends Fragment {

    private ImageView btnadd;
    private final int GALLERY = 1;
    private String upload_URL = "http://187.153.75.150:609/android_upload/upload.php";
    JSONObject jsonObject;
    RequestQueue rQueue;
    ImageView imageView;
    String currentDateandTime;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private String linkfoto;
    private String type;
    private CardView btnok;
    private homeTalentRecyclerAdapter adaptertalent;
    private ArrayList<HomeTalentProfile> homeTalentProfiles;
    private RecyclerView homerv;

    public home() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getDatos();
        homerv = view.findViewById(R.id.homerv);
        homerv.setLayoutManager(new LinearLayoutManager(getContext()));
        btnadd = view.findViewById(R.id.btnadd);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                currentDateandTime = sdf.format(new Date());
                switch (type) {
                    case "Talento":
                        showAlertDialogDocs();
                        break;
                    case "Buscador":
                        showAlertDialogBuisness();
                        break;
                    case "Espectador":

                        break;

                }

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                    uploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            String imgname = auth.getCurrentUser().getUid()+currentDateandTime;
            jsonObject.put("name", imgname);
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);
            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaa", jsonObject.toString());
                        rQueue.getCache().clear();

                        Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        btnok.setEnabled(true);
                        linkfoto = "http://187.153.75.150:609/android_upload/img/"+auth.getCurrentUser().getUid()+currentDateandTime+".JPG";
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(jsonObjectRequest);

    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    private void showAlertDialogDocs() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.addpubli, null);
        builder.setView(customLayout);
        imageView = customLayout.findViewById(R.id.ivpicturepbli);
        final EditText etname = customLayout.findViewById(R.id.ettittlepubli);
        btnok = customLayout.findViewById(R.id.btnokpubli);
        btnok.setEnabled(false);

        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatabaseReference databaseReferenceupload = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()+"/publications").child(auth.getCurrentUser().getUid()+currentDateandTime);
                    Map<String,Object> user = new HashMap<>();
                    user.put("title",etname.getText().toString());
                    user.put("name",etname.getText().toString());
                    user.put("likes",0);
                    user.put("date",currentDateandTime);
                    user.put("id",auth.getCurrentUser().getUid()+currentDateandTime);
                    user.put("picturelink","http://187.153.75.150:609/android_upload/img/"+auth.getCurrentUser().getUid()+currentDateandTime+".jpg");
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

    private void showAlertDialogBuisness() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.addpublibuisnes, null);
        builder.setView(customLayout);
        final EditText ettittlebuisnes = customLayout.findViewById(R.id.ettittlebuisnes);
        final EditText etpublibuisnes = customLayout.findViewById(R.id.etpublibuisnes);
        CardView btnokbuis = customLayout.findViewById(R.id.btnokpublibuisnes);

        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnokbuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d=new Date();
                SimpleDateFormat fecc=new SimpleDateFormat("d, MMMM 'del' yyyy");
                String fechacComplString = fecc.format(d);
                DatabaseReference databaseReferenceupload = firebaseDatabase.getReference("publications").child(auth.getCurrentUser().getUid()+currentDateandTime);
                Map<String,Object> user = new HashMap<>();
                user.put("title",ettittlebuisnes.getText().toString());
                user.put("name",etpublibuisnes.getText().toString());
                user.put("Postulantes",0);
                user.put("date",fechacComplString);
                user.put("id",auth.getCurrentUser().getUid()+currentDateandTime);
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

    private void getDatos() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    type = snapshot.child("mode").getValue().toString();
                    switch (type) {
                        case "Talento":
                            getDatosRV();
                            break;
                        case "Buscador":

                            break;
                        case "Espectador":

                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDatosRV(){

        DatabaseReference databaseReferencesocial = firebaseDatabase.getReference("publications");
        databaseReferencesocial.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeTalentProfiles = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    HomeTalentProfile talentProfile = dataSnapshot1.getValue(HomeTalentProfile.class);
                    homeTalentProfiles.add(talentProfile);
                }
                adaptertalent = new homeTalentRecyclerAdapter(homeTalentProfiles,getContext());
                homerv.setAdapter(adaptertalent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }