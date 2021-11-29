package com.example.lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class progress extends AppCompatActivity {
    ProgressBar cargandoapp;
    int contcargando=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Lcargando();
    }
    public void Lcargando()
    {
        cargandoapp=findViewById(R.id.progress);
        Timer tiempo=new Timer();
        TimerTask tiempotsk=new TimerTask() {
            @Override
            public void run() {
                contcargando++;
                cargandoapp.setProgress(contcargando);
                if(contcargando==100)
                {
                    tiempo.cancel();
                    Intent intent=new Intent(progress.this,opcion.class);
                    startActivity(intent);

                }

            }
        };
        tiempo.schedule(tiempotsk,0,100);
    }
}