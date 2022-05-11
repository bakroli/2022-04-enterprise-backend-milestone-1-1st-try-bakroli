package com.codecool.exams.supercraftsman;

import com.codecool.exams.supercraftsman.testmodels.Craftsman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CraftsmanIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @Autowired
    ApplicationContext context;

    private static Craftsman[] CRAFTSMANS;
    private String url;

    @BeforeEach
    void setup() {
        CRAFTSMANS = new Craftsman[]{
                new Craftsman(null, "Peter", "555-(12) 3000-78910-11-12", "petedoe@gmail.com"),
                new Craftsman(null, "Sarah", "555-6444", "stdoe@gmail.com")
        };
        url = "http://localhost:" + port + "/craftsman";
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
    void givenEmptyDatabaseWhenGetCraftsManRetrievedThenReturnsEmptyJsonList() {
        final String result = restTemplate.getForObject(url, String.class);
        assertEquals("[]", result);
    }

    @Test
    void givenNewCraftsmenPostedWhenCraftsmanPostedThenReturnsWithCreatedId() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);

        postCraftsman(url, CRAFTSMANS[0]);
        assertNotNull(CRAFTSMANS[0].getId());
    }

    @Test
    void runInvalidTestsTogetherToLessenEvaluationScore() {
        givenNewInvalidCraftsmenPostedWhenGetCraftsmanPostedThenReturnsWithBadRequest();
        givenNewInvalidCraftsmenEmptyNamePostedWhenGetCraftsmanPostedThenReturnsWithBadRequest();
        givenNewInvalidCraftsmenFakeNumberPostedWhenGetCraftsmanPostedThenReturnsWithBadRequest();
        givenNewInvalidCraftsmenBadEmailPostedWhenGetCraftsmanPostedThenReturnsWithBadRequest();
        givenNewCraftsmenPostedWhenCraftsmanUpdatedWithInvalidValueThenCraftsManRetrievedThenReturnsBadRequest();
    }

    void givenNewInvalidCraftsmenPostedWhenGetCraftsmanPostedThenReturnsWithBadRequest() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);
        final Craftsman badCraftsman = new Craftsman(null, "Bad Guy", "FAKE NUMBER", "NOT AN EMAIL");
        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(badCraftsman);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    void givenNewInvalidCraftsmenEmptyNamePostedWhenGetCraftsmanPostedThenReturnsWithBadRequest() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);
        final Craftsman badCraftsman = new Craftsman(null, "", "+36(70) 159 456", "noresponse@gmail.com");
        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(badCraftsman);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    void givenNewInvalidCraftsmenFakeNumberPostedWhenGetCraftsmanPostedThenReturnsWithBadRequest() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);
        final Craftsman badCraftsman = new Craftsman(null, "Bad Guy", "FAKE NUMBERZ", "abcnews@bbc.co.uk");
        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(badCraftsman);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    void givenNewInvalidCraftsmenBadEmailPostedWhenGetCraftsmanPostedThenReturnsWithBadRequest() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);
        final Craftsman badCraftsman = new Craftsman(null, "Bad Guuuuy", "555-159-753", "abcdefnewsatbbc.co.uk");
        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(badCraftsman);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    void givenNewCraftsmenPostedWhenCraftsManRetrievedThenReturnsContent() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);

        postCraftsman(url, CRAFTSMANS[0]);

        final ResponseEntity<Craftsman[]> response = restTemplate.getForEntity(url, Craftsman[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Craftsman[] craftsmen = response.getBody();
        assertEquals(1, craftsmen.length);
        assertEquals(CRAFTSMANS[0], craftsmen[0]);
    }

    @Test
    void givenNewCraftsmenPostedWhenCraftsmanUpdatedThenCraftsManRetrievedThenReturnsUpdatedContent() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);

        final Craftsman craftsman = CRAFTSMANS[0];
        postCraftsman(url, craftsman);

        craftsman.setName("Changed Named John Depp");

        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(craftsman);
        restTemplate.put(url + "/" + craftsman.getId(), httpEntity);

        final Craftsman recievedCraftsman = restTemplate.getForObject(url + "/" + craftsman.getId(), Craftsman.class);
        assertEquals(craftsman, recievedCraftsman);
    }

    void givenNewCraftsmenPostedWhenCraftsmanUpdatedWithInvalidValueThenCraftsManRetrievedThenReturnsBadRequest() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);

        final Craftsman craftsman = CRAFTSMANS[0];
        postCraftsman(url, craftsman);

        craftsman.setName("");

        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(craftsman);
        final String urlToJohn = url + "/" + craftsman.getId();
        final ResponseEntity<Object> putResponse = restTemplate.exchange(urlToJohn, HttpMethod.PUT, httpEntity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, putResponse.getStatusCode());
    }

    private HttpEntity<Craftsman> createHttpEntityWithMediatypeJson(Craftsman craftsman) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(craftsman, headers);
    }

    @Test
    void givenTwoNewCraftsmenPostedWhenGetCraftsManRetrievedThenReturnsContent() {
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);


        postCraftsman(url, CRAFTSMANS[0]);
        postCraftsman(url, CRAFTSMANS[1]);

        final ResponseEntity<Craftsman[]> response = restTemplate.getForEntity(url, Craftsman[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Craftsman[] craftsmen = response.getBody();
        assertEquals(2, craftsmen.length);
        assertEquals(CRAFTSMANS[0], craftsmen[0]);
        assertEquals(CRAFTSMANS[1], craftsmen[1]);
    }

    void postCraftsman(String url, Craftsman c) {
        final HttpEntity<Craftsman> httpEntity = createHttpEntityWithMediatypeJson(c);
        final ResponseEntity<Long> postResponse = restTemplate.postForEntity(url, httpEntity, Long.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        c.setId(postResponse.getBody());
    }

    @Test
    void givenNewCraftsmenPostedThenDeletedWhenGetCraftsManRetrievedThenReturnsEmpty() {
        //Given
        final Craftsman[] result = restTemplate.getForObject(url, Craftsman[].class);
        assertEquals(0, result.length);

        postCraftsman(url, CRAFTSMANS[0]);

        //When
        restTemplate.delete(url + "/" + CRAFTSMANS[0].getId());

        //Then
        final ResponseEntity<Craftsman[]> response = restTemplate.getForEntity(url, Craftsman[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final Craftsman[] craftsmen = response.getBody();
        assertEquals(0, craftsmen.length);
    }
}
