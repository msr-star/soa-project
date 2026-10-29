package com.cravedash.order.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Propagates internal service mesh credentials across Feign clients
 * (Order Service -> Restaurant Service, Order Service -> Payment Service).
 */
@Component
public class FeignAuthRequestInterceptor implements RequestInterceptor {

    @Value("${app.gateway.internal-secret:craveDashInternalMeshSecretKey2026Secure}")
    private String gatewayInternalSecret;

    @Override
    public void apply(RequestTemplate template) {
        template.header("X-Gateway-Secret", gatewayInternalSecret);
        template.header("X-User-Role", "INTERNAL_SERVICE");
    }
}
