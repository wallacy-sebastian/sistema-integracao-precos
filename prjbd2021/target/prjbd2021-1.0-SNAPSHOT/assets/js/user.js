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
    e.preventDefault();
    $.get($(this).data('href'), function(data){
        console.log(data);
        
        var productJson = JSON.parse(data);
        var product = productJson.produto;
        var al = productJson.avaliacoas;
        var el = productJson.entregas;
        var pl = productJson.pagamentos;
        var p_img = 'default_avatar.png';
        var modal = $('.modal-visualizar-produto');
        var secao = null;
        var loja = null;
        if(product.secao == '1'){
            secao = "Mouse";
        } else {
            secao = "Teclado";
        }
        
        if(product.loja == '1'){
            loja = "Americanas";
        } else if(product.loja == '2'){
            loja = "Kabum";
        } else {
            loja = "Londritech";
        }
        
        modal.find('.p_id').html('<strong>ID: </strong> '+product.id);
        modal.find('.p_nome').html('<strong>Nome: </strong> ' + product.nome);
        modal.find('.p_secao').html('<strong>Seção: </strong> ' + secao);
        modal.find('.p_descricao').html('<strong>Descrição: </strong> ' + product.descricao);
        modal.find('.p_modelo').html('<strong>Modelo: </strong> ' + product.modelo);
        modal.find('.p_marca').html('<strong>Marca: </strong> ' + product.marca);
        modal.find('.p_fichaTecnica').html('<strong>Ficha técnica: </strong> ' + product.fichaTecnica);
        modal.find('.p_valor').html('<strong>Valor: </strong> R$' + product.valor);
        modal.find('.p_createdAt').html('<strong>Data de criação: </strong> '+product.createdAt);
        modal.find('.p_loja').html('<strong>Loja: </strong> ' + loja);
        
        if(product.urlImg){
            p_img = product.urlImg;
        }
        
        modal.find('.produto-img').prop('src', p_img);
        
        var i = 0, amodal;
        console.log(al[0]);
        al.forEach(function(){
            amodal = modal.find('.produto-avaliacao').append('<div class="col-md-12 ava-'+i+'" style="border: 1px solid #000"></div>');
            amodal.find('.ava-'+i).append('<p><strong>Nome: </strong> '+al[i].nome+' </p>');
            amodal.find('.ava-'+i).append('<p><strong>Comentário: </strong> '+al[i].comentario+'</p>');
            amodal.find('.ava-'+i).append('<p><strong>Estrelas: </strong> '+al[i].estrelas+'</p>');
            i++;
        });
        
        i = 0;
        console.log(el);    
        el.forEach(function(){
            amodal = modal.find('.produto-entrega').append('<div class="col-md-12 ava-'+i+'" style="border: 1px solid #000"></div>');
            amodal.find('.ava-'+i).append('<p><strong>Nome Transportadora: </strong> '+el[i].nome+' </p>');
            amodal.find('.ava-'+i).append('<p><strong>Valor: R$ </strong> '+el[i].valor+'</p>');
            i++;
        });
        
        i = 0;
        var tipo;
        pl.forEach(function(){
            amodal = modal.find('.produto-pagamento').append('<div class="col-md-12 ava-'+i+'" style="border: 1px solid #000"></div>');
            if(pl[i].tipo == '1'){
                tipo = "Cartão";
            } else {
                tipo = "Boleto";
            }
            amodal.find('.ava-'+i).append('<p><strong>Tipo de pagamento: </strong> '+tipo+' </p>');
            amodal.find('.ava-'+i).append('<p><strong>Vezes: </strong> '+pl[i].vezes+' </p>');
            amodal.find('.ava-'+i).append('<p><strong>Valor: R$ </strong> '+pl[i].valor+'</p>');
            i++;
        });
        
        modal.modal();
        
    });
}

function stringSimilares(e){
    e.preventDefault();
    var str1 = $('#string1').val();
    var str2 = $('#string2').val();
    console.log(str1, str2);
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
        $passwordConfirm.next('p.help-block').html('<strong>Erro</strong>: as senhas não coincidem!');
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
});