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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author facun
 */
@Service
public class ProveedorServicio implements UserDetailsService {

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Proveedor proveedor = proveedorRepositorio.buscarPorEmail(email);

        if (proveedor != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + proveedor.getRol().toString()); //ROLE_USER
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", proveedor);
            return new User(proveedor.getEmail(), proveedor.getPassword(), permisos);
        } else {
            return null;
        }

    }

    public Proveedor crearProveedor(String nombre, String apellido, String email, String password,
            String telefono, Categoria categoria, MultipartFile archivo) throws Excepciones {

        validar(nombre, apellido, email, password, telefono, categoria);
        
        System.out.println("PROVEEDOR SERVICIO");
        Proveedor proveedor = new Proveedor();
        Usuario usuario = usuarioServicio.crearUsuario(nombre, apellido, email, password, archivo);

        proveedor.setNombre(usuario.getNombre());
        proveedor.setApellido(usuario.getApellido());
        proveedor.setEmail(usuario.getEmail());
        proveedor.setPassword(usuario.getPassword());

        proveedor.setImagen(usuario.getImagen());

        proveedor.setActivo(true);
        proveedor.setRol(Rol.PROVEEDOR);
        proveedor.setIntentos(0);
        proveedor.setUltimaConexion(null);
     
        proveedor.setCategoriaServicio(categoria);
        proveedor.setTelefono(telefono);

        return proveedor;

    }

    public void persistirProveedor(String nombre, String apellido, String email, String password, String telefono, Categoria categoria, MultipartFile archivo) throws Excepciones {
        try {
            Proveedor proveedor = crearProveedor(nombre, apellido, email, password, telefono, categoria, archivo);
            proveedorRepositorio.save(proveedor);
            System.out.println("Proveedor guardado correctamente");
        } catch (Excepciones e) {
            throw new Excepciones("error al persistir Proveedor");
        }
    }

     public Proveedor buscarPorId(String id) throws Excepciones {
        try {
            Optional<Proveedor> resp = proveedorRepositorio.findById(id);
            if (resp.isPresent()) {
                Proveedor proveedor = resp.get();
                return proveedor;
            }
        } catch (Exception e) {
            throw new Excepciones("Respuesta de usuario no encontrado");
        }
        return null;
    }
    
    public Proveedor getOne(String id){
        Proveedor proveedor = proveedorRepositorio.getOne(id);
        return proveedor;
    }
    
    @Transactional(readOnly = true)
    public List<Usuario> listarProveedor() {

        List<Usuario> proveedores = usuarioRepositorio.listarProveedor();

        return proveedores;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarProveedoresOrdenadosPorCategoria() {

        List<Usuario> proveedoresOrdenados = usuarioRepositorio.listarProveedoresOrdenadosPorCategoria();

        return proveedoresOrdenados;
    }

    // --- Listas por Categoria --- //
    @Transactional(readOnly = true)
    public List<Usuario> listarSalud() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.SALUD);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarElectricidad() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.ELECTRICIDAD);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPlomeria() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.PLOMERIA);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarLimpieza() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.LIMPIEZA);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarJardineria() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.JARDINERIA);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarVarios() {
        return usuarioRepositorio.listarProveedoresPorCategoria(Rol.PROVEEDOR, Categoria.VARIOS);
    }

    
    
     // --- Validaciones --- //
    public void validar(String nombre, String apellido, String email, String password,
            String telefono, Categoria categoria) throws Excepciones{
        
        if(nombre.isEmpty() || nombre == null){
            throw new Excepciones("El nombre no puede ser nulo o estar vacio");
        }
        if(apellido.isEmpty() || apellido == null){
            throw new Excepciones("El apellido no puede ser nulo o estar vacio");
        }
        if(email.isEmpty() || email == null){
            throw new Excepciones("El email no puede ser nulo o estar vacio");
        }
        if(password.isEmpty() || password == null){
            throw new Excepciones("El password no puede ser nulo o estar vacio");
        }
        if(telefono.isEmpty() || telefono == null){
            throw new Excepciones("El telefono no puede ser nulo o estar vacio");
        }
        if(categoria == null){
            throw new Excepciones("La categoria no puede estar vacia");
        }
    }
    
}
