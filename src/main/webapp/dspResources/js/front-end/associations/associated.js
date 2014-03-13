/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        $('.viewLink').click(function() {
           
           $('#orgDetails').attr('action','/associations/'+$(this).attr('rel2'));
           $('#orgId').val($(this).attr('rel'));
           
           $('#orgDetails').submit();
            
            
        });
        
        
    });
});    