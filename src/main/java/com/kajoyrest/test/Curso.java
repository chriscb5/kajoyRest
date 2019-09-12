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

    Curso() { }

    public Curso(String grado, String paralelo) {
        this.grado = grado;
        this.paralelo = paralelo;
    }
}