package com.commonlib.entidades;

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

public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double peso;
    @Column(nullable = false)
    private Double volumen;
    private String estado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ultima_ubicacion", nullable = true, 
    referencedColumnName = "id", 
    foreignKey = @ForeignKey(name = "fk_contenedor_ubicacion"))
    Ubicacion ubicacion;
    

}