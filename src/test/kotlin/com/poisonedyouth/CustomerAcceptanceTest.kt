package com.poisonedyouth

import io.micronaut.http.HttpStatus
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import java.io.File

class CustomerAcceptanceTest : BaseTest() {

    @Test
    fun `scenario save customer is successful`(
        spec: RequestSpecification
    ) {
        // when + then
        spec.`when`()
            .contentType(ContentType.JSON)
            .body((File("src/test/resources/payload.json").readText()))
            .post("/api/v1/customer")
            .then()
            .statusCode(HttpStatus.CREATED.code)
            .assertThat()
            .body(Matchers.matchesPattern("\\{\"customerId\":[\\d]{5}}"))
    }
}
