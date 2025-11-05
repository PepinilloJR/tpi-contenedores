package com.pedidos.service.demo.entidades;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ForeignKey;
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
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double costoEstimado;
    private Double tiempoEstimado;
    private Double tiempoReal;
    private Double costoFinal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", 
    nullable = false, referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_solicitud_cliente"))
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idContenedor",
    nullable = false, referencedColumnName = "id",
    foreignKey = @ForeignKey(name = "fk_solicitud_contenedor"))
    private Contenedor contenedor;
}