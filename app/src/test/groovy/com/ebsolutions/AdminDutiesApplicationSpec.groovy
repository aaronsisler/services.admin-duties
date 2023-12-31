package com.ebsolutions

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class AdminDutiesApplicationSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void 'Application is running correctly'() {
        expect:
            application.running
    }
}
