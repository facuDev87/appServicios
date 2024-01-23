/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.servicios;

import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Servicio;
import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.repositorios.ProveedorRepositorio;
import com.aplicacionservicios.servicios.repositorios.ServicioRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author facun
 */
@Service
public class ServicioServicio {

    @Autowired
    ProveedorRepositorio proveedorRepositorio;

    @Autowired
    ServicioRepositorio servicioRepositorio;

    public void crearServicioProveedor(String idProveedor, String descripcionServicio, Integer precioServicio) throws Excepciones {

        try {
            Servicio servicio = new Servicio();

            System.out.println("--- BUSCANDO PROVEEDOR ---");
            Optional<Proveedor> respProveedor = proveedorRepositorio.findById(idProveedor);
            if (respProveedor.isPresent()) {
                Proveedor prov = respProveedor.get();
                System.out.println("--- Proveedor es : " + prov.getNombre());

                servicio.setDescripcionServicio(descripcionServicio);
                servicio.setPrecioServicio(precioServicio);

                servicio.setCalificacionServicio(null);
                servicio.setEstrellasCalificacionServicio(null);

                servicio.setProveedor(prov);
                prov.getServicios().add(servicio);

                persistirServicio(servicio);
            }

        } catch (Exception e) {
            throw new Excepciones("--- Metodo Crear Servicio -- Servicio --- FALLO --- ");
        }

    }

    @Transactional
    public void persistirServicio(Servicio servicio) {
        servicioRepositorio.save(servicio);
    }


    public Servicio buscarServicioPorId(String id) throws Excepciones {

        try {
            Optional<Servicio> respServicio = servicioRepositorio.findById(id);

            if (respServicio.isPresent()) {
                Servicio servicio = respServicio.get();

                return servicio;
            }
        } catch (Exception e) {
            throw new Excepciones("Buscar Srvicio por Id no encontrado");
        }
        return  null;
    }
    
    
}
