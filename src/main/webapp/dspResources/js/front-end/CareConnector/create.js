/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require(['./main'], function () {
    require(['jquery'], function($) {

        $("input:text,form").attr("autocomplete", "off");

        $('.createLink').click(function() {
           var configId = $(this).attr('rel');
           var selTarget = $('#targetOrg_'+configId).val();
           selTarget = selTarget.split('-');
           var tConfigId = selTarget[0];
           var tOrgId = selTarget[1];

           if(tOrgId > 0) {
               $('#configId').val(configId);
               $('#targetOrg').val(tOrgId);
               $('#targetConfig').val(tConfigId);
               $('#createMessageForm').submit();
           }
           else {
               $('#row_'+configId).addClass("has-error");
               $('#rowMsg_' + configId).html("The target organization is required!");
           }
        });
    });  
});