package com.kajoyrest.test;

class CursoNotFoundException extends  RuntimeException{
    CursoNotFoundException(Long id){
        super("No se pudo encontrar el curso "+id);
    }
}