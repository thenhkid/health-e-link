$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {

    //Go get the existing message types for the selected organization
    $('.selsrcOrganization').change(function() {
        var selOrg = $(this).val();

        if (selOrg === '') {
            $('#srcorgDiv').addClass("has-error");
        }
        else {
            $.ajax({
                url: 'getAvailableConfigurations.do',
                type: "GET",
                data: {'orgId': selOrg},
                success: function(data) {
                    //get value of preselected col
                    var html = '<option value="">- Select - </option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                       html += '<option value="' + data[i].id + '">' + data[i].messageTypeName + '&nbsp;&#149;&nbsp;' + data[i].userName + '&nbsp;&#149;&nbsp;' + data[i].transportMethod + '</option>';
                    }
                    $('#srcConfig').html(html);
                }
            });
        }
    });
    
    //Go get the existing message types for the selected organization
    $('.seltgtOrganization').change(function() {
        var selOrg = $(this).val();

        if (selOrg === '') {
            $('#tgtorgDiv').addClass("has-error");
        }
        else {
            $.ajax({
                url: 'getAvailableConfigurations.do',
                type: "GET",
                data: {'orgId': selOrg},
                success: function(data) {
                    //get value of preselected col
                    var html = '<option value="">- Select - </option>';
                    var len = data.length;
                    for (var i = 0; i < len; i++) {
                       html += '<option value="' + data[i].id + '">' + data[i].messageTypeName + '&nbsp;&#149;&nbsp;' + data[i].userName + '&nbsp;&#149;&nbsp;' + data[i].transportMethod + '</option>';
                    }
                    $('#tgtConfig').html(html);
                }
            });
        }
    });

    

    //This function will save the messgae type field mappings
    $('#saveDetails').click(function() {
        var hasErrors = 0;
        var srcConfig = $('#srcConfig').val();
        var tgtConfig = $('#tgtConfig').val();
        
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
        
        if(hasErrors == 0) {
             $.ajax({
                url: 'addConnection.do',
                type: "POST",
                data: {'srcConfig': srcConfig, 'tgtConfig': tgtConfig},
                success: function(data) {
                    window.location.href='connections?msg=created'
                }
            });
        }
        
        
    });


});

function fadeAlert() {
    $('.alert').delay(2000).fadeOut(1000);
}


