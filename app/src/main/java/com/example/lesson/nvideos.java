package com.example.lesson;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class nvideos
{
    String nombre;

  public nvideos()
  {

  }

    public nvideos(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
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
