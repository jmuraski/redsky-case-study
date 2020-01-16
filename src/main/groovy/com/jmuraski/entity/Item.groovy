package com.jmuraski.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.redis.core.RedisHash

@RedisHash("Item")
class Item implements Serializable{
    String id
    String name
    @JsonProperty("current_price")
    Price currentPrice
}
