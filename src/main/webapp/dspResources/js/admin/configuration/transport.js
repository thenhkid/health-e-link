$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
    
    var selMethodId = $('#transportMethod').val() 
    //Show file/download/FTP fields
    if(selMethodId === "1" || selMethodId === "3") {
        $('#upload-downloadDiv').show();
    }

    if(selMethodId === "3") {
        $('#additionalFTPDiv').show();
    }
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }
    
    $('#transportMethod').change(function() {
       var methodId = $(this).val();
       
       //hide all section divs
       $('.methodDiv').hide();
      
       //Show file/download/FTP fields
       if(methodId === "1" || methodId === "3") {
           $('#upload-downloadDiv').show();
       }
       
       if(methodId === "3") {
           $('#additionalFTPDiv').show();
       }
        
    });
    
    $('#useSource').click(function() {
        if($('#useSource').is(":checked")) {
            $('#fileName').val("USE SOURCE FILE");
        }
        else {
            $('#fileName').val("");
        }
    });

    

    //This function will save the messgae type field mappings
    $('#saveDetails').click(function() {
        $('#action').val('save');
        
        //Need to make sure all required fields are marked if empty.
        var hasErrors = 0;
        hasErrors = checkFormFields();

        if (hasErrors == 0) { 
            $('#transportDetails').submit();
        }
    });

    $('#next').click(function(event) {
        $('#action').val('next');

        var hasErrors = 0;
        hasErrors = checkFormFields();

        if (hasErrors == 0) {
            $('#transportDetails').submit();
        }
    });

});

function checkFormFields() {
    var hasErrors = 0;

    //Remove all has-error class
    $('div.form-group').removeClass("has-error");
    $('span.control-label').removeClass("has-error");
    $('span.control-label').html("");
    
    var selMethodId = $('#transportMethod').val() 
    
    //Make sure a transport method is chosen
    if($('#transportMethod').val() === "") {
       $('#transportMethodDiv').addClass("has-error");
       $('#transportMethodMsg').addClass("has-error");
       $('#transportMethodMsg').html('The transport method is a required field.');
       hasErrors = 1;
    }
    
    if (selMethodId === "1" || selMethodId === "3") {
       
    }
 

    return hasErrors;
}


