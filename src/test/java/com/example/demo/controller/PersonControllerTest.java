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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

/**
 * NOTE:
 * <p>
 * For REST controllers, you really want to use the MockMvc class to emulate HTTP requests.
 * For the goals of this exercise, we're satisfied making direct calls to the controllers.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonController.class)
public class PersonControllerTest {

    // Spring will autowire the controller for us
    @Autowired
    PersonController personController;

    // The controller talks to the service, so it needs to be mocked
    @MockBean
    private PersonService personService;

    // a sample person for reference
    private Person frankSinatra;

    // another sample person
    private Person deanMartin;

    @Before
    public void setUp() {
        // Since the PersonController's constructor needs the PersonService, do this
        personController = new PersonController(personService);

        // Create some sample data
        frankSinatra = new Person();
        frankSinatra.setId(1L);
        frankSinatra.setFirstName("Frank");
        frankSinatra.setLastName("Sinatra");
        frankSinatra.setRank(8);

        // And more sample data
        deanMartin = new Person();
        deanMartin.setId(2L);
        deanMartin.setFirstName("Dean");
        deanMartin.setLastName("Martin");
        deanMartin.setRank(9);
    }


    @Test
    public void getAll_whenWellFormedRequest_expectAllPersonsAreReturned() {
        // Set up mocked data
        List<Person> rs = new LinkedList<>();
        rs.add(frankSinatra);
        rs.add(deanMartin);

        when(personService.findAll()).thenReturn(rs);

        // Invoke the Controller
        ResponseEntity<List<Person>> response = personController.getAll();

        List<Person> data = response.getBody();
        data.forEach(x -> System.out.println(x));

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().contains(frankSinatra));
        assertTrue(response.getBody().contains(deanMartin));
    }

    @Test
    public void testGetOne() {

        when(personService.findPersonById(1L)).thenReturn(Optional.of(frankSinatra));
        when(personService.findPersonById(2L)).thenReturn(Optional.of(deanMartin));

        ResponseEntity<Person> response = personController.get(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void whenSavingExistingPerson_expectUpdatedPersonEntityIsReturned() {

        // Sample data
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Frank");
        person.setLastName("Sinatra");
        person.setRank(8);

        // Mock the response the com.example.demo.service needs to send to the com.example.demo.controller.
        // Since this is a unit test of the com.example.demo.controller, the com.example.demo.controller's dependencies need to be mocked.
        // This is a bit of a white-box test. We know the com.example.demo.controller calls the com.example.demo.service's findByPersonId method
        // and its saveOrUpdate method, so we have to mock a response for both of those method calls.
        when(personService.findPersonById(1L)).thenReturn(Optional.of(person));
        when(personService.saveOrUpdate(person)).thenReturn(person);


        ResponseEntity<?> response = personController.save(person.getId(), person);

        // todo: extract Person from response body if status == 200

        assertThat("Expected a 200 status code", response.getStatusCode(), is(HttpStatus.OK));
        assertThat("Expected the updated person to be in the body", response.getBody(), notNullValue());

        // If we got at 200 status code, then the Response body must be a Person
        assertThat("", response.getBody(), instanceOf(Person.class));

        // Now, let's confirm the person's ID was updated
        Person result = (Person)response.getBody();
        assertThat ("Expected the rank to be updated to '8'", result.getRank(), is(8));
    }


    /**
     * Scenario:  When making the REST call:
     *      PUT /person/-55
     *      '{ "id" : "-55", "firstName" : "Mel", "lastName" : "Gibson", "rank" : 4 }'
     *
     * Several possible problems come to mind:
     *      1) Should an ID be constrained to positive values?
     *      2) Are we OK with the PUT operation adding a new Person (per the spec, the choice is ours),
     *         or should we expect the PUT to only send existing Persons
     *      3) If we -do- allow the PUT to add a new Person, do we want the client assigning the Id or
     *         should the back-end do that? In this example, the database is auto-generating as needed,
     *         but the database will allow us to assign the ID ourselves, so we have to think about those
     *         semantics.
     */
    @Test
    public void whenSavingAndPersonIdIsInvalid_expectBadRequest()
    {
        // todo: Implement me
    }

    /**
     * Scenario: When making the REST call:
     *     PUT /person/1
     *
     * We expect the request body to contain the Person object to be updated.
     * If that Person object isn't found, we should return a BadRequest;
     */
    @Test
    public void whenSavingAndPersonIsMissingFromRequestBody_expectBadRequest()
    {
        // todo: implement me
    }

    /**
     * Scenario: When making the REST call:
     *     PUT /person/1
     *     '{ "id" : 5, "firstName" : "Elton", "lastName" : "John", etc..}'
     *
     * The personId in the query string doesn't match the personId found in the Person object
     * that's in the request body, so  a BadRequest should be returned.
     */
    @Test
    public void whenSavingAndPersonIdInQueryStringDoesNotMatchPersonIdInBody_expectBadRequest()
    {
        // todo: implement me
    }


    /**
     * Scenario: When making the REST call:
     *     curl -x PUT http://localhost:8080/person/5
     *       -d '{ "id" : 5, "firstName" : "", "lastName" : "John", etc..}'
     *
     * Let's say we have the constraint that firstName cannot be null nor an empty string.
     * If a request is sent where the firstName is null/empty, we want to return BadRequest.
     */
    @Test
    public void whenSavingAndFirstNameIsBlank_expectBadRequest() {
        // todo: implement me
    }

    /**
     * Scenario: When making the REST call:
     *     PUT /person/5
     *     '{ "id" : 5, "firstName" : "Elton", "lastName" : "", etc..}'
     *
     * Let's say we have the constraint that lastName cannot be null nor an empty string.
     * If a request is sent where the lastName is null/empty, we want to return BadRequest.
     */
    @Test
    public void whenSavingAndLastNameIsBlank_expectBadRequest() {
        // todo: implement me
    }

    /**
     * Scenario: Since we're sending JSON data, we should also be sending the
     * header, Content-Type: application/json.
     *
     * If we send a different header, what do we want to happen? For example,
     * if the client (e.g., browser) sends: Content-Type: application/xml,
     * what should we do? What will Spring support?
     */
    @Test
    public void wheSavingIfContentTypeHeaderIsNotApplicationJson_expectWhatBehavior() {
        // todo: implement me
    }
}


