/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


const searchInput = document.getElementById('filtro');
const elementos = document.querySelectorAll('.usuario');

searchInput.addEventListener('input', function () {
    const busqueda = searchInput.value.toLowerCase();

    elementos.forEach(elemento => {
        const nombre = elemento.querySelector('.nombre').textContent.toLowerCase();
        const rol = elemento.querySelector('.rol').textContent.toLowerCase();
        const email = elemento.querySelector('.email').textContent.toLowerCase();
        const activo = elemento.querySelector('.activo').textContent.toLowerCase();

        if (busqueda === '' || email.includes(busqueda) || rol.includes(busqueda) || activo.includes(busqueda) || nombre.includes(busqueda)) {
            elemento.style.display = 'table-row';
        } else {
            elemento.style.display = 'none';
        }
    });
});

