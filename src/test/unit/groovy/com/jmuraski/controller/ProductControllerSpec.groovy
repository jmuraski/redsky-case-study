package com.jmuraski.controller

import com.jmuraski.entity.Item
import com.jmuraski.entity.Price
import com.jmuraski.service.ItemService
import com.jmuraski.service.RedskyRestService
import com.jmuraski.util.ConstraintException
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import java.util.concurrent.CompletableFuture

class ProductControllerSpec extends Specification {

    RedskyRestService mockRedskyRestService = Mock(RedskyRestService)
    ItemService mockItemService = Mock(ItemService)
    MessageSource mockMessageSource = Mock(MessageSource)

    ProductController controller = new ProductController(redskyRestService: mockRedskyRestService,
            itemService: mockItemService, messageSource: mockMessageSource)

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
        0 * _
    }

    def "updateProduct"() {
        given:
        redskyItem.id = expectedId

        when:
        Item result = controller.updateProduct(expectedId, redskyItem)

        then:
        result.is(redskyItem)
        1 * mockItemService.saveItem(redskyItem)
        0 * _
    }

    def "updateProduct ids do not match"() {
        when:
        Item result = controller.updateProduct(expectedId, redskyItem)

        then:
        ConstraintException ex = thrown()
        ex.message == 'item.id.notmatching'
    }

    def "handleConstraintViolation works with Constraint Exception"() {
        when:
        ResponseEntity<Map> responseEntity = controller.handleConstraintViolation(new ConstraintException("FOOO"))

        then:
        responseEntity.statusCode == HttpStatus.BAD_REQUEST
        responseEntity.body.errors[0] == "BINGO"
        1 * mockMessageSource.getMessage("FOOO", null, Locale.ENGLISH) >> "BINGO"
        0 * _
    }

    def "handleConstratinViolation works with Violation"() {
        given:
        ConstraintViolationException ex = Mock(ConstraintViolationException)
        Set violations = [{message: "FOOO"} as ConstraintViolation<Map>]

        when:
        ResponseEntity<Map> responseEntity = controller.handleConstraintViolation(ex)

        then:
        responseEntity.statusCode == HttpStatus.BAD_REQUEST
        responseEntity.body.errors[0] == "BINGO"
        1 * ex.constraintViolations >> violations
        1 * mockMessageSource.getMessage("FOOO", null, Locale.ENGLISH) >> "BINGO"
        0 * _
    }
}
