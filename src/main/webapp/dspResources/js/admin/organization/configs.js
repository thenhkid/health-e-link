

require(['./main'], function () {
    require(['jquery'], function($) {
        
        $.ajaxSetup({
            cache: false
        });
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $("input:text,form").attr("autocomplete", "off");

        $('#searchConfigBtn').click(function() {
            $('#searchForm').submit();
        });
        
    });
});
