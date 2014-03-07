/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        $('.providerBody').mouseenter(function() {
             $(this).css('background-color','#F5F5F5');
        });
        
         $('.providerBody').mouseleave(function() {
             $(this).css('background-color','white');
        });
       
    }); 
});