/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require(['./main'], function() {
    require(['jquery'], function($) {

         $('.viewDetals').click(function() {
            $('#selorgId').val($(this).attr('rel'));
            $('#selmessageTypeId').val($(this).attr('rel2'));
            
            $('#viewHistoryDetails').submit();
         });
         
         $('.print').click(function() {
            window.print();
         });

    });
});

