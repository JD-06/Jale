package com.jvr.im;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mabbas007.tagsedittext.TagsEditText;

import static com.facebook.FacebookSdk.getApplicationContext;


public class prfileedit extends Fragment {

    private EditText edname, edage,edubi, edoccupationTalent, edquestionTalent, edtrajectoryTalent,edocupationBuisnes,
            edcompanyBuisnes, edregdescribe, edtrajectoryBuisnes,edocupationEspectador;
    private ImageView imageView;
    private String selection = "";
    private int i;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private final int GALLERY = 1;
    private String upload_URL = "http://187.153.75.150:609/android_upload/upload.php";
    JSONObject jsonObject;
    RequestQueue rQueue;
    private String linkfoto;

    public prfileedit() {
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
        View view = inflater.inflate(R.layout.fragment_prfileedit, container, false);
        final LinearLayout lntalento, lnempresas, lnespectador;
        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("name");
        String age = intent.getStringExtra("age");
        String ubi = intent.getStringExtra("ubi");
        String type = intent.getStringExtra("type");
        String occupation = intent.getStringExtra("occupation");
        String trajectory = intent.getStringExtra("trajectory");
        String question = intent.getStringExtra("question");
        String describe = intent.getStringExtra("describe");
        String copany = intent.getStringExtra("copany");
        linkfoto = intent.getStringExtra("picture");

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final CardView cvregok = view.findViewById(R.id.cvregokedit);
        //Linear Layaouts
        lntalento = view.findViewById(R.id.lntalentoedit);
        lnempresas = view.findViewById(R.id.lnempresaedit);
        lnespectador = view.findViewById(R.id.lnespectadoredit);

        //EditText general
        edname = view.findViewById(R.id.etregnameedit);
        edage = view.findViewById(R.id.etregageedit);
        edubi = view.findViewById(R.id.etregubiedit);
        imageView = view.findViewById(R.id.ivperfiledit);

        //EditText Talentos
        edoccupationTalent = view.findViewById(R.id.edocupationTalentedit);
        edquestionTalent = view.findViewById(R.id.edquestionTalentedit);
        edtrajectoryTalent = view.findViewById(R.id.edtrajectoryTalentedit);
        //EditText Buisnes
        edocupationBuisnes = view.findViewById(R.id.edocupationBuisnesedit);
        edcompanyBuisnes = view.findViewById(R.id.edcompanyBuisnesedit);
        edregdescribe = view.findViewById(R.id.edregdescribeedit);
        edtrajectoryBuisnes = view.findViewById(R.id.edtrajectoryBuisnesedit);

        //EditText Search
        edocupationEspectador = view.findViewById(R.id.edocupationEspectadoredit);
        edname.setText(name);
        edage.setText(age);
        edubi.setText(ubi);

        switch (type){
            case "Talento":
                lntalento.setVisibility(View.VISIBLE);
                lnempresas.setVisibility(View.INVISIBLE);
                lnespectador.setVisibility(View.INVISIBLE);
                edoccupationTalent.setText(occupation);
                edquestionTalent.setText(question);
                edtrajectoryTalent.setText(trajectory);
                break;
            case "Buscador":
                lntalento.setVisibility(View.INVISIBLE);
                lnempresas.setVisibility(View.VISIBLE);
                lnespectador.setVisibility(View.INVISIBLE);
                edocupationBuisnes.setText(occupation);
                edcompanyBuisnes.setText(copany);
                edregdescribe.setText(describe);
                edtrajectoryBuisnes.setText(trajectory);
                break;
            case "Espectador":
                lntalento.setVisibility(View.INVISIBLE);
                lnempresas.setVisibility(View.INVISIBLE);
                lnespectador.setVisibility(View.VISIBLE);
                edocupationEspectador.setText(occupation);
                break;

        }

        CardView cvprofile = view.findViewById(R.id.cvprofileedit);
        if(!linkfoto.equals("")){
            Picasso.get().load(linkfoto).into(imageView);
        }
        edname.setText(name);
        //requestMultiplePermissions();

      cvregok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!selection.equals("")){
                if (edname.getText()!=null&&edage.getText()!=null&&edubi.getText()!=null){
                    FireStoreData();
                }
            }
        }
    });
        cvprofile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



             /*

            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY);



              */
        }
    });
        return view;
    }
    private void FireStoreData(){

        Map<String,Object> user = new HashMap<>();
        Map<String,Object> tags = new HashMap<>();
        switch (selection){
            case "Talento":
                user.put("name",edname.getText().toString());
                user.put("age",edage.getText().toString());
                user.put("ubication",edubi.getText().toString());
                user.put("occupation", edoccupationTalent.getText().toString());
                user.put("question",edquestionTalent.getText().toString());
                user.put("trajectory",edtrajectoryTalent.getText().toString());
                user.put("mode",selection);
                user.put("picture",linkfoto);

                break;
            case "Buscador":
                user.put("name",edname.getText().toString());
                user.put("age",edage.getText().toString());
                user.put("ubication",edubi.getText().toString());
                user.put("occupation", edocupationBuisnes.getText().toString());
                user.put("company", edcompanyBuisnes.getText().toString());
                user.put("describe",edregdescribe.getText().toString());
                user.put("trajectory",edtrajectoryBuisnes.getText().toString());
                user.put("mode",selection);
                user.put("picture",linkfoto);

                break;
            case "Espectador":
                user.put("name",edname.getText().toString());
                user.put("age",edage.getText().toString());
                user.put("ubication",edubi.getText().toString());
                user.put("occupation",edocupationEspectador.getText().toString());
                user.put("mode",selection);
                user.put("picture",linkfoto);

                break;
        }
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
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
        /*
        if(requestCode == PICK_DOC_REQUEST){
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = String.valueOf(auth.getCurrentUser().getUid()+".pdf");
            Log.d("ooooooo",displayName);
            uploadPDF(displayName,uri);
        }

         */
    }

    private void uploadImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            String imgname = auth.getCurrentUser().getUid();
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
                        linkfoto = "http://187.153.75.150:609/android_upload/img/"+auth.getCurrentUser().getUid()+".jpg";
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






}