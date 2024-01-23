/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aplicacionservicios.servicios.repositorios;

import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.enumeradores.Categoria;
import com.aplicacionservicios.servicios.enumeradores.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author facun
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> buscarUsuarioPorEmail(@Param("email")String email);


    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROVEEDOR'")
    public List<Usuario> listarProveedor();

//    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROVEEDOR' ORDER BY u.categoria_servicio")
//    public List<Usuario> listarProveedoresOrdenadosPorCategoria();
    
    
    @Query("SELECT u FROM Usuario u JOIN Proveedor p ON u.id = p.id WHERE u.rol = 'PROVEEDOR' ORDER BY p.categoriaServicio")
    public List<Usuario> listarProveedoresOrdenadosPorCategoria();
    
    

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.categoriaServicio IN :categorias")
    List<Usuario> listarProveedoresPorCategoria(
            @Param("rol") Rol rol,
            @Param("categorias") Categoria categorias
    );
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'USER'")
    public List<Usuario> listarUsuario();
    
//    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROVEEDOR'")
//    public List<Usuario> listarProveedor();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'CLIENTE'")
    public List<Usuario> listarCliente();
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    public List<Usuario> listarUsuarioActivo();
    @Query("SELECT u FROM Usuario u WHERE u.activo = false")
    public List<Usuario> listarUsuarioBaja();

}
