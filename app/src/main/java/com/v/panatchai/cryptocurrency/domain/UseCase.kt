package com.v.panatchai.cryptocurrency.domain

/**
 * Optional
 * Root of all domain UseCase, which provides a layer of abstraction.
 */
abstract class UseCase<T, R> {

    @Throws(IllegalArgumentException::class)
    abstract suspend operator fun invoke(vararg args: T): R
}
