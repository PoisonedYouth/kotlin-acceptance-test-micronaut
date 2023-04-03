package com.poisonedyouth

import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import kotlin.random.Random

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
data class Customer(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val birthdate: LocalDate,
    @Column(nullable = false, unique = true)
    val email: String,
    @OneToOne(cascade = [CascadeType.DETACH])
    val address: Address,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "addresss_id")
    val accounts: Set<Account> = emptySet()
) {
    val customerId: Long = createCustomerId()
    private fun createCustomerId() = Random.nextLong(10000, 99999)

    private val nameRegex = Regex("[A-Za-z]+")

    init {
        require(firstName.matches(nameRegex)) {
            "Firstname '$firstName' contains special characters!"
        }

        require(lastName.matches(nameRegex)) {
            "Lastname '$lastName' contains special characters!"
        }

        val now = LocalDate.now()
        require(
            birthdate.isBefore(now.minusYears(18)) && birthdate.isAfter(now.minusYears(100))
        ) {
            "Age must be between 18 and 100!"
        }
    }
}
