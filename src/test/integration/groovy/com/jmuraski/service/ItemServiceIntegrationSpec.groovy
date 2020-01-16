package com.jmuraski.service

import com.jmuraski.CaseStudyApplication
import com.jmuraski.entity.Item
import com.jmuraski.entity.Price
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(classes = CaseStudyApplication)
@ActiveProfiles("test")
@Testcontainers
class ItemServiceIntegrationSpec extends Specification{

    @Autowired
    ItemService itemService

    @Value('${redis.host.port}')
    int redisPort

    @Value('${redis.host.name}')
    String redisHostName

    @Shared
    public GenericContainer redis = new FixedHostPortGenericContainer<>("redis:5.0.7")
            .withFixedExposedPort(40254, 6379)

    def "Item Service saves and loads"() {
        given:
        Price price = new Price(value: "12.12", currencyCode: "USD")
        Item item = new Item(id: randomId(), name: "MY NAME", currentPrice: price)
        println "I AM HERE"
        println "${redisHostName}:${redisPort}"

        when:
        Optional<Item> optionalItem = itemService.fetchById(item.id).get()

        then:
        ! optionalItem.isPresent()

        when:
        itemService.saveItem(item)
        optionalItem = itemService.fetchById(item.id).get()

        then:
        optionalItem.isPresent()
        optionalItem.get().id == item.id
        optionalItem.get().name == item.name
        optionalItem.get().currentPrice.value == item.currentPrice.value
        optionalItem.get().currentPrice.currencyCode == item.currentPrice.currencyCode

    }

    int randomId(){
        Random r = new Random();
        return r.nextInt((10000 - 1000) + 1) + 1000;
    }
}
