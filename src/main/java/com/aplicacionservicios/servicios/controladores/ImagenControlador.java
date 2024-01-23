/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.controladores;

import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author facun
 */
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {

        Usuario usuario = usuarioServicio.getOne(id);
        if (usuario.getImagen() != null) {

            byte[] imagen = usuario.getImagen().getContenido();

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
            
        }else{
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
