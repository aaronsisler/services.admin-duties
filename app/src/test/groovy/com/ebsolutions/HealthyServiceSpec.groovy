package com.ebsolutions

import com.ebsolutions.config.TestConstants
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import spock.lang.Specification

@MicronautTest
class HealthyServiceSpec extends Specification {
    @Inject
    private HttpClient httpClient

    private String healthUrl = TestConstants.adminServiceUrl + "/health"


    def "Health endpoint is available"() {
        given: "The application is running"
        when: "a request is made to the health endpoint"
            HttpResponse response = httpClient.toBlocking().exchange(healthUrl, String)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct message is returned"
            Assertions.assertEquals("Service is healthy!", response.body())
    }
}