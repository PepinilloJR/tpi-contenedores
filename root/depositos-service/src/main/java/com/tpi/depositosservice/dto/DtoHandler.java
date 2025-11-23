package com.tpi.depositosservice.dto;

import com.commonlib.entidades.Estadia;

public class DtoHandler {

    public static EstadiaDtoOut convertirEstadiaDtoOut(Estadia estadia) {
        if (estadia == null) {
            return null;
        }

        return new EstadiaDtoOut(
                estadia.getIdEstadia(),
                estadia.getIdDeposito(),
                estadia.getIdContenedor(),
                estadia.getFechaHoraEntrada(),
                estadia.getFechaHoraSalida(),
                estadia.getCosto());
    }
}
