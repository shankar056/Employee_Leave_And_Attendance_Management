package com.example.demo.model;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.time.LocalDateTime;

@Entity

@Data

@NoArgsConstructor

@AllArgsConstructor

public class Attendance {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	private int employeeId;

	private LocalDate date;

	private LocalDateTime clockIn;

	private LocalDateTime clockOut;

	private Long workHours;

}
