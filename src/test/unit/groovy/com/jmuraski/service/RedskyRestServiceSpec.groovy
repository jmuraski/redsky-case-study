package com.jmuraski.service

import com.jmuraski.entity.Item
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class RedskyRestServiceSpec extends Specification {

    RestTemplate mockRestTemplate = Mock(RestTemplate)
    ResponseEntity mockResponseEntity = Mock(ResponseEntity)
    String redskyHost = "redskyhost"
    String redskyVersion = "version"
    String redskyEndpoint = "endpoint"
    String expectedName = "expected name"
    String expectedPrice = "12.12"

    String expectedBody = """
     {
        "product": {
            "price": {
                "listPrice": {
                    "price": "${expectedPrice}"
                }
            }, 
            "item": {
                "product_description": {
                    "title": "${expectedName}"
                }
            }
        }
     }
    """

    RedskyRestService service = new RedskyRestService(restTemplate: mockRestTemplate, redskyEndpoint: redskyEndpoint,
            redskyHost: redskyHost, redskyVersion: redskyVersion)

    def "fetchById"() {
        given:
        String expectedId = '1111'
        String expectedUrl = "https://${redskyHost}/${redskyVersion}${redskyEndpoint}/${expectedId}?excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"

        when:
        CompletableFuture<Item> result = service.fetchById(expectedId)
        Item item = result.get()

        then:
        item.id == expectedId
        item.name == expectedName
        item.currentPrice.currencyCode == "USD"
        item.currentPrice.value == expectedPrice
        1 * mockRestTemplate.getForEntity(expectedUrl, String.class) >> mockResponseEntity
        1 * mockResponseEntity.getBody() >> expectedBody
        0 * _
    }

}
