/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.entidades;

import com.aplicacionservicios.servicios.enumeradores.Rol;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author facun
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")// Generar id alfanumerico unico
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date ultimaConexion;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Boolean activo;

    private Integer intentos;

    // @OneToOne(cascade = CascadeType.PERSIST)
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "imagen_id", nullable = true)
    private Imagen imagen;

    
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;


    private int notificacion;
    
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Mensaje> mensajesList;
    
 
}

