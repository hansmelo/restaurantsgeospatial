package com.hans.de.melo.restaurantsgeospatial.restaurantsgeospatial.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Check the request")
class BadRequestException : Exception()

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No matching")
class NotFoundException : Exception()