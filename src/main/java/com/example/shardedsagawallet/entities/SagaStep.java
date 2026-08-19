package com.example.shardedsagawallet.entities;

import org.apache.calcite.model.JsonType;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="saga_step")
public class SagaStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="saga_instance_id",nullable=false)
    private Long sagaInstanceId;

    @Column(name="step_name",nullable=false)
    private String stepName;

    @Column(name="status",nullable=false)
    private StepStatus status;

    @Column(name="error_message",nullable=true)
    private String errorMessage;

    @Type(JsonType.class)
    @Column(name="step_data",columnDefinition="json")
    private String stepData;
}
