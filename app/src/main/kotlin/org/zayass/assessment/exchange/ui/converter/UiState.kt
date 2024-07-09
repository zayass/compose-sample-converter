package org.zayass.assessment.exchange.ui.converter

import org.zayass.assessment.exchange.domain.Amount
import java.util.Currency

sealed class UiState {
    data object Loading : UiState()

    data class Ready(
        val submitEnabled: Boolean = false,
        val sell: Amount,
        val receive: Amount,
        val fee: Amount? = null,

        val availableToSell: List<Currency>,
        val availableToReceive: List<Currency>,

        val showMessage: Boolean = false,
        val sellInput: String? = null,
        val receiveInput: String? = null,
    ) : UiState() {
        val sellValue: String
            get() = sellInput ?: sell.value.stripTrailingZeros().toPlainString()

        val receiveValue: String
            get() = receiveInput ?: receive.value.stripTrailingZeros().toPlainString()
    }
}