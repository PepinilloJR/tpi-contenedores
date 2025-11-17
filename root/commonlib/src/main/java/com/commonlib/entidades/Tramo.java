package com.commonlib.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Tramo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_origen", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_tramo_ubicacion_origen"))
    private Ubicacion origen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_tramo_ubicacion_destino"))
    private Ubicacion destino;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_tramo_ruta"))
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_camion", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_tramo_camion"))
    private Camion camion;

    private String tipo;
    private String estado;
    private Double costoAproximado;
    private Double costoReal;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Double distancia;

    public Double calcularCostoAproximado() {
        if (this.camion == null || this.distancia == null)
            return 0.0;

        var tarifa = this.camion.getTarifa();
        if (tarifa == null)
            return 0.0;

        Double consumoPromedio = this.camion.getConsumoCombustiblePromedio() != null
                ? this.camion.getConsumoCombustiblePromedio()
                : 1.0;

        Double distanciaVal = this.distancia != null ? this.distancia : 0.0;

        Double costoKm = tarifa.getCostoKilometro() != null ? tarifa.getCostoKilometro() : 0.0;
        Double costoLitro = tarifa.getCostoLitro() != null ? tarifa.getCostoLitro() : 0.0;
        Double costoVolumen = tarifa.getCostoVolumen() != null ? tarifa.getCostoVolumen() : 0.0;

        Double capacidadVol = this.camion.getCapacidadVolumen() != null ? this.camion.getCapacidadVolumen() : 1.0;
        Double capacidadPeso = this.camion.getCapacidadPeso() != null ? this.camion.getCapacidadPeso() : 1.0;

        double parteCombustible = consumoPromedio * distanciaVal * costoKm;
        double parteVolumenPeso = costoLitro * costoVolumen * capacidadVol * capacidadPeso;

        // Llama esta funcion al asignar un camion
        return parteCombustible + parteVolumenPeso;
    }

    public Long calcularEstadia() {
        if (fechaHoraInicio == null || fechaHoraFin == null) {
            return 0L;
        }
        return Duration.between(fechaHoraInicio, fechaHoraFin).toDays();
    }

    public Double calcularCostoEstadia() {
        if (this.origen != null || this.origen.getTipo().equalsIgnoreCase("deposito")) {
            return (Double) this.origen.getCosto() * calcularEstadia();
        }
        return 0.0;

    }

    public Double calcularCostoReal() {
        if (this.costoAproximado != null){
            return costoAproximado + calcularCostoEstadia();
        }
        return 0.0;
    }

    // costo real costo aproximado + estadia
}