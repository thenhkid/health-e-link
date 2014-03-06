
require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $("input:text,form").attr("autocomplete", "off");

        //This function will launch the new provider overlay with a blank screen
        $(document).on('click', '#createNewProvider', function() {
            $.ajax({
                url: 'provider.create',
                type: "GET",
                success: function(data) {
                    $("#providersModal").html(data);
                }
            });
        });


        //This function will remvoe the clicked provider 
        $(document).on('click', '.providerDelete', function() {

            var confirmed = confirm("Are you sure you want to remove this provider?");

            if (confirmed) {
                var id = $(this).attr('rel');
                window.location.href = "providerDelete/delete?i=" + id;
            }
        });

        $('#searchProviderBtn').click(function() {
            $('#searchForm').submit();
        });


        //Function to submit the changes to an existing provider or 
        //submit the new provider fields from the modal window.
        $(document).on('click', '#submitButton', function(event) {
            var currentPage = $('#currentPageHolder').attr('rel');

            var formData = $("#providerdetailsform").serialize();

            var actionValue = $(this).attr('rel').toLowerCase();

            $.ajax({
                url: actionValue + 'Provider',
                data: formData,
                type: "POST",
                async: false,
                success: function(data) {

                    if (data.indexOf('providerCreated') != -1) {
                        if (currentPage > 0) {
                            window.location.href = "providers?msg=created&page=" + currentPage;
                        }
                        else {
                            window.location.href = "providers?msg=created";
                        }

                    }
                    else {
                        $("#providersModal").html(data);
                    }
                }
            });
            event.preventDefault();
            return false;

        });
    });
});



