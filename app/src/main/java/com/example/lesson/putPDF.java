package com.example.lesson;

public class putPDF
{

    public String name;
    public String url;
    public String grupo;
    public String materia;

    public putPDF()
    {

    }

    public putPDF(String name, String url, String grupo, String materia) {
        this.name = name;
        this.url = url;
        this.grupo = grupo;
        this.materia = materia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
}
