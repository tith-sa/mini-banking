package spring.minibanksystem.dto

data class ResponsePagination<T>(
    val meta: ResponsePageMeta,
    val transaction: List<T>
)
