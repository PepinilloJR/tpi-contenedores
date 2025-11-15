package com.commonlib.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false)
    private Double precioPorKm;

    @Column(nullable = true)
    private Double precioFijo;

    @Column(nullable = true, length = 10)
    private String moneda;

    @Column(nullable = false)
    private LocalDate vigenciaDesde;

    @Column(nullable = true)
    private LocalDate vigenciaHasta;

    @Column(nullable = false)
    private Boolean activo = true;
}