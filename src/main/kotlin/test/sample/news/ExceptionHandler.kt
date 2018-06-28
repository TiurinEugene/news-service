package test.sample.news

import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import test.sample.news.common.response.FailResponse

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val map = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.associateTo(map) { it.field to it.defaultMessage }
        ex.bindingResult.globalErrors.associateTo(map) { "global" to it.defaultMessage }
        return ResponseEntity(FailResponse(map), headers, BAD_REQUEST)
    }

    override fun handleBindException(
        ex: BindException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val map = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.associateTo(map) { it.field to it.defaultMessage }
        ex.bindingResult.globalErrors.associateTo(map) { "global" to it.defaultMessage }
        return ResponseEntity(FailResponse(map), headers, BAD_REQUEST)
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = "${ex.parameterName} is missing"
        return ResponseEntity(FailResponse(mapOf("error" to error)), headers, BAD_REQUEST)
    }

    override fun handleMissingServletRequestPart(
        ex: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = "${ex.requestPartName} is missing"
        return ResponseEntity(FailResponse(mapOf("error" to error)), headers, BAD_REQUEST)
    }

    override fun handleTypeMismatch(
        ex: TypeMismatchException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val error = "${ex.propertyName} should be of type ${ex.requiredType}"
        return ResponseEntity(FailResponse(mapOf("error" to error)), headers, BAD_REQUEST)
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> =
        ResponseEntity(FailResponse(mapOf("error" to "http method is not allowed")), headers, METHOD_NOT_ALLOWED)

    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException?,
        headers: HttpHeaders?,
        status: HttpStatus?,
        request: WebRequest?
    ): ResponseEntity<Any> =
        ResponseEntity(FailResponse(mapOf("error" to "media type is not supported")), headers, UNSUPPORTED_MEDIA_TYPE)

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.error(ex.message)
        return ResponseEntity(FailResponse(mapOf("error" to "general exception")), BAD_REQUEST)
    }
}
