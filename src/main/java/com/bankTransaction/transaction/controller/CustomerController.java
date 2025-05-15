package com.bankTransaction.transaction.controller;

import com.bankTransaction.transaction.model.dto.customer.CustomerDto;
import com.bankTransaction.transaction.model.dto.customer.AddCustomerRequestDto;
import com.bankTransaction.transaction.model.dto.customer.CustomerSyncRequest;
import com.bankTransaction.transaction.model.dto.customer.UpdateCustomerRequestDto;
import com.bankTransaction.transaction.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Sync Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer Sync is successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input, please try again")
    })
    @PostMapping("/sync")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto syncCustomer(@Valid @RequestBody CustomerSyncRequest request) {
        return customerService.syncCustomer(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getList() {
        return customerService.getList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerByID(@PathVariable Integer id) {
        return customerService.getCustomerByID(id);
    }

    @Operation(summary = "Add Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer added"),
            @ApiResponse(responseCode = "400", description = "Invalid input, please try again")
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto add(@Valid @RequestBody AddCustomerRequestDto addCustomerRequestDto) {
        return customerService.add(addCustomerRequestDto);
    }

    @Operation(summary = "Update Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input, please try again")
    })
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto update(@PathVariable Integer id,
                              @Valid @RequestBody UpdateCustomerRequestDto updateCustomerRequestDto) {
        return customerService.update(id, updateCustomerRequestDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        customerService.delete(id);
    }

}
