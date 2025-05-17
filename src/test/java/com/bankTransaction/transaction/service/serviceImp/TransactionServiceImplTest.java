//package com.bankTransaction.transaction.service.serviceImp;
//
//import com.bankTransaction.transaction.enumeration.AccountStatus;
//import com.bankTransaction.transaction.enumeration.TransactionStatus;
//import com.bankTransaction.transaction.enumeration.TransactionType;
//import com.bankTransaction.transaction.exception.MismatchException;
//import com.bankTransaction.transaction.exception.NotFoundException;
//import com.bankTransaction.transaction.exception.OperationExpiredException;
//import com.bankTransaction.transaction.mapper.TransactionMapper;
//import com.bankTransaction.transaction.model.dto.transactiion.TransactionDto;
//import com.bankTransaction.transaction.model.entity.Account;
//import com.bankTransaction.transaction.model.entity.Transaction;
//import com.bankTransaction.transaction.repository.AccountRepository;
//import com.bankTransaction.transaction.repository.TransactionRepository;
//import com.bankTransaction.transaction.service.AccountService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TransactionServiceImplTest {
//
//    @InjectMocks
//    private TransactionServiceImpl transactionService;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @Mock
//    private TransactionMapper transactionMapper;
//
//    @Mock
//    private AccountService accountService;
//
//    private Transaction transaction1;
//    private Transaction transaction2;
//
//    private TransactionDto transactionDto;
//
//
//    private Account account1;
//    private Account account2;
//
//
//    @BeforeEach
//    void setUp() {
//
//        account1 = new Account();
//        account1.setId(1L);
//        account1.setAccountStatus(AccountStatus.ACTIVE);
//        account1.setAccountNumber("ACCec2-c3c340ed106t");
//        account1.setBalance(BigDecimal.valueOf(200));
//        account1.setAccountStatus(AccountStatus.ACTIVE);
//
//        account2 = new Account();
//        account2.setId(2L);
//        account2.setAccountStatus(AccountStatus.INACTIVE);
//        account2.setAccountNumber("ACCec2-c3c340ed103k");
//        account2.setBalance(BigDecimal.valueOf(200));
//
//        transactionDto = new TransactionDto();
//        transactionDto.setId(1L);
//        transactionDto.setAmount(BigDecimal.valueOf(200));
//        transactionDto.setTransactionType(TransactionType.TOP_UP);
//        transactionDto.setTransactionStatus(TransactionStatus.SUCCESS);
//
//        transaction1 = new Transaction();
//        transaction1.setId(1L);
//        transaction1.setAmount(BigDecimal.valueOf(200));
//        transaction1.setTransactionType(TransactionType.TOP_UP);
//        transaction1.setTransactionStatus(TransactionStatus.PENDING);
//        transaction1.setAccount(account1);
//
//        transaction2 = new Transaction();
//        transaction2.setId(1L);
//        transaction2.setAmount(BigDecimal.valueOf(200));
//        transaction2.setTransactionType(TransactionType.PURCHASE);
//        transaction2.setTransactionStatus(TransactionStatus.PENDING);
//        transaction2.setAccount(account2);
//    }
//
//    @AfterEach
//    void tearDown() {
//
//        transaction1 = null;
//        transaction2 = null;
//
//        transactionDto = null;
//
//        account1 = null;
//        account2 = null;
//
//    }
//
//    @Test
//    void createValidTransaction() {
//
//        when(accountRepository.findAccountByAccountNumber(account1.getAccountNumber())).thenReturn(account1);
//        transaction1.setAccount(account1);
//
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction1);
//        when(transactionMapper.toTransactionDto(transaction1)).thenReturn(transactionDto);
//
//        TransactionDto transactionDto1 = transactionService.createTransaction(account1.getAccountNumber(),
//                BigDecimal.valueOf(200),
//                TransactionType.TOP_UP);
//
//
//        assertThat(transactionDto1.getTransactionType()).isEqualTo(transaction1.getTransactionType());
//
//        verify(accountRepository, times(1)).findAccountByAccountNumber(account1.getAccountNumber());
//        verify(transactionRepository, times(1)).save(any(Transaction.class));
//        verify(transactionMapper, times(1)).toTransactionDto(transaction1);
//
//    }
//
//    @Test
//    void createInvalidTransaction() {
//        String invalidAccountNumber = "hjvdfjvfj";
//
//        when(accountRepository.findAccountByAccountNumber(invalidAccountNumber)).thenReturn(null);
//
//        assertThatThrownBy(() -> transactionService.createTransaction(invalidAccountNumber,
//                BigDecimal.valueOf(200),
//                TransactionType.TOP_UP))
//                .isInstanceOf(NotFoundException.class)
//                .hasMessage("Account not found");
//
//        verify(accountRepository).findAccountByAccountNumber(invalidAccountNumber);
//        verify(transactionRepository, never()).save(any());
//        verify(transactionMapper, never()).toTransactionDto(any());
//
//    }
//
//    @Test
//    void topUpBalanceTransaction() {
//        when(transactionRepository.findById(1L)).thenReturn(Optional.ofNullable(transaction1));
//        when(transactionMapper.toTransactionDto(transaction1)).thenReturn(transactionDto);
//
//        TransactionDto transactionDto1 = transactionService.topUpBalanceTransaction(transaction1.getId(), TransactionStatus.SUCCESS);
//
//        assertThat(transactionDto1.getTransactionStatus()).isEqualTo(TransactionStatus.SUCCESS);
//
//        verify(transactionRepository, times(1)).findById(transaction1.getId());
//        verify(accountService).increaseAccountBalance(account1.getAccountNumber(), transaction1.getAmount());
//        verify(transactionRepository).save(transaction1);
//        verify(transactionMapper).toTransactionDto(transaction1);
//
//    }
//
//    @Test
//    void givenInvalidIdThenTopUpThenThrowNotFoundException(){
//        Long invalidId = 3L;
//
//        when(transactionRepository.findById(invalidId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(()->transactionService.topUpBalanceTransaction(invalidId, TransactionStatus.SUCCESS))
//                .isInstanceOf(NotFoundException.class)
//                .hasMessage("Transaction not found");
//
//        verify(transactionRepository).findById(invalidId);
//    }
//
//    @Test
//    void givenNonTopUpTransactionThenTopUpThenThrowMismatchException(){
//        transaction1.setTransactionType(TransactionType.REFUND);
//
//        when(transactionRepository.findById(transaction1.getId())).thenReturn(Optional.ofNullable(transaction1));
//
//        assertThatThrownBy(()->transactionService.topUpBalanceTransaction(transaction1.getId(), transaction1.getTransactionStatus()))
//                .isInstanceOf(MismatchException.class)
//                .hasMessage("Transaction type is not matched");
//
//        verify(transactionRepository).findById(transaction1.getId());
//    }
//
//    @Test
//    void givenAlreadyDoneTransactionThenTopUpThenThrowOperationExpiredException(){
//        transaction1.setTransactionStatus(TransactionStatus.REFUNDED);
//
//        when(transactionRepository.findById(transaction1.getId())).thenReturn(Optional.ofNullable(transaction1));
//
//        assertThatThrownBy(()->transactionService.topUpBalanceTransaction(transaction1.getId(), TransactionStatus.SUCCESS))
//                .isInstanceOf(OperationExpiredException.class)
//                .hasMessage("Transaction operation is already done");
//
//        verify(transactionRepository).findById(transaction1.getId());
//    }
//
//    @Test
//    void purchaseBalanceTransaction() {
//        when(transactionRepository.findById(transaction2.getId())).thenReturn(Optional.ofNullable(transaction2));
//        when(transactionMapper.toTransactionDto(transaction2)).thenReturn(transactionDto);
//
//        TransactionDto transactionDto1 = transactionService.purchaseBalanceTransaction(transaction2.getId(), TransactionStatus.SUCCESS);
//
//        assertThat(transactionDto1.getTransactionStatus()).isEqualTo(TransactionStatus.SUCCESS);
//
//        verify(transactionRepository, times(1)).findById(transaction2.getId());
//        verify(accountService).decreaseAccountBalance(account2.getAccountNumber(), transaction2.getAmount());
//        verify(transactionRepository).save(transaction2);
//        verify(transactionMapper).toTransactionDto(transaction2);
//    }
//
//    @Test
//    void refundBalanceTransaction() {
//        transaction2.setTransactionStatus(TransactionStatus.SUCCESS);
//        when(transactionRepository.findById(transaction2.getId())).thenReturn(Optional.of(transaction2));
//        when(transactionMapper.toTransactionDto(transaction2)).thenReturn(transactionDto);
//
//        TransactionDto result = transactionService.refundBalanceTransaction(transaction2.getId(), TransactionStatus.SUCCESS);
//
//        assertThat(result.getTransactionStatus()).isEqualTo(TransactionStatus.SUCCESS);
//
//        verify(transactionRepository).findById(transaction2.getId());
//        verify(accountService).refundAccountBalance(account2.getAccountNumber(), transaction2.getAmount());
//        verify(transactionRepository).save(transaction2);
//        verify(transactionMapper).toTransactionDto(transaction2);
//    }
//
//    @Test
//    void delete() {
//
//        transactionService.delete(transaction1.getId().intValue());
//
//        verify(transactionRepository, times(1)).deleteById(transaction1.getId().intValue());
//
//
//    }
//}