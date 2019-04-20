/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }
        
        $("input:text,form").attr("autocomplete", "off");

        $('#saveResources').click(function(event) {
            
            var programIdList = "";
            $('input[type=checkbox]').each(function() {
                if(this.checked) {
                    programIdList += $(this).val() + ',';
                }
            });
            
            $('#programIds').val(programIdList.substring(0,programIdList.length-1));
            
            $("#organizationResources").submit();
        });

    }); 
});