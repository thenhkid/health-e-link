$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }

    //If any field changes need to show the message in red that nothign
    //will be saved unless teh "Saved" button is pressed
    $(document).on('change', '.formField', function() {
        $('#saveMsgDiv').show();
    });

    //function that will get the field mappings for the selected transport method
    $('.changeTransportMethod').click(function() {
        var selTransportMethod = $('#transportMethod').val();

        if (selTransportMethod == "") {
            $('#transportMethodDiv').addClass("has-error");
        }
        else {
            window.location.href = 'mappings?i=' + selTransportMethod;
        }
    });


    //This function will save the messgae type field mappings
    $('#saveDetails').click(function(event) {
        $('#action').val('save');
        $('#seltransportMethod').val($('#transportMethod').val());

        //Need to make sure all required fields are marked if empty.
        var hasErrors = 0;

        if (hasErrors == 0) {
            $('#formFields').attr('action', 'saveFields');
            $('#formFields').submit();
        }
    });

    $('#next').click(function(event) {
        $('#action').val('next');

        var hasErrors = 0;

        if (hasErrors == 0) {
            $('#formFields').attr('action', 'saveFields');
            $('#formFields').submit();
        }
    });


    //Clicking the "Meets Standard" button will preselect the matching
    //field select box to the appropiate template field
    $(document).on('click', '#meetsStandard', function() {
        var fieldNo = null;
        $('#saveMsgDiv').show();

        $('.uFieldRow').each(function() {
            fieldNo = $(this).attr('rel');
            $('#matchingField_' + fieldNo + ' > option').each(function() {
                if ($(this).text() == fieldNo) {
                    $('#matchingField_' + fieldNo).val($(this).val());
                }
            });
        });

        $(this).children("span").addClass("glyphicon-ok");

        $(this).val("Clear Fields");
        $(this).attr('id', 'clearFields');
    });

    //Clicking the "Clear Fields" button will unselect the matching
    //field select box.
    $(document).on('click', '#clearFields', function() {
        $('#saveMsgDiv').show();

        $('.uFieldRow').each(function() {
            fieldNo = $(this).attr('rel');
            $('#matchingField_' + fieldNo).val("0");
        });

        $(this).children("span").removeClass("glyphicon-ok");

        $(this).val("Meets Standard");
        $(this).attr('id', 'meetsStandard');
    });

});


