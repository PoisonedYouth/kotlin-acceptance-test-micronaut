package com.poisonedyouth

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach

@MicronautTest(transactional = false)
open class BaseTest {

    @Inject
    protected lateinit var addressRepository: AddressRepository

    @Inject
    protected lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun beforeTest() {
        addressRepository.deleteAll()
        customerRepository.deleteAll()
    }
}
