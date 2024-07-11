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
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.AccountRepository
import org.zayass.assessment.exchange.domain.Amount
import org.zayass.assessment.exchange.domain.ConversionResult
import org.zayass.assessment.exchange.domain.ConversionService
import org.zayass.assessment.exchange.domain.Converter
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

private sealed class AmountInput {
    data class Sell(val raw: String, val parsed: BigDecimal) : AmountInput()
    data class Receive(val raw: String, val parsed: BigDecimal) : AmountInput()
    data object None : AmountInput()

    fun rawSell() = (this as? Sell)?.raw
    fun rawReceive() = (this as? Receive)?.raw
}

private data class InnerState(
    val amountInput: AmountInput = AmountInput.None,
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
            is UiAction.ChangeSellAmount -> handleSellAmountChanged(action)
            is UiAction.ChangeReceiveAmount -> handleReceiveAmountChanged(action)
            is UiAction.ChangeReceiveCurrency -> handleReceiveCurrencyChanged(action)
            is UiAction.ChangeSellCurrency -> handleSellCurrencyChanged(action)
            UiAction.Submit -> handleSubmit(uiState.value)
            UiAction.ConfirmDialog -> handleConfirmDialog()
        }
    }

    private fun handleSellAmountChanged(action: UiAction.ChangeSellAmount) {
        val amount = parseNumber(action.amount) ?: return

        state.update {
            it.copy(
                amountInput = AmountInput.Sell(action.amount, amount),
            )
        }
    }

    private fun handleReceiveAmountChanged(action: UiAction.ChangeReceiveAmount) {
        val amount = parseNumber(action.amount) ?: return

        state.update {
            it.copy(
                amountInput = AmountInput.Receive(action.amount, amount),
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
                amountInput = AmountInput.None,
                showMessage = false
            )
        }
    }

    private fun reduceState(
        state: InnerState,
        accounts: List<Account>,
        converter: Converter,
    ): UiState.Ready {
        val availableToReceive = converter.availableCurrencies()
        val availableToSell = sellCurrencies(accounts, availableToReceive)

        val sellCurrency = state.sellCurrency ?: availableToSell.first()
        val receiveCurrency = state.receiveCurrency ?: availableToReceive.first()

        val (sell, receive, fee) = converter.computeState(
            state.amountInput,
            sellCurrency, receiveCurrency
        )

        return UiState.Ready(
            sellInput = state.amountInput.rawSell(),
            receiveInput = state.amountInput.rawReceive(),
            submitEnabled = hasSufficientAmount(accounts, sell),
            sell = sell,
            receive = receive,
            fee = fee,
            availableToSell = availableToSell,
            availableToReceive = availableToReceive,
            showMessage = state.showMessage
        )
    }

    private fun sellCurrencies(accounts: List<Account>, available: List<Currency>): List<Currency> {
        val set = available.toSet()
        return accounts
            .map { it.balance.currency }
            .filter { it in set }
    }

    private fun Converter.computeState(
        input: AmountInput,
        sellCurrency: Currency,
        receiveCurrency: Currency
    ): ConversionResult {
        return when (input) {
            is AmountInput.Receive ->
                convertBackward(Amount(input.parsed, receiveCurrency), sellCurrency)
            is AmountInput.Sell ->
                convertForward(Amount(input.parsed, sellCurrency), receiveCurrency)
            AmountInput.None ->
                convertForward(
                    Amount(BigDecimal.ZERO, sellCurrency),
                    receiveCurrency,
                )
        }
    }

    private fun hasSufficientAmount(accounts: List<Account>, sell: Amount): Boolean {
        val account = accounts.firstOrNull { it.balance.currency == sell.currency } ?: return false
        return account.balance.value >= sell.value
    }

    private fun parseNumber(amount: String): BigDecimal? {
        if (amount.contains('-')) {
            return null
        }

        if (amount.isEmpty()) {
            return BigDecimal.ZERO
        }

        return try {
            BigDecimal(amount)
        } catch (e: NumberFormatException) {
            null
        }
    }
}
