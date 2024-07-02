package org.zayass.assessment.exchange.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbAccountRepository @Inject constructor(
    private val dao: AccountDao
) : AccountRepository {
    override fun accounts(): Flow<List<Account>> {
        return dao.loadAccounts().map {
            it.map(AccountEntry::toDomain)
        }
    }
}