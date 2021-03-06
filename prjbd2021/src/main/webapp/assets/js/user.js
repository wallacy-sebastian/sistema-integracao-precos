/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function deleteUser(e) {
    e.preventDefault();
    $('.link_confirmacao_excluir_usuario').attr('href', $(this).data('href'));
    $('.modal_excluir_usuario').modal();
}

function readUser(e) {
    e.preventDefault();
    $.get($(this).data('href'), function (data) {
        var usuario = JSON.parse(data);
        var avatar = 'default_avatar.png';
        var $modal = $('.modal-visualizar-usuario');

        $modal.find('.p_id').html('<strong>ID: </strong>' + usuario.id);
        $modal.find(".p_login").html('<strong>Login: </strong>' + usuario.login);
        $modal.find('.p_nome').html('<strong>Nome: </strong>' + usuario.nome);
        $modal.find('.p_nascimento').html('<strong>Data de nascimento: </strong>' + usuario.nascimento);
        if (usuario.avatar) {
            avatar = usuario.avatar;
        }
        $modal.find('.usuario-img').prop('src', $.url("//img/" + avatar));
        
        $modal.modal();
    });
}

function readProduct(e){
    $('.modal-visualizar-produto').modal('hide');
    e.preventDefault();
//    $.get($(this).data('href'), function(data){
//        console.log(data);
    $.ajax({
        type: "GET",
        url: $(this).data('href'),
        dataType: 'text',
        success: function(data){
//            console.log(data);;
            var productJson = JSON.parse(data);
//            console.log(productJson);
            var product = productJson.produto;
            var al = productJson.avaliacoas;
            var el = productJson.entregas;
            var pl = productJson.pagamentos;
            var pil = productJson.produtosIntegrados;
            var p_img = 'default_avatar.png';
            var modal = $('.modal-visualizar-produto');
            var secao = null;
            var loja = null;
            if(product.secao == '1'){
                secao = "Mouse";
            } else {
                secao = "Monitor";
            }

            if(product.loja == '1'){
                loja = "Americanas";
            } else if(product.loja == '2'){
                loja = "Kabum";
            } else if(product.loja == '3') {
                loja = "Londritech";
            } else {
                loja = "Colombo";
            }

            modal.find('.p_id').html('<strong>ID: </strong> '+product.id);
            modal.find('.p_nome').html('<strong>Nome: </strong> ' + product.nome);
            modal.find('.p_secao').html('<strong>Se????o: </strong> ' + secao);
            modal.find('.p_descricao').html('<strong>Descri????o: </strong> ' + product.descricao);
            modal.find('.p_modelo').html('<strong>Modelo: </strong> ' + product.modelo);
            modal.find('.p_marca').html('<strong>Marca: </strong> ' + product.marca);
            modal.find('.p_fichaTecnica').html('<strong>Ficha t??cnica: </strong> ' + product.fichaTecnica);
            modal.find('.p_valor').html('<strong>Valor: </strong> R$' + product.valor);
            modal.find('.p_createdAt').html('<strong>Data de cria????o: </strong> '+product.createdAt);
            modal.find('.p_loja').html('<strong>Loja: </strong> ' + loja);

            if(product.urlImg){
                p_img = product.urlImg;
            }

            modal.find('.produto-img').prop('src', p_img);

            var i = 0, amodal;
            var a1 = modal.find('.produto-avaliacao');
            a1.empty();
            a1.append('<h3>Avalia????es</h3>');

            al.forEach(function(){
                amodal = a1.find('.produto-avaliacao').append('<div class="col-md-12 ava-'+i+'" style="border: 1px solid #000"></div>');
                amodal.find('.ava-'+i).append('<p><strong>Nome: </strong> '+al[i].nome+' </p>');
                amodal.find('.ava-'+i).append('<p><strong>Coment??rio: </strong> '+al[i].comentario+'</p>');
                amodal.find('.ava-'+i).append('<p><strong>Estrelas: </strong> '+al[i].estrelas+'</p>');
                i++;
            });

            i = 0;  
            a1 = modal.find('.produto-entrega');
            a1.empty();
            a1.append('<h3>Formas de entregas</h3>');

            el.forEach(function(){
                amodal = a1.append('<div class="col-md-12 ava-'+i+'" style="border: 1px solid #000"></div>');
                amodal.find('.ava-'+i).append('<p><strong>Nome Transportadora: </strong> '+el[i].nome+' </p>');
                amodal.find('.ava-'+i).append('<p><strong>Valor: R$ </strong> '+el[i].valor+'</p>');
                i++;
            });

            i = 0;
            a1 = modal.find('.produto-pagamento');
            a1.empty();
            a1.append('<h3>Formas de pagamento</h3>');
            var tipo;
            pl.forEach(function(){
                amodal = a1.append('<div class="col-md-12 ava-'+i+'" style="border: 1px solid #000"></div>');
                if(pl[i].tipo == '1'){
                    tipo = "Cart??o";
                } else {
                    tipo = "Boleto";
                }
                amodal.find('.ava-'+i).append('<p><strong>Tipo de pagamento: </strong> '+tipo+' </p>');
                amodal.find('.ava-'+i).append('<p><strong>Vezes: </strong> '+pl[i].vezes+' </p>');
                amodal.find('.ava-'+i).append('<p><strong>Valor: R$ </strong> '+pl[i].valor+'</p>');
                i++;
            });
            i = 0;
            amodal = modal.find('.produto-integrados');
            amodal.empty();
            amodal.append('<h3>Produtos Integrados</h3>');
            var context = $(document).find('#a-prod-i').val();
            pil.forEach(function(){
//                console.log(pil[i]+" "+product.id);
                if(pil[i] != product.id){
                    amodal.append('<a href="#" data-href="'+context+'/product/read?id='+pil[i]+'" class="link_visualizar_produto col-md-2">'+pil[i]+'</a>');
                }
                i++;
            });

            modal.modal();

        },
        error: function(jqXhr, textStatus, errorMessage, json){
            console.log("@@@"+errorMessage);
//            console.log(textStatus);
//            console.log(jqXhr);
//            console.log(json);
//            $('error-msg').removeClass('d-none');
//            $('error-msg').addClass('d-flex');
            alert('Erro ao carregar produto');

        }
    });
}

function stringSimilares(e){
    e.preventDefault();
    var str1 = $('#string1').val();
    var str2 = $('#string2').val();
//    console.log(str1, str2);
    $.get($(this).data('href'),{string1: str1, string2: str2}, function(data){
        
        var json = JSON.parse(data);
        console.log(json.str);
        $('#resultado-string').text(json.str);
    });   
}

function deleteUsers(e) {
    e.preventDefault();
    $('.form_excluir_usuarios').submit();
}

//function searchProduct(e){
//    e.preventDefault();
//    location.href = location.href + $('#search-input').val();
//    console.log($('#search-input').val());
//    console.log(location.href);
//    $('.form_p_show').submit();
//}

$(document).on('focusout', '.password-input,.password-confirm', function(e) {
    var $form = $(this).closest("form");
    var $password = $form.find(".password-input");
    var $passwordConfirm = $form.find(".password-confirm");

    if ($password.val().trim() == '') {
        return false;
    }

    if ($password.val() !== $passwordConfirm.val()) {
        $passwordConfirm.closest('.form-group').addClass('has-error');
        $password.closest('.form-group').addClass('has-error');
        $passwordConfirm.next('p.help-block').html('<strong>Erro</strong>: as senhas n??o coincidem!');
        $form.find("button,input[type='submit']").prop('disabled', true);
    } else {
        $passwordConfirm.closest('.form-group').removeClass('has-error').addClass('has-success');
        $password.closest('.form-group').removeClass('has-error').addClass('has-success');
        $passwordConfirm.next('p.help-block').html('');
        $form.find("button,input[type='submit']").prop('disabled', false);
    }
});

$(document).on('focusout', '#usuario-login', function (e) {
    console.log($("#usuario-login").val());
    var $input = $(this);
    if ($("#usuario-login").val() == $(this).data('value')) {
        var $formGroup = $input.parents(".form-group").first();
        if ($formGroup.hasClass("has-error")) {
            $formGroup.removeClass("has-error");
        }
        $input.next("p").html("");
    }
    else {
        $.post($.url("//user/checkLogin"), { login: $("#usuario-login").val() }, function(data) {
            var $formGroup = $input.parents(".form-group").first();
            if (data.status == "USADO") {
                console.log("usado");
                if (!$formGroup.hasClass("has-error")) {
                    $formGroup.addClass("has-error");
                }
                $input.next("p").html("O login escolhido existe. Por favor, tente outro.");
            } else {
                console.log("nao usado");
                if ($formGroup.hasClass("has-error")) {
                    $formGroup.removeClass("has-error");
                }
                $input.next("p").html("");
            }
        });
    }
});

$(document).ready(function () {
    $(document).on('click', '.link_excluir_usuario', deleteUser);
    $(document).on('click', '.link_visualizar_usuario', readUser);
    $(document).on('click', '.link_visualizar_produto', readProduct);
    $(document).on('click', '.button_confirmacao_excluir_usuarios', deleteUsers);
    $(document).on('click', '#btn-ver-str', stringSimilares);
    $(document).on('click', '.selecionar_todos', function(){
        if($('.selecionar_todos').attr("selec") == "false"){
            $('input[name="delete"]').each(function(){
                $(this).prop("checked", true);
                $('.selecionar_todos').attr("selec", "true");
            });
            $('.selecionar_todos').text("Desselecionar todos produtos");
        }else{
            $('input[name="delete"]').each(function(){
                $(this).prop("checked", false);
                $('.selecionar_todos').attr("selec", "false");
            });
            $('.selecionar_todos').text("Selecionar todos produtos");
        }
        
        console.log($('.selecionar_todos').attr("selec"));
    });
    
    $("*[data-toggle='tooltip']").tooltip({
        'container': 'body'
    });
    
    $(document).on('click', '.btn-func', searchProduct);
});