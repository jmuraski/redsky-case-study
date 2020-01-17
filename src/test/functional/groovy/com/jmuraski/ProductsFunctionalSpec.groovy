package com.jmuraski

import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder
import spock.lang.Specification

class ProductsFunctionalSpec extends Specification{

    HttpBuilder http

    def setup() {
        http = HttpBuilder.configure {
            request.uri = 'http://localhost:8080'
        }
    }

    def "products returns item data in correct format"() {
        when:
        Map data
        FromServer serverResponse
        http.get {
            request.uri.path = '/products/13860428'
            response.success { FromServer fs, Object body ->
                serverResponse = fs
                data = body
            }
        }

        then:
        serverResponse.statusCode == 200
        data.id == '13860428'
        data.name == 'The Big Lebowski (Blu-ray)'
        data.current_price.value != null
        data.current_price.currency_code == 'USD'

    }

    def "product price is updated on put"() {
        given:
        Map data
        FromServer serverResponse
        String initialPrice

        when:
        http.get {
            request.uri.path = '/products/13860428'
            response.success { FromServer fs, Object body ->
                serverResponse = fs
                data = body
                initialPrice = body.current_price.value
            }
        }

        then:
        serverResponse.statusCode == 200

        when:
        serverResponse = null
        data = null
        BigDecimal newPrice = randomDollarAmount()
        http.put{
            request.uri.path = '/products/13860428'
            request.contentType = 'application/json'
            request.body = [id: "13860428", name: "foo", current_price: [value: "${newPrice}", currency_code: "USD"]]
            response.success {FromServer fs, Object body ->
                serverResponse = fs
            }
        }

        then:
        serverResponse.statusCode == 200
        newPrice != initialPrice

        when:
        serverResponse = null
        data = null
        http.get {
            request.uri.path = '/products/13860428'
            response.success { FromServer fs, Object body ->
                serverResponse = fs
                data = body
            }
        }

        then:
        serverResponse.statusCode == 200
        data.id == '13860428'
        data.name == 'The Big Lebowski (Blu-ray)'
        data.current_price.value == "${newPrice}"
        data.current_price.currency_code == 'USD'
    }

    BigDecimal randomDollarAmount(){
        Random r = new Random();
        return (r.nextInt((10000 - 1000) + 1) + 1000)/100
    }

    def "products get returns messages for invalid data"(){
        when:
        Map data
        FromServer serverResponse
        http.get {
            request.uri.path = '/products/2xase23412'
            response.failure {FromServer fs, Object body ->
                serverResponse = fs
                data = body
            }
        }

        then:
        serverResponse.statusCode == 400
        data.errors[0] == "ID must be numeric"
    }

    def "products update does not allow for ids not matching"() {
        when:
        FromServer serverResponse
        Map data
        http.put{
            request.uri.path = '/products/13860444'
            request.contentType = 'application/json'
            request.body = [id: "13860428", name: "foo", current_price: [value: "12.12", currency_code: "USD"]]
            response.failure {FromServer fs, Object body ->
                serverResponse = fs
                data = body
            }
        }

        then:
        serverResponse.statusCode == 400
        data.errors[0] == "ID on path must match ID in JSON"
    }

}
