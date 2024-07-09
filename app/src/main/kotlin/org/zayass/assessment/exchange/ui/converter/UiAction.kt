package org.zayass.assessment.exchange.ui.converter

import java.util.Currency

sealed class UiAction {
    data class ChangeSellAmount(val amount: String) : UiAction()
    data class ChangeReceiveAmount(val amount: String) : UiAction()
    data class ChangeSellCurrency(val currency: Currency) : UiAction()
    data class ChangeReceiveCurrency(val currency: Currency) : UiAction()

    data object ConfirmDialog : UiAction()
    data object Submit : UiAction()
}
