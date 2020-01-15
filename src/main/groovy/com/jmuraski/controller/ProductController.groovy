package com.jmuraski.controller

import com.jmuraski.entity.Item
import com.jmuraski.service.ItemService
import com.jmuraski.service.RedskyRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import java.util.concurrent.CompletableFuture

@RestController
class ProductController {

    @Autowired
    RedskyRestService redskyRestService

    @Autowired
    ItemService itemService
    
    @GetMapping("/products/{id}")
    Item getProduct(@PathVariable String id){
        // validate ID

        CompletableFuture<Item> redskyFuture = redskyRestService.fetchById(id)

        Item redskyItem = redskyFuture.get()
        Optional<Item> optional = itemService.fetchById(id).get()
        if(optional.isPresent()){
            Item item = optional.get()
            redskyItem.currentPrice = item.currentPrice
        } else {
            itemService.saveItem(redskyItem)
        }

        return redskyItem
    }

    @PutMapping("/products/{id}")
    Item updateProduct(@PathVariable String id, @RequestBody Item item){
        // todo validate id against item.id
        itemService.saveItem(item)
        return item
    }
}
