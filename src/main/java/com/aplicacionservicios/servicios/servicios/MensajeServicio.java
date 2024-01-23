/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.servicios;

import com.aplicacionservicios.servicios.entidades.Contrato;
import com.aplicacionservicios.servicios.entidades.Mensaje;
import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Servicio;
import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.repositorios.MensajeRepositorio;
import com.aplicacionservicios.servicios.repositorios.UsuarioRepositorio;
import java.time.LocalDateTime;
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
public class MensajeServicio {

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Mensaje crearMensajeSistema(Usuario usuario, Proveedor proveedor, Servicio servicio, Contrato contrato) {

        Mensaje mensaje = new Mensaje();

        mensaje.setUsuario(usuario);
        mensaje.setProveedor(proveedor);
        mensaje.setServicio(servicio);
        mensaje.setContrato(contrato);

        mensaje.setVisto(false);
        mensaje.setFechaCompletaMensaje(LocalDateTime.now());
        mensaje.setFechaMensaje(mensaje.getFechaCompletaMensaje().toLocalDate());
        mensaje.setHoraMensaje(mensaje.getFechaCompletaMensaje().toLocalTime());

        System.out.println(" --- ");
        System.out.println(" --- ");
        System.out.println(" --- CREAR MENSAJE --- Mensaje creado por " + usuario.getEmail());
        System.out.println(" --- ");
        System.out.println(" --- ");

        return mensaje;
    }

    @Transactional
    public void enviarPeticionServicio(Usuario usuario, Proveedor proveedor, Servicio servicio, Contrato contrato) {

        // Se crea un mensaje " Hey soy Usuario, quiero contratar este servicio"
        Mensaje mensaje = crearMensajeSistema(usuario, proveedor, servicio, contrato);

        // Datos del servicio y la peticion
        String mensajeSistema = "Peticion : ' " + servicio.getDescripcionServicio() + " ' , del usuario : ' " + usuario.getEmail() + " '";
        mensaje.setMensaje(mensajeSistema);

        // Se busca el destinatario
        int noti = (proveedor.getNotificacion() + 1);
        proveedor.setNotificacion(noti);

        // Se agrega el mensaje a la lista de mensajes del Destinatario
        proveedor.getMensajesList().add(mensaje);
        mensajeRepositorio.save(mensaje);

    }

    @Transactional
    public void aceptarPeticionServicio(Usuario usuario, Proveedor proveedor, Servicio servicio, Contrato contrato) {

        // Se crea un mensaje " Hey soy Usuario, quiero contratar este servicio"
        Mensaje mensaje = crearMensajeSistema(usuario, proveedor, servicio, contrato);

        // Datos del servicio y la peticion
        String mensajeSistema = "Peticion aceptada por el proveedor " + proveedor.getEmail() + " '";
        mensaje.setMensaje(mensajeSistema);

        // Se busca el destinatario
        int noti = (usuario.getNotificacion() + 1);
        usuario.setNotificacion(noti);

        // Se agrega el mensaje a la lista de mensajes del Destinatario
        usuario.getMensajesList().add(mensaje);
        mensajeRepositorio.save(mensaje);

    }

    @Transactional
    public void finalizarPeticionServicio(Usuario usuario, Proveedor proveedor, Servicio servicio, Contrato contrato) {

        // Se crea un mensaje " Hey soy Usuario, quiero contratar este servicio"
        Mensaje mensaje = crearMensajeSistema(usuario, proveedor, servicio, contrato);

        // Datos del servicio y la peticion
        String mensajeSistema = "El servicio solicitado ha sido finalizado por el proveedor " + proveedor.getEmail() + " '";
        mensaje.setMensaje(mensajeSistema);

        // Se busca el destinatario
        int noti = (usuario.getNotificacion() + 1);
        usuario.setNotificacion(noti);

        // Se agrega el mensaje a la lista de mensajes del Destinatario
        usuario.getMensajesList().add(mensaje);
        mensajeRepositorio.save(mensaje);

    }

    @Transactional
    public void cancelarPeticionServicio(Usuario usuario, Proveedor proveedor, Servicio servicio, Contrato contrato) {

        // Se crea un mensaje " Hey soy Usuario, quiero contratar este servicio"
        Mensaje mensaje = crearMensajeSistema(usuario, proveedor, servicio, contrato);

        // Datos del servicio y la peticion
        String mensajeSistema = "El servicio solicitado ha sido cancelado por el proveedor " + proveedor.getEmail() + " '";
        mensaje.setMensaje(mensajeSistema);

        // Se busca el destinatario
        int noti = (usuario.getNotificacion() + 1);
        usuario.setNotificacion(noti);

        // Se agrega el mensaje a la lista de mensajes del Destinatario
        usuario.getMensajesList().add(mensaje);
        mensajeRepositorio.save(mensaje);

    }

    @Transactional
    public void iniciarPeticionServicio(Usuario usuario, Proveedor proveedor, Servicio servicio, Contrato contrato) {

        // Se crea un mensaje " Hey soy Usuario, quiero contratar este servicio"
        Mensaje mensaje = crearMensajeSistema(usuario, proveedor, servicio, contrato);

        // Datos del servicio y la peticion
        String mensajeSistema = "El servicio solicitado aceptado por el proveedor " + proveedor.getEmail() + " '";
        mensaje.setMensaje(mensajeSistema);

        // Se busca el destinatario
        int noti = (usuario.getNotificacion() + 1);
        usuario.setNotificacion(noti);

        // Se agrega el mensaje a la lista de mensajes del Destinatario
        usuario.getMensajesList().add(mensaje);
        mensajeRepositorio.save(mensaje);

    }

    // --- Eliminar mensajes del sistema --- //
    @Transactional
    public void eleminarMensajeSiatema(String idMensaje, String idUsuario) throws Excepciones {
        try {
            Optional<Usuario> respUsuario = usuarioRepositorio.findById(idUsuario);
            if (respUsuario.isPresent()) {

                Usuario usuario = respUsuario.get();
                List<Mensaje> listMensajes = usuario.getMensajesList();
                listMensajes.removeIf(mensaje -> mensaje.getId().equals(idMensaje));
                if (usuario.getNotificacion() > 0) {
                    usuario.setNotificacion((usuario.getNotificacion() - 1));
                }
            }
        } catch (Exception e) {
            throw new Excepciones("Error al tratar de eliminar Mensaje del Sistema");
        }
    }

}
