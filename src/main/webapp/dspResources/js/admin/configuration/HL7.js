

require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
         
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }


        $('#saveDetails').click(function(event) {
            var hasErrors = 0;
            hasErrors = checkform();
            if (hasErrors == 0) {
                $("#HL7Details").submit();
            }

        });
        
    });
    
});

function checkform() {
    var errorFound = 0;

    //Check segment names
    $('.segmentName').each(function() {
        row = $(this).attr('rel');

        $('#segmentName_' + row).removeClass("has-error");
        $('#segmentNameMsg_' + row).html("");
        if ($(this).val() == '') {
            $('#segmentName_' + row).addClass("has-error");
            $('#segmentNameMsg_' + row).html("The segment name is required!");

            errorFound = 1;

        }
    });


    //Check element names
    $('.elementName').each(function() {
        row = $(this).attr('rel');
        $('#elementName_' + row).removeClass("has-error");
        $('#elementNameMsg_' + row).html("");
        if ($(this).val() == '') {
            $('#elementName_' + row).addClass("has-error");
            $('#elementNameMsg_' + row).html("The element name is required!");

            errorFound = 1;

        }
    });

    return errorFound;

}

