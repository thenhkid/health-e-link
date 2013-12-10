$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }
    ;

    //Add a new transport method
    $('.addTransportMethod').click(function() {
        var configId = $('#configId').last().val();
        var selMethod = $('#transportMethod').val();

        if (selMethod == '') {
            $('#transportMethodDiv').addClass("has-error");
        }
        else {
            $.ajax({
                url: 'addTransportMethod.do',
                type: "POST",
                data: {'configId': configId, 'transportMethod': selMethod},
                success: function(data) {
                    if (data == 1) {
                        window.location.href = "transport";
                    }
                }
            });
        }
    });

    //This function will save the messgae type field mappings
    $('#saveDetails').click(function(event) {
        $('#action').val('save');
        
        //Need to make sure all required fields are marked if empty.
        var hasErrors = 0;
        hasErrors = checkFormFields();

        if (hasErrors == 0) {
            $('#transportMethods').submit();
        }
    });

    $('#next').click(function(event) {
        $('#action').val('next');

        var hasErrors = 0;
        hasErrors = checkFormFields();

        if (hasErrors == 0) {
            $('#transportMethods').submit();
        }
    });

});

function checkFormFields() {
    var hasErrors = 0;

    //Remove all has-error class
    $('div.form-group').removeClass("has-error");
    $('span.control-label').removeClass("has-error");
    $('span.control-label').html("");

    //Loop through each transport method chosen
    $('.transportMethod').each(function() {
        var sectionVal = $(this).attr('rel');

        //Validate the File Upload fields
        if (sectionVal == 1 || sectionVal == 3) {
            var headVal = "";
            if (sectionVal == 3) {
                headVal = "FTP";
            }

            if ($('#' + headVal + 'currFile').val() != '') {
                if ($('#' + headVal + 'templateFile').val() != '' && $('#' + headVal + 'templateFile').val().indexOf('.xlsx') == -1) {
                    $('#templateFileDiv').addClass("has-error");
                    $('#templateFileMsg').addClass("has-error");
                    $('#templateFileMsg').html('The template file must be an excel file (.xlsx format).');
                    hasErrors = 1;
                }
            }
            else {
                if ($('#' + headVal + 'templateFile').val() == '' || $('#' + headVal + 'templateFile').val().indexOf('.xlsx') == -1) {
                    $('#' + headVal + 'templateFileDiv').addClass("has-error");
                    $('#' + headVal + 'templateFileMsg').addClass("has-error");
                    $('#' + headVal + 'templateFileMsg').html('The template file must be an excel file (.xlsx format).');
                    hasErrors = 1;
                }
            }
            
            //Make sure a valid max file size is entered
            if (!$.isNumeric($('#' + headVal + 'maxFileSize').val()) || $('#' + headVal + 'maxFileSize').val() == 0) {
                $('#' + headVal + 'maxFileSizeDiv').addClass("has-error");
                $('#' + headVal + 'maxFileSizeMsg').addClass("has-error");
                $('#' + headVal + 'maxFileSizeMsg').html('The max file size must be a numeric value greater than 0!');
                hasErrors = 1;
            }
            
            if($('#configType').val() === 1) {

                //Make sure a valid field no is entered
                if (!$.isNumeric($('#' + headVal + 'targetOrgColNo').val())) {
                    $('#' + headVal + 'targetOrgColNoDiv').addClass("has-error");
                    $('#' + headVal + 'targetOrgColNoMsg').addClass("has-error");
                    $('#' + headVal + 'targetOrgColNoMsg').html('The target organziation field No must be a numeric value!');
                    hasErrors = 1;
                }
                if (!$.isNumeric($('#' + headVal + 'messageTypeColNo').val())) {
                    $('#' + headVal + 'messageTypeColNoDiv').addClass("has-error");
                    $('#' + headVal + 'messageTypeColNoMsg').addClass("has-error");
                    $('#' + headVal + 'messageTypeColNoMsg').html('The message type field No must be a numeric value!');
                    hasErrors = 1;
                }
            }

            //Make sure the file type and delimiter is selected
            if ($('#' + headVal + 'fileType').val() == '') {
                $('#' + headVal + 'fileTypeDiv').addClass("has-error");
                $('#' + headVal + 'fileTypeMsg').addClass("has-error");
                $('#' + headVal + 'ileTypeMsg').html('The file type is a required field!');
                hasErrors = 1;
            }
            if ($('#' + headVal + 'delimiter').val() == '') {
                $('#' + headVal + 'fileDelimDiv').addClass("has-error");
                $('#' + headVal + 'fileDelimMsg').addClass("has-error");
                $('#' + headVal + 'fileDelimMsg').html('The file delimiter is a required field!');
                hasErrors = 1;
            }
            if (hasErrors == 1) {
                $('#collapse' + sectionVal).show();
            }
        }

    });

    return hasErrors;
}


