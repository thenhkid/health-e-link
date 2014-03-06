
require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $("input:text,form").attr("autocomplete", "off");

        //This function will launch the new system user overlay with a blank screen
        $(document).on('click', '#createNewUser', function() {
            $.ajax({
                url: 'newSystemUser',
                type: "GET",
                success: function(data) {
                    $("#systemUsersModal").html(data);
                }
            });
        });

        //This function will launch the edit system user overlay populating the fields
        //with the data of the clicked user.
        $(document).on('click', '.userEdit', function() {
            var userDetailsAction = 'user/' + $(this).attr('rel');

            $.ajax({
                url: userDetailsAction,
                type: "GET",
                success: function(data) {
                    $("#systemUsersModal").html(data);
                }
            });
        });

        $('#searchOrgBtn').click(function() {
            $('#searchForm').submit();
        });


        //Function to submit the changes to an existing user or 
        //submit the new user fields from the modal window.
        $(document).on('click', '#submitButton', function(event) {
            var currentPage = $('#currentPageHolder').attr('rel');
            var passwordVal = $('#password').val();
            var confirmPasswordVal = $('#confirmPassword').val();

            if (passwordVal != confirmPasswordVal) {
                $('#confirmPasswordDiv').addClass("has-error");
                $('#passwordDiv').addClass("has-error");
                $('#confimPasswordMsg').addClass("has-error");
                $('#confimPasswordMsg').html('The two passwords do not match.');
                event.preventDefault();
                return false;
            }
            else {
                var formData = $("#userdetailsform").serialize();

                var actionValue = $(this).attr('rel').toLowerCase();

                $.ajax({
                    url: actionValue,
                    data: formData,
                    type: "POST",
                    async: false,
                    success: function(data) {

                        if (data.indexOf('userUpdated') != -1) {
                            if (currentPage > 0) {
                                window.location.href = "users?msg=updated&page=" + currentPage;
                            }
                            else {
                                window.location.href = "users?msg=updated";
                            }

                        }
                        else if (data.indexOf('userCreated') != -1) {
                            if (currentPage > 0) {
                                window.location.href = "users?msg=created&page=" + currentPage;
                            }
                            else {
                                window.location.href = "users?msg=created";
                            }

                        }
                        else {
                            $("#systemUsersModal").html(data);
                        }
                    }
                });
                event.preventDefault();
                return false;
            }

        });
        
    });
});




