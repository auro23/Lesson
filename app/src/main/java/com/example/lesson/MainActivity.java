package com.example.lesson;


import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

import static com.airbnb.lottie.L.TAG;

public class MainActivity extends inicio {
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    SignInButton btnsign;
   String correoSI, contraseñaSI;
    //OBJETOS DE LOGIN CON CORREO
    Button btnacceder;
    EditText edtcorreo;
    EditText edtpass;
    View view4;
    Button btnregistrarse;
    TextView txtnocuenta,txttema,txto,txtregres;
    //----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txto=findViewById(R.id.textView6);
        txtregres=findViewById(R.id.textView12);

        txtregres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               botonesiniciarS();
            }
        });
        btnregistrarse=findViewById(R.id.button3);
        view4=findViewById(R.id.view4);
        btnacceder=findViewById(R.id.button2);
        txttema=findViewById(R.id.textView3);
        txtnocuenta=findViewById(R.id.textView11);
        txtnocuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttema.setText("Registrate");
                btnacceder.setVisibility(View.INVISIBLE);
                txtregres.setVisibility(View.VISIBLE);
                txto.setVisibility(View.INVISIBLE);
                view4.setVisibility(View.INVISIBLE);
                btnsign.setVisibility(View.INVISIBLE);
                txtnocuenta.setVisibility(View.INVISIBLE);
                btnregistrarse.setVisibility(View.VISIBLE);

            }
        });
        btnsign=findViewById(R.id.btngmail);
        edtcorreo=findViewById(R.id.editTextTextEmailAddress);
        edtpass=findViewById(R.id.editTextTextPassword);
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
cargando();
            }
        });
        btnacceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                iniciarSesion();
            }
        });
        btnregistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                registrar();
            }
        });
    }
    @Override public void onBackPressed()
    {

       if(btnregistrarse.getVisibility()==View.INVISIBLE)
       {
            Intent intent=new Intent(this, opcion.class);
            startActivity(intent);
        }
       else{

       }

    }
    public void cargando()
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

              Toast.makeText(this,"OPKJ",Toast.LENGTH_SHORT).show();
              progressDialog.dismiss();
              finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this,"OOO",Toast.LENGTH_SHORT).show();
progressDialog.dismiss();
                            finish();
                        }

                    }


                    private void updateUI(FirebaseUser user)
                    {

                        Intent intent=new Intent(MainActivity.this,menumaes.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    }
                });
    }
  private void iniciarSesion()
  {
      String correoSI=edtcorreo.getText().toString().trim();
      String  contraseñaSI=edtpass.getText().toString().trim();

        if (!correoSI.isEmpty() && !contraseñaSI.isEmpty()) {
            mAuth.signInWithEmailAndPassword(correoSI, contraseñaSI).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, menumaes.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Esta cuenta no existe, verifique sus datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }

    }
   private void registrar()
    {
       String correoSI=edtcorreo.getText().toString();
      String  contraseñaSI=edtpass.getText().toString();

        if(correoSI.isEmpty() && contraseñaSI.isEmpty())
        {
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(contraseñaSI.length()>6)
            {


            mAuth.createUserWithEmailAndPassword(correoSI,contraseñaSI).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task4) {
                    if(task4.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Registro exitoso, inicie sesión", Toast.LENGTH_SHORT).show();
                        botonesiniciarS();
                    }
                    else
                        {

                            Toast.makeText(MainActivity.this, "Ha ocurrido un error, intente más tarde", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
            else
            {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void botonesiniciarS()
    {
        txttema.setText("Inicia sesión");
        btnacceder.setVisibility(View.VISIBLE);
        txtregres.setVisibility(View.INVISIBLE);
        txto.setVisibility(View.VISIBLE);
        view4.setVisibility(View.VISIBLE);
        btnsign.setVisibility(View.VISIBLE);
        txtnocuenta.setVisibility(View.VISIBLE);
        btnregistrarse.setVisibility(View.INVISIBLE);
    }
}