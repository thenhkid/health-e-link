
require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        //The function that will be called when the "Save" button
        //is clicked
        $('#saveDetails').click(function(event) {
            $('#action').val('save');
            $("#providerdetailsform").submit();
        });

        //The function that will be called when the "Save & Close" button
        // is clicked
        $('#saveCloseDetails').click(function(event) {
            $('#action').val('close');
            $('#providerdetailsform').submit();
        });

        //Function to delete a provider
        $('#deleteProvider').click(function(event) {
            var providerId = $(this).attr('rel');
            var confirmed = confirm("Are you sure you want to remove this provider?");

            if (confirmed) {
                window.location.href = 'providerDelete/delete?i=' + providerId;
            }

        });

        //This function will launch the new provider Id overlay with a blank form
        $(document).on('click', '#createNewId', function() {
            $.ajax({
                url: 'newProviderId' + '?providerId=' + $('#id').val(),
                type: "GET",
                success: function(data) {
                    $("#providerIdModal").html(data);
                }
            });
        });

        //This function will launch the edit provider Id overlay populating the fields
        //with the data of the clicked id.
        $(document).on('click', '.editId', function() {
            var Action = 'providerId/' + $(this).attr('rel');

            $.ajax({
                url: Action,
                type: "GET",
                success: function(data) {
                    $("#providerIdModal").html(data);
                }
            });
        });

        //This function will delete the selected provider Id
        $(document).on('click', '.deleteId', function() {
            var confirmed = confirm("Are you sure you want to remove this provider Id?");

            if (confirmed) {
                var id = $(this).attr('rel');

                $.ajax({
                    url: "providerIdDelete/delete?i=" + id,
                    type: "GET",
                    async: false,
                    success: function(data) {
                        //Strip out any remaming msg parameters in the string
                        var url = removeVariableFromURL($(location).attr('href'), 'msg');
                        window.location.href = url + "&msg=iddeleted";
                    }
                });
            }
        });

        //Function to submit the changes to an existing provider Id or 
        //submit the new provider Id fields from the modal window.
        $(document).on('click', '#submitIdButton', function(event) {

            var formData = $("#providerIddetailsform").serialize();

            var actionValue = 'providerId/' + $(this).attr('rel').toLowerCase();

            $.ajax({
                url: actionValue,
                data: formData,
                type: "POST",
                async: false,
                success: function(data) {
                    //Strip out any remaming msg parameters in the string
                    var url = removeVariableFromURL($(location).attr('href'), 'msg');
                    if (data.indexOf('idUpdated') != -1) {
                        window.location.href = url + "&msg=idupdated";
                    }
                    else if (data.indexOf('idCreated') != -1) {
                        window.location.href = url + "&msg=idcreated";
                    }
                    else {
                        $("#providerIdModal").html(data);
                    }
                }
            });
            event.preventDefault();
            return false;

        });

        //This function will launch the new provider address overlay with a blank form
        $(document).on('click', '#createNewAddress', function() {
            $.ajax({
                url: 'newProviderAddress' + '?providerId=' + $('#id').val(),
                type: "GET",
                success: function(data) {
                    $("#systemAddressModal").html(data);
                }
            });
        });

        //This function will launch the edit address overlay populating the fields
        //with the data of the clicked address.
        $(document).on('click', '.editAddress', function() {
            var Action = 'providerAddress/' + $(this).attr('rel');

            $.ajax({
                url: Action,
                type: "GET",
                success: function(data) {
                    $("#systemAddressModal").html(data);
                }
            });
        });

        //This function will delete the selected address
        $(document).on('click', '.deleteAddress', function() {
            var confirmed = confirm("Are you sure you want to remove this address?");

            if (confirmed) {
                var id = $(this).attr('rel');

                $.ajax({
                    url: "addressDelete/delete?i=" + id,
                    type: "GET",
                    async: false,
                    success: function(data) {
                        //Strip out any remaming msg parameters in the string
                        var url = removeVariableFromURL($(location).attr('href'), 'msg');
                        window.location.href = url + "&msg=deleted";
                    }
                });
            }
        });

        //Function to submit the changes to an existing address or 
        //submit the new address fields from the modal window.
        $(document).on('click', '#submitButton', function(event) {

            var formData = $("#addressdetailsform").serialize();

            var actionValue = 'address/' + $(this).attr('rel').toLowerCase();

            $.ajax({
                url: actionValue,
                data: formData,
                type: "POST",
                async: false,
                success: function(data) {
                    //Strip out any remaming msg parameters in the string
                    var url = removeVariableFromURL($(location).attr('href'), 'msg');
                    if (data.indexOf('addressUpdated') != -1) {
                        window.location.href = url + "&msg=updated";
                    }
                    else if (data.indexOf('addressCreated') != -1) {
                        window.location.href = url + "&msg=created";
                    }
                    else {
                        $("#systemAddressModal").html(data);
                    }
                }
            });
            event.preventDefault();
            return false;

        });
        
    });
});


function removeVariableFromURL(url_string, variable_name) {
    var URL = String(url_string);
    var regex = new RegExp("\\?" + variable_name + "=[^&]*&?", "gi");
    URL = URL.replace(regex, '?');
    regex = new RegExp("\\&" + variable_name + "=[^&]*&?", "gi");
    URL = URL.replace(regex, '&');
    URL = URL.replace(/(\?|&)$/, '');
    regex = null;
    return URL;
}


