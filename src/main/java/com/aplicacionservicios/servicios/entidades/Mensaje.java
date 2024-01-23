/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author facun
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Mensaje {
    
    @Id
    @GeneratedValue(generator = "uuid")// Generar id alfanumerico unico
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Proveedor proveedor;
    
    @ManyToOne
    private Contrato contrato;
    
    @ManyToOne
    private Servicio servicio;
    
    private String mensaje;
    
    private Boolean visto;
    
    @Column(name = "fecha_hora_mensaje")
    private LocalDateTime fechaCompletaMensaje;
    
    @Column(name = "fecha_mensaje")
    private LocalDate fechaMensaje;

    @Column(name = "hora_mensaje")
    private LocalTime horaMensaje;
    
    

    
}
