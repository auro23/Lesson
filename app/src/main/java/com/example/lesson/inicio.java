package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class inicio extends AppCompatActivity {

    //ver PDF

    ListView listView;
    DatabaseReference databaseReference;
    List<putPDF> uploadedPDF;
    //---------------

    //objetos de panel oficial
    ImageView borregin,doc,vid;
    Button btndocumento,btnvideo;
    TextView titulopaneloficial;


    //--------------------------------

    //objetos panel documento
    TextView txttitulodoc,txtordenes,txtmateri;
    ImageView momo;

    //_________________________________________________-
    Spinner spmateria;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth mAuth;

    String url="https://www.facebook.com/Escuela-Primaria-Emiliano-Zapata-T-M-Nuevo-Progreso-Campeche-107617857730005";
    GoogleSignInClient mGoogleSignInClient;
    String elegmateria="";
    Button btnbuscar;
    String matricula="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Intent intentrecibir=getIntent();
       matricula= intentrecibir.getStringExtra("dato");
        //Toast.makeText(this, matricula, Toast.LENGTH_SHORT).show();
        spmateria=(Spinner)findViewById(R.id.spmaterias);
        ArrayAdapter<CharSequence>adapter3=ArrayAdapter.createFromResource(this,R.array.opcionesmaterias, android.R.layout.simple_spinner_item);
        spmateria.setAdapter(adapter3);
        btnbuscar=findViewById(R.id.button4);
        borregin=findViewById(R.id.btnpanel1);
        doc=findViewById(R.id.btnpanel4);
        vid=findViewById(R.id.btnpanel5);
        btndocumento=findViewById(R.id.btnpanel2);
        btnvideo=findViewById(R.id.btnpanel3);
        titulopaneloficial=findViewById(R.id.btnpanel6);
        momo=findViewById(R.id.imageView);
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent is=new Intent(getApplicationContext(),clasvideo.class);
                is.putExtra("dato",matricula);
                startActivity(is);

            }
        });
        btndocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderpaneloficial();
                mostrarpaneldocumento();
            }
        });



//panel documento
        txttitulodoc=findViewById(R.id.textView17);
        txtordenes=findViewById(R.id.textView19);
        txtmateri=findViewById(R.id.textView22);
        //:_____________________________________________________
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(inicio.this, gso);
        listView=findViewById(R.id.listapdf);
        uploadedPDF=new ArrayList<>();


                listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        putPDF putPDF=uploadedPDF.get(i);
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setType("application/pdf");
                        intent.setData(Uri.parse(putPDF.getUrl()));
                        startActivity(intent);

                    }
                }
        );

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrievePDFFiles();
            }
        });
        setUpToolbar();

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_materia:
                        {

                            Intent intentses=new Intent(inicio.this,inicio.class);
                            startActivity(intentses);

                    }
                    break;
                 
                    case  R.id.nav_ayud:
                    {

                        Intent intentq=new Intent(inicio.this,ayudaalumn.class);
                        startActivity(intentq);

                    }
                    break;
                    case  R.id.nav_redes:{

                        Uri uri=Uri.parse(url);
                        Intent i=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(i);

                    }
                    break;

                    case R.id.nav_sesion:
                        mGoogleSignInClient.signOut();
                        mAuth.signOut();
                        Intent intentses=new Intent(inicio.this,opcion.class);
                        startActivity(intentses);
                        break;
                }
                return false;
            }
        });


    }

    private void retrievePDFFiles()
    {

        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Alumnos").child(matricula).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String grupo=snapshot.child("salon").getValue().toString();
                    elegmateria = spmateria.getSelectedItem().toString();
                    databaseReference.child("Recursos").child(grupo).child(elegmateria).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                putPDF putPDF = ds.getValue(com.example.lesson.putPDF.class);
                                uploadedPDF.add(putPDF);

                            }

                            String[] uploadsName = new String[uploadedPDF.size()];
                            for (int i = 0; i < uploadsName.length; i++) {
                                uploadsName[i] = uploadedPDF.get(i).getName()+".pdf";
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploadsName) {


                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                                    View view= super.getView(position, convertView, parent);
                                    TextView textView=(TextView)view.findViewById(android.R.id.text1);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(20);
                                    return view;


                                }
                            };

                            listView.setAdapter(arrayAdapter);

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

    public void onBackPressed() {

        esconderpaneldocumento();
        mostrarpaneloficial();

    }
    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    public void esconderpaneloficial()
    {


     borregin.setVisibility(View.INVISIBLE);
        doc.setVisibility(View.INVISIBLE);
        vid.setVisibility(View.INVISIBLE);
        btndocumento.setVisibility(View.INVISIBLE);
        btnvideo.setVisibility(View.INVISIBLE);
        titulopaneloficial.setVisibility(View.INVISIBLE);

    }
    public void mostrarpaneloficial()
    {
        borregin.setVisibility(View.VISIBLE);
        doc.setVisibility(View.VISIBLE);
        vid.setVisibility(View.VISIBLE);
        btndocumento.setVisibility(View.VISIBLE);
        btnvideo.setVisibility(View.VISIBLE);
        titulopaneloficial.setVisibility(View.VISIBLE);
    }

    public void esconderpaneldocumento()
    {

        listView.setVisibility(View.INVISIBLE);
        spmateria.setVisibility(View.INVISIBLE);
        btnbuscar.setVisibility(View.INVISIBLE);
        txttitulodoc.setVisibility(View.INVISIBLE);
        txtmateri.setVisibility(View.INVISIBLE);
        txtordenes.setVisibility(View.INVISIBLE);
        momo.setVisibility(View.INVISIBLE);



    }

    public void mostrarpaneldocumento()
    {

        listView.setVisibility(View.VISIBLE);
        spmateria.setVisibility(View.VISIBLE);
        btnbuscar.setVisibility(View.VISIBLE);
        txttitulodoc.setVisibility(View.VISIBLE);
        txtmateri.setVisibility(View.VISIBLE);
        txtordenes.setVisibility(View.VISIBLE);
momo.setVisibility(View.VISIBLE);

    }
}