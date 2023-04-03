package com.poisonedyouth

import java.util.Locale
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Address(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val street: String,
    val number: String,
    val zipCode: Int,
    val city: String,
    val country: String
) {
    init {
        require(zipCode in 10000..99999) {
            "ZipCode must contain 5 digits!"
        }

        require(Locale.getISOCountries().contains(country)) {
            "Country '$country' must be ISO conform!"
        }
    }
}
