package com.poisonedyouth

import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CustomerApplicationServiceTest : BaseTest() {

    @Inject
    private lateinit var customerApplicationService: CustomerApplicationService

    @Test
    fun `addNewCustomer returns failure result for duplicate email`() {
        // given
        val email = "john.doe@mail.com"
        val customer = createCustomer(email)

        val addressDto = AddressDto(
            street = customer.address.street,
            number = customer.address.number,
            zipCode = customer.address.zipCode,
            city = customer.address.city,
            country = customer.address.country
        )

        val customerDuplicate = CustomerDto(
            firstName = "John",
            lastName = "Doe",
            birthdate = "10.05.2000",
            email = "john.doe@mail.com",
            address = addressDto,
            accounts = setOf(
                AccountDto(
                    number = 12345,
                    balance = 200
                ),
                AccountDto(
                    number = 12346,
                    balance = -150
                )
            )
        )

        // when
        val actual = customerApplicationService.addNewCustomer(customerDuplicate)

        // then
        assertThat(actual).isInstanceOf(ApiResult.Failure::class.java)
        assertThat((actual as ApiResult.Failure).errorCode).isEqualTo(ErrorCode.DUPLICATE_EMAIL)
        assertThat(actual.errorMessage).isEqualTo("For the email 'john.doe@mail.com' a customer already exists!")
    }

    @Test
    fun `addNewCustomer returns failure result for invalid birthdate`() {
        // given
        val email = "john.doe@mail.com"
        val customer = createCustomer(email)

        val addressDto = AddressDto(
            street = customer.address.street,
            number = customer.address.number,
            zipCode = customer.address.zipCode,
            city = customer.address.city,
            country = customer.address.country
        )

        val customerDuplicate = CustomerDto(
            firstName = "John",
            lastName = "Doe",
            birthdate = "2000.5.10",
            email = "john.doe@mail.com",
            address = addressDto,
            accounts = setOf(
                AccountDto(
                    number = 12345,
                    balance = 200
                ),
                AccountDto(
                    number = 12346,
                    balance = -150
                )
            )
        )

        // when
        val actual = customerApplicationService.addNewCustomer(customerDuplicate)

        // then
        assertThat(actual).isInstanceOf(ApiResult.Failure::class.java)
        assertThat((actual as ApiResult.Failure).errorCode).isEqualTo(ErrorCode.INVALID_DATE)
        assertThat(actual.errorMessage).isEqualTo("The birthdate '2000.5.10' is not in expected format (dd.MM.yyyy)!")
    }

    @Test
    fun `addNewCustomer returns success result for unique email`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address)
        val addressDto = AddressDto(
            street = address.street,
            number = address.number,
            zipCode = address.zipCode,
            city = address.city,
            country = address.country
        )

        val customer = CustomerDto(
            firstName = "John",
            lastName = "Doe",
            birthdate = "10.05.2000",
            email = "john.doe@mail.com",
            address = addressDto,
            accounts = setOf(
                AccountDto(
                    number = 12345,
                    balance = 200
                ),
                AccountDto(
                    number = 12346,
                    balance = -150
                )
            )
        )

        // when
        val actual = customerApplicationService.addNewCustomer(customer)

        // then
        assertThat(actual).isInstanceOf(ApiResult.Success::class.java)
        assertThat((actual as ApiResult.Success).value).isNotNull
    }

    @Test
    fun `addNewCustomer not persists address twice`() {
        // given
        val email = "john.doe@mail.com"
        createCustomer(email)

        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        val addressDto = AddressDto(
            street = address.street,
            number = address.number,
            zipCode = address.zipCode,
            city = address.city,
            country = address.country
        )

        val customer = CustomerDto(
            firstName = "Max",
            lastName = "DeMarco",
            birthdate = "10.05.2000",
            email = "max.demarco@mail.com",
            address = addressDto,
            accounts = setOf(
                AccountDto(
                    number = 12345,
                    balance = 200
                ),
                AccountDto(
                    number = 12346,
                    balance = -150
                )
            )
        )

        // when
        customerApplicationService.addNewCustomer(customer)

        // then
        assertThat(addressRepository.findAll()).hasSize(1)
    }

    private fun createCustomer(email: String): Customer {
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
            birthdate = LocalDate.of(2000, 5, 10),
            email = email,
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
        return customerRepository.save(customer)
    }
}
