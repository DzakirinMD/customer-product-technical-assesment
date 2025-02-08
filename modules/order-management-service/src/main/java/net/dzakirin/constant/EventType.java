package net.dzakirin.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    ORDER_CREATED("Order created");

    public final String eventName;
}
