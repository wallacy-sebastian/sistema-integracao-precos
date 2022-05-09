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
    var s = location.href ;
    console.log(typeof(s)+" "+s)
    const url = new URL(s);
    
    var a = $('#search-input').val();
    if(a == ''){
        console.log('campo vazio');
    }else{
        url.searchParams.set('searchInput', a);
        console.log(a+' '+url);
    }
    a = $('#categoria').val();
    if(a == 0){
        console.log('campo vazio');
    }else{
        url.searchParams.set('categoria', a);
        console.log(a+' '+url);
    }
    a = $('#sort').val();
    if(a == 0){
        console.log('campo vazio');
    }else{
        url.searchParams.set('sort', a);
        console.log(a+' '+url);
    }
    a = $('#loja').val();
    if(a == 0){
        console.log('campo vazio');
    }else{
        url.searchParams.set('loja', a);
        console.log(a+' '+url);
    }
    console.log(url.href);
    location.href = url.href;
});

