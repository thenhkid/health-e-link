$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {

    //Add a new organization to the existing connection
    $('.addOrganization').click(function() {
        var selOrg = $('#organization').val();

        if (selOrg === '') {
            $('#organizationDiv').addClass("has-error");
        }
        else {
            $.ajax({
                url: 'addConnection.do',
                type: "POST",
                data: {'org': selOrg},
                success: function(data) {
                    if (data === 1) {
                        window.location.href = "connections";
                    }
                }
            });
        }
    });

    //Update the status of the connection
    $('.connectionStatus').click(function() {
        var currStatus = $(this).attr('rel'); //1 = enabled 0 = disabled
        var newStatusVal = null;
        var newStatus = null;
        var statusTitle = null;
        var connectionId = $(this).attr('rel2');

        if (currStatus === '1') {
            newStatusVal = false;
            newStatus = 'Enable';
            statusTitle = 'Enable this Connection!';
        }
        else {
            newStatusVal = true;
            newStatus = 'Disable';
            statusTitle = 'Disable this Connection!';
        }
        $(this).attr('rel', newStatusVal);
        $(this).attr('title', statusTitle);
        $(this).html(newStatus);

        $.ajax({
            url: 'changeConnectionStatus.do',
            type: "POST",
            data: {'statusVal': newStatusVal, 'connectionId': connectionId},
            success: function(data) {
                if (data === 1) {
                    $('.alert').show();
                    $('#saveStatus').html('The connection status has been successfully changed!');
                    fadeAlert();
                }
            }
        });

    });

    //This function will save the messgae type field mappings
    $('#saveDetails').click(function() {
        $('.alert').show();
        $('#saveStatus').html('The connection has been successfully saved!');
        fadeAlert();
    });

    $('#next').click(function() {
        window.location.href = "scheduling";

    });

});

function fadeAlert() {
    $('.alert').delay(2000).fadeOut(1000);
}


