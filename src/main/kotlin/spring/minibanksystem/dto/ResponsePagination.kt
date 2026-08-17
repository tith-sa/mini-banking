package spring.minibanksystem.dto

data class ResponsePagination<T>(
    val meta: ResponsePageMeta,
    val transaction: List<T>
){
    data class ResponsePageMeta(
        val page: Int,
        val pageSize: Int,
        val totalElements: Long,
        val totalPages: Int,
    )
}
