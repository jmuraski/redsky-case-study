package com.jmuraski.repository

import com.jmuraski.entity.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository extends CrudRepository<Item, String> {}
