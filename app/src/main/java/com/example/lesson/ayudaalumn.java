package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ayudaalumn extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    ImageView btface,btinsta;
    TextView txtnombre,txtmatricula;
    String url="https://www.facebook.com/Escuela-Primaria-Emiliano-Zapata-T-M-Nuevo-Progreso-Campeche-107617857730005";
    GoogleSignInClient mGoogleSignInClient;
    String urlface="https://www.facebook.com/santifs234/";
    String urlinsta="https://www.instagram.com/sann_au/?hl=es";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayudaalumn);

        btface=findViewById(R.id.imgfacebook);
        btinsta=findViewById(R.id.imginsta);

        btface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriface=Uri.parse(urlface);
                Intent i=new Intent(Intent.ACTION_VIEW,uriface);
                startActivity(i);
            }
        });

        btinsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriinsta=Uri.parse(urlinsta);
                Intent i=new Intent(Intent.ACTION_VIEW,uriinsta);
                startActivity(i);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(ayudaalumn.this, gso);
     /*   FirebaseUser currentUser=mAuth.getCurrentUser();
        txtnombre.setText(currentUser.getDisplayName());
        txtemail.setText(currentUser.getEmail());*/
        // Glide.with(this).load(currentUser.getPhotoUrl()).into(ima);

        setUpToolbar();

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_materia:
                    {

                        Intent intentses=new Intent(ayudaalumn.this,inicio.class);
                        startActivity(intentses);

                    }

                    break;
                    case  R.id.nav_ayud:
                    {

                        Intent intentq=new Intent(ayudaalumn.this,ayudaalumn.class);
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
                        Intent intentses=new Intent(ayudaalumn.this,opcion.class);
                        startActivity(intentses);
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
    public void onBackPressed() { }
}