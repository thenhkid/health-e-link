/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    
    //This function will launch the status detail overlay with the selected
    //status
    $(document).on('click', '.viewStatus', function() {
        $.ajax({
            url: '/Health-e-Web/viewStatus' + $(this).attr('rel'),
            type: "GET",
            success: function(data) {
                $("#statusModal").html(data);
            }
        });
    });
    
    
    //This function will load the original transaction details
    $(document).on('click','#viewOriginalTransaction', function() {
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
        
        //Need to set the action for the form
        //If user has edit capability then 'pending/details'
        //otherwise only view 'sent/messageDetails'
        if(editAuthority === "true") {
            if(['17','18','19','20'].indexOf(statusId) >= 0) {
                $('#viewTransactionDetails').attr("action","/Health-e-Web/sent/messageDetails"); 
            }
            else {
                $('#viewTransactionDetails').attr("action","/Health-e-Web/pending/details");
            }
            
        }
        else {
            $('#viewTransactionDetails').attr("action","/Health-e-Web/sent/messageDetails"); 
        }
        
        $('#viewTransactionDetails').submit();
       
    });
    
});


