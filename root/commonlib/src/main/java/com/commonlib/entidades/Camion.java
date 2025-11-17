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
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa", nullable = true, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_camion_tarifa"))
    private Tarifa tarifa;

    @Column(nullable = false, unique = true, length = 12)
    private String patente;

    @Column(nullable = false, length = 80)
    private String nombreTransportista;

    @Column(nullable = false, length = 20)
    private String telefonoTransportista;

    @Column(nullable = false)
    private Double capacidadPeso;

    @Column(nullable = false)
    private Double capacidadVolumen;

    @Column(nullable = false)
    private Double consumoCombustiblePromedio;

    private Boolean disponible;
}
