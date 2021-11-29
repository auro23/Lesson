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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class seccayuda extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
//objetos de ayuda
    ImageView btface,btinsta;
    //------------------
    FirebaseAuth mAuth;
    String url="https://www.facebook.com/Escuela-Primaria-Emiliano-Zapata-T-M-Nuevo-Progreso-Campeche-107617857730005";
    String urlface="https://www.facebook.com/santifs234/";
    String urlinsta="https://www.instagram.com/sann_au/?hl=es";
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccayuda);
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
        mAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(seccayuda.this, gso);
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

                    case R.id.nav_materia:
                        Intent intent1 = new Intent(seccayuda.this, menumaes.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_classe:
                        Intent intent2 = new Intent(seccayuda.this, clasemaest.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_alumn:
                        Intent intent4 = new Intent(seccayuda.this, registroalum.class);
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
                        Intent intentses=new Intent(seccayuda.this,MainActivity.class);
                        startActivity(intentses);
                        break;

                    case R.id.nav_ayud:
                        Intent intenta = new Intent(seccayuda.this, seccayuda.class);
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
}