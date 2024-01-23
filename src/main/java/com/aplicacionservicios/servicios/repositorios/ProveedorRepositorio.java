/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aplicacionservicios.servicios.repositorios;

import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author facun
 */
@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String> {

    @Query("SELECT p FROM Proveedor p WHERE p.email = :email")
    Proveedor buscarPorEmail(@Param("email") String email);

//    @Query("SELECT p FROM Proveedor p ORDER BY p.categoria_servicio ")
//    public List<Proveedor> listarProveedoresOrdenadosPorCategoria();
    
//    @Query("SELECT p FROM Proveedor p JOIN p.categoriaServicio c WHERE p.rol = 'PROVEEDOR' ORDER BY c.nombre")
//    public List<Proveedor> listarProveedoresOrdenadosPorCategoria();

}
