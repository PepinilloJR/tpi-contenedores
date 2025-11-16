package com.commonlib.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Tramo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_origen", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_tramo_ubicacion_origen"))
    private Ubicacion origen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_tramo_ubicacion_destino"))
    private Ubicacion destino;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta",
    nullable = false, referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "fk_tramo_ruta"))
    private Ruta ruta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_camion",
    nullable = false, referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "fk_tramo_camion"))
    private Camion camion;

    private String tipo;
    private String estado;
    private BigDecimal costoAproximado;
    private BigDecimal costoReal;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Double distancia;
}