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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Fill me in
 */
@Service
@Transactional
public class PersonService {

    public Set<Person> findAll() {
        return new TreeSet<>();
    }

    public Optional<Person> findPersonById(Long id) {
        return Optional.empty();
    }

    /**
     * Updates the given Person.  Returns true on success; f no such Person is found, returns false.
     * @param person the Person entity to update
     * @return true if successful, false otherwise
     */
    public boolean updatePerson(Person person) {
        return true;
    }
    
}
