

require(['./main'], function() {
    require(['jquery','bootstrapwysihtlm5', 'wysihtlm5'], function($) {

        $("input:text,form").attr("autocomplete", "off");
        
        $('#longDesc').wysihtml5( {
            "font-styles": true, 
            "emphasis": true, 
            "lists": true, 
            "html": false, 
            "link": true,
            "image": false, 
            "color": true,
            "stylesheets": ["/dspResources/css/admin/wysiwyg-color.css"]
        });

        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }


        $('#saveDetails').click(function(event) {
            $('#action').val('save');

            //Need to make sure all required fields are marked if empty.
            var hasErrors = 0;
            hasErrors = checkFormFields();

            if (hasErrors == 0) {
                $("#organization").submit();
            }
        });


    });
});

function checkFormFields() {
    var hasErrors = 0;

    
    if ($('#file').val() != "") {

        var filename = $('#file').val();
        var extension = filename.replace(/^.*\./, '');

        if (extension == filename) {
            extension = '';
        } else {
            // if there is an extension, we convert to lower case
            // (N.B. this conversion will not effect the value of the extension
            // on the file upload.)
            extension = extension.toLowerCase();
        }

        if (extension != "jar") {
            $('#parsingTemplateDiv').addClass("has-error");
            $('#parsingTemplateMsg').addClass("has-error");
            $('#parsingTemplateMsg').html('The Parsing Script must be a jar file.');
            hasErrors = 1;
        }

    }
    
    return hasErrors;


}

