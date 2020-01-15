package com.jmuraski.service

import com.jmuraski.CaseStudyApplication
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(classes = CaseStudyApplication)
@ActiveProfiles("test")
class RedskyRestServiceIntegrationSpec extends Specification{

    @Autowired
    RedskyRestService service

    def "Validate Fetch by ID"() {
        given:
        String id = '78315118'

        when:
        def response = service.fetchById(id)

        then:
        response != null
        id == response.id
        response.name == "Abominable (Blu-Ray + DVD + Digital)"
        response.current_price.value == 24.99
        response.current_price.currency_code == "USD"
    }

}