package com.poisonedyouth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddressRepositoryTest : BaseTest() {

    @Test
    fun `save address is possible`() {
        // given
        val address = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )

        // when
        val actual = addressRepository.save(address)

        // then
        assertThat(addressRepository.findById(actual.id).get()).isEqualTo(address)
    }

    @Test
    fun `findAddressesByZipCode returns matching address`() {
        // given
        val address1 = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address1)

        val address2 = Address(
            street = "Hauptstrasse",
            number = "25A",
            zipCode = 10115,
            city = "Berlin",
            country = "DE"
        )
        addressRepository.save(address2)

        // when
        val actual = addressRepository.findByZipCodeEqual(10115)

        // then
        assertThat(actual).isEqualTo(address2)
    }

    @Test
    fun `findAddressesByCity returns empty optional for no matching address`() {
        // given
        val address1 = Address(
            street = "Main Street",
            number = "13",
            zipCode = 90001,
            city = "Los Angeles",
            country = "US"
        )
        addressRepository.save(address1)

        val address2 = Address(
            street = "Hauptstrasse",
            number = "25A",
            zipCode = 10115,
            city = "Berlin",
            country = "DE"
        )
        addressRepository.save(address2)

        // when
        val actual = addressRepository.findByZipCodeEqual(88888)

        // then
        assertThat(actual).isNull()
    }
}
