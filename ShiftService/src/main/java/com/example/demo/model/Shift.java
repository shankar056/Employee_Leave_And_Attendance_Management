package com.example.demo.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
import java.time.LocalDate;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shift {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 
    @Min(value = 1, message = "Employee ID must be a positive number")
    private int employeeId;
 
    @NotBlank(message = "Shift type must not be blank")
    @Pattern(regexp = "Day|Night", message = "Shift type must be either 'Day' or 'Night'")
    private String shiftType;
 
    private boolean swapRequested;
 
    @NotNull(message = "Date must not be null")
    private LocalDate date;
}