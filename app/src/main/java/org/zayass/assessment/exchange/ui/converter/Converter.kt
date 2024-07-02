package org.zayass.assessment.exchange.ui.converter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.zayass.assessment.exchange.R
import org.zayass.assessment.exchange.ui.Header
import org.zayass.assessment.exchange.ui.ThemedSurface
import org.zayass.assessment.exchange.ui.theme.Green40
import org.zayass.assessment.exchange.ui.theme.Red40
import org.zayass.assessment.exchange.ui.theme.dimens

@Composable
fun Converter(modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = MaterialTheme.dimens.medium)) {
        Header(stringRes = R.string.currency_exchange)
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))

        SellRow()
        HorizontalDivider(modifier = Modifier.padding(start = 36.dp))
        ReceiveRow()

        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.veryLarge)
                .padding(vertical = MaterialTheme.dimens.large)
                .align(Alignment.CenterHorizontally),
            onClick = { /*TODO*/ }
        ) {
            Text(text = stringResource(R.string.submit).uppercase())
        }
    }
}

@Composable
fun SellRow() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = "",
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimens.small)
                .size(32.dp)
                .background(Red40, shape = CircleShape)
        )

        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))
        Text(
            text = stringResource(R.string.sell),
            fontWeight = FontWeight.Medium
        )

        TextFieldWithoutDecor(
            value = "100",
            onValueChange = { },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.small),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Medium,
            ),
        )

        DropDown()
    }
}

@Composable
fun ReceiveRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = "",
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimens.small)
                .size(32.dp)
                .background(Green40, shape = CircleShape)
        )

        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))
        Text(
            text = stringResource(R.string.receive),
            fontWeight = FontWeight.Medium
        )

        TextFieldWithoutDecor(
            value = "100",
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.small),
            onValueChange = { },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Medium,
                color = Green40
            ),
        )

        DropDown()
    }
}

@Composable
fun DropDown(modifier: Modifier = Modifier) {
    val currencies = listOf("USD", "EUR", "GBP", "BTC", "ETC")
    var selectedText by remember { mutableStateOf(currencies[0]) }

    DropDown(
        values = currencies,
        value = selectedText,
        modifier = modifier,
        onValueChanged = {
            selectedText = it
        }
    )
}

@Preview
@Composable
private fun ConverterPreview() {
    ThemedSurface {
        Converter(Modifier.fillMaxHeight())
    }
}