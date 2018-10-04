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

import java.util.*;

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
    public ResponseEntity<List<Person>> getAll() {
        List<Person> resultSet = personService.findAll();
        return ResponseEntity.ok(resultSet);
    }


    @GetMapping(value="/{id}")
    public ResponseEntity<Person> get(@PathVariable final Long id)
    {
        // todo: verify ID

        // Call the com.example.demo.service to look up the person
        Optional<Person> optionalPerson = personService.findPersonById(id);

        if (optionalPerson.isPresent()) {
            return ResponseEntity.ok(optionalPerson.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates a Person
     *
     * Update the given person. We choose PUT over POST because POST -always- causes a new
     * record (resource) to be added, while PUT works for both inserting/updating data.
     * The semantics of PUT are that its idempotent, so the same PUT request can be made again and
     * again, and still only the one Person resource is touched.
     *
     * Note: if a 409 (conflict) is sent back to the client, then the update semantics aren't working.
     * If we've never seen this Person before, they should be added.  If its an existing Person, the
     * existing Person is updated.  We only have the ID to uniquely identify the Person.
     *
     * @param id
     * @param person
     *
     * @return Http:OK if the person was successfully added/updated.
     *         Http:BadRequest if the Person cannot be updated
     */
    @PutMapping(value="/{id}")
    public ResponseEntity<?> save (@PathVariable final Long id, @RequestBody final Person person)
    {
        // Check Preconditions:

        // The ID in the query string must match the Person ID in the request body
        // TODO: an idea -- we could define an Error POJO and return that in the body. Then the client would
        // know to check for an Error POJO when an http error code was returned.
        if (!person.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Mismatch in the Id in the query string and the Id in the request body");
        }

        // Verify the Person exists in the DB
        Optional<Person> existingPerson = personService.findPersonById(id);
        if (!existingPerson.isPresent()) {
            return ResponseEntity.badRequest().body("No matching record found");
        }

        // Call Person com.example.demo.service to add/update
        try {
            Person updatedPerson = personService.saveOrUpdate(person);
            return ResponseEntity.ok(updatedPerson);
        }
        catch (Exception e) {
            // If we land here, a runtime exception was thrown
            // (since PersonService::saveOrUpdate doesn't throw any checked exceptions)
            // Rather than fall down, we'll return a BadRequest
            // TODO: Not really sure this is right strategy, so we'll see.
            return ResponseEntity.badRequest().build();
        }

    }



}
