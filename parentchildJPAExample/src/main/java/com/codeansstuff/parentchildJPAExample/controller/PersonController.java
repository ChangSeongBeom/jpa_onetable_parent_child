package com.codeansstuff.parentchildJPAExample.controller;

import com.codeansstuff.parentchildJPAExample.dtos.NestedPersonDTO;
import com.codeansstuff.parentchildJPAExample.dtos.PersonDTO;
import com.codeansstuff.parentchildJPAExample.entity.Person;
import com.codeansstuff.parentchildJPAExample.repositories.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author MalkeithSingh on 11-09-2019
 */
@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    //private Function<Person, PersonDTO> mapToPersonDTO = this::convertToDTO;
    @Autowired
    private PersonRepo personRepo;
    private Function<Person, PersonDTO> mapToPersonDTO = this::convertToDTO;


    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getAllDetails(@PathVariable("id") Long id) {
        return personRepo.findById(id).map(mapToPersonDTO).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private PersonDTO convertToDTO(Person person) {
        PersonDTO personDTO = PersonDTO.builder()
                .id(person.getId())
                .fullName(person.getFullName())
                .build();

        if (person.getChildren() != null && !person.getChildren().isEmpty()) {
            Set<PersonDTO> childrenDTO = person.getChildren().stream()
                    .map(this::convertToDTO) // Convert each child to DTO recursively
                    .collect(Collectors.toSet());
            personDTO.setChildren(childrenDTO);
        }

        return personDTO;
    }





}
