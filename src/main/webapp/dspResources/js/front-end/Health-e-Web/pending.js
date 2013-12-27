/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    
    $('.viewLink').click(function() {
       var configId = $(this).attr('rel2');
       var transactionId = $(this).attr('rel');
       
        $('#configId').val(configId);
        $('#transactionId').val(transactionId);
        $('#viewMessageForm').submit();
       
    });
    
});