

require(['./main'], function() {
    require(['jquery'], function($) {

        $("input:text,form").attr("autocomplete", "off");

        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        //When the user starts to enter their username make the delete button active
        $('#username').keyup(function(event) {
            if ($('#username').val() === '') {
                $('#submitButton').attr("disabled", "disabled");
            }
            else {
                $('#submitButton').removeAttr("disabled");
            }
        });

        //Make sure the two values equal before the delete function is allowed
        $('#submitButton').click(function(event) {
            if ($('#realUsername').val() != $('#username').val()) {
                $('#confirmDiv').addClass("has-error");
                $('#confirmMsg').html('That is not correct!');
            }
            else {
                $('#confirmOrgDelete').submit();
            }

        });

        $('#saveDetails').click(function(event) {
            $('#action').val('save');

            //Need to make sure all required fields are marked if empty.
            var hasErrors = 0;
            hasErrors = checkFormFields();

            if (hasErrors == 0) {
                $("#organization").submit();
            }
        });

        $('#saveCloseDetails').click(function(event) {
            $('#action').val('close');

            var hasErrors = 0;
            hasErrors = checkFormFields();

            if (hasErrors == 0) {
                $("#organization").submit();
            }
        });

        //Need to set the organization clean url based off of the organization name
        $('#orgName').keyup(function(event) {
            var orgName = $(this).val();
            var strippedorgName = orgName.replace(/ +/g, '');
            $('#cleanURL').val(strippedorgName);
            $('#nameChange').val(1);
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
            $('#CCDJarTemplateDiv').addClass("has-error");
            $('#CCDJarTemplateMsg').addClass("has-error");
            $('#CCDJarTemplateMsg').html('The XML/CCD Parsing Script must be a jar file.');
            hasErrors = 1;
        }

    }
    
    return hasErrors;


}

