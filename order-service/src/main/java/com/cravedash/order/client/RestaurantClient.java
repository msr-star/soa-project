package com.cravedash.order.client;

import com.cravedash.order.client.dto.MenuItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/items/{itemId}")
    MenuItemDto getMenuItemById(@PathVariable("itemId") Long itemId);
}
