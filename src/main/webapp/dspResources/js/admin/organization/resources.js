
require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $("input:text,form").attr("autocomplete", "off");

        $('#saveDetails').click(function(event) {
            $('#action').val('save');
            
            var programIdList = "";
            $('input[type=checkbox]').each(function() {
                if(this.checked) {
                    programIdList += $(this).val() + ',';
                }
            });
            
            $('#programIds').val(programIdList.substring(0,programIdList.length-1));
            
            $("#organizationResources").submit();
        });

        $('#saveCloseDetails').click(function(event) {
            $('#action').val('close');

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



