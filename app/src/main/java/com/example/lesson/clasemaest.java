package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class clasemaest extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    String url = "https://www.facebook.com/Escuela-Primaria-Emiliano-Zapata-T-M-Nuevo-Progreso-Campeche-107617857730005";
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mDatabase;

    //OBJETOS DE AGREGAR
    Button btningresardatos;
    Spinner opciones, opciones2,materiassp;
    private String grado="";
    private String grupo="";
    private String salon="";
    private String materia="";
    private String contador="";
    //--------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasemaest);
        opciones=(Spinner)findViewById(R.id.spgrado);
        opciones2=(Spinner)findViewById(R.id.spgrupo);
        materiassp=(Spinner)findViewById(R.id.spmaterias);
        btningresardatos=findViewById(R.id.btnagregarclas);

        ArrayAdapter<CharSequence>adapter1=ArrayAdapter.createFromResource(this,R.array.opcionesgrado, android.R.layout.simple_spinner_item);
        opciones.setAdapter(adapter1);
        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this,R.array.opcionesgrupo, android.R.layout.simple_spinner_item);
        opciones2.setAdapter(adapter2);
        mAuth = FirebaseAuth.getInstance();
        ArrayAdapter<CharSequence>adapter3=ArrayAdapter.createFromResource(this,R.array.opcionesmaterias, android.R.layout.simple_spinner_item);
        materiassp.setAdapter(adapter3);

        btningresardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datos();
            }
        });
        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(clasemaest.this, gso);
     /*   FirebaseUser currentUser=mAuth.getCurrentUser();
        txtnombre.setText(currentUser.getDisplayName());
        txtemail.setText(currentUser.getEmail());*/
        // Glide.with(this).load(currentUser.getPhotoUrl()).into(ima);


        setUpToolbar();


        navigationView = (NavigationView) findViewById(R.id.navigation_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_materia:
                        Intent intent1 = new Intent(clasemaest.this, menumaes.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_classe:
                        Intent intent2 = new Intent(clasemaest.this, clasemaest.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_alumn:
                        Intent intent4 = new Intent(clasemaest.this, registroalum.class);
                        startActivity(intent4);
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
                        Intent intentses=new Intent(clasemaest.this,MainActivity.class);
                        startActivity(intentses);
                        break;

                    case R.id.nav_ayud:
                        Intent intenta = new Intent(clasemaest.this, seccayuda.class);
                        startActivity(intenta);
                        break;
                }
                return false;
            }
        });


    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }
    public void contmaterias()
    {
        if(materia.equals("Español"))
        {
            contador="ES";
        }
        if(materia.equals("Matematicas"))
        {
            contador="MA";
        }
        if(materia.equals("Historia"))
        {
            contador="HI";
        }
        if(materia.equals("Ciencias Naturales"))
        {
            contador="CN";
        }
        if(materia.equals("Artes"))
        {
            contador="AR";
        }
        if(materia.equals("Atlas de méxico"))
        {
            contador="AM";
        }
        if(materia.equals("Formación civica y etica"))
        {
            contador="FC";
        }

    }
    public void datos() {

        grado = opciones.getSelectedItem().toString();
        grupo = opciones2.getSelectedItem().toString();
        materia = materiassp.getSelectedItem().toString();
        salon = grado + grupo;
        contmaterias();


        mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("Clases").child(salon).child(contador).setValue(materia).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(clasemaest.this, "Registro exitoso", Toast.LENGTH_SHORT).show();


                            }

                        }
                    });

    }
    public void onBackPressed() { }
}