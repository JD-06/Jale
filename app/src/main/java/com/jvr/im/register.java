package com.jvr.im;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;
import com.tiper.MaterialSpinner;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mabbas007.tagsedittext.TagsEditText;

public class register extends AppCompatActivity {

private EditText edname, edage,edubi, edoccupationTalent, edquestionTalent, edtrajectoryTalent,edocupationBuisnes,
        edcompanyBuisnes, edregdescribe, edtrajectoryBuisnes,edocupationEspectador;
private TagsEditText tagsEditTextHabilities, tagsEditTextSearch,tagsEditTextInteres;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final LinearLayout lntalento, lnempresas, lnespectador;
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        linkfoto = intent.getStringExtra("picture");
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final CardView cvregok = findViewById(R.id.cvregok);
        //Linear Layaouts
        lntalento = findViewById(R.id.lntalento);
        lnempresas = findViewById(R.id.lnempresa);
        lnespectador = findViewById(R.id.lnespectador);

        //EditText general
        edname = findViewById(R.id.etregname);
        edage = findViewById(R.id.etregage);
        edubi = findViewById(R.id.etregubi);
        imageView = findViewById(R.id.ivperfil);

        //EditText Talentos
        edoccupationTalent = findViewById(R.id.edocupationTalent);
        edquestionTalent = findViewById(R.id.edquestionTalent);
        edtrajectoryTalent = findViewById(R.id.edtrajectoryTalent);
        tagsEditTextHabilities =  findViewById(R.id.tagsEditTextHabilities);
        //EditText Buisnes
        edocupationBuisnes = findViewById(R.id.edocupationBuisnes);
        edcompanyBuisnes = findViewById(R.id.edcompanyBuisnes);
        edregdescribe = findViewById(R.id.edregdescribe);
        edtrajectoryBuisnes = findViewById(R.id.edtrajectoryBuisnes);
        tagsEditTextInteres = findViewById(R.id.tagsEditTextSearch);
        //EditText Search
        edocupationEspectador = findViewById(R.id.edocupationEspectador);
        tagsEditTextSearch = findViewById(R.id.tagsEditTextInteres);

        CardView cvprofile = findViewById(R.id.cvprofile);
        if(!linkfoto.equals("")){
            Picasso.get().load(linkfoto).into(imageView);
        }
        edname.setText(name);
        requestMultiplePermissions();
        final MaterialSpinner materialSpinner = findViewById(R.id.spinnerpersona);
        String[] tipos={"Talento esperado a ser encontrado","Buscador de Talento","Espectador"};
        //---------------------------

        //---------------------------
        ArrayAdapter<String> listatipusu=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipos);
        listatipusu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(listatipusu);
        materialSpinner.setOnItemClickListener(new MaterialSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(MaterialSpinner materialSpinner, View view, int i, long l) {
                Toast.makeText(register.this, materialSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                switch (i){
                    case 0:
                        //Toast.makeText(register.this, tagsEditTextHabilities.getTags().toString(), Toast.LENGTH_SHORT).show();
                        //Talento
                        selection = "Talento";
                        lntalento.setVisibility(View.VISIBLE);
                        lnespectador.setVisibility(View.INVISIBLE);
                        lnempresas.setVisibility(View.INVISIBLE);
                    break;
                    case 1:
                        //Buscador
                        selection = "Buscador";
                        lntalento.setVisibility(View.INVISIBLE);
                        lnempresas.setVisibility(View.VISIBLE);
                        lnespectador.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        //Espectador
                        selection = "Espectador";
                        lntalento.setVisibility(View.INVISIBLE);
                        lnespectador.setVisibility(View.VISIBLE);
                        lnempresas.setVisibility(View.INVISIBLE);
                        break;

                }
            }
        });
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
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });
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
                for ( i = 0; i < tagsEditTextHabilities.getTags().size() ; i++ ) {
                    tags.put("tag"+i,tagsEditTextHabilities.getTags().get(i));
                }
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
                for ( i = 1; i < tagsEditTextHabilities.getTags().size() ; i++ ) {
                    tags.put("tag"+i,tagsEditTextSearch.getTags().get(i));
                }
                break;
            case "Espectador":
                user.put("name",edname.getText().toString());
                user.put("age",edage.getText().toString());
                user.put("ubication",edubi.getText().toString());
                user.put("occupation",edocupationEspectador.getText().toString());
                user.put("mode",selection);
                user.put("picture",linkfoto);
                for ( i = 1; i < tagsEditTextHabilities.getTags().size() ; i++ ) {
                    tags.put("tag"+i,tagsEditTextInteres.getTags().get(i));
                }
                break;
        }
        databaseReference.child("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference.child("Users").child(auth.getCurrentUser().getUid()).child("tags").setValue(tags).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Intent intent = new Intent(register.this,MainActivity.class);
                               startActivity(intent);
                               finish();
                           }
                        }
                    });
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
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                    uploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(register.this, "Failed!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        linkfoto = "http://187.153.75.150:609/android_upload/img/"+auth.getCurrentUser().getUid()+".jpg";
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(register.this);
        rQueue.add(jsonObjectRequest);

    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

}