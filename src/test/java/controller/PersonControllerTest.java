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
package controller;

import com.example.demo.controller.PersonController;
import com.example.demo.persistence.model.Person;
import com.example.demo.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * NOTE:
 *
 * For REST controllers, you really want to use the MockMvc class to emulate HTTP requests.
 * For the goals of this exercise, we're satisfied making direct calls to the controllers.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonController.class)
public class PersonControllerTest {

    @MockBean
    private PersonService personService;

    @Autowired
    PersonController personController;

    private Person frankSinatra;

    private Person deanMartin;

    @Before
    public void setUp() {
        personController = new PersonController(personService);

        frankSinatra = new Person();
        frankSinatra.setId(1L);
        frankSinatra.setFirstName("Frank");
        frankSinatra.setLastName("Sinatra");
        frankSinatra.setRank(8);

        deanMartin = new Person();
        deanMartin.setId(2L);
        deanMartin.setFirstName("Dean");
        deanMartin.setLastName("Martin");
        deanMartin.setRank(9);
    }


    @Test
    public void testGetAll() {
        // Set up mocked data
        Set<Person> rs = new HashSet<>();
        rs.add(frankSinatra);
        rs.add(deanMartin);

        when(personService.findAll()).thenReturn(rs);

        // Invoke the Controller
        ResponseEntity<Set<Person>> response = personController.getAll();

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
    public void testUpdatePerson() {
        // Person person = new Person();
        // person.setId(1);
        // person.setFirstName("Frank");
        // person.setLastName("Sinatra");
        // person.setRank(8);
        // Person p = controller.get(1);

        // controller.updatePerson(person.getId(), person);

    }
}
