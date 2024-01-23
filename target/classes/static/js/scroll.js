/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */





// Guardar la posición del scroll en localStorage
window.onbeforeunload = function () {
    localStorage.setItem("scrollPosition", window.scrollY);
};
// Restaurar la posición del scroll desde localStorage
// window.onload = function () {
//     var scrollPosition = localStorage.getItem("scrollPosition");
//     if (scrollPosition !== null) {
//         window.scrollTo(0, scrollPosition);
//     }
// };
// Restaurar la posición del scroll desde localStorage
window.onload = function () {
    var scrollPosition = localStorage.getItem("scrollPosition");
    if (scrollPosition !== null) {
        // Utilizar un desplazamiento suave
        window.scrollTo({
            top: scrollPosition,
            behavior: 'smooth'
        });

        // Limpiar la posición del scroll almacenada después de usarla
        localStorage.removeItem("scrollPosition");
    }
};

 function contarCaracteres(textarea) {
        // Obtener el textarea específico
        var textareaEspecifico = textarea;

        // Obtener el contenedor principal de la tarjeta
        var tarjetaContenedor = textareaEspecifico.closest(".comentario");

        // Obtener elementos relacionados dentro de la tarjeta actual
        var totalCaracteres = tarjetaContenedor.querySelector(".totalCaracteres");
        var caracteresRestantes = tarjetaContenedor.querySelector(".caracteresRestantes");

        // Obtener el número actual de caracteres y el máximo permitido
        var longitudTexto = textareaEspecifico.value.length;
        var maxCaracteres = parseInt(textareaEspecifico.getAttribute("maxlength"));

        // Actualizar los contadores
        totalCaracteres.innerText = longitudTexto;
        caracteresRestantes.innerText = maxCaracteres - longitudTexto;
    }