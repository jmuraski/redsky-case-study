package com.jmuraski.service

import com.jmuraski.entity.Item
import com.jmuraski.entity.Price
import com.jmuraski.util.ApplicationConstants
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import java.util.concurrent.CompletableFuture

@Service
class RedskyRestService {

    @Value('${redsky.host}')
    String redskyHost

    @Value('${redsky.version}')
    String redskyVersion

    @Value('${redsky.endpoint}')
    String redskyEndpoint

    @Autowired
    RestTemplate restTemplate

    @Async(ApplicationConstants.ITEM_TASK_EXECUTOR)
    CompletableFuture<Item> fetchById(String id) {
        String excludedSections = "taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"
        String redSkyUrl = "https://${redskyHost}/${redskyVersion}${redskyEndpoint}/${id}?excludes=" + excludedSections
        JsonSlurper parser = new JsonSlurper()

        ResponseEntity response = restTemplate.getForEntity(redSkyUrl, String.class)

        def parsedResponse = parser.parseText(response.getBody())

        Price price = new Price(value:  parsedResponse.product.price.listPrice.price, currencyCode: "USD")
        Item item = new Item(id: id, name: parsedResponse.product.item.product_description.title, currentPrice: price)

        return CompletableFuture.completedFuture(item)
    }
}
