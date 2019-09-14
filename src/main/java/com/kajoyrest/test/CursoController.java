package com.kajoyrest.test;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
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

    private final  CursoResourceAssembler assembler;

    public CursoController(CursoRepository repository, CursoResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*@GetMapping("/cursos")
    List<Curso> all() {
        return repository.findAll();
    }*/

    @GetMapping("/cursos")
    Resources<Resource<Curso>> all() {

        List<Resource<Curso>> cursos = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(cursos,
                linkTo(methodOn(CursoController.class).all()).withSelfRel());
    }

    @PostMapping("/cursos")
    ResponseEntity<?> newEmployee(@RequestBody Curso newCurso) throws URISyntaxException {

        Resource<Curso> resource = assembler.toResource(repository.save(newCurso));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/cursos/{id}")
    Resource<Curso> one(@PathVariable Long id) {

        Curso curso = repository.findById(id)
                .orElseThrow(() -> new CursoNotFoundException(id));

        /*return new Resource<>(curso,
                linkTo(methodOn(CursoController.class).one(id)).withSelfRel(),
                linkTo(methodOn(CursoController.class).all()).withRel("cursos"));*/

        return  assembler.toResource(curso);
    }

    @PutMapping("/cursos/{id}")
    ResponseEntity<?> replaceCurso(@RequestBody Curso newCurso, @PathVariable Long id) throws URISyntaxException {

        Curso updatedCurso = repository.findById(id)
                .map(curso -> {
                    curso.setGrado(newCurso.getGrado());
                    curso.setParalelo(newCurso.getParalelo());
                    curso.setBloque(newCurso.getBloque());
                    return repository.save(curso);
                })
                .orElseGet(() -> {
                    newCurso.setId(id);
                    return repository.save(newCurso);
                });

        Resource<Curso> resource = assembler.toResource(updatedCurso);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/cursos/{id}")
    void deleteCurso(@PathVariable Long id) {
        repository.deleteById(id);
    }

}