package com.commonlib.dto;

import com.commonlib.entidades.Contenedor;

public interface DtoHandler {
       private ContenedorDto convertirContenedorDto(Contenedor c) {
        if (c == null)
            return null;
        return new ContenedorDto(c.getId(), c.getPeso(), c.getVolumen(), c.getEstado(), c.getCostoVolumen());
    }

    
    private Contenedor convertirContenedorEntidad(ContenedorDto dto) {
        Contenedor c = new Contenedor();
        c.setPeso(dto.peso());
        c.setVolumen(dto.volumen());
        c.setEstado(dto.estado());
        c.setCostoVolumen(dto.costoVolumen());
        return c;
    }

}
