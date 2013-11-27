$.ajaxSetup({
    cache: false
});

//var searchTimeout;

jQuery(document).ready(function($) {

    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(5000);
    }

    $("input:text,form").attr("autocomplete", "off");

    //This function will launch the new dataItem overlay with a blank screen
    $(document).on('click', '#createNewdataItem', function() {
        alert("here");
        $.ajax({
            url: '../../std/data/${tableInfo.urlId}',
            type: "GET",
            success: function(data) {
                $("#systemdataItemsModal").html(data);
            }
        });
    });

    //this function will send them to table view
    $('#searchdataItemBtn').click(function() {
        $('#searchForm').submit();
    });

    //This function will remove the clicked dataItem 
    $(document).on('click', '.dataItemDelete', function() {

        var confirmed = confirm("Are you sure you want to remove this data?");

        if (confirmed) {
            var id = $(this).attr('rel');
            var path = window.location.href + "/delete?i=" + id;
            window.location.href = path;
        }
    });

    $('#tableDetailsLink').click(function() {
        window.location.href = window.location.href.replace("data/", "");
    });


    //Function to submit the changes to an existing dataItem or 
    //submit the new dataItem fields from the modal window.
    $(document).on('click', '#submitButton', function(event) {
        var currentPage = $('#currentPageHolder').attr('rel');

        var formData = $("#dataItemdetailsform").serialize();

        var actionValue = $(this).attr('rel').toLowerCase();

        $.ajax({
            url: actionValue + 'dataItem',
            data: formData,
            type: "POST",
            async: false,
            success: function(data) {

                if (data.indexOf('dataItemUpdated') != -1) {
                    if (currentPage > 0) {
                        window.location.href = "dataItems?msg=updated&page=" + currentPage;
                    }
                    else {
                        window.location.href = "dataItems?msg=updated";
                    }

                }
                else if (data.indexOf('dataItemCreated') != -1) {
                    if (currentPage > 0) {
                        window.location.href = "dataItems?msg=created&page=" + currentPage;
                    }
                    else {
                        window.location.href = "dataItems?msg=created";
                    }

                }
                else {
                    $("#systemdataItemsModal").html(data);
                }
            }
        });
        event.preventDefault();
        return false;

    });

});


