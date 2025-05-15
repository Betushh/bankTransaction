package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.exception.NotFoundException;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImpTest {

    @InjectMocks
    private CustomerServiceImp customerServiceImp;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Customer customer1;
    private Customer customer2;
    private Customer updatedcustomer;

    private CustomerDto customerDto1;
    private CustomerDto customerDto2;

    private AddCustomerRequestDto addCustomerRequestDto;
    private UpdateCustomerRequestDto updateCustomerRequestDto;
    private CustomerDto expectedDto;

    private Account account1;


    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Sahib");
        customer1.setEmail("sahibsahib123@Hmail.com");

        updatedcustomer = new Customer();
        updatedcustomer.setId(1L);
        updatedcustomer.setFirstName("Sahib");
        updatedcustomer.setEmail("sahibsahib123@Hmail.com");

        updateCustomerRequestDto = new UpdateCustomerRequestDto();
        updateCustomerRequestDto.setFirstName("Sahib1");

        expectedDto = new CustomerDto();
        expectedDto.setFirstName("Sahib1");
        expectedDto.setEmail(customer1.getEmail());


        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Nihad");
        customer2.setEmail("nihadnihad123@Hmail.com");

        customerDto1 = new CustomerDto();
        customerDto1.setFirstName("Sahib");
        customerDto1.setEmail("sahibsahib123@Hmail.com");

        customerDto2 = new CustomerDto();
        customerDto2.setFirstName("Nihad");
        customerDto2.setEmail("nihadnihad123@Hmail.com");

        addCustomerRequestDto = new AddCustomerRequestDto();
        addCustomerRequestDto.setFirstName("Sahib");
        addCustomerRequestDto.setEmail("sahibsahib123@Hmail.com");

        account1 = new Account();

        account1.setId(1L);
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setAccountNumber("ACCec2-c3c340ed106t");
        account1.setBalance(BigDecimal.valueOf(200));
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setCustomer(customer1);

    }

    @AfterEach
    void tearDown() {
        customer1=null;
        customer2=null;
        updateCustomerRequestDto = null;
        updatedcustomer = null;

        customerDto1=null;
        customerDto2=null;

        addCustomerRequestDto = null;

        account1 = null;
        expectedDto = null;

    }

    @Test
    void givenAllThenReturnThenOk() {

        List<Customer> customerList = List.of(customer1,customer2);
        List<CustomerDto> customerDtoList = List.of(customerDto1,customerDto2);

        when(customerRepository.findAll()).thenReturn(customerList);
        when(customerMapper.toCustomerDtoList(customerList)).thenReturn(customerDtoList);

        List<CustomerDto> customerDtoList1 = customerServiceImp.getList();

        assertThat(customerDtoList1.getFirst().getFirstName()).isEqualTo("Sahib");

        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toCustomerDtoList(customerList);
    }

    @Test
    void givenNameThenReturnThenOk() {

        when(customerRepository.findById(customer1.getId().intValue())).thenReturn(Optional.ofNullable(customer1));
        when(customerMapper.toCustomerDto(customer1)).thenReturn(customerDto1);

        CustomerDto customerDto = customerServiceImp.getCustomerByID(customer1.getId().intValue());

        assertThat(customerDto).isNotNull();
        assertThat(customerDto.getFirstName()).isEqualTo("Sahib");

        verify(customerRepository, times(1)).findById(customer1.getId().intValue());
        verify(customerMapper, times(1)).toCustomerDto(customer1);

    }

    @Test
    void givenCustomerThenSaveThenOk() {

        when(customerMapper.toCustomer(addCustomerRequestDto)).thenReturn(customer1);
        when(customerRepository.existsByEmail(customer1.getEmail())).thenReturn(false);

        when(customerRepository.save(customer1)).thenReturn(customer1);
//        when(accountRepository.save(account1)).thenReturn(account1);
        when(customerMapper.toCustomerDto(customer1)).thenReturn(customerDto1);

        CustomerDto customerDto = customerServiceImp.add(addCustomerRequestDto);

        assertThat(customerDto.getFirstName()).isEqualTo("Sahib");
        assertThat(account1.getCustomer()).isEqualTo(customer1);

        verify(customerRepository, times(1)).save(customer1);
//        verify(accountRepository, times(1)).save(account1);
        verify(customerMapper, times(1)).toCustomerDto(customer1);

    }

    @Test
    void givenCustomerThenUpdateThenOK(){

        when(customerRepository.findById(customer1.getId().intValue())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerMapper.toCustomerDto(any(Customer.class))).thenReturn(expectedDto);

        CustomerDto actualDto = customerServiceImp.update(customer1.getId().intValue(), updateCustomerRequestDto);

        assertThat(actualDto.getFirstName()).isEqualTo("Sahib1");
        verify(customerRepository).save(customer1);
        verify(customerMapper).toCustomerDto(customer1);

    }

    @Test
    void deleteInvalidCustomerThenThrowNotFoundException(){

        Integer nonExistingId = 3;
        when(customerRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerServiceImp.delete(nonExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerRepository).findById(nonExistingId);
        verify(customerRepository, never()).deleteById(any());

    }



}