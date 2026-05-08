package com.julian.spring_demo.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    @EventListener
    public void onProductCreated (ProductCreatedEvent event) {
        log.info("EVENT: product created - name: {}, price: ${}",
                event.getProduct().getName(),
                event.getProduct().getPrice());
    }
}
