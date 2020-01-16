package com.jmuraski.entity

import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.Digits
import javax.validation.constraints.NotEmpty

class Price implements Serializable{
    @Digits(integer = 10, fraction = 2, message = "price.value.numeric")
    @NotEmpty(message = "price.value.notempty")
    String value
    @JsonProperty("currency_code")
    String currencyCode
}
