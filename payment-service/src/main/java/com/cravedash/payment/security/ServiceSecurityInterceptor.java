package com.cravedash.payment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Real Defense-in-Depth Security Interceptor:
 * Blocks unauthenticated direct calls to payment-service (:8084).
 */
@Component
public class ServiceSecurityInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceSecurityInterceptor.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.gateway.internal-secret}")
    private String gatewayInternalSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 1. Allow preflight CORS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 2. Allow actuator / health pings
        if (uri.startsWith("/actuator") || uri.endsWith("/health")) {
            return true;
        }

        // 3. Authenticate via Gateway Secret (Internal Service Mesh or Feign)
        String gatewaySecretHeader = request.getHeader("X-Gateway-Secret");
        if (gatewaySecretHeader != null && gatewaySecretHeader.equals(gatewayInternalSecret)) {
            String email = request.getHeader("X-User-Email");
            String role = request.getHeader("X-User-Role");
            request.setAttribute("userEmail", email != null ? email : "");
            request.setAttribute("userRole", role != null ? role : "");
            return true;
        }

        // 4. Authenticate via direct JWT Bearer token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            try {
                Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                request.setAttribute("userEmail", claims.getSubject());
                request.setAttribute("userRole", claims.get("role"));
                return true;
            } catch (Exception e) {
                logger.warn("Direct JWT validation failed in payment-service: {}", e.getMessage());
            }
        }

        // Direct unauthenticated call on port 8084 blocked!
        logger.warn("Direct unauthenticated access blocked on payment-service {}: {}", method, uri);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized: Direct access to payment-service blocked. Valid JWT Bearer token or Gateway authentication required.\"}");
        return false;
    }
}
