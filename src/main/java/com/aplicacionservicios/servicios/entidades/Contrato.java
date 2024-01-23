/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class Contrato {

    @Id
    @GeneratedValue(generator = "uuid")// Generar id alfanumerico unico
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    private Usuario cliente;

    @ManyToOne
    private Proveedor proveedor;

    @ManyToOne
    private Servicio servicio;

    private Integer precio;

    private Boolean contratoPendiente;
    private Boolean contratoAceptado;
    //private Boolean contratoIniciado;
    private Boolean contratoFinalizado;
    private Boolean contratoCancelado;

    @OneToMany
    private List<Comentario> comentariosServicio;

    private Double calificacion;

    private String estrellas;

    @ElementCollection
    private List<Double> calificaciones;

    @Column(name = "fecha_hora_creacion")
    private LocalDateTime fechaHoraCreacion;
    
    @Column(name = "fecha_hora_servicio")
    private LocalDateTime fechaHoraServicio;

    @Column(name = "fecha_servicio")
    private LocalDate fechaServicio;

    @Column(name = "hora_servicio")
    private LocalTime horaServicio;
}
