/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
    
    //Fade out the alert message after being displayed.
    if ($('.alert').length > 0) {
        $('.alert').delay(2000).fadeOut(1000);
    }
});

$(function() {
    
    $('.viewLink').click(function() {
       var transactionId = $(this).attr('rel');
       var configId = $(this).attr('rel2');
       var editAuthority = $(this).attr('rel3');
       
        $('#transactionId').val(transactionId);
        $('#configId').val(configId);
        
        //Need to set the action for the form
        //If user has edit capability then 'pending/details'
        //otherwise only view 'sent/messageDetails'
        if(editAuthority === "true") {
            $('#viewTransactionDetails').attr("action","/Health-e-Web/pending/details");
        }
        else {
            $('#viewTransactionDetails').attr("action","/Health-e-Web/sent/messageDetails"); 
        }
        
        
        $('#viewTransactionDetails').submit();
       
    });
    
    //This function will launch the status detail overlay with the selected
    //status
    $(document).on('click', '.viewStatus', function() {
        $.ajax({
            url: '../viewStatus' + $(this).attr('rel'),
            type: "GET",
            success: function(data) {
                $("#statusModal").html(data);
            }
        });
    });
    
});