package com.apposum.domain.common

abstract class Mapper<in T, out E>{

    abstract fun mapFrom(from: T): E
}