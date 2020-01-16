package com.jmuraski.util

class ConstraintException extends RuntimeException{

    String message

    ConstraintException(String message){
        this.message = message
    }
}
