package com.julian.spring_demo.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    @Async
    @EventListener
    public void onProductCreated (ProductCreatedEvent event) {
        log.info("EVENT: product created - name: {}, price: ${}",
                event.getProduct().getName(),
                event.getProduct().getPrice());

        try {
            Thread.sleep(2000);
            log.info("EVENT [ASYNC]: Post-processing completed for product: {}",
                    event.getProduct().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
