/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    
    $('.createLink').click(function() {
       var configId = $(this).attr('rel');
       var selTarget = $('#targetOrg_'+configId).val();
       
       if(selTarget > 0) {
           $('#configId').val(configId);
           $('#targetOrg').val(selTarget);
           $('#createMessageForm').submit();
       }
       else {
           $('#row_'+configId).addClass("has-error");
           $('#rowMsg_' + configId).html("The target organization is required!");
       }
    });
    
});