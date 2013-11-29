$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
});

$(function() {
    //Fade out the updated/created message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }

    //Populate the available message types when a organization is selected
    $('#organization').change(function() {
        var orgId = $(this).val();

        if (orgId !== "") {
            $.getJSON('getAvailableMessageTypes.do', {
                orgId: orgId, ajax: true
            }, function(data) {
                var html = '<option value="">- Select - </option>';
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<option value="' + data[i][0] + '">' + data[i][1] + '</option>';
                }
                $('#messageTypeId').html(html);
            });
        }
    });

    $('#saveDetails').click(function(event) {
        $('#action').val('save');
        var hasErrors = 0;
        if (hasErrors == 0) {
            $("#configuration").submit();
        }

    });

    $('#next').click(function(event) {
        $('#action').val('next');
        var hasErrors = 0;
        if (hasErrors == 0) {
            $("#configuration").submit();
        }
    });

});


