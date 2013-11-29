/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$.ajaxSetup({
    cache: false
});

//var searchTimeout;

jQuery(document).ready(function($) {

    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }

    $("input:text,form").attr("autocomplete", "off");

    $('#searchOrgBtn').click(function() {
        $('#searchForm').submit();
    });

});


