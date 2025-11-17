package com.commonlib.entidades;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private Double costoEstimado;
    private Double tiempoEstimado;
    private Long tiempoReal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_origen", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_solicitud_ubicacion_origen"))
    private Ubicacion origen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_solicitud_ubicacion_destino"))
    private Ubicacion destino;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_solicitud", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_solicitud_seguimiento"))
    private List<Seguimiento> seguimiento;

}