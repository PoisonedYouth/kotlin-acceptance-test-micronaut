package com.poisonedyouth

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller
class CustomerRestController(
    private val customerApplicationService: CustomerApplicationService
) {

    @Post(
        "/api/v1/customer",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun addCustomer(@Body customerDto: CustomerDto): HttpResponse<out Any> {
        return customerApplicationService.addNewCustomer(customerDto).let { result ->
            when (result) {
                is ApiResult.Success -> handleSuccess(result)
                is ApiResult.Failure -> handleFailure(result)
            }
        }
    }

    private fun handleSuccess(result: ApiResult.Success<Any>): HttpResponse<SuccessDto> {
        return HttpResponse.created(SuccessDto(result.value)).status(HttpStatus.CREATED)
    }

    private fun handleFailure(
        result: ApiResult.Failure
    ): HttpResponse<ErrorDto> {
        val code = result.errorCode
        val status = when (code) {
            ErrorCode.INVALID_DATE,
            ErrorCode.DUPLICATE_EMAIL -> HttpStatus.BAD_REQUEST
            ErrorCode.GENERAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        val errorDto = ErrorDto(code.name, result.errorMessage)
        return HttpResponse.created(errorDto).status(status)
    }
}

data class ErrorDto(val errorCode: String, val errorMessage: String)

data class SuccessDto(val customerId: Any)
