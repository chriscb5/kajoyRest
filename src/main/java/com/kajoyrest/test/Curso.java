package com.kajoyrest.test;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Curso {

    private @Id @GeneratedValue Long id;
    private String grado;
    private String paralelo;
    private String bloque;

    Curso() { }

    public Curso(String grado, String paralelo, String bloque) {
        this.grado = grado;
        this.paralelo = paralelo;
        this.bloque = bloque;
    }

    public String getNivel() {
        return  this.grado+" "+this.paralelo;
    }

    public void setNivel(String nivel){
        String[] parts = nivel.split(" ");
        this.grado = parts[0];
        this.paralelo = parts[1];
    }
}