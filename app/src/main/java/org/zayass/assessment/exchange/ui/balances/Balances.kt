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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import org.zayass.assessment.exchange.R
import org.zayass.assessment.exchange.ui.Header
import org.zayass.assessment.exchange.ui.ThemedSurface
import org.zayass.assessment.exchange.ui.theme.dimens

data class Balance(
    val amount: Int,
    val currency: String
)

@Composable
fun Balances(modifier: Modifier = Modifier, viewModel: BalancesViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = "") {
        val rates = viewModel.api.getRates()
        println(rates)
    }

    val accounts = listOf(
        Balance(1000, "EUR"),
        Balance(1000, "EUR"),
        Balance(1000, "EUR"),
        Balance(1000, "EUR"),
        Balance(1000, "EUR"),
    )

    Balances(accounts, modifier)
}

@Composable
fun Balances(accounts: List<Balance>, modifier: Modifier = Modifier) {
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

            itemsIndexed(items = accounts) { _, account ->
                Balance(account)
            }
        }
    }
}

@Composable
private fun Balance(balance: Balance) {
    Text(
        text = "${balance.amount} ${balance.currency}",
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(end = MaterialTheme.dimens.veryLarge)
    )
}

@Preview
@Composable
private fun BalancesPreview() {
    ThemedSurface {
        Balances()
    }
}