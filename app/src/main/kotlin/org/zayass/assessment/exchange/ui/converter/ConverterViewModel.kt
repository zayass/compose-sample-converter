package org.zayass.assessment.exchange.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.zayass.assessment.exchange.domain.AccountRepository
import org.zayass.assessment.exchange.domain.ConversionService
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

private data class InnerState(
    val rawAmount: String = "0",
    val amount: BigDecimal = BigDecimal.ZERO,
    val sellCurrency: Currency? = null,
    val receiveCurrency: Currency? = null,
    val showMessage: Boolean = false
)

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    conversionService: ConversionService
) : ViewModel() {

    private val state = MutableStateFlow(InnerState())

    val uiState = combine(
        state,
        accountRepository.accounts(),
        conversionService.converter().distinctUntilChanged(),
        ::reduceState
    ).stateIn(viewModelScope, WhileSubscribed(5000), UiState.Loading)

    fun dispatchAction(action: UiAction) {
        when (action) {
            is UiAction.ChangeAmount -> handleAmountChanged(action)
            is UiAction.ChangeReceiveCurrency -> handleReceiveCurrencyChanged(action)
            is UiAction.ChangeSellCurrency -> handleSellCurrencyChanged(action)
            UiAction.Submit -> handleSubmit(uiState.value)
            UiAction.ConfirmDialog -> handleConfirmDialog()
        }
    }

    private fun handleAmountChanged(action: UiAction.ChangeAmount) {
        if (action.amount.contains('-')) return
        val amount = parseNumber(action.amount) ?: return

        state.update {
            it.copy(
                amount = amount,
                rawAmount = action.amount
            )
        }
    }

    private fun handleSellCurrencyChanged(action: UiAction.ChangeSellCurrency) {
        state.update {
            it.copy(sellCurrency = action.currency)
        }
    }

    private fun handleReceiveCurrencyChanged(action: UiAction.ChangeReceiveCurrency) {
        state.update {
            it.copy(receiveCurrency = action.currency)
        }
    }

    private fun handleSubmit(uiState: UiState) {
        if (uiState !is UiState.Ready) return

        viewModelScope.launch {
            accountRepository.transfer(uiState.sell, uiState.receive, uiState.fee)
            state.update {
                it.copy(showMessage = true)
            }
        }
    }

    private fun handleConfirmDialog() {
        state.update {
            it.copy(
                amount = BigDecimal.ZERO,
                rawAmount = "0",
                showMessage = false
            )
        }
    }

    private fun reduceState(
        state: InnerState,
        accounts: List<org.zayass.assessment.exchange.domain.Account>,
        converter: org.zayass.assessment.exchange.domain.Converter,
    ): UiState.Ready {
        val availableToSell = accounts.map { it.balance.currency }
        val availableToReceive = converter.availableCurrencies()

        val sellCurrency = state.sellCurrency ?: availableToSell.first()
        val receiveCurrency = state.receiveCurrency ?: availableToReceive.first()

        val sell = org.zayass.assessment.exchange.domain.Amount(
            value = state.amount,
            currency = sellCurrency
        )

        val conversionResult = converter.convertForward(sell, receiveCurrency)

        return UiState.Ready(
            submitEnabled = hasSufficientAmount(accounts, sell),
            rawInput = state.rawAmount,
            sell = sell,
            receive = conversionResult.result,
            fee = conversionResult.fee,
            availableToSell = availableToSell,
            availableToReceive = availableToReceive,
            showMessage = state.showMessage
        )
    }

    private fun hasSufficientAmount(accounts: List<org.zayass.assessment.exchange.domain.Account>, sell: org.zayass.assessment.exchange.domain.Amount): Boolean {
        val account = accounts.firstOrNull { it.balance.currency == sell.currency } ?: return false
        return account.balance.value >= sell.value
    }

    private fun parseNumber(amount: String): BigDecimal? {
        return try {
            BigDecimal(amount)
        } catch (e: NumberFormatException) {
            null
        }
    }
}
