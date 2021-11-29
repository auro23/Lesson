package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.HashMap;

public class menumaes extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth mAuth;
    String url="https://www.facebook.com/Escuela-Primaria-Emiliano-Zapata-T-M-Nuevo-Progreso-Campeche-107617857730005";
    GoogleSignInClient mGoogleSignInClient;


    //objetos de los recursos
    EditText edturlpdf,edturlvideo;
    Button btnpdf,btnlisto,btnvideo;
    ProgressDialog progressvideo;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    //------------

    //objetos del alumno

    Spinner opciones,opciones2,materiassp;
    private String grado="";
    private String grupo="";
    private String salon="";
    private String materia="";
    //---------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menumaes);
        opciones=(Spinner)findViewById(R.id.spgrado);
        opciones2=(Spinner)findViewById(R.id.spgrupo);
        materiassp=(Spinner)findViewById(R.id.spmaterias);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.opcionesgrado, android.R.layout.simple_spinner_item);
        opciones.setAdapter(adapter1);
        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this,R.array.opcionesgrupo, android.R.layout.simple_spinner_item);
        opciones2.setAdapter(adapter2);
        mAuth = FirebaseAuth.getInstance();
        ArrayAdapter<CharSequence>adapter3=ArrayAdapter.createFromResource(this,R.array.opcionesmaterias, android.R.layout.simple_spinner_item);
        materiassp.setAdapter(adapter3);

        btnpdf=findViewById(R.id.btnpdf);
        edturlvideo=findViewById(R.id.edtvideo);
        btnvideo=findViewById(R.id.btnvideo);
        edturlpdf=findViewById(R.id.edtpdf);
        btnlisto=findViewById(R.id.btnsubir);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressvideo=new ProgressDialog(menumaes.this);
                choosevideo();
            }
        });


        mAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(menumaes.this, gso);
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
                        Intent intent1 = new Intent(menumaes.this, menumaes.class);
                        startActivity(intent1);
                        break;
                        case R.id.nav_classe:
                    Intent intent2 = new Intent(menumaes.this, clasemaest.class);
                    startActivity(intent2);
                    break;
                    case R.id.nav_alumn:
                        Intent intent4 = new Intent(menumaes.this, registroalum.class);
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
                        Intent intentses=new Intent(menumaes.this,MainActivity.class);
                        startActivity(intentses);
                        break;

                    case R.id.nav_ayud:
                        Intent intenta = new Intent(menumaes.this, seccayuda.class);
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
    private void selectPDF(){
        Intent intent =new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);

    }
    private void choosevideo(){
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,5);
    }
Uri videouri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            edturlpdf.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
            edturlpdf.setEnabled(true);
            btnlisto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDFFileFirebase(data.getData());
                }
            });


        }
        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            edturlvideo.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
            edturlvideo.setEnabled(true);
            btnlisto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videouri = data.getData();
                    progressvideo.setTitle("Cargando...");
                    progressvideo.show();
                    uploadvideo();
                }
            });

        }
    }

    private String getfiletype(Uri videouri) {
        ContentResolver r = getContentResolver();
        // get the file type ,in this case its mp4
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));
    }
    private void uploadvideo() {
        if (videouri != null) {
            // save the selected video in Firebase storage
            final StorageReference reference = FirebaseStorage.getInstance().getReference("Files/" + System.currentTimeMillis() + "." + getfiletype(videouri));
            reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    grado = opciones.getSelectedItem().toString();
                    grupo = opciones2.getSelectedItem().toString();
                    materia = materiassp.getSelectedItem().toString();
                    String nombre=edturlvideo.getText().toString();
                    salon = grado + grupo;
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Video");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Videolink", downloadUri);
                    map.put("Grupo",salon);
                    map.put("Materia",materia);
                    reference1.child(salon).child(materia).child(nombre).setValue(map);
                    // Video uploaded successfully
                    // Dismiss dialog
                    progressvideo.dismiss();
                    edturlvideo.setEnabled(false);
                    Toast.makeText(menumaes.this, "Subido con exito!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressvideo.dismiss();
                    edturlvideo.setEnabled(false);
                    Toast.makeText(menumaes.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // show the progress bar
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressvideo.setMessage("Subiendo video " + (int) progress + "%");
                }
            });
        }
    }



    private void uploadPDFFileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando archivo...");
        progressDialog.show();

        StorageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                grado = opciones.getSelectedItem().toString();
                grupo = opciones2.getSelectedItem().toString();
                materia = materiassp.getSelectedItem().toString();
                salon = grado + grupo;
                Uri uri=uriTask.getResult();
                putPDF putPDF=new putPDF(edturlpdf.getText().toString(),uri.toString(),salon,materia);
                databaseReference.child("Recursos").child(salon).child(materia).child(databaseReference.push().getKey()).setValue(putPDF);
                progressDialog.dismiss();
                edturlpdf.setEnabled(false);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Subiendo archivo..."+(int)progress+"%");

            }
        });


    }
    public void onBackPressed() { }
}