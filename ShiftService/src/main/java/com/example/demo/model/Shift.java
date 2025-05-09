package com.example.demo.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shift {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int employeeId;
    private String shiftType;
    private String shiftDate;
    private String shiftTime;
    private boolean swapRequested;
}