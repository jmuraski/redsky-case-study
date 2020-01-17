package com.jmuraski.controller

import com.jmuraski.entity.Item
import com.jmuraski.service.ItemService
import com.jmuraski.service.RedskyRestService
import com.jmuraski.util.ConstraintException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.Pattern
import java.util.concurrent.CompletableFuture

@RestController
@Validated
class ProductController {

    Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    RedskyRestService redskyRestService

    @Autowired
    ItemService itemService

    @Autowired
    MessageSource messageSource
    
    @GetMapping("/products/{id}")
    Item getProduct(@PathVariable @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "item.id.notnumeric") String id){

        CompletableFuture<Item> redskyFuture = redskyRestService.fetchById(id)

        Item redskyItem = redskyFuture.get()
        Optional<Item> optional = itemService.fetchById(id).get()
        if(optional.isPresent()){
            Item item = optional.get()
            redskyItem.currentPrice = item.currentPrice
        }

        return redskyItem
    }

    @PutMapping("/products/{id}")
    Item updateProduct(@PathVariable  @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "item.id.notnumeric") String id,
                       @Valid @RequestBody Item item){

        if(id != item.id) {
            throw new ConstraintException("item.id.notmatching")
        }

        itemService.saveItem(item)
        return item
    }

    @ExceptionHandler([ConstraintViolationException, ConstraintException])
    ResponseEntity<Map> handleConstraintViolation(Exception ex){
        Map errors = [errors: []]
        if(ex instanceof ConstraintViolationException){
            ex.constraintViolations.each { ConstraintViolation v ->
                errors.errors.add(convertMessage(v))
            }
        } else {
            errors.errors.add(convertMessage(ex))
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST)
    }

    private String convertMessage(def messageCarrier) {
        return messageSource.getMessage(messageCarrier.message, null, Locale.ENGLISH)
    }

    @ExceptionHandler(Exception)
    ResponseEntity<Map> handleUnknownExceptions(Exception ex){
        log.error("Unknown Exception", ex)
        return new ResponseEntity<Map>(null, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
