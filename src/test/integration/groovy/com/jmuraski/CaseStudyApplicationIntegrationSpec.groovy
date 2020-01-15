package com.jmuraski

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
@SpringBootTest(classes = CaseStudyApplication)
@ActiveProfiles("test")
class CaseStudyApplicationIntegrationSpec extends Specification {

    @Autowired
    ApplicationContext context

    def "Validate Context"() {
        expect:
        context != null
    }
}
