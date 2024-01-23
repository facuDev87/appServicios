/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


const stars = document.querySelectorAll('.star');

stars.forEach(function(star, index){
    star.addEventListener('mouseover', function(){ // star.addEventListener('click', function(){
        for(let i=0; i<=index; i++){
            stars[i].classList.add('checked');
        }
        for(let i=index+1; i<stars.length; i++){
            stars[i].classList.remove('checked');
        }
    })
});