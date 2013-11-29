$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(5000);
    }
});

jQuery(document).ready(function($) {
    //This function will remove the clicked dataItem 

    $('#detailDataDelete').click(function(event) {
        var dataId = $(this).attr('rel');
        var confirmed = confirm("Are you sure you want to remove this data?");
        if (confirmed) {
            window.location.href = 'delete?i=' + dataId;
        }

    });

    //The function that will be called when the "Save" button
    //is clicked
    $('#saveDetails').click(function(event) {
        $('#action').val('save');
        $("#tableDataForm").submit();
    });

    //The function that will be called when the "Save & Close" button
    // is clicked
    $('#saveCloseDetails').click(function(event) {
        $('#action').val('close');
        $('#tableDataForm').submit();
    });


});

