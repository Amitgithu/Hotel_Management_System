package com.example.apigatewayservice.filter;

import com.example.apigatewayservice.exception.ForbiddenAccessException;
import com.example.apigatewayservice.exception.UnauthorizedUserException;
import com.example.apigatewayservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter{

    private final JwtUtil jwtUtil;

    private final List<String> openApiEndpoints = List.of(

            "/swagger-ui/",
            "/v3/api-docs/",
            "/api/auth/"
    );

    private final Map<String, List<String>> routeRoleMap = Map.of(
            "/api/rooms", List.of("ADMIN", "MANAGER"),
            "/api/rooms/available", List.of("ADMIN", "MANAGER", "RECEPTIONIST"),
            "/api/inventory", List.of("ADMIN", "MANAGER"),
            "/api/bookings", List.of("ADMIN", "RECEPTIONIST"),
            "/api/guests/", List.of("ADMIN", "RECEPTIONIST"),
            "/api/staff", List.of("ADMIN", "MANAGER"),
            "/api/bills", List.of("ADMIN", "RECEPTIONIST")

    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        if (isOpenApi(path)) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty("Authorization");

        if (authHeaders.isEmpty()) {
            throw new UnauthorizedUserException("Authorization header is missing");
        }

        String token = authHeaders.get(0);

        try {
            jwtUtil.validateToken(token);
            String role = jwtUtil.extractRole(token);

            if (!isAuthorized(path, role)) {
                throw new ForbiddenAccessException("Access Denied: Role " + role + " is not permitted for this route.");
            }

        } catch (UnauthorizedUserException | ForbiddenAccessException e) {
            throw e; // will be handled by GlobalExceptionHandler
        } catch (Exception e) {
            throw new UnauthorizedUserException("Invalid or expired token");
        }

        return chain.filter(exchange);
    }

    private boolean isOpenApi(String path) {
        return openApiEndpoints.stream().anyMatch(path::contains);
    }

    private boolean isAuthorized(String path, String role) {
        System.out.println(path + " : " + role);
        for (Map.Entry<String, List<String>> entry : routeRoleMap.entrySet()) {
            String route = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            if (path.startsWith(route)) {
                return allowedRoles.contains(role.toUpperCase());
            }
        }
        return false;
    }

}
