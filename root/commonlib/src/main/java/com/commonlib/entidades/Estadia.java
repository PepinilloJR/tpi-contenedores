package com.commonlib.entidades;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tramo", nullable = false)
    private Tramo tramo; 

    private LocalDateTime fechaHoraEntrada;

    private LocalDateTime fechaHoraSalida;

    public Long calcularEstadia() {
        if (fechaHoraEntrada == null || fechaHoraSalida == null) {
            return 0L;
        }
        return Duration.between(fechaHoraEntrada, fechaHoraSalida).toDays();
    }

    public Double calcularCostoEstadia() {
        if (this.tramo != null && this.tramo.getOrigen() != null) {
            return this.tramo.getOrigen().getCosto() * calcularEstadia();
        }
        return 0.0;

    }
}