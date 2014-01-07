
$(function() {
    
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }
    
    //Update the status of the connection
    $('.changeStatus').click(function() {
        var connectionId = $(this).attr('rel'); 
        var newStatusVal = $(this).attr('rel2');

        $.ajax({
            url: 'changeConnectionStatus.do',
            type: "POST",
            data: {'statusVal': newStatusVal, 'connectionId': connectionId},
            success: function(data) {
                if (data === 1) {
                    window.location.href='connections?msg=updated'
                }
            }
        });
    });
    
    //This function will launch the new configuration connection overlay with a blank screen
    $(document).on('click', '#createNewConnection', function() {
        $.ajax({
            url: 'createConnection',
            type: "GET",
            success: function(data) {
                $("#connectionsModal").html(data);
            }
        });
    });
    
    //Go get the existing message types for the selected organization'
    $(document).on('change', '.selsrcOrganization', function() {
        var selOrg = $(this).val();
        
        if (selOrg === '') {
            $('#srcorgDiv').addClass("has-error");
        }
        else {
            populateConfigurations(selOrg, 'srcConfig');
            populateUsers(selOrg, 'srcUsers');
        }
    });
    
    
    //Go get the existing message types for the selected organization
    $(document).on('change', '.seltgtOrganization', function() {
        var selOrg = $(this).val();

        if (selOrg === '') {
            $('#tgtorgDiv').addClass("has-error");
        }
        else {
            populateConfigurations(selOrg, 'tgtConfig');
            populateUsers(selOrg, 'tgtUsers');
        }
    });

    
    //This function will save the messgae type field mappings
    $(document).on('click', '#submitButton', function() {
        var hasErrors = 0;
        var srcConfig = $('#srcConfig').val();
        var tgtConfig = $('#tgtConfig').val();
        var srcUsers = $('#srcUsers').val();
        var tgtUsers = $('#tgtUsers').val();
        
        $('div.form-group').removeClass("has-error");
        $('span.control-label').removeClass("has-error");
        $('span.control-label').html("");
        $('.alert-danger').hide();
        
        if (srcConfig === '') {
            $('#srcConfigDiv').addClass("has-error");
            hasErrors = 1;
        }
        
        if (tgtConfig === '') {
            $('#tgtConfigDiv').addClass("has-error");
            hasErrors = 1;
        }
        
        if (srcUsers === '') {
            $('#srcUsersDiv').addClass("has-error");
            hasErrors = 1;
        }
        
        if (tgtUsers === '') {
            $('#tgtUsersDiv').addClass("has-error");
            hasErrors = 1;
        }
        
        if(hasErrors == 0) {
             $.ajax({
                url: 'addConnection.do',
                type: "POST",
                data: {'srcConfig': srcConfig, 'tgtConfig': tgtConfig, 'srcUsers' : srcUsers, 'tgtUsers' : tgtUsers},
                success: function(data) {
                    window.location.href='connections?msg=created'
                }
            });
        }
        
    });
});

function populateConfigurations(orgId, selectBoxId) {
    $.ajax({
        url: 'getAvailableConfigurations.do',
        type: "GET",
        data: {'orgId': orgId},
        success: function(data) {
            //get value of preselected col
            var html = '<option value="">- Select - </option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
               html += '<option value="' + data[i].id + '">' + data[i].messageTypeName + '&nbsp;&#149;&nbsp;' + data[i].transportMethod + '</option>';
            }
            $('#'+selectBoxId).html(html);
        }
    });
}

function populateUsers(orgId, selectBoxId) {
    $.ajax({
        url: 'getAvailableUsers.do',
        type: "GET",
        data: {'orgId': orgId},
        success: function(data) {
            //get value of preselected col
            var html = '<option value="">- Select - </option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
               html += '<option value="' + data[i].id + '">' + data[i].firstName + ' ' + data[i].lastName + '</option>';
            }
            $('#'+selectBoxId).html(html);
        }
    });
}
