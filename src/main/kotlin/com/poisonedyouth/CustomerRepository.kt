package com.poisonedyouth

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import javax.transaction.Transactional

@Repository
@Transactional(value = Transactional.TxType.REQUIRED)
interface CustomerRepository : CrudRepository<Customer, Long> {
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Customer?
    fun existsEmailBy(email: String): Boolean
}
