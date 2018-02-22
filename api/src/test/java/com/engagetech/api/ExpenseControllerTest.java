package com.engagetech.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ExpenseControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ExpenseRepository repository;

	private MockMvc mvc;

	@Before
	public void setUp() {
	  mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void addExpenseTest() throws Exception {
		Expense expense = new Expense();
		expense.setDate(LocalDate.now());
		expense.setAmount(23);
		expense.setVat(20);
		expense.setReason("some reason");
		
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	
		mvc.perform(post("/app/expense").content(objectMapper.writeValueAsString(expense)).contentType(MediaType.APPLICATION_JSON))
		 .andExpect(status().isOk())
		 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		 .andExpect(jsonPath("$.date", equalTo(expense.getDate().format(formater).toString())))
		 .andExpect(jsonPath("$.amount", equalTo(expense.getAmount())))
		 .andExpect(jsonPath("$.vat", equalTo(expense.getVat())))
		 .andExpect(jsonPath("$.reason", equalTo(expense.getReason())));
	}
	
	@Test
	public void listExpenseTest() throws Exception {
		Expense expense = new Expense();
		expense.setDate(LocalDate.now());
		expense.setAmount(23);
		expense.setVat(20);
		expense.setReason("some reason");
		
		repository.save(expense);
		
		mvc.perform(get("/app/expense")).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andDo(result -> {
			List<Expense> expenseList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Collection<Expense>>() {
			});
			assertThat(expenseList).isNotEmpty();		
		});
		
	}

}
