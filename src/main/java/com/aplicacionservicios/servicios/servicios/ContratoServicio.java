/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.servicios;

import com.aplicacionservicios.servicios.entidades.Contrato;
import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Servicio;
import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.enumeradores.Rol;
import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.repositorios.ContratoRepositorio;
import com.aplicacionservicios.servicios.repositorios.ProveedorRepositorio;
import com.aplicacionservicios.servicios.repositorios.ServicioRepositorio;
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
public class ContratoServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ServicioRepositorio servicioRepositorio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private ContratoRepositorio contratoRepositorio;

    @Autowired
    private MensajeServicio mensajeServicio;

    @Transactional
    public Contrato crearContrato(String idProveedor, String idCliente, String idServicio, Integer precio,
            String nombre, String apellido, String telefono, String direccion, LocalDateTime fechaHoraServicio) throws Excepciones {

        // validar(idProveedor, idCliente, idServicio, nombre, apellido, telefono, direccion);
        System.out.println("--- Servicio Crear Contrato ----");
        try {

            Optional<Usuario> respCliente = usuarioRepositorio.findById(idCliente);
            Optional<Proveedor> respProveedor = proveedorRepositorio.findById(idProveedor);
            Optional<Servicio> respServicio = servicioRepositorio.findById(idServicio);

            if (respCliente.isPresent() && respProveedor.isPresent() && respServicio.isPresent()) {

                System.out.println("IDS ENCONTRADOS");
                System.out.println("ID CLIENTE - " + respCliente.get().getId());
                System.out.println("ID PROVEEDOR - " + respProveedor.get().getId());
                System.out.println("ID SERVICIO - " + respServicio.get().getId());

                Contrato contrato = new Contrato();
                System.out.println("ID NUEVO CONTRATO - " + contrato.getId());

                Usuario cliente = respCliente.get();
                Proveedor proveedor = respProveedor.get();
                Servicio servicio = respServicio.get();

                if (cliente.getRol() == Rol.USER) {
                    System.out.println("-------- MODIFICANDO ROL DE USER A CLIENTE");
                    cliente.setRol(Rol.CLIENTE);
                    System.out.println("-------- USUARIO NUEVO ROL " + cliente.getRol());
                }
                cliente.setTelefono(telefono);
                cliente.setDireccion(direccion);

                contrato.setFechaHoraCreacion(LocalDateTime.now());

                contrato.setFechaHoraServicio(fechaHoraServicio);

                contrato.setHoraServicio(fechaHoraServicio.toLocalTime());

                contrato.setFechaServicio(fechaHoraServicio.toLocalDate());

                contrato.setPrecio(precio);
                contrato.setContratoAceptado(false);
                contrato.setContratoPendiente(true);
                contrato.setContratoFinalizado(false);
                contrato.setContratoCancelado(false);

                // ---- Enviar notificacion o mensaje del sistema --- //
                mensajeServicio.enviarPeticionServicio(cliente, proveedor, servicio, contrato);

                System.out.println(" --- ");
                System.out.println(" --- ");
                System.out.println(" --- Enviando peticion de servicio por " + cliente.getEmail() + " al destinatario : " + proveedor.getEmail());
                System.out.println(" --- ");
                System.out.println(" --- ");

                contrato.setCliente(cliente);
                contrato.setProveedor(proveedor);
                contrato.setServicio(servicio);

                System.out.println("------------------------------INTENTANDO GUARDA EN BD USUARIO ACTUALIZADO DEL CONTRATO-----------------------------------");
                contratoRepositorio.save(contrato);

                return contrato;
            }

        } catch (Exception e) {
            throw new Excepciones("Servicio crear contrato falla");
        }
        return null;
    }


    @Transactional
    public void contratoAceptar(String idContrato) {

        Optional<Contrato> resp = contratoRepositorio.findById(idContrato);
        System.out.println("Servicio aceptar Contrato id " + idContrato);
        if (resp.isPresent()) {
            Contrato contrato = resp.get();
            if (contrato.getContratoPendiente() == true && contrato.getContratoFinalizado() == false) {

                contrato.setContratoPendiente(Boolean.FALSE);
                contrato.setContratoCancelado(Boolean.FALSE);
                contrato.setContratoFinalizado(Boolean.FALSE);
                contrato.setContratoAceptado(Boolean.TRUE);

                mensajeServicio.aceptarPeticionServicio(contrato.getCliente(), contrato.getProveedor(), contrato.getServicio(), contrato);

                System.out.println(" --- ");
                System.out.println("Contrato ACEPTADO");
                System.out.println(" --- ");
            } else {
                System.out.println(" --- ");
                System.out.println("El contrato ya ha sido aceptado --");
                System.out.println(" --- ");
            }
        }

    }

    @Transactional
    public void contratoCancelar(String idContrato) {

        Optional<Contrato> resp = contratoRepositorio.findById(idContrato);
        System.out.println("Servicio cancelar Contrato id " + idContrato);
        if (resp.isPresent()) {
            Contrato contrato = resp.get();
            if (contrato.getContratoAceptado() == true && contrato.getContratoFinalizado() == false) {

                contrato.setContratoAceptado(Boolean.FALSE);
                contrato.setContratoCancelado(Boolean.TRUE);
                contrato.setContratoFinalizado(Boolean.FALSE);
                contrato.setContratoAceptado(Boolean.FALSE);

                mensajeServicio.cancelarPeticionServicio(contrato.getCliente(), contrato.getProveedor(), contrato.getServicio(), contrato);

                System.out.println(" --- ");
                System.out.println("Contrato CALCELADO");
                System.out.println(" --- ");
            } else {
                System.out.println(" --- ");
                System.out.println("El contrato ya ha sido cancelado --");
                System.out.println(" --- ");
            }
        }

    }


    public Contrato traerContrato(String id) {

        Optional<Contrato> contResp = contratoRepositorio.findById(id);
        if (contResp.isPresent()) {
            Contrato contrato = contResp.get();
            return contrato;
        }
        return null;
    }

    @Transactional
    public void contratoFinalizar(String idContrato) {

        Optional<Contrato> resp = contratoRepositorio.findById(idContrato);
        System.out.println("Servicio finalizar Contrato id " + idContrato);
        if (resp.isPresent()) {
            Contrato contrato = resp.get();
            if (contrato.getContratoAceptado()== true && contrato.getContratoFinalizado() == false) {

                //contrato.setContratoIniciado(Boolean.FALSE);
                contrato.setContratoPendiente(Boolean.FALSE);
                contrato.setContratoCancelado(Boolean.FALSE);
                contrato.setContratoFinalizado(Boolean.TRUE);
                contrato.setContratoAceptado(Boolean.FALSE);

                mensajeServicio.finalizarPeticionServicio(contrato.getCliente(), contrato.getProveedor(), contrato.getServicio(), contrato);

                System.out.println(" --- ");
                System.out.println("Contrato ya esta FINALIZADO");
                System.out.println(" --- ");
            } else {
                System.out.println(" --- ");
                System.out.println("El contrato ya esta finalizado");
                System.out.println(" --- ");
            }
        }

    }

    //  ---------- listas de contratos Usuarios, Proveedores, Mod, Admin ---------- //
    @Transactional(readOnly = true)
    public List<Contrato> listarContratosPorCliente(Usuario cliente) {
        //return contratoRepositorio.findByCliente(cliente);
        //return contratoRepositorio.findByClienteOrderByFechaHoraServicioDesc(cliente);
        return contratoRepositorio.findByClienteOrderByFechaHoraCreacionDesc(cliente);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosPorProveedor(Usuario proveedor) {
        return contratoRepositorio.findByProveedor(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosAceptados(Usuario cliente) {
        return contratoRepositorio.findByClienteAndContratoAceptadoIsTrue(cliente);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosFinalizados(Usuario cliente) {
        return contratoRepositorio.findByClienteAndContratoFinalizadoIsTrue(cliente);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosCancelados(Usuario cliente) {
        return contratoRepositorio.findByClienteAndContratoCanceladoIsTrue(cliente);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosPendientes(Usuario cliente) {
        return contratoRepositorio.findByClienteAndContratoPendienteIsTrue(cliente);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosPendientesProveedor(Usuario cliente) {
        return contratoRepositorio.findByProveedorAndContratoPendienteIsTrue(cliente);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosAceptadosProveedor(Usuario proveedor) {
        return contratoRepositorio.findByProveedorAndContratoAceptadoIsTrue(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosFinalizadosProveedor(Usuario proveedor) {
        return contratoRepositorio.findByProveedorAndContratoFinalizadoIsTrue(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Contrato> listarContratosCanceladosProveedor(Usuario proveedor) {
        return contratoRepositorio.findByProveedorAndContratoCanceladoIsTrue(proveedor);
    }

    // --- Calificaciones --- //
    @Transactional
    public void calificacion(String idContrato, String num) {

        System.out.println("--- ENTRANDO  A SERVICIO CALIFICACION");
        System.out.println("--- BUSCANDO CONTRATO");
        Optional<Contrato> respContrato = contratoRepositorio.findById(idContrato);
        if (respContrato.isPresent()) {
            System.out.println("--- CONTRATO ENCONTRADO");

            Double numero = Double.valueOf(num);
            System.out.println("--- NUMERO DE CALIFICACION INGRESADO ES " + numero);

            
            Contrato contrato = respContrato.get();
            contrato.getCalificaciones().add(numero);
            System.out.println("--- NUMERO AÑADIDO A LISTA DE CALIFICACIONES");
            System.out.println("--- LISTA DE CALIFICACIONES TAMAÑO === "+ contrato.getCalificaciones().size());
            System.out.println("--- PROMEDIANDO LISTA DE CALIFICACIONES");
            Double cali = promediar(contrato.getCalificaciones());
            System.out.println("--- PROMEDIO DE LISTA DE CALIFICACIONES DE CONTRATO ES: " + cali);
            contrato.setCalificacion(cali);
            contrato.getServicio().setCalificacionServicio(cali);
            System.out.println("--- PROMEDIO GUARDADO EN CALIFICACION DE SERVICIO ");
            System.out.println("--- CALCULANDO ESTRELLAS INGRESA UN VALOR DOUBLE DEVUELVE UN STRING ");
            contrato.setEstrellas(estrellasServicio(cali));
            contrato.getServicio().setEstrellasCalificacionServicio(estrellasServicio(cali));
//
            System.out.println("--- GUARDANDO EN PROVEEDOR PROMEDIO GENERAL Y PROMEDIO GENERAL STRING ---- ACA SE ROMPE");
            System.out.println("--- PROVEEDOR PROMEDIO GENERAL :::: "+contrato.getProveedor().getPromedioGeneral());
            double promGeneral = calcularPromedio(contrato.getProveedor().getServicios());
            System.out.println("--- PROVEEDOR PROMEDIO GENERAL :::: "+promGeneral);
            contrato.getProveedor().setPromedioGeneral(promGeneral);
//            System.out.println("--- EN PROVEEDOR PROMEDIO GENERAL ES === " + contrato.getProveedor().getPromedioGeneral());
            contrato.getProveedor().setPromedioGeneralString(estrellasServicio(contrato.getProveedor().getPromedioGeneral()));
            System.out.println("--- EN PROVEEDOR PROMEDIO GENERAL EN ESTRELLAS ES === " + contrato.getProveedor().getPromedioGeneralString());

            System.out.println("SERVICIO CALIFICACION ----- OK");
        } else {

            System.out.println("SERVICIO CALIFICACION ----- FAIL");
        }

    }
    
    
     public static Double calcularPromedio(List<Servicio> servicios) {
        if (servicios != null && !servicios.isEmpty()) {
            double sumaCalificaciones = 0.0;

            for (Servicio servicio : servicios) {
                if (servicio.getCalificacionServicio() != null) {
                    sumaCalificaciones += servicio.getCalificacionServicio();
                }
            }

            return sumaCalificaciones / servicios.size();
        } else {
            
            return 0.0;
        }
    }
    



    public static Double promediar(List<Double> listaCalificaciones) {
        if (listaCalificaciones == null || listaCalificaciones.isEmpty()) {
            throw new IllegalArgumentException("La lista de números no puede ser nula o vacía");
        }

        double suma = 0;
        for (Double numero : listaCalificaciones) {
            suma += numero;
        }

        return (double) suma / listaCalificaciones.size();
    }

    public static String estrellasServicio(Double num) {

        Integer var = (int) Math.floor(num);

        System.out.println("--- GENERANDO ESTRELLAS .... EL NUMERO ES : "+var);
        if( var < 1 ){
            var = 1;
        }
        
        switch (var) {
            case 1 -> {
                return "⭐";
            }
            case 2 -> {
                return "⭐⭐";
            }
            case 3 -> {
                return "⭐⭐⭐";
            }
            case 4 -> {
                return "⭐⭐⭐⭐";
            }
            case 5 -> {
                return "⭐⭐⭐⭐⭐";
            }

            default ->
                throw new AssertionError();
        }

    }

}
