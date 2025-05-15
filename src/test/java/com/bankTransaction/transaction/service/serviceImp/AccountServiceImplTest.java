package com.bankTransaction.transaction.service.serviceImp;

import com.bankTransaction.transaction.enumeration.AccountStatus;
import com.bankTransaction.transaction.mapper.AccountMapper;
import com.bankTransaction.transaction.mapper.CustomerMapper;
import com.bankTransaction.transaction.model.dto.account.AccountDto;
import com.bankTransaction.transaction.model.dto.account.AddAccountRequestDto;
import com.bankTransaction.transaction.model.entity.Account;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.repository.AccountRepository;
import com.bankTransaction.transaction.repository.CustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private AddAccountRequestDto addAccountRequestDto;


    private Customer customer1;
    private Customer customer2;

    private Account account1;
    private Account account2;

    private AccountDto accountDto1;
    private AccountDto accountDto2;


    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setId(1L);
        account1 = new Account();

        account1.setId(1L);
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setAccountNumber("ACCec2-c3c340ed106t");
        account1.setBalance(BigDecimal.valueOf(200));
        account1.setAccountStatus(AccountStatus.ACTIVE);
        account1.setCustomer(customer1);

        accountDto1 = new AccountDto();
        accountDto1.setAccountNumber("ACCec2-c3c340ed106t");
        accountDto1.setBalance(BigDecimal.valueOf(200));
        accountDto1.setAccountStatus(AccountStatus.ACTIVE);
        accountDto1.setCustomer(customer1);


        customer2 = new Customer();
        customer2.setId(2L);
        account2 = new Account();

        account2 = new Account();
        account2.setId(2L);
        account2.setAccountStatus(AccountStatus.INACTIVE);
        account2.setAccountNumber("ACCec2-c3c340ed103k");
        account2.setBalance(BigDecimal.valueOf(200));
        account2.setCustomer(customer2);

        accountDto2 = new AccountDto();
        accountDto2.setAccountNumber("ACCec2-c3c340ed103k");
        accountDto2.setBalance(BigDecimal.valueOf(200));
        accountDto2.setAccountStatus(AccountStatus.INACTIVE);
        accountDto2.setCustomer(customer2);

    }

    @AfterEach
    void tearDown() {
        customer1 = null;
        customer2 = null;

        account1 = null;
        account2 = null;

        accountDto1 = null;
        accountDto2 = null;
    }

    @Test
    void givenAllThenReturnThenOk() {
        List<Account> accountList = List.of(account1, account2);
        List<AccountDto> accountDtoList = List.of(accountDto1, accountDto2);

        when(accountRepository.findAll()).thenReturn(accountList);
        when(accountMapper.toAccountDtoList(anyList())).thenReturn(accountDtoList);

        List<AccountDto> accountDtoList1 = accountService.getList();

        assertThat(accountDtoList1.getFirst().getAccountNumber()).isEqualTo("ACCec2-c3c340ed106t");

        verify(accountRepository, times(1)).findAll();
        verify(accountMapper, times(1)).toAccountDtoList(anyList());
    }

    @Test
    void givenIdThenReturnActiveThenOk() {
        customer1.setAccounts(List.of(account1, account2));

        List<Account> activeAccountList = List.of(account1);
        List<AccountDto> activeAccountDtoList = List.of(accountDto1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer1));
        when(accountMapper.toAccountDtoList(activeAccountList)).thenReturn(activeAccountDtoList);

        List<AccountDto> accountDtos = accountService.getActiveAccountsByCustomerId(1);

        assertThat(accountDtos).isNotNull();
        assertThat(accountDtos.getFirst().getAccountNumber()).isEqualTo("ACCec2-c3c340ed106t");

        verify(customerRepository, times(1)).findById(1);
        verify(accountMapper, times(1)).toAccountDtoList(activeAccountList);
    }

    @Test
    void givenIdThenReturnAllThenOk() {

        customer1.setAccounts(List.of(account1, account2));

        List<Account> accountList = List.of(account1, account2);
        List<AccountDto> accountDtoList = List.of(accountDto1, accountDto2);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer1));
        when(accountMapper.toAccountDtoList(accountList)).thenReturn(accountDtoList);

        List<AccountDto> accountDtos = accountService.getAccountsByCustomerId(1);

        assertThat(accountDtos).isNotNull();
        assertThat(accountDtos.getFirst().getAccountNumber()).isEqualTo("ACCec2-c3c340ed106t");

        verify(customerRepository, times(1)).findById(1);
        verify(accountMapper, times(1)).toAccountDtoList(accountList);
    }

    @Test
    void givenAccountThenSaveThenOk() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer1));

        when(accountRepository.save(any())).thenReturn(account1);
        when(accountMapper.toAccountDto(any(Account.class))).thenReturn(accountDto1);

        Integer customerId = customer1.getId().intValue();

        AccountDto accountDto = accountService.add(customerId);

        assertThat(accountDto.getAccountNumber()).isEqualTo("ACCec2-c3c340ed106t");

        verify(accountRepository, times(1)).save(any());
        verify(accountMapper,times(1)).toAccountDto(any(Account.class));
    }

    @Test
    void givenAccountNumberAndAmountThenIncreaseAccount(){

        BigDecimal amount = BigDecimal.valueOf(200);
        when(accountRepository.findAccountByAccountNumber(account1.getAccountNumber())).thenReturn(account1);
        account1.setBalance(amount);
        when(accountRepository.save(account1)).thenReturn(account1);
        when(accountMapper.toAccountDto(account1)).thenReturn(accountDto1);

        AccountDto accountDto = accountService.increaseAccountBalance(account1.getAccountNumber(),amount);

        assertThat(accountDto.getBalance()).isBetween(BigDecimal.valueOf(200),BigDecimal.valueOf(800));

        verify(accountRepository, times(1)).save(account1);
        verify(accountMapper, times(1)).toAccountDto(account1);


    }

    @Test
    void givenAccountNumberAndAmountThenDecreaseAccount(){

        BigDecimal amount = BigDecimal.valueOf(100);
        when(accountRepository.findAccountByAccountNumber(account1.getAccountNumber())).thenReturn(account1);
        account1.setBalance(amount);
        when(accountRepository.save(account1)).thenReturn(account1);
        when(accountMapper.toAccountDto(account1)).thenReturn(accountDto1);

        AccountDto accountDto = accountService.decreaseAccountBalance(account1.getAccountNumber(),amount);

        assertThat(accountDto.getBalance()).isBetween(BigDecimal.valueOf(50),BigDecimal.valueOf(200));

        verify(accountRepository, times(1)).save(account1);
        verify(accountMapper, times(1)).toAccountDto(account1);
    }

    @Test
    void givenAccountNumberAndAmountRefundAccount(){

        BigDecimal amount = BigDecimal.valueOf(100);
        when(accountRepository.findAccountByAccountNumber(account1.getAccountNumber())).thenReturn(account1);
        BigDecimal newAmount = account1.getBalance().add(amount);
        account1.setBalance(newAmount);
        when(accountRepository.save(account1)).thenReturn(account1);
        when(accountMapper.toAccountDto(account1)).thenReturn(accountDto1);

        AccountDto accountDto = accountService.refundAccountBalance(account1.getAccountNumber(),newAmount);

        assertThat(accountDto.getBalance()).isBetween(BigDecimal.valueOf(50),BigDecimal.valueOf(300));

        verify(accountRepository, times(1)).save(account1);
        verify(accountMapper, times(1)).toAccountDto(account1);
    }

    @Test
    void deleteAccountThenOk(){

        Integer id = account1.getId().intValue();

        accountService.delete(id);

        verify(accountRepository, times(1)).deleteById(id);

    }
}