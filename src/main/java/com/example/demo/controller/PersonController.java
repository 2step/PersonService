/*
 * Copyright 2018 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.controller;

import com.example.demo.persistence.model.Person;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Fill me in
 */
@RestController
@RequestMapping(value="/person")
public class PersonController {
    
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService service) {
      personService = service;
    }

    @GetMapping(value="/")
    public ResponseEntity<Set<Person>> getAll() {
        Set<Person> resultSet = personService.findAll();
        return ResponseEntity.ok(resultSet);
    }


    @GetMapping(value="/{id}")
    public ResponseEntity<Person> get(@PathVariable final Long id)
    {
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody final Person person)
    {
        return ResponseEntity.ok().build();
    }



}
