package com.kajoyrest.test;
import java.util.List;

import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
class CursoController {
    private final CursoRepository repository;

    public CursoController(CursoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cursos")
    List<Curso> all() {
        return repository.findAll();
    }

    @PostMapping("/cursos")
    Curso newCurso(@RequestBody Curso newCurso) {
        return repository.save(newCurso);
    }

    @GetMapping("/cursos/{id}")
    Resource<Curso> one(@PathVariable Long id) {

        Curso curso = repository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));

        return new Resource<>(curso,
                linkTo(methodOn(CursoController.class).one(id)).withSelfRel(),
                linkTo(methodOn(CursoController.class).all()).withRel("cursos"));
    }

    @PutMapping("/employees/{id}")
    Curso replaceEmployee(@RequestBody Curso newCurso, @PathVariable Long id) {

        return repository.findById(id)
                .map(curso -> {
                    curso.setGrado(newCurso.getGrado());
                    curso.setParalelo(newCurso.getParalelo());
                    return repository.save(curso);
                })
                .orElseGet(() -> {
                    newCurso.setId(id);
                    return repository.save(newCurso);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}