package org.zayass.assessment.exchange.ui.balances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.AccountRepository
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    repository: AccountRepository,
) : ViewModel() {
    val accounts: StateFlow<List<Account>> = repository.accounts()
        .stateIn(viewModelScope, WhileSubscribed(5000), emptyList())
}
