/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.servicios;

import com.aplicacionservicios.servicios.entidades.Comentario;
import com.aplicacionservicios.servicios.entidades.Contrato;
import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.repositorios.ComentarioRepositorio;
import com.aplicacionservicios.servicios.repositorios.ContratoRepositorio;
import com.aplicacionservicios.servicios.repositorios.ProveedorRepositorio;
import com.aplicacionservicios.servicios.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author facun
 */
@Service
public class ComentarioServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private ContratoRepositorio contratoRepositorio;

    // --- CREAR COMENTARIO --- //
    public Comentario crearComentario(String comentarioTexto, String idUsuario, String idProveedor) throws Excepciones {

        validar(comentarioTexto, idUsuario, idProveedor);
        Comentario comentario = new Comentario();

        Usuario usuario = usuarioServicio.getOne(idUsuario);

        comentario.setComentario(comentarioTexto);
        comentario.setUsuario(usuario);
        comentario.setFecha(new Date());

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.getComentarios().add(comentario);
        }

        return comentario;
    }

    // --- PERSISTIR COMENTARIO --- //
    @Transactional
    public void persistirComentario(String comentarioTexto, String idUsuario, String idProveedor) throws Excepciones {
        validar(comentarioTexto, idUsuario, idProveedor);
        Comentario comentario = crearComentario(comentarioTexto, idUsuario, idProveedor);
        comentarioRepositorio.save(comentario);
    }

    
    // --- COMENTARIOS EN CONTRATO --- //
    public Comentario crearComentarioServicio(String comentarioTexto, String idUsuario, String idProveedor, String idContrato) throws Excepciones {

        validar(comentarioTexto, idUsuario, idProveedor);
        Comentario comentario = new Comentario();

        //  Usuario usuario = usuarioServicio.getOne(idUsuario);
        Optional<Usuario> respUsuario = usuarioRepositorio.findById(idUsuario);
        Optional<Contrato> respContrato = contratoRepositorio.findById(idContrato);

        if (respUsuario.isPresent()) {
            Usuario usuario = respUsuario.get();
            comentario.setComentario(comentarioTexto);
            comentario.setUsuario(usuario);
            System.out.println("DESDE COMENTARIO SERVICIO USUARIO ID " + usuario.getId());
            System.out.println("DESDE COMENTARIO SERVICIO USUARIO ID " + usuario.getNombre());
            comentario.setFecha(new Date());

        }

        Optional<Usuario> respProveedor = usuarioRepositorio.findById(idProveedor);

        if (respContrato.isPresent()) {
            Contrato contrato = respContrato.get();
            contrato.getComentariosServicio().add(comentario);
        }

        System.out.println("El usuario " + respUsuario.get().getNombre() + ", a comentado en el contrato " + respContrato.get().getId() + " del proveedor " + respProveedor.get().getNombre());

        return comentario;
    }

    
    // --- PERSISTIR COMENTARIOS EN CONTATO --- //
    @Transactional
    public void persistirComentarioServicio(String comentarioTexto, String idUsuario, String idProveedor, String idContrato) throws Excepciones {
        validar(comentarioTexto, idUsuario, idProveedor);
        Comentario comentario = crearComentarioServicio(comentarioTexto, idUsuario, idProveedor, idContrato);
        comentarioRepositorio.save(comentario);
    }

    
    // --- VALIDACIONES --- //
    private void validar(String comentario, String idUsuario, String idProveedor) throws Excepciones {
        if (comentario.isEmpty() || comentario == null) {
            throw new Excepciones("el comentario no puede ser nulo o estar vacio");
        }
        if (idUsuario.isEmpty() || idUsuario == null) {
            throw new Excepciones("el idUsuario no puede ser nulo o estar vacio");
        }
        if (idUsuario.isEmpty() || idProveedor == null) {
            throw new Excepciones("el idProveedor no puede ser nulo o estar vacio");
        }

    }

    // --- LISTAR COMENTARIOS --- //
    public List<Comentario> listarComentarios() {
        List<Comentario> comentarios = new ArrayList();

        comentarios = comentarioRepositorio.findAll();

        return comentarios;
    }

    // --- MODIFICAR COMENTARIOS EN PROVEEDOR --- //
    @Transactional
    public Comentario modificarComentarioProveedor(String idComentario, String comentario) {
        Optional<Comentario> resp = comentarioRepositorio.findById(idComentario);
        if (resp.isPresent()) {
            Comentario coment = resp.get();
            coment.setComentario(comentario);
            return coment;
        }
        return null;
    }
    
    // --- MODIFICAR COMENTARIOS EN CONTATO --- //
    @Transactional
    public Comentario modificarComentarioContrato(String idComentario, String comentario) {
        Optional<Comentario> resp = comentarioRepositorio.findById(idComentario);
        if (resp.isPresent()) {
            Comentario coment = resp.get();
            coment.setComentario(comentario);
            return coment;
        }
        return null;
    }

    // --- ELIMINAR COMENTARIO EN CONTRATO --- //
    @Transactional
    public void eliminarComentarioProveedor(String idComent, String idProv){
        
        Optional<Proveedor> respUsuario = proveedorRepositorio.findById(idProv);
        if(respUsuario.isPresent()){
            
            Proveedor usuario = respUsuario.get();
            List<Comentario> listComent = usuario.getComentarios();
            
            listComent.removeIf(comentario -> comentario.getId().equals(idComent));
        }
    }
    // --- ELIMINAR COMENTARIO EN CONTRATO --- //
    @Transactional
    public void eliminarComentarioContrato(String idComent, String idContrato){
        
        Optional<Contrato> respContrato = contratoRepositorio.findById(idContrato);
        if(respContrato.isPresent()){
            
            Contrato contrato = respContrato.get();
            List<Comentario> listComent = contrato.getComentariosServicio();
            
            listComent.removeIf(comentario -> comentario.getId().equals(idComent));
        }
    }

}
