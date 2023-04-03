package com.poisonedyouth

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CustomerRepositoryTest : BaseTest() {

    @Test
    fun `save Customer is possible`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )

        // when
        val actual = customerRepository.save(customer)

        // then
        assertThat(customerRepository.findById(actual.id).get()).isEqualTo(customer)
    }

    @Test
    fun `save Customer not allows duplicate email`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )
        customerRepository.save(customer)

        val customerDuplicate = Customer(
            firstName = "Duplicate",
            lastName = "Customer",
            birthdate = LocalDate.of(1984, 12, 1),
            email = "john.doe@mail.com",
            address = address,
            accounts = emptySet()
        )

        // when + then
        assertThatThrownBy {
            customerRepository.save(customerDuplicate)
        }.isInstanceOf(RuntimeException::class.java)
    }

    @Test
    fun `save Customer without saved address fails`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )

        // when + then
        assertThatThrownBy {
            customerRepository.save(customer)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `findByFirstNameAndLastName returns matching customer`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )
        customerRepository.save(customer)

        // when
        val actual = customerRepository.findByFirstNameAndLastName("John", "Doe")

        // then
        assertThat(actual).isEqualTo(customer)
    }

    @Test
    fun `existsCustomerByEmail returns true if customer exists`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )
        customerRepository.save(customer)

        // when
        val actual = customerRepository.existsEmailBy(customer.email)

        // then
        assertThat(actual).isTrue
    }

    @Test
    fun `existsCustomerByEmail returns false if customer not exists`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )
        customerRepository.save(customer)

        // when
        val actual = customerRepository.existsEmailBy("otherCustomer@mail.com")

        // then
        assertThat(actual).isFalse
    }

    @Test
    fun `delete customer not deletes address`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)

        val customer = Customer(
            firstName = "John",
            lastName = "Doe",
            birthdate = LocalDate.of(2001, 5, 10),
            email = "john.doe@mail.com",
            address = address,
            accounts = setOf(
                Account(
                    number = 12345,
                    balance = 200
                ),
                Account(
                    number = 12346,
                    balance = -150
                )
            )
        )
        customerRepository.save(customer)

        // when
        customerRepository.delete(customer)

        // then
        assertThat(customerRepository.findAll()).isEmpty()
        assertThat(addressRepository.findAll()).isNotEmpty
    }
}
