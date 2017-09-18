/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {

    
        //This function will launch the status detail overlay with the selected
        //status
        $(document).on('click', '.viewStatus', function() {
            $.ajax({
                url: '/CareConnector/viewStatus' + $(this).attr('rel'),
                type: "GET",
                success: function(data) {
                    $("#statusModal").html(data);
                }
            });
        });
    
    
        //This function will load the original transaction details
        $(document).on('click','#viewOriginalTransaction', function() {

            if($('#fromPage').val() == 'sent') {
                $('#viewOriginalTransactionDetails').attr('action','/CareConnector/sent/messageDetails');
            }
            else {
               $('#viewOriginalTransactionDetails').attr('action','/CareConnector/inbox/messageDetails'); 
            }

            $('#originalTransactionId').val($(this).attr('rel'));
            $('#viewOriginalTransactionDetails').submit();
        });
    
        $('.viewLink').click(function() {
           var transactionId = $(this).attr('rel');
           var configId = $(this).attr('rel2');
           var editAuthority = $(this).attr('rel3');
           var statusId = $(this).attr('rel4');

            $('#transactionId').val(transactionId);
            $('#configId').val(configId);

            //If viewing a sent message need to look at the feedback report
            //details from the inbox side.

            if($('#fromPage').val() == 'sent') {
                $('#viewTransactionDetails').attr("action","/CareConnector/inbox/messageDetails"); 
            }
            else {

                //Need to set the action for the form
                //If user has edit capability then 'pending/details'
                //otherwise only view 'sent/messageDetails'
                if(editAuthority === "true") {
                    if(['17','18','19','20'].indexOf(statusId) >= 0) {
                        $('#viewTransactionDetails').attr("action","/CareConnector/sent/messageDetails"); 
                    }
                    else {
                        $('#viewTransactionDetails').attr("action","/CareConnector/pending/details");
                    }

                }
                else {
                    $('#viewTransactionDetails').attr("action","/CareConnector/sent/messageDetails"); 
                }
            }

            $('#viewTransactionDetails').submit();

        });
    });
});


