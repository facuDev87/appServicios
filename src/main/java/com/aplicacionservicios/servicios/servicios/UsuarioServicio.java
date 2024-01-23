/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.servicios;

import com.aplicacionservicios.servicios.entidades.Imagen;
import com.aplicacionservicios.servicios.entidades.Proveedor;
import com.aplicacionservicios.servicios.entidades.Usuario;
import com.aplicacionservicios.servicios.enumeradores.Categoria;
import com.aplicacionservicios.servicios.enumeradores.Rol;
import com.aplicacionservicios.servicios.excepciones.Excepciones;
import com.aplicacionservicios.servicios.repositorios.ProveedorRepositorio;
import com.aplicacionservicios.servicios.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author facun
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("EMAIL ----- " + email);

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        System.out.println("USUARIO EMAIL ---- " + usuario.getEmail());

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //ROLE_USER
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_MODERADOR')")
//    @GetMapping("/inicio")
//    public String inicio(HttpSession session, ModelMap modelo) {
//
//        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//
//        if (logueado.getRol().toString().equals("ADMIN")) {
//            return "redirect:/admin/dashboard";
//        }
//
//        return "redirect:/";
//    }
    // --- Creacion Usuario --- //
    public Usuario crearUsuario(String nombre, String apellido, String email, String password, MultipartFile archivo) throws Excepciones {

        validar(nombre, apellido, email, password);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        System.out.println("CREANDO USUARIO SERVICIO---- ARCHIVO : " + archivo);
        if (!archivo.isEmpty()) {
            Imagen imagen = imagenServicio.guardar(archivo);
            usuario.setImagen(imagen);
        } else //usuario.setImagen(null);
        {
            usuario.setImagen(null);
        }
        usuario.setActivo(true);
        usuario.setRol(Rol.USER);
        usuario.setIntentos(0);
        usuario.setUltimaConexion(null);
        usuario.setMensajesList(null);

        return usuario;
    }

    @Transactional
    public void persistirUsuario(String nombre, String apellido, String email, String password, MultipartFile archivo) throws Excepciones {
        try {
            usuarioRepositorio.save(crearUsuario(nombre, apellido, email, password, archivo));
        } catch (Exception e) {
            throw new Excepciones("Error al persistir usuario");
        }
    }

    // --- Busqueda Usuario --- //
    public Usuario buscarPorId(String id) throws Excepciones {
        try {
            Optional<Usuario> resp = usuarioRepositorio.findById(id);
            if (resp.isPresent()) {
                Usuario usuario = resp.get();
                return usuario;
            }
        } catch (Exception e) {
            throw new Excepciones("Respuesta de usuario no encontrado");
        }
        return null;
    }

    public Usuario getOne(String id) {

        Usuario usuario = usuarioRepositorio.getOne(id);
        return usuario;
    }

    @Transactional
    public void modificarDatos(String idUsuario, Rol rol, String nombre, String apellido, String email,
            String telefono, String direccion, Categoria categoria) throws Excepciones {

        System.out.println(" --- EL ROL DEL USUARIO ES : " + rol);

        if (rol == Rol.USER) {

            try {
                Optional<Usuario> respUsuario = usuarioRepositorio.findById(idUsuario);
                if (respUsuario.isPresent()) {
                    System.out.println("""
                                   --- Usuario Encontrado --- ACTUALIZANDO
                                   """);

                    Usuario usu = respUsuario.get();
                    usu.setNombre(nombre);
                    usu.setApellido(apellido);

                    System.out.println("""
                                       --- Usuario Actualizado - SERVICIO ---
                                       """);
                }
            } catch (Exception e) {
                throw new Excepciones(" --- Error al actualizar Usuario --- ");
            }
        }
        if (rol == Rol.CLIENTE) {
            try {
                Optional<Usuario> respCliente = usuarioRepositorio.findById(idUsuario);
                if (respCliente.isPresent()) {
                    System.out.println("""
                                   --- Cliente Encontrado --- ACTUALIZANDO
                                   """);
                    Usuario cliente = respCliente.get();
                    cliente.setNombre(nombre);
                    cliente.setApellido(apellido);
                    cliente.setDireccion(direccion);
                    cliente.setTelefono(telefono);

                }
            } catch (Exception e) {
                throw new Excepciones(" --- Error al actualizar Cliente ---");
            }
        }
        if (rol == Rol.PROVEEDOR) {
            try {
                Optional<Proveedor> respProv = proveedorRepositorio.findById(idUsuario);
                if (respProv.isPresent()) {
                    System.out.println("""
                                   --- Proveedor Encontrado --- ACTUALIZANDO
                                   """);
                    Proveedor prov = respProv.get();
                    prov.setNombre(nombre);
                    prov.setApellido(apellido);
                    prov.setTelefono(telefono);
                    prov.setCategoriaServicio(categoria);

                }
            } catch (Exception e) {
                throw new Excepciones(" --- Error al actualizar Proveedor ---");
            }

        }
    }

    @Transactional
    public void modificarImagenPerfil(String idUsuario, Rol rol, MultipartFile archivo) throws Excepciones {

        System.out.println(" --- EL ROL DEL USUARIO ES : " + rol);

        try {
            if (rol != Rol.PROVEEDOR) {
                Optional<Usuario> respUsuario = usuarioRepositorio.findById(idUsuario);
                if (respUsuario.isPresent()) {
                    Usuario usu = respUsuario.get();
                    String idImagen = null;
                    if (usu.getImagen() == null) { // Si no hay Imagen se carga una Imagen Nueva
                        System.out.println(" ------ IMAGEN NULA ------ CARGANDO IMAGEN ");
                        Imagen imagen = imagenServicio.guardar(archivo);
                        usu.setImagen(imagen);
                        usuarioRepositorio.save(usu);
                    }
                    if (usu.getImagen() != null && !archivo.isEmpty()) { // si hay imagen se actualiza la imagen por una nueva
                        System.out.println(" ---- IMAGEN Y ARCHIVO NUEVO ENCONTADOS ---- ACTUALIZANDO IMAGEN");
                        idImagen = usu.getImagen().getId();
                        Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                        usu.setImagen(imagen);
                        usuarioRepositorio.save(usu);
                    }
                }
            } else {
                Optional<Proveedor> respProveedor = proveedorRepositorio.findById(idUsuario);
                if (respProveedor.isPresent()) {
                    Proveedor proveedor = respProveedor.get();
                    String idImagen = null;
                    if (proveedor.getImagen() == null) {// Si no hay Imagen se carga una Imagen Nueva
                        System.out.println(" ------ IMAGEN NULA ------ CARGANDO IMAGEN ");
                        Imagen imagen = imagenServicio.guardar(archivo);
                        proveedor.setImagen(imagen);
                        usuarioRepositorio.save(proveedor);
                    }
                    if (proveedor.getImagen() != null && !archivo.isEmpty()) {// si hay imagen se actualiza la imagen por una nueva
                        System.out.println(" ---- IMAGEN Y ARCHIVO NUEVO ENCONTADOS ---- ACTUALIZANDO IMAGEN");
                        idImagen = proveedor.getImagen().getId();
                        Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                        proveedor.setImagen(imagen);
                        usuarioRepositorio.save(proveedor);
                    }
                }
            }

        } catch (Exception e) {
            throw new Excepciones("--- Error al modificar Imagen ---");
        }

    }

    @Transactional
    public void eliminarImagenPerfil(String idUsuario) throws Excepciones {
        try {
            Optional<Usuario> respUsu = usuarioRepositorio.findById(idUsuario);
            String idImg = null;
            if (respUsu.isPresent()) {
                Usuario usuario = respUsu.get();
                idImg = usuario.getImagen().getId();
                imagenServicio.eliminarImagenBD(idImg);
                usuario.setImagen(null);
            }
            System.out.println("--- Imagen de perfil ELIMINADA");
        } catch (Exception e) {
            throw new Excepciones(" --- Error al eliminar la Imagen ---");
        }
    }

    // --- Lisstas --- //
    public List<Usuario> listarTodosLosUsuarios() {

        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarios;

    }

    @Transactional
    public void cambiarAlta(String idUsuario) {
        Optional<Usuario> respUsu = usuarioRepositorio.findById(idUsuario);
        if (respUsu.isPresent()) {
            respUsu.get().setActivo(!respUsu.get().getActivo());

        }
    }

    @Transactional
    public void cambiarRol(String idUsuario, String palabra) {
        Optional<Usuario> respUsu = usuarioRepositorio.findById(idUsuario);
        if (respUsu.isPresent()) {

            System.out.println("LA PALABRA ES   " + palabra);

            switch (palabra) {
                case "USER":
                    respUsu.get().setRol(Rol.USER);
                    break;
                case "CLIENTE":
                    respUsu.get().setRol(Rol.CLIENTE);
                    break;
                case "PROVEEDOR":
                    respUsu.get().setRol(Rol.PROVEEDOR);
                    break;
                case "MOD":
                    respUsu.get().setRol(Rol.MOD);
                    break;
                case "ADMIN":
                    respUsu.get().setRol(Rol.ADMIN);
                    break;

                default:
                    throw new AssertionError();
            }

        }
    }

    @Transactional
    public void eliminarCuentaBd(String idUsuario) {
        Optional<Usuario> respUsu = usuarioRepositorio.findById(idUsuario);
        respUsu.ifPresent(usuario -> usuarioRepositorio.delete(usuario));
    }

    // --- ADMIN LISTAS --- //
    @Transactional(readOnly = true)
    public List<Usuario> filtrarUsuarios() {
        return usuarioRepositorio.listarUsuario();
    }

    @Transactional(readOnly = true)
    public List<Usuario> filtrarProveedor() {
        return usuarioRepositorio.listarProveedor();
    }

    @Transactional(readOnly = true)
    public List<Usuario> filtrarCliente() {
        return usuarioRepositorio.listarCliente();
    }

    @Transactional(readOnly = true)
    public List<Usuario> filtrarActivo() {
        return usuarioRepositorio.listarUsuarioActivo();
    }

    @Transactional(readOnly = true)
    public List<Usuario> filtrarBaja() {
        return usuarioRepositorio.listarUsuarioBaja();
    }

    // --- Validaciones --- //
    public void validar(String nombre, String apellido, String email, String password) throws Excepciones {
        if (nombre.isEmpty() || nombre == null) {
            throw new Excepciones("El nombre no puede ser nulo o estar vacio");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Excepciones("El apellido no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new Excepciones("El email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null) {
            throw new Excepciones("El password no puede ser nulo o estar vacio");
        }
    }

    public Usuario getUsuario(String email) {
        Optional<Usuario> respuesta = usuarioRepositorio.buscarUsuarioPorEmail(email);
        if (respuesta.isPresent()) {
            return respuesta.get();
        }
        return null;
    }

    
    // --- Actualizar session usuario ---//
    public void actualizarSession(){
            
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = loadUserByUsername(auth.getName());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

    }

}
