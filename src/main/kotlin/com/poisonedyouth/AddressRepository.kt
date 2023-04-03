package com.poisonedyouth

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import javax.transaction.Transactional

@Repository
@Transactional
interface AddressRepository : CrudRepository<Address, Long> {
    fun findByZipCodeEqual(zipCode: Int): Address?
}
