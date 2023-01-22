package microservices.com.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.com.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");



	@DynamicPropertySource
	static public void setProperties(DynamicPropertyRegistry registry){
		System.out.println(mongoDBContainer.getReplicaSetUrl());
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}


	@Test
	void createProduct() throws Exception {

		String requestContent = objectMapper.writeValueAsString(getProductRequest());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestContent)
		).andExpect(status().isCreated());

	}

	public ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("Iphone 12")
				.description("This phone is really amazing")
				.price(BigDecimal.valueOf(1300))
				.build();
	}

}
