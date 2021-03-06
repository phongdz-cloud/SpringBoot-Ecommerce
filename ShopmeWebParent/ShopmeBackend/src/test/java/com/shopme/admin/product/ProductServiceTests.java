package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.product.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	@MockBean
	private ProductRepository repo;

	@InjectMocks
	private ProductService service;

	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null;
		String name = "Dell Inspirion 3000";

		Product product = new Product(id, name);

		Mockito.when(repo.findByName(name)).thenReturn(product);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("DuplicateName");
	}

	@Test
	public void testCheckUniqueInNewModeReturnDuplicateOK() {
		Integer id = null;
		String name = "Dell Inspirion 30001";

		Mockito.when(repo.findByName(name)).thenReturn(null);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueEditModeReturnDuplicateName() {
		Integer id = 2;
		String name = "Dell Inspirion 3000";

		Product product = new Product(2, name);

		Mockito.when(repo.findByName(name)).thenReturn(product);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueEditModeReturnOK() {
		Integer id = 2;
		String name = "Acer Aspire Desktop";

		Mockito.when(repo.findByName(name)).thenReturn(null);

		String result = service.checkUnique(id, name);

		assertThat(result).isEqualTo("OK");
	}


}
