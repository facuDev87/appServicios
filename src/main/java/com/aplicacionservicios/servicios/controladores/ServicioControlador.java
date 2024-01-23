/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.controladores;

import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.servicios.ServicioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author facun
 */
@Controller
@RequestMapping("/servicios-control")
public class ServicioControlador {
    
    @Autowired
    ServicioServicio servicioServicio;
    
    @PostMapping("/registro-servicio")
    public String registrarServicio(@RequestParam String idProveedor, @RequestParam String descripcionServicio, @RequestParam Integer precioServicio) throws Excepciones{
        
        try {
            System.out.println("Los datos enviados son : \nidProveedor "+idProveedor+"\n Descripcion Servicio "+descripcionServicio
                    + "\nPrecio Servicio "+precioServicio);
            
            servicioServicio.crearServicioProveedor(idProveedor, descripcionServicio, precioServicio);
            System.out.println("--- Servicio Controlador OK ---");
            return "redirect:/servicio-perfil/"+idProveedor;
        } catch (Excepciones e) {
            throw new Excepciones("--- Registro Servicio Controlador FALLO ---");
            
        }
        
    }
    
    
    
    
    
    
}
