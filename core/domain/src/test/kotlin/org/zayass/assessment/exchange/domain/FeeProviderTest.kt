package org.zayass.assessment.exchange.domain

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.math.BigDecimal

class FeeProviderTest {
    @Test
    fun testPolicyFlow() = runTest {
        val repository = mock<AccountRepository> {
            on { transfersCount() } doReturn flowOf(
                0, 1, 2, 5, 6,
            )
        }

        val feeProvider = FeeProvider(repository)

        feeProvider.feePolicy().test {
            assertEquals(ZeroFee, awaitItem())
            assertEquals(ZeroFee, awaitItem())
            assertEquals(ZeroFee, awaitItem())

            assertEquals(PercentFee(BigDecimal(7).movePointLeft(3)), awaitItem())
            assertEquals(PercentFee(BigDecimal(7).movePointLeft(3)), awaitItem())

            awaitComplete()
        }
    }
}
