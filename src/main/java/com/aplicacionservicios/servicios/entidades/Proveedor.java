/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.entidades;

import com.aplicacionservicios.servicios.enumeradores.Categoria;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
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
public class Proveedor extends Usuario {



    @Enumerated(EnumType.STRING)
    private Categoria categoriaServicio;

    private Double promedioGeneral;
    private String promedioGeneralString;

    @OneToMany
    private List<Servicio> servicios;
    
    @OneToMany
    private List<Comentario> comentarios;

}
