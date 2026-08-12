package spring.minibanksystem.repository.specification

import org.springframework.data.jpa.domain.Specification
import spring.minibanksystem.dto.request.TransactionSearchRequest
import spring.minibanksystem.model.Transaction
import spring.minibanksystem.model.enum.TransactionStatus
import spring.minibanksystem.model.enum.TransactionType
import java.time.LocalDateTime

class TransactionSpecification {
    companion object {
        fun findByIdAndAccounts(id: Long, account: String?): Specification<Transaction> {
            return Specification { root,query, cb ->

                // 1. Column equals filter: t.id = :id
                val idPredicate = cb.equal(root.get<Long>("id"),id)

                // 2. OR filter: (t.fromAccount = :account OR t.toAccount = :account)
                val fromAccountPredicate = cb.equal(root.get<String>("fromAccount"), account)
                val toAccountPredicate = cb.equal(root.get<String>("toAccount"), account)
                val accountOrPredicate = cb.or(fromAccountPredicate, toAccountPredicate)

                // 4. Combine everything with AND: id AND (from OR to)
                cb.and(idPredicate,accountOrPredicate)
            }
        }

        // Searching
        fun accountNumber(accountNumber: String?): Specification<Transaction>? {
            // If account number is not provided, skip this filter
            if (accountNumber.isNullOrBlank()) return null

            return Specification { root, _, cb ->

                // Check if the account is the sender (fromAccount)
                val fromAccountPredicate =
                    cb.equal(root.get<String>("fromAccount"), accountNumber)

                // Check if the account is the receiver (toAccount)
                val toAccountPredicate =
                    cb.equal(root.get<String>("toAccount"), accountNumber)

                // Match transactions where the account is either
                // the sender OR the receiver
                cb.or(fromAccountPredicate, toAccountPredicate)
            }
        }

        fun statuses(
            statuses: List<TransactionStatus>?
        ): Specification<Transaction>? {

            // If no statuses are provided, skip this filter
            if (statuses.isNullOrEmpty()) return null

            return Specification { root, _, _ ->

                // Filter transactions whose status is included
                // in the provided list.
                //
                // Example:
                // statuses = [PENDING, COMPLETED]
                // SQL: status IN ('PENDING', 'COMPLETED')
                root.get<TransactionStatus>("status").`in`(statuses)
            }
        }

        fun types(
            types: List<TransactionType>?
        ): Specification<Transaction>? {

            // If no transaction types are provided, skip this filter
            if (types.isNullOrEmpty()) return null

            return Specification { root, _, _ ->

                // Filter transactions whose type is included
                // in the provided list.
                //
                // Example:
                // types = [TRANSFER, DEPOSIT]
                // SQL: type IN ('TRANSFER', 'DEPOSIT')
                root.get<TransactionType>("type").`in`(types)
            }
        }

        fun fromDate(
            fromDate: LocalDateTime?
        ): Specification<Transaction>? {

            // If fromDate is not provided, skip this filter
            if (fromDate == null) return null

            return Specification { root, _, cb ->

                // Find transactions created on or after fromDate
                //
                // Example:
                // fromDate = 2026-08-01 00:00:00
                // createdAt >= 2026-08-01 00:00:00
                cb.greaterThanOrEqualTo(
                    root.get("createdAt"),
                    fromDate
                )
            }
        }

        fun toDate(
            toDate: LocalDateTime?
        ): Specification<Transaction>? {

            // If toDate is not provided, skip this filter
            if (toDate == null) return null

            return Specification { root, _, cb ->

                // Find transactions created on or before toDate
                //
                // Example:
                // toDate = 2026-08-12 23:59:59
                // createdAt <= 2026-08-12 23:59:59
                cb.lessThanOrEqualTo(
                    root.get("createdAt"),
                    toDate
                )
            }
        }

        fun buildSpecification(
            request: TransactionSearchRequest
        ): Specification<Transaction> {

            // Build a list containing only the filters
            // that were actually provided by the user.
            //
            // listOfNotNull() removes filters that returned null.
            return Specification.allOf(
                listOfNotNull(
                    accountNumber(request.accountNumber),
                    statuses(request.statuses),
                    types(request.types),
                    fromDate(request.fromDate),
                    toDate(request.toDate)
                )
            )
        }
    }
}
