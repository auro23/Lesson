package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class clasvideo extends AppCompatActivity {

VideoView videovw;
TextView url;
ProgressDialog pd;
String salon="";
String idgrupo="";
String selectmate="";
ListView listView;
ImageView imgovejin;
    List<nvideos> videitos;
    List<nvideos2>videitos2=new ArrayList<>();
private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
private DatabaseReference reference=firebaseDatabase.getReference();

Spinner mSpinnerCities;
Button btnir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasvideo);
        listView=findViewById(R.id.listavideos);
        imgovejin=findViewById(R.id.imagenovejin);
        btnir=findViewById(R.id.vervideos);
        mSpinnerCities=findViewById(R.id.spmaterias);
        elspnier();
       btnir.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               lista();
           }
       });

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
               nvideos2 video=videitos2.get(i);
               String nomvide=video.getNombres();
               videovw=(VideoView)findViewById(R.id.video);
               pd=new ProgressDialog(clasvideo.this);
               MediaController mediaController=new MediaController(clasvideo.this);
               videovw.setMediaController(mediaController);
               mediaController.setAnchorView(videovw);
               url=(TextView)findViewById(R.id.text);
               imgovejin.setVisibility(View.INVISIBLE);
               pd.setMessage("Cargando...");
               videovw.setVisibility(View.VISIBLE);
               pd.show();
               reference.child("Video").child(idgrupo).child(selectmate).child(nomvide).child("Videolink").addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                       String message=snapshot.getValue(String.class);
                       Uri uri= Uri.parse(message);
                       videovw.setVideoURI(uri);
                       videovw.start();
                       videitos2.clear();
                       videovw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                           @Override
                           public void onPrepared(MediaPlayer mp) {
                               pd.dismiss();
                           }
                       });



                   }

                   @Override
                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                   }
               });

           }
       });

        /*video=(VideoView)findViewById(R.id.video);
    pd=new ProgressDialog(clasvideo.this);
    MediaController mediaController=new MediaController(this);
    video.setMediaController(mediaController);
    mediaController.setAnchorView(video);
    url=(TextView)findViewById(R.id.text);
    pd.setMessage("Cargando...");
    pd.show();*/


    }
public void lista(){
        String materiaselect=mSpinnerCities.getSelectedItem().toString();
    selectmate=materiaselect;
        reference.child("Video").child(idgrupo).child(materiaselect).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre = ds.getKey();
                        videitos2.add(new nvideos2(nombre));
                    }

                    ArrayAdapter<nvideos2> arrayAdapter = new ArrayAdapter<nvideos2>(getApplicationContext(), android.R.layout.simple_list_item_1, videitos2) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setTextColor(Color.BLACK);
                            textView.setTextSize(20);
                            return view;

                        }
                    };

                    listView.setAdapter(arrayAdapter);
                }

                }



            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
}
    public void elspnier()
    {
        Intent intentrecibir=getIntent();
        salon= intentrecibir.getStringExtra("dato");

        videitos=new ArrayList<>();

        reference.child("Alumnos").child(salon).child("salon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String date = snapshot.getValue().toString();
                    idgrupo =date;
                    reference.child("Video").child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                String nombre=ds.getKey();

                                videitos.add(new nvideos(nombre));

                            }
                            ArrayAdapter<nvideos>arrayAdapter=new ArrayAdapter<>(clasvideo.this, android.R.layout.simple_dropdown_item_1line,videitos);

                            mSpinnerCities.setAdapter(arrayAdapter);


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}