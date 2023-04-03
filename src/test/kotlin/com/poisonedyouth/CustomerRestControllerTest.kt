package com.poisonedyouth

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpStatus
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CustomerRestControllerTest : BaseTest() {

    @Test
    fun `addCustomer returns error result for invalid birthdate`(
        spec: RequestSpecification,
        objectMapper: ObjectMapper
    ) {
        // given
        val addressDto = AddressDto(
            street = "Main Street",
            number = "13",
            zipCode = 123,
            city = "Los Angeles",
            country = "US"
        )

        val customerDto = CustomerDto(
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

        // when + then
        spec.`when`()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(customerDto))
            .post("/api/v1/customer")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.code)
            .assertThat()
            .body("errorCode", Matchers.equalTo("INVALID_DATE"))
            .body("errorMessage", Matchers.equalTo("The birthdate '2000.5.10' is not in expected format (dd.MM.yyyy)!"))
    }

    @Test
    fun `addCustomer returns error result for invalid zipCode`(
        spec: RequestSpecification,
        objectMapper: ObjectMapper
    ) {
        // given
        val addressDto = AddressDto(
            street = "Main Street",
            number = "13",
            zipCode = 123,
            city = "Los Angeles",
            country = "US"
        )

        val customerDto = CustomerDto(
            firstName = "John",
            lastName = "Doe",
            birthdate = "05.10.2000",
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

        // when + then
        spec.`when`()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(customerDto))
            .post("/api/v1/customer")
            .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.code)
            .assertThat()
            .body("errorCode", Matchers.equalTo("GENERAL_ERROR"))
            .body("errorMessage", Matchers.equalTo("An unexpected error occurred (ZipCode must contain 5 digits!!"))
    }

    @Test
    fun `addCustomer returns error result for duplicate email`(
        spec: RequestSpecification,
        objectMapper: ObjectMapper
    ) {
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

        val addressDto = AddressDto(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )

        val customerDto = CustomerDto(
            firstName = "John",
            lastName = "Doe",
            birthdate = "05.10.2000",
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

        // when + then
        spec.`when`()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(customerDto))
            .post("/api/v1/customer")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.code)
            .assertThat()
            .body("errorCode", Matchers.equalTo("DUPLICATE_EMAIL"))
            .body("errorMessage", Matchers.equalTo("For the email 'john.doe@mail.com' a customer already exists!"))
    }
}
