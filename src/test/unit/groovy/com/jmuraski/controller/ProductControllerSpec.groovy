package com.jmuraski.controller

import com.jmuraski.entity.Item
import com.jmuraski.entity.Price
import com.jmuraski.service.ItemService
import com.jmuraski.service.RedskyRestService
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class ProductControllerSpec extends Specification {

    RedskyRestService mockRedskyRestService = Mock(RedskyRestService)
    ItemService mockItemService = Mock(ItemService)

    ProductController controller = new ProductController(redskyRestService: mockRedskyRestService, itemService: mockItemService)

    String expectedId = '1111'
    Item redskyItem = new Item(currentPrice: new Price())
    Item redisItem = new Item(currentPrice: new Price())

    def "getProduct with stored value"() {
        when:
        Item result = controller.getProduct(expectedId)

        then:
        result.is(redskyItem)
        result.currentPrice.is(redisItem.currentPrice)
        1 * mockRedskyRestService.fetchById(expectedId) >> CompletableFuture.completedFuture(redskyItem)
        1 * mockItemService.fetchById(expectedId) >> CompletableFuture.completedFuture(Optional.of(redisItem))
        0 * _
    }

    def "getProduct without stored value"() {
        given:
        Price originalPrice = redskyItem.currentPrice

        when:
        Item result = controller.getProduct(expectedId)

        then:
        result.is(redskyItem)
        result.currentPrice.is(originalPrice)
        1 * mockRedskyRestService.fetchById(expectedId) >> CompletableFuture.completedFuture(redskyItem)
        1 * mockItemService.fetchById(expectedId) >> CompletableFuture.completedFuture(Optional.empty())
        1 * mockItemService.saveItem(redskyItem)
        0 * _
    }

    def "updateProduct"() {
        when:
        Item result = controller.updateProduct(expectedId, redskyItem)

        then:
        result.is(redskyItem)
        1 * mockItemService.saveItem(redskyItem)
        0 * _
    }
}
