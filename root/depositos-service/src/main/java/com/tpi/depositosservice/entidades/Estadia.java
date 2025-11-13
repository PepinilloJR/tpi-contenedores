package com.tpi.depositosservice.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estadias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadia;

    // Relación con Deposito (la entidad de tu microservicio)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDeposito", nullable = false)
    private Deposito deposito; 

    
    @Column(name = "idTramo", nullable = false)
    private Long idTramo; // FK a TRAMOS del servicio de solicitudes/tramos/rutas

    private java.time.LocalDateTime fechaHoraEntrada;
    private java.time.LocalDateTime fechaHoraSalida;
}