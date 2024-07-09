package org.zayass.assessment.exchange.ui.balances

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.zayass.assessment.exchange.R
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.Amount
import org.zayass.assessment.exchange.domain.Currency
import org.zayass.assessment.exchange.ui.Header
import org.zayass.assessment.exchange.ui.ThemedSurface
import org.zayass.assessment.exchange.ui.applyPrecision
import org.zayass.assessment.exchange.ui.theme.dimens
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun Balances(modifier: Modifier = Modifier, viewModel: AccountsViewModel = hiltViewModel()) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    Balances(accounts, modifier)
}

@Composable
fun Balances(accounts: List<Account>, modifier: Modifier = Modifier) {
    if (accounts.isNotEmpty()) {
        Column(modifier) {
            Header(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium),
                stringRes = R.string.my_balances
            )

            Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))
            LazyRow {
                item {
                    Spacer(Modifier.size(MaterialTheme.dimens.medium))
                }

                itemsIndexed(items = accounts, key = { _, account -> account.id ?: -1 }) { _, account ->
                    Balance(account.balance)
                }
            }
        }
    }
}

@Composable
private fun Balance(balance: Amount) {
    Text(
        text = balance.format(),
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(end = MaterialTheme.dimens.veryLarge)
    )
}

private fun Amount.format(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    numberFormat.currency = currency
    numberFormat.applyPrecision(value)
    return numberFormat.format(value)
}

@Preview
@Composable
private fun BalancesPreview() {
    ThemedSurface {
        Balances(listOf(
            Account(
                id = 1,
                balance = Amount(
                    value = BigDecimal.TEN,
                    currency = Currency("USD")
                )
            ),
            Account(
                id = 2,
                balance = Amount(
                    value = BigDecimal(1_000_000_000),
                    currency = Currency("EUR")
                )
            )
        ))
    }
}