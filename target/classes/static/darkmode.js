/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
//
//
//// Función para aplicar el tema claro
//const aplicarTemaClaro = () => {
//    const bodyElement = document.querySelector("body");
//    if (bodyElement) {
//        bodyElement.setAttribute("data-bs-theme", "light");
//        document.querySelector("#dl-icon").setAttribute("class", "bi bi-moon-fill");
//        // Almacena la preferencia del usuario en localStorage
//        localStorage.setItem('tema', 'claro');
//    }
//};
//
//// Función para aplicar el tema oscuro
//const aplicarTemaOscuro = () => {
//    const bodyElement = document.querySelector("body");
//    if (bodyElement) {
//        bodyElement.setAttribute("data-bs-theme", "dark");
//        document.querySelector("#dl-icon").setAttribute("class", "bi bi-sun-fill");
//        // Almacena la preferencia del usuario en localStorage
//        localStorage.setItem('tema', 'oscuro');
//    }
//};
//// Función para cambiar entre el tema oscuro y claro
//const cambiarTema = () => {
//    const temaActual = document.querySelector("body").getAttribute("data-bs-theme");
//
//    if (temaActual === "light") {
//        aplicarTemaOscuro();
//    } else {
//        aplicarTemaClaro();
//    }
//    
//    // Almacena la preferencia del usuario en localStorage
//    localStorage.setItem('tema', temaActual === 'light' ? 'oscuro' : 'claro');
//};
//// Función para cargar el tema almacenado en localStorage al cargar la página
//const cargarTemaAlmacenado = () => {
//    const temaAlmacenado = localStorage.getItem('tema');
//    
//    if (temaAlmacenado === 'oscuro') {
//        aplicarTemaOscuro();
//    } else {
//        aplicarTemaClaro();
//    }
//};
//cargarTemaAlmacenado();
//
//// Llama a la función para cargar el tema almacenado al cargar la página
//
//// Añadir evento al botón de cambio de tema
////document.getElementById('theme-switch').addEventListener('click', cambiarTema);

/*!
 * Color mode toggler for Bootstrap's docs (https://getbootstrap.com/)
 * Copyright 2011-2022 The Bootstrap Authors
 * Licensed under the Creative Commons Attribution 3.0 Unported License.
 */

//   --- bootstrap darkmode ---- //

//(() => {
//  "use strict";
//
//  const storedTheme = localStorage.getItem("theme");
//
//  const getPreferredTheme = () => {
//    if (storedTheme) {
//      return storedTheme;
//    }
//
//    return window.matchMedia("(prefers-color-scheme: dark)").matches
//      ? "dark"
//      : "light";
//  };
//
//  const setTheme = function (theme) {
//    if (
//      theme === "auto" &&
//      window.matchMedia("(prefers-color-scheme: dark)").matches
//    ) {
//      document.documentElement.setAttribute("data-bs-theme", "dark");
//    } else {
//      document.documentElement.setAttribute("data-bs-theme", theme);
//    }
//  };
//
//  setTheme(getPreferredTheme());
//
//  const showActiveTheme = (theme) => {
//    const activeThemeIcon = document.querySelector(".theme-icon-active use");
//    const btnToActive = document.querySelector(
//      `[data-bs-theme-value="${theme}"]`
//    );
//    const svgOfActiveBtn = btnToActive
//      .querySelector("svg use")
//      .getAttribute("href");
//
//    document.querySelectorAll("[data-bs-theme-value]").forEach((element) => {
//      element.classList.remove("active");
//    });
//
//    btnToActive.classList.add("active");
//    activeThemeIcon.setAttribute("href", svgOfActiveBtn);
//  };
//
//  window
//    .matchMedia("(prefers-color-scheme: dark)")
//    .addEventListener("change", () => {
//      if (storedTheme !== "light" || storedTheme !== "dark") {
//        setTheme(getPreferredTheme());
//      }
//    });
//
//  window.addEventListener("DOMContentLoaded", () => {
//    showActiveTheme(getPreferredTheme());
//
//    document.querySelectorAll("[data-bs-theme-value]").forEach((toggle) => {
//      toggle.addEventListener("click", () => {
//        const theme = toggle.getAttribute("data-bs-theme-value");
//        localStorage.setItem("theme", theme);
//        setTheme(theme);
//        showActiveTheme(theme);
//      });
//    });
//  });
//})();



const toggle = document.querySelector('#toggle');
const themeActual = localStorage.getItem('theme');

if (themeActual) {
  document.documentElement.setAttribute('data-theme', themeActual);
}

if (themeActual === 'oscuro') {
  toggle.checked = true;
}

const cambiarTheme = (event) => {
  if (event.target.checked) {
    document.documentElement.setAttribute('data-theme', 'oscuro');
    localStorage.setItem('theme', 'oscuro');
  } else {
    document.documentElement.setAttribute('data-theme', null);
    localStorage.setItem('theme', null);
  }
};

toggle.addEventListener('click', cambiarTheme);