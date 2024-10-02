package com.example.orderservice.service

import com.example.orderservice.api.rest.model.Status
import com.example.orderservice.api.rest.model.TotalPrice
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class MetricService(private val meterRegistry: MeterRegistry) {

    private companion object {
        const val ORDERS_TOTAL_METRIC = "online_shop_orders_total"
        const val ORDER_STATUS_TAG = "status"
        const val LAST_ORDER_TIME_METRIC = "online_shop_last_order_time"
        const val ORDER_PRICE_SUMMARY_METRIC = "online_shop_order_price_summary"
    }

    private val orderPriceSummary = DistributionSummary.builder(ORDER_PRICE_SUMMARY_METRIC)
        .description("distribution of the total price for orders")
        .publishPercentiles(0.5, 0.9)
        .register(meterRegistry)

    fun countOrders(status: Status) {
        meterRegistry.counter(ORDERS_TOTAL_METRIC, listOf(Tag.of(ORDER_STATUS_TAG, status.name)))
            .increment()
    }

    fun lastOrderTime(dateTime: OffsetDateTime) {
        meterRegistry.gauge(LAST_ORDER_TIME_METRIC, dateTime.toEpochSecond())
    }

    fun orderPriceSummary(totalPrice: TotalPrice) {
        orderPriceSummary.record(totalPrice.value.toDouble())
    }
}