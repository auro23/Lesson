package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class opcion extends AppCompatActivity {
TextView lgprofe;
Button validar;
FirebaseAuth mAuth;
FirebaseUser currentUser;
EditText txtmatri,txtpass;
    String smatricula="";
    String spass="";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion);
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        validar=findViewById(R.id.button);
        txtmatri=findViewById(R.id.editTextTextPersonName);
        txtpass=findViewById(R.id.editTextTextPassword);
        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              smatricula=txtmatri.getText().toString();
              spass=txtpass.getText().toString();

              if(!smatricula.isEmpty() && !spass.isEmpty())
              {
                  buscarregistro();
              }
              else{
                  Toast.makeText(opcion.this,"Rellene todos los campos",Toast.LENGTH_SHORT).show();
              }
            }
        });

        lgprofe=findViewById(R.id.loginprofe);
        lgprofe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(opcion.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void buscarregistro()
    {
        mDatabase= FirebaseDatabase.getInstance().getReference();
       mDatabase.child("Alumnos").child(smatricula).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String password=spass;
                    String contraseña=snapshot.child("pass").getValue().toString();
                    String txmatricula=snapshot.child("matricula").getValue().toString();
                            String txnombre=snapshot.child("nombre").getValue().toString();

                        if(spass==password)
                        {

                            if (currentUser == null) {                                       //check if the user is new then signIn anonymously
                                mAuth.signInAnonymously().                                 //.signInAnonymously is a method provided by Firebase
                                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {        //insert a Listener that listen
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)          // insert a method that will executes when the process is completed
                                    {
                                        if (task.isSuccessful())                    // check the required task is completed successfully
                                        {
                                            Intent intent1=new Intent(getApplicationContext(),inicio.class);
                                           // intent1.putExtra("dato",txmatricula);
                                          //  intent1.putExtra("dato2",txnombre);
                                            intent1.putExtra("dato",smatricula);

                                            startActivity(intent1);
                                        }
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {         //if the signin failed
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(opcion.this, "" + e, Toast.LENGTH_SHORT).show();     //return error in logs
                                            }
                                        });

                            }
                            else{
                                Toast.makeText(opcion.this, "error", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(opcion.this,"La contraseña no coincide",Toast.LENGTH_SHORT).show();
                        }

                }
                else{
                    Toast.makeText(opcion.this,"La matricula no existe o es incorrecta",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(opcion.this,""+error,Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onBackPressed() { super.onBackPressed(); finishAffinity(); System.exit(0); }
}