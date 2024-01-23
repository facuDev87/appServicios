/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aplicacionservicios.servicios.repositorios;

import com.aplicacionservicios.servicios.entidades.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author facun
 */
public interface ServicioRepositorio extends JpaRepository<Servicio, String>{
    
}
