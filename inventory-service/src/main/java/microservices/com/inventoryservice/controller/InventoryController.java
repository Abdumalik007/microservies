package microservices.com.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import microservices.com.inventoryservice.dto.InventoryResponse;
import microservices.com.inventoryservice.repository.InventoryRepository;
import microservices.com.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        System.out.println(System.getProperty("server.port"));
        return inventoryService.isInStock(skuCode);
    }
}
