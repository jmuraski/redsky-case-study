package com.jmuraski.service

import com.jmuraski.entity.Item
import com.jmuraski.repository.ItemRepository
import com.jmuraski.util.ApplicationConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import java.util.concurrent.CompletableFuture

@Service
class ItemService {
    @Autowired
    ItemRepository itemRepository

    @Async(ApplicationConstants.ITEM_TASK_EXECUTOR)
    CompletableFuture<Optional<Item>> fetchById(String id) {
        Optional<Item> item = itemRepository.findById(id)
        return CompletableFuture.completedFuture(item)
    }

    void saveItem(Item item){
        itemRepository.save(item)
    }
}
