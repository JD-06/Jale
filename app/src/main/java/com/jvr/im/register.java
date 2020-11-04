package com.jvr.im;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tiper.MaterialSpinner;

import mabbas007.tagsedittext.TagsEditText;

public class register extends AppCompatActivity {

private EditText edname, edae,edubi, edoccupationTalent, edquestionTalent, edtrajectoryTalent,edocupationBuisnes,
        edcompanyBuisnes, edregdescribe, edtrajectoryBuisnes,edocupationEspectador;

private TagsEditText tagsEditTextHabilities, tagsEditTextSearch,tagsEditTextInteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final LinearLayout lntalento, lnempresas, lnespectador;
        lntalento = findViewById(R.id.lntalento);
        lnempresas = findViewById(R.id.lnempresa);
        lnespectador = findViewById(R.id.lnespectador);
        tagsEditTextHabilities =  findViewById(R.id.tagsEditTextHabilities);
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
                        lntalento.setVisibility(View.VISIBLE);
                        lnespectador.setVisibility(View.INVISIBLE);
                        lnempresas.setVisibility(View.INVISIBLE);
                    break;
                    case 1:
                        //Buscador
                        lntalento.setVisibility(View.INVISIBLE);
                        lnempresas.setVisibility(View.VISIBLE);
                        lnespectador.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        //Espectador
                        lntalento.setVisibility(View.INVISIBLE);
                        lnespectador.setVisibility(View.VISIBLE);
                        lnempresas.setVisibility(View.INVISIBLE);
                        break;

                }
            }
        });
    }
}