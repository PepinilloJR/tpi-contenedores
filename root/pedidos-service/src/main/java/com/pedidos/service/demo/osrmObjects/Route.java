package com.pedidos.service.demo.osrmObjects;
import java.util.List;

import lombok.Data;

@Data
public class Route {
        private Double distance;
        private Double duration;
        //private Geometry geometry;
        private List<Leg> legs;
}