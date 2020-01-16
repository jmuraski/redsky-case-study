package com.jmuraski.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.redis.core.RedisHash

import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

@RedisHash("Item")
class Item implements Serializable{

    @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message = "item.id.notnumeric")
    @NotEmpty(message = "item.id.notempty")
    String id

    @NotEmpty(message = "item.name.notempty")
    String name

    @JsonProperty("current_price")
    @Valid
    Price currentPrice
}
