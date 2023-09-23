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
    private HttpClient client

    def "Health endpoint is available"() {
        given: "The application is running"
        when: "a request is made to the health endpoint"
            HttpResponse response = client.toBlocking().exchange("http://localhost:8080/health", String)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct message is returned"
            Assertions.assertEquals("Service is healthy!", response.body())
    }
}