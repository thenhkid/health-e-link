$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }

    //Populate the available users when a organization is selected
    $('#organization').change(function() {
        var orgId = $(this).val();

        if (orgId !== "") {
           
            $.getJSON('getAvailableUsers.do', {
                orgId: orgId, ajax: true
            }, function(data) {
                var html = '<option value="">- Select - </option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i][0] + '">' + data[i][1] + ' ' + data[i][2] + '</option>';
                }
                $('#users').html(html);
            });
        }
    });

    $('#saveDetails').click(function(event) {
        $('#action').val('save');
       var hasErrors = 0;
        hasErrors = checkform();
        if (hasErrors == 0) {
            $("#configuration").submit();
        }

    });

    $('#next').click(function(event) {
        $('#action').val('next');
        var hasErrors = 0;
        hasErrors = checkform();
        if (hasErrors == 0) {
            $("#configuration").submit();
        }
    });

});

function checkform() {
    var errorFound = 0;
    
    //Check to make sure an organization is selected
    if($('#organization').val() === '') {
        $('#orgDiv').addClass("has-error");
        $('#configOrgMsg').addClass("has-error");
        $('#configOrgMsg').html('The organization is a required field.');
        errorFound = 1;
    }
    
    //Check to make sure an authorized user is selected
    if($('#users').val() === '') {
        $('#userDiv').addClass("has-error");
        $('#configUserMsg').addClass("has-error");
        $('#configUserMsg').html('The authorized user is a required field.');
        errorFound = 1;
    }
    
    //Check to make sure a message type is selected
    if($('#messageTypeId').val() === '') {
        $('#messageTypeDiv').addClass("has-error");
        $('#configMessageTypeMsg').addClass("has-error");
        $('#configMessageTypeMsg').html('The authorized user is a required field.');
        errorFound = 1;
    }
    
    
    return errorFound;
    
}


