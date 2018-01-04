/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {

        $("input:text,form").attr("autocomplete", "off");
        
        $(document).on('click','.submit', function() {
            $('#assessmentForm').submit();
        });
        
        $(document).on('click','.cancel', function() {
            window.location.href="/clients/search";
        });
       
    });
});    