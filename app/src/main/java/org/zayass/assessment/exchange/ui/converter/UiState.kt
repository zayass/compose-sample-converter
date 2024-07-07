package org.zayass.assessment.exchange.ui.converter

import org.zayass.assessment.exchange.domain.Amount
import java.util.Currency

sealed class UiState {
    data object Loading : UiState()

    data class Ready(
        val submitEnabled: Boolean,
        val rawInput: String,
        val sell: org.zayass.assessment.exchange.domain.Amount,
        val receive: org.zayass.assessment.exchange.domain.Amount,
        val fee: org.zayass.assessment.exchange.domain.Amount?,

        val availableToSell: List<Currency>,
        val availableToReceive: List<Currency>,

        val showMessage: Boolean = false
    ) : UiState()
}