package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.mapper.ProductMapper;
import com.gokhancomert.b2bapplication.repository.ProductRepository;
import com.gokhancomert.b2bapplication.service.ProductService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    // @Mock: Bu sınıfların sahte (mock) versiyonlarını oluşturur. Bu sahte nesneler, gerçek veritabanına veya implementasyona sahip değildir.
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    // @InjectMocks: Test edeceğimiz asıl sınıfı belirtir. Mockito, yukarıda @Mock ile oluşturulan sahte nesneleri otomatik olarak bu productService nesnesinin içine enjekte eder (constructor'ına verir).
    @InjectMocks
    private ProductService productService;
}
