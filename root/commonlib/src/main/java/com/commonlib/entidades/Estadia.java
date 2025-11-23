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

    @Column(name = "id_ubicacion")
    private Long idDeposito;

    @Column(name = "id_contenedor")
    private Long idContenedor;

    private LocalDateTime fechaHoraEntrada;

    private LocalDateTime fechaHoraSalida;

    private Double costo;

    public void calcularCosto(Double costoEstadia) {
        if (this.fechaHoraEntrada != null && this.fechaHoraSalida != null) {
            long dias = java.time.Duration.between(this.fechaHoraEntrada, this.fechaHoraSalida).toDays();

            if (dias == 0) {
                dias = 1;
            }

            this.costo = dias * costoEstadia;
        }
    }

}