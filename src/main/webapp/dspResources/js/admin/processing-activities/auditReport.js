/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
        
        //This function will launch the status detail overlay with the selected
        //status
        $(document).on('click', '.viewStatus', function() {
            $.ajax({
                url: '../../viewStatus' + $(this).attr('rel'),
                type: "GET",
                success: function(data) {
                    $("#statusModal").html(data);
                }
            });
        });
        
        $(document).on('click', '.viewLink', function() {
            
            var transactionId = $(this).attr('rel');
            var configId = $(this).attr('rel2');
           
            $.ajax({
                url: '../../ViewMessageDetails',
                data: {'Type': 1, 'transactionId': transactionId, 'configId': configId},
                type: "GET",
                success: function(data) {
                    $("#messageDetailsModal").html(data);
                }
            });
            
        });
        
      //This function will process the batch
        $(document).on('click', '.processBatch', function() {
            
            var confirmed = confirm("Are you sure you want to process the batch now?");

             if (confirmed) {
            	 $.ajax({
                  url: '../../inboundBatchOptions',
                  data:{'batchOption': $(this).attr('rel'), 'batchId': $(this).attr('rel2')},
                  type: "POST",
                  success: function(data) {
                	  location.reload();
                  }
                });
             }
           
        });
        
      //This function will cancel the batch
        $(document).on('click', '.cancelBatch', function() {
            
            var confirmed = confirm("Are you sure you want to set this batch to 'Cancel'?");

             if (confirmed) {
            	 $.ajax({
                  url: '../../inboundBatchOptions',
                  data:{'batchOption': $(this).attr('rel'), 'batchId': $(this).attr('rel2')},
                  type: "POST",
                  success: function(data) {
                	  location.reload();
                  }
                });
             }
           
        });

      //This function will reset the batch
        $(document).on('click', '.resetBatch', function() {
            
            var confirmed = confirm("Are you sure you want to reset this batch?");

             if (confirmed) {
            	 $.ajax({
                  url: '../../inboundBatchOptions',
                  data:{'batchOption': $(this).attr('rel'), 'batchId': $(this).attr('rel2')},
                  type: "POST",
                  success: function(data) {
                	  location.reload();
                  }
                });
             }
           
        });
        
        //This function will reject all errors
        $(document).on('click', '.rejectMessages', function() {
            
            var confirmed = confirm("Are you sure you want to reject all transactions with errors?");

             if (confirmed) {
            	 $('input[name="batchOption"]').val("rejectMessages"); 
            	 $('#massReject').submit();
             }
           
        });
        
        //This function will release the batch
        $(document).on('click', '.releaseBatch', function() {
            
            var confirmed = confirm("Are you sure you want to release this batch?");

             if (confirmed) {
            	 $.ajax({
                  url: '../../inboundBatchOptions',
                  data:{'batchOption': $(this).attr('rel'), 'batchId': $(this).attr('rel2')},
                  type: "POST",
                  success: function(data) {
                	  location.reload();
                  }
                });
             }    
        });
        
      //This function will release the batch
        $(document).on('click', '.rejectMessage', function() {
            
            var confirmed = confirm("Are you sure you want to reject this transaction?");

             if (confirmed) {
            	 $.ajax({
                  url: '../../inboundBatchOptions',
                  data:{'batchOption': $(this).attr('rel'), 'batchId': $(this).attr('rel2'), 'tId': $(this).attr('rel3')},
                  type: "POST",
                  success: function(data) {
                	  location.reload();
                  }
                });
             }    
        });
        
        //this function will submit the transactionInId to the ERG form for edit
        $(document).on('click', '.fixErrors', function() {
        	$('input[name="transactionInId"]').val($(this).attr('rel'));
        	$('#editTransaction').submit();
        });
        
    });
});