package microservices.com.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import microservices.com.inventoryservice.dto.InventoryResponse;
import microservices.com.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository
                .findBySkuCodeIn(skuCode)
                .stream()
                .map(inv ->
                        InventoryResponse.builder()
                                .skuCode(inv.getSkuCode())
                                .isInStock(inv.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
