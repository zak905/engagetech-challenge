package com.engagetech.api.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.engagetech.api.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DefaultExhangeRateService implements ExchangeRateService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ObjectMapper mapper;
	
	@Value("${exchange.api}")
	String exchangeRateAPI;

	@Override
	@Cacheable("exchangeRates")
	public double getExchangeRate(String currency) throws Exception {
		String responseJson = restTemplate.getForObject(exchangeRateAPI, String.class);
		double rate = mapper.readTree(responseJson).path("rates").path(currency).asDouble();
		return rate;
	}

}
