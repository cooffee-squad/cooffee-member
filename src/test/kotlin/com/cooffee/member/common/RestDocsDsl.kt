package com.cooffee.member.common

import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.headers.RequestHeadersSnippet
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.request.QueryParametersSnippet
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.ResultActions

infix fun String.type(docsFieldType: DocsFieldType): Field {
    return createField(this, docsFieldType.type)
}

infix fun String.headerMeans(description: String): Header {
    return createHeader(this, description)
}

infix fun String.pathMeans(description: String): Parameter {
    return createPathParam(this, description)
}

infix fun String.paramMeans(description: String): Parameter {
    return createParam(this, description)
}

fun requestHeaders(vararg header: Header): RequestHeadersSnippet = requestHeaders(header.map { it.descriptor })

fun queryParameters(vararg parameters: Parameter): QueryParametersSnippet = queryParameters(parameters.map { it.descriptor })

fun requestPathParams(vararg parameters: Parameter): PathParametersSnippet = pathParameters(parameters.map { it.descriptor })

fun requestBody(vararg fields: Field): RequestFieldsSnippet = requestFields(fields.map { it.descriptor })

fun responseBody(vararg fields: Field): ResponseFieldsSnippet = responseFields(fields.map { it.descriptor })

private fun createField(
    value: String,
    type: JsonFieldType,
    optional: Boolean = false,
): Field {
    val descriptor =
        PayloadDocumentation.fieldWithPath(value)
            .type(type)
            .description("")

    if (optional) descriptor.optional()

    return Field(descriptor)
}

private fun createHeader(
    name: String,
    description: String,
    optional: Boolean = false,
): Header {
    val descriptor =
        headerWithName(name)
            .description(description)

    if (optional) descriptor.optional()

    return Header(descriptor)
}

private fun createPathParam(
    name: String,
    description: String,
    optional: Boolean = false,
): Parameter {
    val descriptor =
        RequestDocumentation.parameterWithName(name)
            .description(description)

    if (optional) descriptor.optional()

    return Parameter(descriptor)
}

private fun createParam(
    name: String,
    description: String,
    optional: Boolean = false,
): Parameter {
    val descriptor =
        RequestDocumentation.parameterWithName(name)
            .description(description)

    if (optional) descriptor.optional()

    return Parameter(descriptor)
}

sealed class DocsFieldType(val type: JsonFieldType)

object ARRAY : DocsFieldType(JsonFieldType.ARRAY)

object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)

object OBJECT : DocsFieldType(JsonFieldType.OBJECT)

object NUMBER : DocsFieldType(JsonFieldType.NUMBER)

object STRING : DocsFieldType(JsonFieldType.STRING)

object NULL : DocsFieldType(JsonFieldType.NULL)

object ANY : DocsFieldType(JsonFieldType.VARIES)

object DATE : DocsFieldType(JsonFieldType.STRING)

object DATETIME : DocsFieldType(JsonFieldType.STRING)

fun ResultActions.andDocument(
    identifier: String,
    vararg snippets: Snippet,
): ResultActions {
    return andDo(MockMvcRestDocumentation.document(identifier, *snippets))
}

fun pageableResponseFields(): Array<Field> {
    return arrayOf(
        "data.pageable.sort.empty" type BOOLEAN means "sort info",
        "data.pageable.sort.sorted" type BOOLEAN means "sort info",
        "data.pageable.sort.unsorted" type BOOLEAN means "sort info",
        "data.pageable.offset" type NUMBER means "offset info",
        "data.pageable.pageNumber" type NUMBER means "pageNumber info",
        "data.pageable.pageSize" type NUMBER means "pageSize info",
        "data.pageable.paged" type BOOLEAN means "paged info",
        "data.pageable.unpaged" type BOOLEAN means "unpaged info",
        "data.sort.empty" type BOOLEAN means "sort info",
        "data.sort.sorted" type BOOLEAN means "sort info",
        "data.sort.unsorted" type BOOLEAN means "sort info",
        "data.first" type BOOLEAN means "Is it the first page?",
        "data.last" type BOOLEAN means "Is it the last page?",
        "data.number" type NUMBER means "The current page number.",
        "data.numberOfElements" type NUMBER means "The number of elements on the current page.",
        "data.size" type NUMBER means "The size of the page.",
        "data.totalElements" type NUMBER means "The total number of elements.",
        "data.totalPages" type NUMBER means "The total number of pages.",
        "data.empty" type BOOLEAN means "Data is Empty",
    )
}
