package com.gateway.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.commonlib.dto.RutaDto;
import com.commonlib.dto.TramoDto;

@RestController
@RequestMapping("/controlled/rutas")
public class rutasController {
    @Autowired
    RestClient rutasClient;

    @PostMapping
    public ResponseEntity<?> agregarTramos(@RequestBody TramoDto tramoDto)  {
        RutaDto rutaActual;
        try {
            rutaActual = rutasClient.get().uri("/" + tramoDto.ruta().id()).retrieve().toEntity(RutaDto.class).getBody();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("la ruta ingresada no existe");
        }
        
        // debo garantizar el orden en cada tramo

        // asegurarse que el primer tramo sea un destino
        if (rutaActual.cantidadTramos() == 0) {

        }
        


        return null;
    }
}
