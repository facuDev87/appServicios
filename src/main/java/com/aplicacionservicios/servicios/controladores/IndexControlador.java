/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.controladores;

import com.aplicacionservicios.servicios.entidades.Contrato;
import com.aplicacionservicios.servicios.entidades.Mensaje;
import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.enumeradores.Categoria;
import com.aplicacionservicios.servicios.enumeradores.Rol;
import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.repositorios.ProveedorRepositorio;
import com.aplicacionservicios.servicios.servicios.ComentarioServicio;
import com.aplicacionservicios.servicios.servicios.ContratoServicio;
import com.aplicacionservicios.servicios.servicios.MensajeServicio;
import com.aplicacionservicios.servicios.servicios.ProveedorServicio;
import com.aplicacionservicios.servicios.servicios.ServicioServicio;
import com.aplicacionservicios.servicios.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author facun
 */
@Controller
@RequestMapping("/")
public class IndexControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ServicioServicio servicioServicio;

    @Autowired
    private ContratoServicio contratoServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    @Autowired
    private MensajeServicio mensajeServicio;

    @GetMapping("/home")
    public String index(ModelMap modelo, HttpServletRequest httpServletRequest) {
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        return "index.html";
    }

    // --- Registrar Usuario --- //
    @GetMapping("/registro-usuario")
    public String usuarioRegistro(ModelMap modelo, HttpServletRequest httpServletRequest) {
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        return "usuario_registro.html";
    }

    @PostMapping("registro-usuario")
    public String registrarUsuario(@RequestParam String nombre, @RequestParam String apellido, HttpServletRequest httpServletRequest,
            @RequestParam String email, @RequestParam String password, @RequestParam MultipartFile archivo, ModelMap modelo) throws Excepciones {
        try {
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            usuarioServicio.persistirUsuario(nombre, apellido, email, password, archivo);
            return "index.html";
        } catch (Excepciones e) {
            throw new Excepciones("Controlador PersistirUsuario - error -");
        }
    }

    @GetMapping("/registro-cliente")
    public String clienteRegistro() {

        return "cliente_registro.html";
    }

    // --- Modificar Datos Usuario --- //
    @PostMapping("/modificar-datos-usuario")
    public String modificarDatosUsuario(@RequestParam String idUsuario, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String email, String telefono, @RequestParam Rol rol,
            String direccion, Categoria categoria, HttpSession session) throws Excepciones {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        System.out.println("""
                               --- Controlador MODIFICAR DATOS ----
                               """);
        usuarioServicio.modificarDatos(idUsuario, rol, nombre, apellido, email, telefono, direccion, categoria);
        System.out.println("""
                               --- Controlador MODIFICAR DATOS OK...----
                               """);
        if (logueado.getRol().toString().equals("ADMIN")) {

            return "redirect:/usuario_lista";
        }
        return "redirect:/perfil-usuario/" + idUsuario;
        
    }

    // --- Modificar o Eliminar Imagen Perfil --- //
    @PostMapping("/modificar-imagen-perfil")
    public String modificarImagenPerfil(@RequestParam String idUsuario, @RequestParam Rol rol, @RequestParam MultipartFile archivo) throws Excepciones {
        usuarioServicio.modificarImagenPerfil(idUsuario, rol, archivo);
        return "redirect:/perfil-usuario/" + idUsuario;
    }

    @PostMapping("/eliminar-imagen-perfil")
    public String eliminarImagenPerfil(@RequestParam String idUsuario) throws Excepciones {
        usuarioServicio.eliminarImagenPerfil(idUsuario);
        return "redirect:/perfil-usuario/" + idUsuario;
    }

    // --- Registrar Proveedor --- //
    @GetMapping("/registro-proveedor")
    public String proveedorRegistro(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Categoria> categorias = Arrays.asList(Categoria.values());
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("categorias", categorias);
        return "proveedor_registro.html";
    }

    @PostMapping("registro-proveedor")
    public String registrarProveedor(@RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String email, @RequestParam String password, @RequestParam String telefono, ModelMap modelo,
            @RequestParam Categoria categoria, @RequestParam MultipartFile archivo, HttpServletRequest httpServletRequest) throws Excepciones {
        try {
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            proveedorServicio.persistirProveedor(nombre, apellido, email, password, telefono, categoria, archivo);

            return "index.html";
        } catch (Excepciones e) {
            throw new Excepciones("Controlador PersistirUsuario - error -");
        }
    }

    // --- Login --- //
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo, String password, HttpSession session,
            HttpServletRequest httpServletRequest) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (error != null) {
            session.invalidate();

            modelo.put("error", "Lo sentimos, el usuario o la contraseña no coinciden.");

            System.out.println("");
            return "login.html"; // Retornar inmediatamente en caso de error

        }
        if (logueado != null) {

            System.out.println("");
            return "redirect:/home";
        } else {
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            return "login.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo, HttpServletRequest httpServletRequest) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        List<Categoria> categorias = Arrays.asList(Categoria.values());
        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("httpServletRequest", httpServletRequest);

        if (logueado.getRol().toString().equals("ADMIN")) {
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            //return "redirect:/admin/dashboard";
            return "redirect:/usuario_lista";
        }

        //return "usuario_perfil.html";
        return "redirect:/perfil-usuario/" + logueado.getId();
    }

    // --- Perfil del Servicio de Proveedor --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/servicio-perfil/{id}")
    public String servicioPerfil(@PathVariable String id, ModelMap modelo, HttpServletRequest httpServletRequest) {

        List<Categoria> categorias = Arrays.asList(Categoria.values());
        modelo.addAttribute("categorias", categorias);

        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.addAttribute("proveedor", proveedor);

        modelo.addAttribute("httpServletRequest", httpServletRequest);

        return "servicio_perfil.html";
    }

    // --- Perfil Personal --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/perfil-usuario/{id}")
    public String usuarioPerfil(@PathVariable String id, HttpSession session, ModelMap modelo, HttpServletRequest httpServletRequest) throws Excepciones {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario != null && usuario.getId().equals(id)) {
            System.out.println("-    ENTANDO A PERFIL DE USUARIO    ----------- ROL -----" + usuario.getRol());
            List<Categoria> categorias = Arrays.asList(Categoria.values());
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);
            session.setAttribute("usuariosession", usuario);
            return "usuario_perfil.html";
        } else {
            System.out.println("-    USUARIO NULO   ----------------");
            return "redirect:/";
        }

    }

    //
    // --- Usuario Mensajes --- //
    //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/perfil-usuario/mensajes/{id}")
    public String usuarioMensajes(@PathVariable String id, HttpSession session, ModelMap modelo, HttpServletRequest httpServletRequest) throws Excepciones {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario != null && usuario.getId().equals(id)) {
            System.out.println("-    ENTANDO A PERFIL DE MENSAJES    ----------- ROL -----" + usuario.getRol());
            List<Categoria> categorias = Arrays.asList(Categoria.values());
            List<Mensaje> mensajesUsu = usuario.getMensajesList();

            modelo.addAttribute("httpServletRequest", httpServletRequest);
            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("mensajes", mensajesUsu);
            session.setAttribute("usuariosession", usuario);
            return "usuario_mensajes.html";
        } else {
            System.out.println("-    USUARIO NULO   ----------------");
            return "redirect:/";
        }

    }

    // --- Citas - Contrato --- //
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/confirmar-cita/{id}")
    public String confirmarCita(@PathVariable String id, ModelMap modelo, HttpServletRequest httpServletRequest) throws Excepciones {
        try {
            modelo.addAttribute("servicio", servicioServicio.buscarServicioPorId(id));
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            return "contrato.html";
        } catch (Excepciones e) {
            throw new Excepciones("--- Controlador CONFIRMAR CITA FALLO ---");
        }
    }

    @PostMapping("/cita/{id}")
    public String contratoCita(@RequestParam String idCliente, @RequestParam String idProveedor, @RequestParam String idServicio,
            @RequestParam Integer precio, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String telefono, @RequestParam String direccion, @RequestParam LocalDateTime fechaHoraServicio, HttpSession session,
            ModelMap modelo) throws Excepciones {

        try {

            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            // Usuario usuario = usuarioServicio.getOne(idCliente);

            System.out.println(" --- ");
            System.out.println("CONTRATO POR CONFIRMAR");
            System.out.println(" --- ");
            System.out.println(" --- CONTROLADOR Index... id Servicio : " + idServicio);
            Contrato contrato = contratoServicio.crearContrato(idProveedor, idCliente, idServicio, precio, nombre, apellido, telefono, direccion, fechaHoraServicio);

            System.out.println(" --- ");
            System.out.println(" CONTRATO CONFIRMADO --- OK");
            System.out.println(" --- ");

            System.out.println(" --- ");
            Usuario usuarioActualizado = contrato.getCliente();
            usuario = usuarioActualizado;

            usuarioServicio.actualizarSession();

            session.setAttribute("usuariosession", usuario);
            System.out.println(" ROL DE LA SESSION : " + usuario.getRol());
            System.out.println(" --- ");
            //return "redirect:/servicio-perfil/" + idProveedor;
            return "redirect:/citas/" + usuario.getId();

        } catch (Excepciones e) {
            throw new Excepciones("ERROR CREAR CLIENTE CONTROLADOR");
        }

    }

    // --- Listado de Citas o Servicios contratados --- //
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/citas/{id}")
    public String serviciosContratados(@PathVariable String id, HttpSession session, ModelMap modelo,
            HttpServletRequest httpServletRequest) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        //Usuario usuario = usuarioServicio.getOne(id);
        System.out.println("----- CITAS ----- USUARIO ROL  - " + usuario.getRol());
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("todos", "Todos los contratos");
        if (usuario.getRol().toString().equals("CLIENTE") || usuario.getRol().toString().equals("MOD") || usuario.getRol().toString().equals("ADMIN")) {
            // TRAE CONTRATOS CLIENTE
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            List<Contrato> contratosCliente = contratoServicio.listarContratosPorCliente(usuario);
            modelo.addAttribute("contratos", contratosCliente);
        }
        if (usuario.getRol().toString().equals("PROVEEDOR")){// || usuario.getRol().toString().equals("MOD") || usuario.getRol().toString().equals("ADMIN")) 
            // TRAE CONTRATOS PROVEEDOR
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            List<Contrato> contratosProveedor = contratoServicio.listarContratosPorProveedor(usuario);
            modelo.addAttribute("contratos", contratosProveedor);
        }
        session.setAttribute("usuariosession", usuario);

        return "citas.html";
    }

    // --- Filtros Contratos --- //
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/citas-iniciado/{id}")
    public String serviciosIniciados(ModelMap modelo, HttpSession session, HttpServletRequest httpServletRequest) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("iniciado", "Citas iniciadas");

        if (usuario.getRol() == Rol.CLIENTE || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS CLIENTE
            List<Contrato> contratos = contratoServicio.listarContratosAceptados(usuario);
            modelo.addAttribute("contratos", contratos);

        }
        if (usuario.getRol() == Rol.PROVEEDOR || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS PROVEEDOR
            List<Contrato> contratosProveedor = contratoServicio.listarContratosAceptadosProveedor(usuario);
            modelo.addAttribute("contratos", contratosProveedor);
        }

        return "citas.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/citas-cancelado/{id}")
    public String serviciosCancelados(ModelMap modelo, HttpSession session, HttpServletRequest httpServletRequest) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("cancelado", "Citas canceladas");

        if (usuario.getRol() == Rol.CLIENTE || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS CLIENTE

            List<Contrato> contratos = contratoServicio.listarContratosCancelados(usuario);
            modelo.addAttribute("contratos", contratos);

        }
        if (usuario.getRol() == Rol.PROVEEDOR || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS PROVEEDOR
            List<Contrato> contratosProveedor = contratoServicio.listarContratosCanceladosProveedor(usuario);
            modelo.addAttribute("contratos", contratosProveedor);
        }

        return "citas.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/citas-finalizado/{id}")
    public String serviciosFinalizados(ModelMap modelo, HttpSession session, HttpServletRequest httpServletRequest) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("finalizado", "Citas Finalizadas");

        if (usuario.getRol() == Rol.CLIENTE || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS CLIENTE

            List<Contrato> contratos = contratoServicio.listarContratosFinalizados(usuario);
            modelo.addAttribute("contratos", contratos);

        }
        if (usuario.getRol() == Rol.PROVEEDOR || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS PROVEEDOR
            List<Contrato> contratosProveedor = contratoServicio.listarContratosFinalizadosProveedor(usuario);
            modelo.addAttribute("contratos", contratosProveedor);
        }

        return "citas.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/citas-pendiente/{id}")
    public String serviciosPendiente(ModelMap modelo, HttpSession session, HttpServletRequest httpServletRequest) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("finalizado", "Citas Finalizadas");

        if (usuario.getRol() == Rol.CLIENTE || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS CLIENTE
            List<Contrato> contratos = contratoServicio.listarContratosPendientes(usuario);
            modelo.addAttribute("contratos", contratos);
        }
        if (usuario.getRol() == Rol.PROVEEDOR || usuario.getRol() == Rol.MOD || usuario.getRol() == Rol.ADMIN) {
            // TRAE CONTRATOS PROVEEDOR
            List<Contrato> contratosProveedor = contratoServicio.listarContratosPendientesProveedor(usuario);
            modelo.addAttribute("contratos", contratosProveedor);
        }

        return "citas.html";
    }

    // --- CANCELAR -- FINALIZAR CONTRATO --- //
    @GetMapping("/cancelar-contrato/{id}")
    public String cancelarContrato(@PathVariable String id, HttpSession session, HttpServletRequest httpServletRequest, ModelMap modelo) throws Excepciones {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            System.out.println("Tratando de cancelar contrato de id " + id);
            contratoServicio.contratoCancelar(id);
            return "redirect:/citas/" + usuario.getId();

        } catch (Exception e) {
            throw new Excepciones("FALLA AL TRATAR DE CANCELAR CONTRATO");
        }
    }

    @GetMapping("/finalizar-contrato/{id}")
    public String finalizarContrato(@PathVariable String id, HttpSession session, HttpServletRequest httpServletRequest, ModelMap modelo) throws Excepciones {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            System.out.println("Tratando de FINALIZAR contrato de id " + id);
            contratoServicio.contratoFinalizar(id);
            return "redirect:/citas/" + usuario.getId();

        } catch (Exception e) {
            throw new Excepciones("FALLA AL TRATAR DE CANCELAR CONTRATO");
        }
    }

    @GetMapping("/aceptar-contrato/{id}")
    public String aceptarContrato(@PathVariable String id, HttpSession session, HttpServletRequest httpServletRequest, ModelMap modelo) throws Excepciones {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            modelo.addAttribute("httpServletRequest", httpServletRequest);
            System.out.println("Tratando de ACEPTAR contrato de id " + id);
            contratoServicio.contratoAceptar(id);
            return "redirect:/citas/" + usuario.getId();

        } catch (Exception e) {
            throw new Excepciones("FALLA AL TRATAR DE CANCELAR CONTRATO");
        }
    }

    // --- Filtros de Servicios --- //
//    @PostMapping("/filtro")
//    public String filtroLista(@RequestParam String filtro) throws Excepciones {
//
//        System.out.println("ESTA BUSCANDO EL FILTRO DE " + filtro);
//        try {
//
//            if (filtro.equalsIgnoreCase("todos")) {
//                return "redirect:/servicios";
//            } else if (filtro.equalsIgnoreCase("salud")) {
//                return "redirect:/salud";
//            } else if (filtro.equalsIgnoreCase("plomeria")) {
//                return "redirect:/plomeria";
//            } else if (filtro.equalsIgnoreCase("electricidad")) {
//                return "redirect:/electricidad";
//            } else if (filtro.equalsIgnoreCase("limpieza")) {
//               return "redirect:/limpieza";
//            } else if (filtro.equalsIgnoreCase("jardineria")) {
//               return "redirect:/jardineria";
//            } else if (filtro.equalsIgnoreCase("varios")) {
//               return "redirect:/varios";
//            } else {
//               return "redirect:/servicios";
//            }
//            
//        } catch (Exception e) {
//            throw new Excepciones("Error de Filtro Admin");
//        }
//
//    }
    // --- Servicios Ofrecidos - Proveedores listas --- //
    @GetMapping("/servicios")
    public String servicios(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> proveedoresOrdenados = proveedorServicio.listarProveedoresOrdenadosPorCategoria();
        modelo.addAttribute("proveedoresOrdenados", proveedoresOrdenados);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("todos", "Todos los Proveedores");

        return "servicios2.html";
    }

    @GetMapping("/salud")
    public String filtrarProveedoresSalud(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = proveedorServicio.listarSalud();
        modelo.addAttribute("proveedoresOrdenados", usuarios);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("salud", "Proveedores de Salud");
        return "servicios2.html";
    }

    @GetMapping("/plomeria")
    public String filtrarProveedoresPlomeria(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = proveedorServicio.listarPlomeria();
        modelo.addAttribute("proveedoresOrdenados", usuarios);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("plomeria", "Proveedores de Plomeria");
        return "servicios2.html";
    }

    @GetMapping("/electricidad")
    public String filtrarProveedoresElectricidad(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = proveedorServicio.listarElectricidad();
        modelo.addAttribute("proveedoresOrdenados", usuarios);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("electricidad", "Proveedores de Electricidad");
        return "servicios2.html";
    }

    @GetMapping("/limpieza")
    public String filtrarProveedoresLimpieza(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = proveedorServicio.listarLimpieza();
        modelo.addAttribute("proveedoresOrdenados", usuarios);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("limpieza", "Proveedores de Limpieza");
        return "servicios2.html";
    }

    @GetMapping("/jardineria")
    public String filtrarProveedoresJardineria(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = proveedorServicio.listarJardineria();
        modelo.addAttribute("proveedoresOrdenados", usuarios);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("jardineria", "Proveedores de Jardineria");
        return "servicios2.html";
    }

    @GetMapping("/varios")
    public String filtrarProveedoresVarios(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = proveedorServicio.listarVarios();
        modelo.addAttribute("proveedoresOrdenados", usuarios);
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.put("varios", "Proveedores Varios");
        return "servicios2.html";
    }

    //--------------------  ADMIN PARTE  ----------------------------------//
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/usuario_lista")
    public String listarUsuarios(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Categoria> categorias = Arrays.asList(Categoria.values());
        List<Usuario> usuarios = usuarioServicio.listarTodosLosUsuarios();
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/alta/{idUsuario}")
    public String cambiarAlta(@PathVariable String idUsuario, HttpSession session) {
       // Optional<Usuario> logueado = (Optional<Usuario>) session.getAttribute("usuariosession");
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        //if (logueado.isPresent() && logueado.get().getRol().toString().equals("ADMIN")) {
        if (logueado != null && logueado.getRol().toString().equals("ADMIN")) {
            System.out.println("   ---  USUARIO LOGUEADO - " + logueado.getEmail());
            usuarioServicio.cambiarAlta(idUsuario);
            System.out.println("   ---  USUARIO LOGUEADO - " + logueado.getEmail() + " HA CAMBIADO EL ALTA ");
            return "redirect:/usuario_lista";
        }
        System.out.println("   ---  USUARIO SIN PERMISO - ");
        return "error.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/cambiarRol/{idUsuario}/{palabra}")
    public String cambiarRol(@PathVariable String idUsuario, @PathVariable String palabra, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            usuarioServicio.cambiarRol(idUsuario, palabra);
            return "redirect:/usuario_lista";
        } else {
            return "error.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/eliminar_cuenta/{idUsuario}")
    public String eliminarCuentaBd(@PathVariable String idUsuario, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            usuarioServicio.eliminarCuentaBd(idUsuario);
        }
        return "redirect:/usuario_lista";
    }

    //ADMIN FILTROS LISTA DE USUARIOS //
    // --- FILTROS LISTA USUARIOS --- //
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/filtro")
    public String filtroLista(@RequestParam String filtro) throws Excepciones {

        System.out.println("ESTA BUSCANDO EL FILTRO DE " + filtro);
        try {

            if (filtro.equalsIgnoreCase("todos")) {
                return "redirect:/admin/usuarios";
            } else if (filtro.equalsIgnoreCase("usuarios")) {
                return "redirect:/admin/usuarios-filtros-usuarios";
            } else if (filtro.equalsIgnoreCase("proveedores")) {
                return "redirect:/admin/usuarios-filtros-proveedores";
            } else if (filtro.equalsIgnoreCase("clientes")) {
                return "redirect:/admin/usuarios-filtros-clientes";
            } else if (filtro.equalsIgnoreCase("activos")) {
                return "redirect:/admin/usuarios-filtros-activos";
            } else if (filtro.equalsIgnoreCase("baja")) {
                return "redirect:/admin/usuarios-filtros-baja";
            } else {
                return "redirect:/admin/usuarios";
            }

        } catch (Exception e) {
            throw new Excepciones("Error de Filtro Admin");
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/filtro-user")
    public String filtrarUsuarios(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = usuarioServicio.filtrarUsuarios();
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/filtro-proveedor")
    public String filtrarProveedores(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = usuarioServicio.filtrarProveedor();
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/filtro-clientes")
    public String filtrarClientes(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = usuarioServicio.filtrarCliente();
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/filtro-activos")
    public String filtrarActivos(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = usuarioServicio.filtrarActivo();
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOD')")
    @GetMapping("/filtro-baja")
    public String filtrarInactivos(ModelMap modelo, HttpServletRequest httpServletRequest) {
        List<Usuario> usuarios = usuarioServicio.filtrarBaja();
        modelo.addAttribute("httpServletRequest", httpServletRequest);
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    // --- COMENTOARIOS --- //
    //---------- Comentarios Perfil Proveedor------------//
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/servicios/proveedor/{id}")
    public String comentar(@RequestParam String comentario,
            @RequestParam String id,// id de usuario
            @RequestParam String idProveedor,
            ModelMap modelo) {

        try {

            comentarioServicio.persistirComentario(comentario, id, idProveedor);

            modelo.put("exito", "COMENTARIO REALIZADO CORRECTAMENTE");
            return "redirect:/servicio-perfil/" + idProveedor;
        } catch (Excepciones ex) {
            System.out.println("1----------- EXCEPCION DE CARGA DE COMENTARIO");
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "servicio_perfil.html";
    }

    // --- Comentarios en Contrato --- //
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/servicios/comentario/{id}")
    public String comentarServicio(@RequestParam String comentario,
            @RequestParam String id,// id de usuario
            @RequestParam String idProveedor,
            @RequestParam String idContrato,
            ModelMap modelo) {

        try {

            comentarioServicio.persistirComentarioServicio(comentario, id, idProveedor, idContrato);

            modelo.put("exito", "COMENTARIO REALIZADO CORRECTAMENTE");
            return "redirect:/citas/" + id;
        } catch (Excepciones ex) {
            System.out.println("1----------- EXCEPCION DE CARGA DE COMENTARIO");
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "servicio_pruebas.html";
    }

    // --- Modificar Comentarios Perfil Proveedor --- //
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/modificar-comentario/{id}")
    public String modificarComentarioProveedor(@RequestParam String idProv, @RequestParam String idComent, @RequestParam String comentario) {

        try {
            comentarioServicio.modificarComentarioProveedor(idComent, comentario);
            System.out.println("---");
            System.out.println("--- Comentario modificado ---");
            System.out.println("---");
            return "redirect:/servicio-perfil/" + idProv;

        } catch (Exception e) {
            return "index.html";
        }

    }

    // --- eliminar comentario en Proveedor --- //
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/eliminar-comentario-proveedor/{id}")
    public String eliminarComentarioProveedor(@RequestParam String idProv, @RequestParam String idComent) throws Excepciones {
        try {
            if (!idComent.isEmpty() && !idProv.isEmpty()) {

                System.out.println("idComentario - " + idComent);
                System.out.println("idContrato - " + idProv);

                comentarioServicio.eliminarComentarioProveedor(idComent, idProv);
                System.out.println("---");
                System.out.println("--- Comentario eliminado ---");
                System.out.println("---");
                return "redirect:/servicio-perfil/" + idProv;
            }
        } catch (Exception e) {
            throw new Excepciones("ERROR AL ELIMINAR COMENTARIO --- CONTROLADOR ---");
        }

        return "index.html";
    }

    // --- Modificar Comentarios en contrato --- //
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/modificar-comentario-usuario/{id}")
    public String modificarComentarioContrato(@PathVariable String id, @RequestParam String idUser, @RequestParam String comentario) {

        try {
            comentarioServicio.modificarComentarioContrato(id, comentario);
            System.out.println("---");
            System.out.println("--- Comentario modificado ---");
            System.out.println("---");
            //return "redirect:/servicios-contratados/"+idUser;
            return "redirect:/citas/" + idUser;

        } catch (Exception e) {
            return "servicios_contratados_lista.html";
        }

    }

    // --- eliminar comentario en contrato --- //
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MOD')")
    @PostMapping("/eliminar-comentario/{id}")
    public String eliminarComentarioContrato(@RequestParam String idUser, @RequestParam String idContrato, @RequestParam String idComent) throws Excepciones {
        try {
            if (!idComent.isEmpty() && !idContrato.isEmpty()) {

                System.out.println("idComentario - " + idComent);
                System.out.println("idContrato - " + idContrato);

                comentarioServicio.eliminarComentarioContrato(idComent, idContrato);
                System.out.println("---");
                System.out.println("--- Comentario eliminado ---");
                System.out.println("---");
                //return "redirect:/servicios-contratados/"+idUser;
                return "redirect:/citas/" + idUser;

            }
        } catch (Exception e) {
            throw new Excepciones("ERROR AL ELIMINAR COMENTARIO --- CONTROLADOR ---");
        }

        return "index.html";
    }

    // --- Calificaciones --- //
    @PostMapping("/calificacion/{id}")
    public String calificar(@PathVariable String id, @RequestParam String idContrato, @RequestParam String num) throws Excepciones {

        System.out.println("EL ID del URL  ES = " + id);
        System.out.println("EL ID del CONTRATO  ES = " + idContrato);
        System.out.println("EL NUMERO INGRESADO ES = " + num + " * ");

        try {
            contratoServicio.calificacion(idContrato, num);
            System.out.println("CALIFICACION ---- OK  ");
        } catch (Exception e) {
            throw new Excepciones("FALLO CONTROLADOR CALIFICAR");
        }

        return "redirect:/citas/" + id;
    }

    // --- Mesajes   - Eliminar mensaje del sistema --- //
    
    @PostMapping("eliminar-mensaje-sistema")
    public String eliminarMensajeSistema(@RequestParam String idUsuario, @RequestParam String idMensaje) throws Excepciones {
        try {
            System.out.println("---");
            System.out.println("--- Eliminar mensaje de sistema ---");
            System.out.println("--- idUsuario ---"+idUsuario);
            System.out.println("--- idMensaje ---"+idMensaje);
            System.out.println("---");
            mensajeServicio.eleminarMensajeSiatema(idMensaje, idUsuario);
            System.out.println("---");
            System.out.println("--- Mensaje de sistema eliminado OK ---");
            System.out.println("---");
        } catch (Excepciones e) {
            throw new Excepciones(e.getMessage());
        }
        
        
        return "redirect:/perfil-usuario/mensajes/" + idUsuario;
    }







}
