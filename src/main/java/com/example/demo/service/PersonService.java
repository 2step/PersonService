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
package com.example.demo.service;

import com.example.demo.persistence.model.Person;
import com.example.demo.persistence.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * We could do @Component, but since we know this is a Service, we opt to be precise.
 *
 */
@Service
@Transactional
public class PersonService {

    private PersonRepository repository;


    /**
     * Its best practice to define a constructor that accepts the items to be auto-wired.
     * The reason is, the constructor's give Spring a hint as to which items need to be initialized
     * first. For example, since this Service takes a Repository in the constructor, Spring knows
     * the Repository has to be initialized first, then the Service.
     *
     * Its also valid to put @Autowired on the field definition of repository; the technique shown here
     * eliminates the aforementioned "race conditions".
     *
     * @param repository the database
     */
    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }


    // TODO: look up the annotation to mark this as read-only
    public List<Person> findAll() {
        return repository.findAll();
    }

    // TODO: read-only transaction
    public Optional<Person> findPersonById(Long id) {
        return repository.findById(id);
    }

    /**
     * Updates the given Person.  Returns true on success; f no such Person is found, returns false.
     *
     * @param person the Person entity to update
     *
     * @return the updated Person
     */
    public Person saveOrUpdate(Person person)
    {
        return repository.save(person);
    }
}
