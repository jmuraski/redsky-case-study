package com.jmuraski.service

import com.jmuraski.entity.Item
import com.jmuraski.repository.ItemRepository
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class ItemServiceSpec extends Specification {

    ItemRepository mockItemRepository = Mock(ItemRepository)
    ItemService service = new ItemService(itemRepository: mockItemRepository)
    Item expectedItem = new Item()

    def "fetchById"() {
        given:
        String expectedId = '1111'
        Optional<Item> expectedOptional = Optional.of(expectedItem)

        when:
        CompletableFuture<Optional<Item>> result = service.fetchById(expectedId)

        then:
        result.get().is(expectedOptional)
        1 * mockItemRepository.findById(expectedId) >> expectedOptional
        0 * _
    }

    def "saveItem"() {
        when:
        service.saveItem(expectedItem)

        then:
        1 * mockItemRepository.save(expectedItem)
        0 * _
    }
}
