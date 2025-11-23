package com.commonlib.entidades;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadia;

    @Column(name = "id_tramo")
    private Long idTramo; 

    @Column(name = "id_contenedor")
    private Long idContenedor;

    private LocalDateTime fechaHoraEntrada;

    private LocalDateTime fechaHoraSalida;

    private Double costo;


/* 
    public Double calcularCostoEstadia() {
        if (this.tramo != null && this.tramo.getOrigen() != null) {
            return this.tramo.getOrigen().getCosto() * calcularEstadia();
        }
        return 0.0;

    }
        */
}