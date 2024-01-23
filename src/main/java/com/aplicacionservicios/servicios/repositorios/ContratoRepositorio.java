/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aplicacionservicios.servicios.repositorios;

import com.aplicacionservicios.servicios.entidades.Contrato;
import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author facun
 */
public interface ContratoRepositorio extends JpaRepository<Contrato, String> {

    List<Contrato> findByCliente(Usuario cliente);

    List<Contrato> findByProveedor(Usuario proveedor);

    
    List<Contrato> findByClienteAndContratoAceptadoIsTrue(Usuario cliente);
    
    List<Contrato> findByClienteAndContratoCanceladoIsTrue(Usuario cliente);
    
    List<Contrato> findByClienteAndContratoFinalizadoIsTrue(Usuario cliente);
    
    List<Contrato> findByClienteAndContratoPendienteIsTrue(Usuario cliente);
    
    List<Contrato> findByProveedorAndContratoAceptadoIsTrue(Usuario proveedor);
    
    List<Contrato> findByProveedorAndContratoPendienteIsTrue(Usuario proveedor);

    List<Contrato> findByProveedorAndContratoCanceladoIsTrue(Usuario proveedor);
    
    List<Contrato> findByProveedorAndContratoFinalizadoIsTrue(Usuario proveedor);
    
    
    List<Contrato> findByClienteOrderByFechaHoraServicioDesc(Usuario cliente);
   
    List<Contrato> findByClienteOrderByFechaHoraCreacionDesc(Usuario cliente);
   // List<Contrato> findByClienteOrderByFechaCreacionDesc(Usuario cliente);
    @Query("SELECT c FROM Contrato c WHERE c.id = :id")
    public Contrato buscarPorId(@Param("id") String id);

}
