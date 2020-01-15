package com.jmuraski

import spock.lang.Specification

class SampleSpec extends Specification {

    def "Sample Spec"() {
        given:
        String a = "A"

        when:
        String b = "B"

        then:
        a != b
    }
}
