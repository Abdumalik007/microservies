package microservices.com.orderservie.service;

import lombok.RequiredArgsConstructor;
import microservices.com.orderservie.dto.InventoryResponse;
import microservices.com.orderservie.dto.OrderLineItemsDto;
import microservices.com.orderservie.dto.OrderRequest;
import microservices.com.orderservie.model.Order;
import microservices.com.orderservie.model.OrderLineItems;
import microservices.com.orderservie.repository.OrderRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCode =
                orderLineItems.stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponses = webClient.method(HttpMethod.GET)
                .uri(
                        "http://localhost:8002/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCode).build()
                )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean result =
                Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);

        if(result)
            orderRepository.save(order);
        else
            throw new IllegalStateException("Product is not in stock, please try later");

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
