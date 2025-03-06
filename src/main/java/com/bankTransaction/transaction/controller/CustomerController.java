package com.bankTransaction.transaction.controller;

import com.bankTransaction.transaction.model.dto.CustomerDto;
import com.bankTransaction.transaction.model.dto.request.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.request.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.model.entity.Customer;
import com.bankTransaction.transaction.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getList() {
        return customerService.getList();
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerByID(@RequestParam Integer id) {
        return customerService.getCustomerByID(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto add(@RequestBody AddCustomerRequestDto addCustomerRequestDto) {
        return customerService.add(addCustomerRequestDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto update(@PathVariable Integer id,@RequestBody UpdateCustomerRequestDto updateCustomerRequestDto) {
        return customerService.update(id,updateCustomerRequestDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Integer id) {
        customerService.delete(id);
    }


}
