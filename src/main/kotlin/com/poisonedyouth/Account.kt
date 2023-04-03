package com.poisonedyouth

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Account(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val number: Int,
    val balance: Int
)
