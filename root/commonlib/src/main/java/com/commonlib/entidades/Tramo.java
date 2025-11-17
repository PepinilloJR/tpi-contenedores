package com.commonlib.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
    private Integer combustibleConsumido;

    public void calcularCostoAproximado() {
        if (this.camion == null || this.distancia == null) {
            this.costoAproximado = null;
            return;
        }

        var tarifa = this.camion.getTarifa();
        if (tarifa == null) {
            this.costoAproximado = null;
            return;
        }

        Double consumoPromedio = this.camion.getConsumoCombustiblePromedio() != null
                ? this.camion.getConsumoCombustiblePromedio()
                : 1.0;

        Double distanciaVal = this.distancia != null ? this.distancia : 0.0;

        Double costoKm = tarifa.getCostoKilometro() != null ? tarifa.getCostoKilometro() : 0.0;
        Double costoLitro = tarifa.getCostoLitro() != null ? tarifa.getCostoLitro() : 0.0;
        Double costoVolumen = tarifa.getCostoVolumen() != null ? tarifa.getCostoVolumen() : 0.0;
        //Double costoPeso = tarifa.getcoso() != null ? tarifa.getCostoVolumen() : 0.0;
        //Double capacidadVol = this.camion.getCapacidadVolumen() != null ? this.camion.getCapacidadVolumen() : 1.0;
        //Double capacidadPeso = this.camion.getCapacidadPeso() != null ? this.camion.getCapacidadPeso() : 1.0;
        Double volumenContenedor = this.ruta.getSolicitud().getContenedor().getVolumen();       
        //Double pesoContenedor = this.ruta.getSolicitud().getContenedor().getPeso();
        double parteCombustible = consumoPromedio * costoLitro + distanciaVal * costoKm;
        double parteVolumenPeso = costoVolumen * volumenContenedor;

        // Llama esta funcion al asignar un camion
        this.costoAproximado = parteCombustible + parteVolumenPeso;
    }

    // Esto hay que cambiar

    public void calcularCostoReal(Double costoEstadia) {
        if (this.camion == null || this.distancia == null) {
            this.costoReal = null;
            return;
        }

        var tarifa = this.camion.getTarifa();
        if (tarifa == null) {
            this.costoAproximado = null;
            return;
        }

        if (costoEstadia == null || costoEstadia == 0.0) {
            costoEstadia = 0.0;
        }

        Double consumoPromedio = this.combustibleConsumido != null
                ? this.combustibleConsumido
                : 1.0;

        Double distanciaVal = this.distancia != null ? this.distancia : 0.0;

        Double costoKm = tarifa.getCostoKilometro() != null ? tarifa.getCostoKilometro() : 0.0;
        Double costoLitro = tarifa.getCostoLitro() != null ? tarifa.getCostoLitro() : 0.0;
        Double costoVolumen = tarifa.getCostoVolumen() != null ? tarifa.getCostoVolumen() : 0.0;

        //Double capacidadVol = this.camion.getCapacidadVolumen() != null ? this.camion.getCapacidadVolumen() : 1.0;
        //Double capacidadPeso = this.camion.getCapacidadPeso() != null ? this.camion.getCapacidadPeso() : 1.0;
        Double volumenContenedor = this.ruta.getSolicitud().getContenedor().getVolumen();       

        double parteCombustible = consumoPromedio * costoLitro + distanciaVal * costoKm;
        double parteVolumenPeso = costoVolumen * volumenContenedor;

        this.costoReal = parteCombustible + parteVolumenPeso + costoEstadia;

    }

}