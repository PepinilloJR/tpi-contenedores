package com.commonlib.entidades;

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
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private Double costoEstimado;
    private Double tiempoEstimado;
    private Double tiempoReal;
    private Double costoFinal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_solicitud_cliente"))
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contenedor",
    nullable = false, referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "fk_solicitud_contenedor"))
    private Contenedor contenedor;

}