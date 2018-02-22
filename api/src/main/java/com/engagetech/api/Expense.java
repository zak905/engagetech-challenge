package com.engagetech.api;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
public class Expense {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {@Parameter(name="uuid_gen_strategy_class", value="org.hibernate.id.uuid.CustomVersionOneStrategy")})
	private String id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate date;
	
	private double amount;
	
	@Formula("amount*0.2")
	private double vat;
	
	private String reason;

	public Expense() {
	}

	public String getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}

	public double getVat() {
		return vat;
	}

	public String getReason() {
		return reason;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}