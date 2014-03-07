/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
    
        //Fade out the alert message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }
        
        $('.submitMessage').click(function() {
           var errorsFound = 0;
          
           if(errorsFound == 0) {
                $('#orgProfileForm').submit(); 
           }
           
        });
        
        //Need to set the organization clean url based off of the organization name
        $('#orgName').keyup(function(event) {
            var orgName = $(this).val();
            var strippedorgName = orgName.replace(/ +/g, '');
            $('#cleanURL').val(strippedorgName);
        });
        
    }); 
});
