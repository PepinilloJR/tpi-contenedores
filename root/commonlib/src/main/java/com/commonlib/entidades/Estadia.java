package com.commonlib.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "estadias")
@Getter
@Setter
@ToString
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