/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
function ess(){
    var a = $('#search-input').val();
    if(a == ''){
        console.log('campo vazio');
    }else{
        location.href = $('#contextPath').val() + '?searchInput=' + a;
        console.log(a);
        console.log(location.href);
    }
}

$(document).on('submit', '.form_p_show', function(e){
    e.preventDefault();
    var a = $('#search-input').val();
    if(a == ''){
        console.log('campo vazio');
    }else{
        location.href = $('#contextPath').val() + '?searchInput=' + a;
        console.log(a);
        console.log(location.href);
    }
});

