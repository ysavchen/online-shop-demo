package com.example.orderservice.service

import com.example.orderservice.api.rest.model.Status
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Service

@Service
class MetricService(private val meterRegistry: MeterRegistry) {

    private companion object {
        const val ORDERS_TOTAL_METRIC = "online_shop_orders_total"
        const val ORDER_STATUS_TAG = "status"
    }

    fun countOrders(status: Status) {
        meterRegistry.counter(ORDERS_TOTAL_METRIC, listOf(Tag.of(ORDER_STATUS_TAG, status.name)))
            .increment()
    }
}