package com.bankTransaction.transaction.controller;

import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.service.CustomerService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    private Customer customer1;

    private CustomerDto updatedCustomerDto;

    private CustomerDto customerDto1;
    private CustomerDto customerDto2;

    private AddCustomerRequestDto addCustomerRequestDto;
    private UpdateCustomerRequestDto updateCustomerRequestDto;

    @BeforeEach
    void setUp() {

        customer1 = new Customer();
        customer1.setId(1L);

        customerDto1 = new CustomerDto();
        customerDto1.setFirstName("customer1");
        customerDto1.setEmail("customer1@gmail.com");

        customerDto2 = new CustomerDto();
        customerDto2.setFirstName("customer2");
        customerDto2.setEmail("customer2@gmail.com");

        addCustomerRequestDto = new AddCustomerRequestDto();
        addCustomerRequestDto.setFirstName("customer1");
        addCustomerRequestDto.setEmail("customer1@gmail.com");

        updateCustomerRequestDto = new UpdateCustomerRequestDto();
        updateCustomerRequestDto.setFirstName("customer11");
        updateCustomerRequestDto.setEmail("customer11@gmail.com");

        updatedCustomerDto = new CustomerDto();
        updatedCustomerDto.setFirstName("customer11");
        updatedCustomerDto.setEmail("customer11@gmail.com");
    }

    @AfterEach
    void tearDown() {
        customerService = null;
        updateCustomerRequestDto = null;

        customer1 = null;

        customerDto1 = null;
        customerDto2 = null;
        updatedCustomerDto = null;

        addCustomerRequestDto = null;
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CustomerService customerService() {
            return mock(CustomerService.class);
        }
    }

    @Test
    void givenAllThenReturnThenOk() throws Exception{
        List<CustomerDto> customerDtoList = List.of(customerDto1,customerDto2);

        when(customerService.getList()).thenReturn(customerDtoList);

        mockMvc.perform(get("/customers",1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("customer1"))
                .andExpect(jsonPath("$[0].email").value("customer1@gmail.com"))
                .andExpect(jsonPath("$[1].firstName").value("customer2"))
                .andExpect(jsonPath("$[1].email").value("customer2@gmail.com"))
                .andDo(print());

        verify(customerService, times(1)).getList();
    }

    @Test
    void givenIdThenReturnThenOk() throws Exception {

        when(customerService.getCustomerByID(customer1.getId().intValue())).thenReturn(customerDto1);

        mockMvc.perform(get("/customers/id?id=1", 2)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("customer1"))
                .andExpect(jsonPath("$.email").value("customer1@gmail.com"))
                .andDo(print());

        verify(customerService, times(1)).getCustomerByID(1);
    }

        @Test
    void givenCustomerThenSaveThenOk() throws Exception {

        when(customerService.add(addCustomerRequestDto)).thenReturn(customerDto1);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCustomerRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("customer1"))
                .andExpect(jsonPath("$.email").value("customer1@gmail.com"))
                .andDo(print());

        verify(customerService, times(1)).add(addCustomerRequestDto);
    }

    @Test
    void givenCustomerThenUpdateThenOk() throws Exception{
       when(customerService.update(1, updateCustomerRequestDto)).thenReturn(updatedCustomerDto);

       ObjectMapper objectMapper = new ObjectMapper();

       mockMvc.perform(patch("/customers/{id}",1)
               .accept(MediaType.APPLICATION_JSON)
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(updateCustomerRequestDto)))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value("customer11"))
               .andExpect(jsonPath("$.email").value("customer11@gmail.com"))
               .andDo(print());

       verify(customerService, times(1)).update(1, updateCustomerRequestDto);

    }

    @Test
    void givenIdThenDeleteThenOk() throws Exception {

            mockMvc.perform(delete("/customers/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(customerService, times(1)).delete(1);
        }

}