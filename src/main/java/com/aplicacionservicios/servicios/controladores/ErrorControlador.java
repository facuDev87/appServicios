/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aplicacionservicios.servicios.controladores;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author facun
 */
@Controller
public class ErrorControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest, ModelMap modelo) {

        ModelAndView errorPage = new ModelAndView("error");

        String errorMsg = "";

        int httpErrorCode = getErrorCode(httpRequest);
        
        System.out.println("HTTP CODE ========="+httpErrorCode);
        
        
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe. ";
                 modelo.addAttribute("httpServletRequest", httpRequest);
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso.";
                modelo.addAttribute("httpServletRequest", httpRequest);
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado";
                 modelo.addAttribute("httpServletRequest", httpRequest);
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado";
                 modelo.addAttribute("httpServletRequest", httpRequest);
                break;
            }
            case 500: {
                errorMsg = "Ocurrio un error";
                 modelo.addAttribute("httpServletRequest", httpRequest);
                break;
            }
            default: errorMsg = "ESTO ES EL DEFAULT";
             modelo.addAttribute("httpServletRequest", httpRequest);
            break;
          
        }
        
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {

         System.out.println("HTTP CODE DEL METODO GET ERROR CODE ========="+httpRequest);
       
            return (int) httpRequest.getAttribute("jakarta.servlet.error.status_code");
      
    }
    
    public String getErrorPath(){
        return "/error.html";
    }
}

