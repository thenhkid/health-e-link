/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
    
        //Fade out the alert message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }
        
        $('.submitMessage').click(function() {
           var errorsFound = 0;
          
          
           if(errorsFound == 0) {
                $('#providerForm').submit(); 
           }
           
        });
        
        //This function will launch the new provider address overlay with a blank form
        $(document).on('click', '#createNewAddress', function() {
            $.ajax({
                url: '/OrgProfile/newProviderAddress' + '?providerId=' + $('#id').val(),
                type: "GET",
                success: function(data) {
                    $("#systemAddressModal").html(data);
                }
            });
        });
        
        //This function will launch the edit address overlay populating the fields
        //with the data of the clicked address.
        $(document).on('click', '.editAddress', function() {
            var Action = '/OrgProfile/providerAddress/' + $(this).attr('rel');

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
                    url: "/OrgProfile/addressDelete/delete?i=" + id,
                    type: "GET",
                    async: false,
                    success: function(data) {
                        //Strip out any remaming msg parameters in the string
                        var url = removeVariableFromURL($(location).attr('href'), 'msg');
                        window.location.href = url + "?msg=deleted";
                    }
                });
            }
        });
        
        //Function to submit the changes to an existing address or 
        //submit the new address fields from the modal window.
        $(document).on('click', '#submitButton', function(event) {

            var formData = $("#addressdetailsform").serialize();

            var actionValue = '/OrgProfile/address/' + $(this).attr('rel').toLowerCase();

            $.ajax({
                url: actionValue,
                data: formData,
                type: "POST",
                async: false,
                success: function(data) {
                    //Strip out any remaming msg parameters in the string
                    var url = removeVariableFromURL($(location).attr('href'), 'msg');
                    if (data.indexOf('addressUpdated') != -1) {
                        window.location.href = url + "?msg=updated";
                    }
                    else if (data.indexOf('addressCreated') != -1) {
                        window.location.href = url + "?msg=created";
                    }
                    else {
                        $("#systemAddressModal").html(data);
                    }
                }
            });
            event.preventDefault();
            return false;

        });
        
        
        //This function will launch the new provider Id overlay with a blank form
        $(document).on('click', '#createNewId', function() {
            $.ajax({
                url: '/OrgProfile/newProviderId' + '?providerId=' + $('#id').val(),
                type: "GET",
                success: function(data) {
                    $("#providerIdModal").html(data);
                }
            });
        });

        //This function will launch the edit provider Id overlay populating the fields
        //with the data of the clicked id.
        $(document).on('click', '.editId', function() {
            var Action = '/OrgProfile/providerId/' + $(this).attr('rel');

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
                    url: "/OrgProfile/providerIdDelete/delete?i=" + id,
                    type: "GET",
                    async: false,
                    success: function(data) {
                        //Strip out any remaming msg parameters in the string
                        var url = removeVariableFromURL($(location).attr('href'), 'msg');
                        window.location.href = url + "?msg=iddeleted";
                    }
                });
            }
        });

        //Function to submit the changes to an existing provider Id or 
        //submit the new provider Id fields from the modal window.
        $(document).on('click', '#submitIdButton', function(event) {

            var formData = $("#providerIddetailsform").serialize();

            var actionValue = '/OrgProfile/providerId/' + $(this).attr('rel').toLowerCase();

            $.ajax({
                url: actionValue,
                data: formData,
                type: "POST",
                async: false,
                success: function(data) {
                    //Strip out any remaming msg parameters in the string
                    var url = removeVariableFromURL($(location).attr('href'), 'msg');
                    if (data.indexOf('idUpdated') != -1) {
                        window.location.href = url + "?msg=idupdated";
                    }
                    else if (data.indexOf('idCreated') != -1) {
                        window.location.href = url + "?msg=idcreated";
                    }
                    else {
                        $("#providerIdModal").html(data);
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