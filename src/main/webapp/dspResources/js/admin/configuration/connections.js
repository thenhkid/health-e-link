
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
});
