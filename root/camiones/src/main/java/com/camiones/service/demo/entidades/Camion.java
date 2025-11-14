package com.camiones.service.demo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String patente;

    @Column(nullable = false, length = 80)
    private String nombreTransportista;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false)
    private Double capacidadPesoKg;

    @Column(nullable = false)
    private Double capacidadVolumenM3;

    @Column(nullable = false)
    private Double costoPorKm;

    @Column(nullable = false)
    private Double consumoCombustibleLx100km;

    @Column(nullable = false)
    private Boolean disponible;
}
