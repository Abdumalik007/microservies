package microservices.com.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import microservices.com.inventoryservice.dto.InventoryResponse;
import microservices.com.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Wait started");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Wait finished");

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
