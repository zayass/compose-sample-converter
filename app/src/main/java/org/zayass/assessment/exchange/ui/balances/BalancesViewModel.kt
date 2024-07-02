package org.zayass.assessment.exchange.ui.balances

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.zayass.assessment.exchange.network.ExchangeRatesApi
import javax.inject.Inject

@HiltViewModel
class BalancesViewModel @Inject constructor(val api: ExchangeRatesApi) : ViewModel() {

}