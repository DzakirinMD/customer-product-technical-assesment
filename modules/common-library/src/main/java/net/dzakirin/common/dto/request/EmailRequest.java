package net.dzakirin.common.dto.request;

import java.util.UUID;

public record EmailRequest(
        UUID customerId,
        String customerEmail,
        int points)
{ }
