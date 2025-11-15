package com.ceos22.cgv_clone.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMetrics {

    private final MeterRegistry meterRegistry;

    public void incrementPaymentSuccess() {
        Counter.builder("payment.total")
                .tag("status", "success")
                .description("Total successful payments")
                .register(meterRegistry)
                .increment();
    }

    public void incrementPaymentFailure() {
        Counter.builder("payment.total")
                .tag("status", "failure")
                .description("Total failed payments")
                .register(meterRegistry)
                .increment();
    }

    public void recordStockDecrease(int quantity) {
        Counter.builder("stock.decrease.total")
                .description("Stock decrease events")
                .register(meterRegistry)
                .increment(quantity);
    }
}