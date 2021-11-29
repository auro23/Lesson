package com.example.lesson;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class nvideos2
{
    String nombre;

            public nvideos2()
            {

            }

    public nvideos2(String nombre) {
        this.nombre = nombre;
    }

    public String getNombres() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return nombre;
    }
}
