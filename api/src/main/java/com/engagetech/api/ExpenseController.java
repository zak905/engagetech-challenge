package com.engagetech.api;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.engagetech.api.service.ExchangeRateService;

@RestController("/app/expenses")
@EnableCaching
public class ExpenseController {
	
	@Autowired
	ExpenseRepository expenseRepository;
	
	@Autowired
	ExchangeRateService exchangeRateService;
	
	@GetMapping
	public ResponseEntity getExpenses(@RequestParam(defaultValue="GBP", required=true) String currency) throws Exception {
		
		List<Expense> expenses = expenseRepository.findAll();
		
        if (!Objects.equals(currency, "GBP")) {
        	double rate = exchangeRateService.getExchangeRate(currency);
        	expenses = expenses.stream().peek(expense -> {expense.setAmount(expense.getAmount() * rate); expense.setVat(expense.getVat() * rate);}).collect(Collectors.toList());
		}
		return ResponseEntity.ok(expenses);
	}
	
	@PostMapping
	public ResponseEntity saveExpense(@RequestBody Expense expense, @RequestParam(defaultValue="GBP", required=true) String currency) throws Exception {
		if (!Objects.equals(currency, "GBP")) {
			double rate = exchangeRateService.getExchangeRate(currency);
			expense.setAmount(expense.getAmount() / rate);
			expense.setVat(expense.getVat() / rate);
		}
		return ResponseEntity.ok(expenseRepository.save(expense));
	}


}