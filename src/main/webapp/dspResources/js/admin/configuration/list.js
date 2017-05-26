


require(['./main'], function () {
    require(['jquery'], function($) {
        
    	var oSettings = datatable.fnSettings();
        
        datatable.fnSort( [ [5,'desc'] ] );
    	
        $.ajaxSetup({
            cache: false
        });
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $("input:text,form").attr("autocomplete", "off");

        $(document).on('click', '#configRow', function() {
            window.location.href = "details?i=" + $(this).attr('rel');
        });

        $('#searchConfigBtn').click(function() {
            $('#searchForm').submit();
        });
   
    });
});


