/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        
        $(document).on('click', '#enrollClients', function() {
            var atLeastOneIsChecked = false;
            $('input:checkbox').each(function () {
              if ($(this).is(':checked')) {
                atLeastOneIsChecked = true;
                return false;
              }
            });
           
           if(atLeastOneIsChecked === true) {
               var selectedPrograms = "";
               
                $('input:checkbox').each(function () {
                    if ($(this).is(':checked')) {
                        var configId = $(this).val();
                        var selTarget = $('#targetOrg_'+configId).val();
                        
                        if(selTarget !== "") {
                            selTarget = selTarget.split('-');
                            var tConfigId = selTarget[0];
                            var tOrgId = selTarget[1];
                            
                            if(selectedPrograms === "") {
                                selectedPrograms = configId+"-"+tConfigId+"-"+tOrgId;
                            }
                            else {
                                selectedPrograms = selectedPrograms + ","+configId+"-"+tConfigId+"-"+tOrgId;
                            }
                        }
                        else {
                            $(this).prop('checked', false);
                        }
                    }
                });
                
                $('#selectedPrograms').val(selectedPrograms);
                
                $('body').overlay({
                    message : 'Creating Referrals...'
                 });
                    
                 setTimeout( function () { 
                   $("#enrollClientForm").submit();
                }, 300);    
               
           }
           else {
               
           }
            
        });

    });  
});       