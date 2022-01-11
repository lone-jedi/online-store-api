package com.yarkin.onlineshop.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yarkin.onlineshop.entity.Product;
import com.yarkin.onlineshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(value = ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private List<Product> products = List.of(
            Product.builder().id(1).name("Car").price(10500).creationDate(LocalDateTime.of(
                            2020, 10, 21, 22, 0, 22, 32))
                    .build(),
            Product.builder().id(2).name("Apple").price(13.21).creationDate(LocalDateTime.of(
                            2022, 1, 3, 13, 23, 4, 39))
                    .build(),
            Product.builder().id(3).name("Laptop").price(150.12).creationDate(LocalDateTime.of(
                            2012, 12, 13, 23, 20, 4, 39))
                    .build()
            );

    @Test
    public void getAllProducts() throws Exception {
        Mockito.when(productService.getAll()).thenReturn(products);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"id\":1,\"name\":\"Car\",\"price\":10500.0,\"creationDate\":\"2020-10-21T22:00:22.000000032\"},{\"id\":2,\"name\":\"Apple\",\"price\":13.21,\"creationDate\":\"2022-01-03T13:23:04.000000039\"},{\"id\":3,\"name\":\"Laptop\",\"price\":150.12,\"creationDate\":\"2012-12-13T23:20:04.000000039\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getAllProductsWhenProductsNotExists() throws Exception {
        Mockito.when(productService.getAll()).thenReturn(Collections.emptyList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        JSONAssert.assertEquals("[]", response.getContentAsString(), false);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getProductById() throws Exception {
        Mockito.when(productService.get(3)).thenReturn(products.get(2));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/3")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        String expected = "{\"id\":3,\"name\":\"Laptop\",\"price\":150.12,\"creationDate\":\"2012-12-13T23:20:04.000000039\"}";
        JSONAssert.assertEquals(expected, response.getContentAsString(), false);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getProductByUnknownId() throws Exception {
        Mockito.when(productService.get(5)).thenThrow(RuntimeException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/5")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void addNewProduct() throws Exception {
        Product expectedProduct = Product.builder().id(32).name("Test").price(21.24).creationDate(LocalDateTime.now()).build();

        Mockito.when(productService.add(Mockito.any(Product.class))).thenReturn(expectedProduct);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\", \"price\":21.24}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        String expected = "{id:4, message:\"Product Test successfully added\"}";
        JSONAssert.assertEquals(expected, response.getContentAsString(), false);

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void addNewProductWhenMissingRequiredNameOrPrice() throws Exception {
        Product expectedProduct = Product.builder().id(32).name("Test").price(21.24).creationDate(LocalDateTime.now()).build();

        Mockito.when(productService.add(Mockito.any(Product.class))).thenReturn(expectedProduct);

        MockHttpServletResponse responseWhereMissingPrice = mockMvc.perform(
                MockMvcRequestBuilders
                .post("/products")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\"}")
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        MockHttpServletResponse responseWhereMissingName = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"price\":21.24}")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        MockHttpServletResponse responseWhereMissingNameAndPrice = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        MockHttpServletResponse responseWithIncorrectNaming = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"Name\":\"Test\", \"Price\":21.24}")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        JSONAssert.assertEquals("{message:\"Error! Missing name parameter\"}", responseWhereMissingName.getContentAsString(), false);
        JSONAssert.assertEquals("{message:\"Error! Missing name parameter\"}", responseWhereMissingNameAndPrice.getContentAsString(), false);
        JSONAssert.assertEquals("{message:\"Error! Missing name parameter\"}", responseWithIncorrectNaming.getContentAsString(), false);

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseWhereMissingName.getStatus());
        assertEquals(HttpStatus.CREATED.value(), responseWhereMissingPrice.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseWhereMissingNameAndPrice.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseWithIncorrectNaming.getStatus());
    }
}
