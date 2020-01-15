package com.jmuraski.entity

import org.springframework.data.redis.core.RedisHash

@RedisHash("Item")
class Item implements Serializable{
    String id
    String name
    Price currentPrice
}
