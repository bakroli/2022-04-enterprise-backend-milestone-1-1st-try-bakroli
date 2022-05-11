package com.codecool.exams.supercraftsman;

import com.codecool.exams.supercraftsman.testmodels.BaseEntity;
import com.codecool.exams.supercraftsman.testmodels.Craftsman;
import com.codecool.exams.supercraftsman.testmodels.Expertise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ExpertiseIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @Autowired
    ApplicationContext context;

    private static Expertise[] EXPERTISE;
    private String url;
    private String baseUrl;

    @BeforeEach
    void setup() {
        EXPERTISE = new Expertise[]{
                new Expertise(null, "Flooring"),
                new Expertise(null, "Roofing"),
                new Expertise(null, "Electrical repairs"),
                new Expertise(null, "Carpentry"),
                new Expertise(null, "Dry wall hanging"),
                new Expertise(null, "Plumbing")
        };
        url = "http://localhost:" + port + "/expertise";
        baseUrl = "http://localhost:" + port;
        resetRepos();
    }
    private void resetRepos() {
        final Map<String, Object> repos = context.getBeansWithAnnotation(Repository.class);
        repos.values().forEach(o -> {
            try {
                final Method clear = o.getClass().getMethod("clear", (Class<?>[]) null);
                clear.invoke(o);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void givenEmptyDatabaseWhenExpertiseRetrievedThenReturnsEmptyList() {
        final String result = restTemplate.getForObject(url, String.class);
        assertEquals("[]", result);
    }

    @Test
    void whenExpertisePostedThenReturnsWithCreatedId() {
        final Expertise[] result = restTemplate.getForObject(url, Expertise[].class);
        assertEquals(0, result.length);

        postExperties(url, EXPERTISE[0]);
        assertNotNull(EXPERTISE[0].getId());
    }


    @Test
    void givenNewExpertisePostedWhenExpertiseRetrievedThenReturnsContent() {
        final Expertise[] result = restTemplate.getForObject(url, Expertise[].class);
        assertEquals(0, result.length);

        postExperties(url, EXPERTISE[0]);

        final ResponseEntity<Expertise[]> response = restTemplate.getForEntity(url, Expertise[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Expertise[] expertise = response.getBody();
        assertEquals(1, expertise.length);
        assertNotNull(expertise[0].getId());
        assertEquals(EXPERTISE[0], expertise[0]);
    }

    @Test
    void givenNewExpertisePostedWhenUpdatedThenExpertiseRetrievedThenReturnsUpdatedContent() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);

        final Expertise expertise = EXPERTISE[0];
        postExperties(url, expertise);

        expertise.setName("Oil rig drilling");

        var httpEntity = createHttpEntityWithMediaTypeJson(expertise);
        restTemplate.put(url + "/" + expertise.getId(), httpEntity);

        final Expertise recieved = restTemplate.getForObject(url + "/" + expertise.getId(), Expertise.class);
        assertEquals(expertise, recieved);
    }

    @Test
    void runInvalidTestsTogetherToLessenEvaluationScore() {
        givenNewExpertisePostedWhenPostedWithInvalidValueThenReturnsBadRequest();
        givenNewExpertisePostedWhenUpdatedWithInvalidValueThenReturnsBadRequest();
    }

    void givenNewExpertisePostedWhenPostedWithInvalidValueThenReturnsBadRequest() {
        final Expertise[] result = restTemplate.getForObject(url, Expertise[].class);
        assertEquals(0, result.length);

        final Expertise expertise = EXPERTISE[0];
        expertise.setName("");

        var httpEntity = createHttpEntityWithMediaTypeJson(expertise);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    void givenNewExpertisePostedWhenUpdatedWithInvalidValueThenReturnsBadRequest() {
        final Expertise[] result = restTemplate.getForObject(url, Expertise[].class);
        assertEquals(0, result.length);

        final Expertise expertise = EXPERTISE[1];
        postExperties(url, expertise);

        expertise.setName("");

        var httpEntity = createHttpEntityWithMediaTypeJson(expertise);
        final String urlToJohn = url + "/" + expertise.getId();
        final ResponseEntity<Object> putResponse = restTemplate.exchange(urlToJohn, HttpMethod.PUT, httpEntity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, putResponse.getStatusCode());
    }

    private HttpEntity<BaseEntity> createHttpEntityWithMediaTypeJson(BaseEntity e) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(e, headers);
    }

    private void postExperties(String url, Expertise e) {
        var httpEntity = createHttpEntityWithMediaTypeJson(e);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        e.setId(postResponse.getBody());
    }

    @Test
    void givenNewExpertisePostedThenDeletedWhenExpertiseRetrievedThenReturnsEmpty() {
        //Given
        postExperties(url, EXPERTISE[0]);

        //When
        restTemplate.delete(url + "/" + EXPERTISE[0].getId());

        //Then
        final ResponseEntity<Expertise[]> response = restTemplate.getForEntity(url, Expertise[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Expertise[] craftsmen = response.getBody();
        assertEquals(0, craftsmen.length);
    }


    @Test
    void givenExpertiseAndCraftsManInDBWhenPostedExpertiseToCraftsManThenReturnOK() {
        postExperties(url, EXPERTISE[0]);
        postExperties(url, EXPERTISE[1]);
        postExperties(url, EXPERTISE[2]);
        final Craftsman john_hurt = new Craftsman(null, "John Hurt", "1234", "jh@gmail.com");
        postCraftsman(baseUrl + "/craftsman", john_hurt);

        var e1 = EXPERTISE[0];
        var e2 = EXPERTISE[1];
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var postResponse = restTemplate.postForEntity(
                url + "/" + EXPERTISE[0].getId() + "/" + john_hurt.getId(),
                new HttpEntity<>(e1, headers),
                Long.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    void givenExpertiseAndCraftsManInDBWhenPostedExpertiseToCraftsManThenReturnWhenQueried() {
        //Given
        postExperties(url, EXPERTISE[0]);
        postExperties(url, EXPERTISE[1]);
        postExperties(url, EXPERTISE[2]);
        final Craftsman john_hurt = new Craftsman(null, "John Hurt", "1234", "jh@gmail.com");
        postCraftsman(baseUrl + "/craftsman", john_hurt);

        // When posted
        var e1 = EXPERTISE[0];
        var e2 = EXPERTISE[1];
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var postResponse = restTemplate.postForEntity(
                url + "/" + e1.getId() + "/" + john_hurt.getId(),
                new HttpEntity<>(e1, headers),
                Long.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        // Then
        final Craftsman[] craftsmen = restTemplate.getForObject(url + "/" + e1.getId() + "/craftsmen", Craftsman[].class);
        assertEquals(1, craftsmen.length);
        assertEquals(john_hurt, craftsmen[0]);
    }

    @Test
    void givenExpertiseAndCraftsManInDBWhenPostedExpertiseToCraftsManThenReturnWhenQueriedFromCraftsmanSide() {
        //Given
        postExperties(url, EXPERTISE[0]);
        postExperties(url, EXPERTISE[1]);
        postExperties(url, EXPERTISE[2]);
        final Craftsman john_hurt = new Craftsman(null, "John Hurt", "1234", "jh@gmail.com");
        postCraftsman(baseUrl + "/craftsman", john_hurt);

        // When posted
        var e1 = EXPERTISE[0];
        var e2 = EXPERTISE[1];
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var postResponse = restTemplate.postForEntity(
                url + "/" + e1.getId() + "/" + john_hurt.getId(),
                new HttpEntity<>(e1, headers),
                Long.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        // Then
        final Expertise[] expertise = restTemplate.getForObject(baseUrl + "/craftsman/" + john_hurt.getId() + "/expertise", Expertise[].class);
        assertEquals(1, expertise.length);
        assertEquals(e1, expertise[0]);
    }

    void postCraftsman(String url, Craftsman c) {
        var httpEntity = createHttpEntityWithMediaTypeJson(c);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        c.setId(postResponse.getBody());
    }
}
