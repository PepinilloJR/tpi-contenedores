package com.camiones.service.demo.dto;

import com.commonlib.entidades.Camion;
import com.commonlib.entidades.Tarifa;

public interface DtoHandler {

    // Tarifa

    public static TarifaDto convertirTarifaDto(Tarifa t) {
        if (t == null)
            return null;
        return new TarifaDto(t.getId(), t.getCostoVolumen(), t.getCostoKilometro());
    }

    public static Tarifa convertirTarifaEntidad(TarifaDto dto) {
        if (dto == null)
            return null;
        Tarifa t = new Tarifa();
        t.setId(dto.id());
        t.setCostoKilometro(dto.costoKilometro());
        t.setCostoVolumen(dto.costoVolumen());
        return t;
    }

    // Camion

    public static CamionDto convertirCamionDto(Camion c) {
        if (c == null)
            return null;

        return new CamionDto(
                c.getId(),
                c.getTarifa() != null ? c.getTarifa().getId() : null,
                c.getPatente(),
                c.getNombreTransportista(),
                c.getTelefonoTransportista(),
                c.getCapacidadPeso(),
                c.getCapacidadVolumen(),
                c.getConsumoCombustiblePromedio(),
                c.getDisponible());

    }

    public static Camion convertirCamionEntidad(CamionDto dto) {
        if (dto == null)
            return null;

        Camion c = new Camion();
        c.setId(dto.id());


        // Setear la tarifa en el controller

        c.setPatente(dto.patente());
        c.setNombreTransportista(dto.nombreTransportista());
        c.setTelefonoTransportista(dto.telefonoTransportista());
        c.setCapacidadPeso(dto.capacidadPeso());
        c.setCapacidadVolumen(dto.capacidadVolumen());
        c.setConsumoCombustiblePromedio(dto.consumoCombustiblePromedio());
        c.setDisponible(dto.disponible());

        return c;
    }

}
