/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require(['./main'], function() {
    require(['jquery'], function($) {
        
        $('.print').click(function() {
            window.print();
        });
        
        //This function will launch the status detail overlay with the selected
        //status
        $(document).on('click', '.viewStatus', function() {
            $.ajax({
                url: 'viewStatus' + $(this).attr('rel'),
                type: "GET",
                success: function(data) {
                    $("#statusModal").html(data);
                }
            });
        });
        
        var aoColumns = [
           null,
           null,
           null,
           null
        ];
        
        var aoColumnsDetails = [
           null,
           null,
           null,
           null,
           null,
           null
        ];
        
        $('#dataTable').dataTable({
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sSearch": "_INPUT_",
                "sLengthMenu": '<select class="form-control" style="width:150px">' +
                        '<option value="10">10 Records</option>' +
                        '<option value="20">20 Records</option>' +
                        '<option value="30">30 Records</option>' +
                        '<option value="40">40 Records</option>' +
                        '<option value="50">50 Records</option>' +
                        '<option value="-1">All</option>' +
                        '</select>'
            },
           "aoColumns" : aoColumns,
           "aaSorting" : [[3, "desc"]]
        });
        
        $('#dataTableDetails').dataTable({
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sSearch": "_INPUT_",
                "sLengthMenu": '<select class="form-control" style="width:150px">' +
                        '<option value="10">10 Records</option>' +
                        '<option value="20">20 Records</option>' +
                        '<option value="30">30 Records</option>' +
                        '<option value="40">40 Records</option>' +
                        '<option value="50">50 Records</option>' +
                        '<option value="-1">All</option>' +
                        '</select>'
            },
           "aoColumns" : aoColumnsDetails,
           "aaSorting" : [[5, "desc"]]
        });
        
        $('#dataTableReceived').dataTable({
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sSearch": "_INPUT_",
                "sLengthMenu": '<select class="form-control" style="width:150px">' +
                        '<option value="10">10 Records</option>' +
                        '<option value="20">20 Records</option>' +
                        '<option value="30">30 Records</option>' +
                        '<option value="40">40 Records</option>' +
                        '<option value="50">50 Records</option>' +
                        '<option value="-1">All</option>' +
                        '</select>'
            },
           "aoColumns" : aoColumns,
           "aaSorting" : [[3, "desc"]]
        });

        $('#dataTableReceivedDetails').dataTable({
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sSearch": "_INPUT_",
                "sLengthMenu": '<select class="form-control" style="width:150px">' +
                        '<option value="10">10 Records</option>' +
                        '<option value="20">20 Records</option>' +
                        '<option value="30">30 Records</option>' +
                        '<option value="40">40 Records</option>' +
                        '<option value="50">50 Records</option>' +
                        '<option value="-1">All</option>' +
                        '</select>'
            },
           "aoColumns" : aoColumnsDetails,
           "aaSorting" : [[5, "desc"]]
        });
        
        
        /* View Details */
        $(document).on('click', '.viewReceived', function() {
           
            var transactionId = $(this).attr('rel');
            
            $.ajax({
                url: 'history/received/messageDetails',
                data: {'transactionId': transactionId, 'fromPage': 'inbox'},
                type: "POST",
                success: function(data) {
                    $("#messageDetailsModal").html(data);
                }
            });
            
        });
        
        $(document).on('click', '.viewSent', function() {
           
            var transactionId = $(this).attr('rel');
            
            $.ajax({
                url: 'history/sent/messageDetails',
                data: {'transactionId': transactionId, 'fromPage': 'sent'},
                type: "POST",
                success: function(data) {
                    $("#messageDetailsModal").html(data);
                }
            });
            
        });
        
        //Function to handle the form actions
        $(document).on('change', '#formAction', function() {

            if($(this).val() === 'print') {
                window.print();
            }
            if($(this).val() === 'feedbackReports') {
               $('#viewTransactionDetails').attr("action","/Health-e-Web/feedbackReports");
               $('#viewTransactionDetails').submit();
            }
            else if($(this).val() === 'originalReferral') {
               if($('#fromPage').val() == 'inbox') {
                  $('#viewTransactionDetails').attr("action","/Health-e-Web/sent/messageDetails"); 
               }

               $('.transactionId').val($('#formAction').attr('rel'));
               $('#viewTransactionDetails').submit();
            }
            else if($.isNumeric($(this).val())) {
                $('#feedbackConfigId').val($(this).val());
                $('#newFeedbackForm').submit();
            }

        });

    });
});


