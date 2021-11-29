package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class registroalum extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    String url = "https://www.facebook.com/Escuela-Primaria-Emiliano-Zapata-T-M-Nuevo-Progreso-Campeche-107617857730005";
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mDatabase;
    Spinner opciones,opciones2;
    //OBJETOS DEL REGISTRO

    Button btnregistrar;
    String mat = "";
    TextView txtpass;
    EditText txtnombre, txtmatricula, txtap, txtam;
    private String nombre = "";
    private String apellidoma = "";
    private String apellidopa = "";
    private String matricula = "";
    private String pass = "";
    private String grado="";
    private String grupo="";
    private String salon="";


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroalum);
        //declaracion de registro
        opciones=(Spinner)findViewById(R.id.sp01);
        opciones2=(Spinner)findViewById(R.id.sp);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.opcionesgrado, android.R.layout.simple_spinner_item);
        opciones.setAdapter(adapter);

        ArrayAdapter<CharSequence>adapter1=ArrayAdapter.createFromResource(this,R.array.opcionesgrupo, android.R.layout.simple_spinner_item);
        opciones2.setAdapter(adapter1);
        txtnombre = findViewById(R.id.edtnombre);
        txtmatricula = findViewById(R.id.edtmatricula);
        txtap = findViewById(R.id.edtap);
        txtam = findViewById(R.id.edtam);
        txtpass = findViewById(R.id.edtpass);
        btnregistrar = findViewById(R.id.btnregist);
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        ((EditText) findViewById(R.id.edtmatricula)).setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        generarMatricula();
                        matricula = txtmatricula.getText().toString();
                        txtpass.setText(matricula + mat);
                        return false;
                    }
                }
        );

        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(registroalum.this, gso);
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
                        Intent intent1 = new Intent(registroalum.this, menumaes.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_classe:
                        Intent intent2 = new Intent(registroalum.this, clasemaest.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_alumn:
                        Intent intent4 = new Intent(registroalum.this, registroalum.class);
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
                        Intent intentses=new Intent(registroalum.this,MainActivity.class);
                        startActivity(intentses);
                        break;

                    case R.id.nav_ayud:
                        Intent intenta = new Intent(registroalum.this, seccayuda.class);
                        startActivity(intenta);
                        break;
                }
                return false;
            }
        });


    }
    public void onBackPressed() { }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private String generarMatricula() {
        mat = "";
        int a;
        for (int i = 0; i < 7; i++) {
            if (i < 4) {    // 0,1,2,3 posiciones de numeros
                mat = (int) (Math.random() * 9) + "" + matricula;

            } else {       // 4,5,6 posiciones de letras
                do {
                    a = (int) (Math.random() * 26 + 65);///
                } while (a == 65 || a == 69 || a == 73 || a == 79 || a == 85);

                char letra = (char) a;
                if (i == 4) {
                    mat = mat + "-" + letra;
                } else {
                    mat = mat + "" + letra;
                }

            }
        }
        return mat;
    }

    public void registrar() {

        nombre = txtnombre.getText().toString();
        apellidopa = txtap.getText().toString();
        apellidoma = txtam.getText().toString();
        matricula = txtmatricula.getText().toString();
        pass = txtpass.getText().toString();
       grado=opciones.getSelectedItem().toString();
       grupo=opciones2.getSelectedItem().toString();
       salon=grado+grupo;

        if (!nombre.isEmpty() && !apellidopa.isEmpty() && !apellidoma.isEmpty() && !matricula.isEmpty() && !pass.isEmpty()) {
            ProcessRegist();


        } else {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();

        }

    }

    public void ProcessRegist() {

        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("paterno", apellidopa);
        map.put("materno", apellidoma);
        map.put("matricula", matricula);
        map.put("pass", pass);
        map.put("salon",salon);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Alumnos").child(matricula).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
               try {
                   if (task2.isSuccessful()) {
                       txtmatricula.setText("");
                       txtam.setText("");
                       txtap.setText("");
                       txtpass.setText("");
                       txtnombre.setText("");
                       Toast.makeText(registroalum.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                   }
                   else{
                       Toast.makeText(registroalum.this, "Intente más tarde", Toast.LENGTH_SHORT).show();
                   }
               }
               catch (Exception e)
               {
                   Toast.makeText(registroalum.this,""+e,Toast.LENGTH_LONG).show();
               }

            }
        });
    }

}