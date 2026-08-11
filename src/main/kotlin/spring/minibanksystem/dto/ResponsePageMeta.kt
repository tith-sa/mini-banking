package spring.minibanksystem.dto

data class ResponsePageMeta(
    val page: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
)
