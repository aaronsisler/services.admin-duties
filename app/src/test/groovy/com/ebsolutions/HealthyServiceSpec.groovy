package com.ebsolutions


import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import spock.lang.Specification

@MicronautTest
class HealthyServiceSpec extends Specification {
    @Inject
    private HttpClient client;

    def "health check"() {
        when: "a request is made to the health endpoint"
            HttpResponse response = client.toBlocking().exchange("http://localhost:8080/health", String);

        then: "the correct status code is returned"
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_OK, response.code());

        and: "the correct body is returned"
            Assertions.assertEquals("Service is healthy!", response.body());
    }
}