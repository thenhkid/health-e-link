$(document).ready(function() {
    
    populateExistingAttachments();
   
    //Function to handle the form actions
    $(document).on('change','#formAction',function() {
        
        if($(this).val() === 'print') {
            window.print();
        }
        
    });
    
    //Function to display the note modal box when 
    //the internal status id is changed.
    $(document).on('change','#internalStatus',function() {
       
       //Open the note modal window
       $('#messageNoteModal').modal('show');
       $('#internalStatusId').val($('#internalStatus').val());
       $('#messagetransactionId').val($('#transactionId').val());
       $('#statusDisplayText').html($('#internalStatus option[value="'+$('#internalStatus').val()+'"]').text());
       $('#selStatus').show();
        
    });
    

});

function populateExistingAttachments() {
    var transactionId = $('#transactionId').val();
    var transactionInId = $('#transactionInId').val();
    var currentIdList = $('#attachmentIds').val();
    
    //If viewing an inbox transaction need to get attachments
    //for the orginating transaction=
    if(transactionInId > 0) {
        transactionId = transactionInId;
    }
    
    $.ajax({
        url: '/Health-e-Web/populateExistingAttachments.do',
        type: 'GET',
        data: {'transactionId': transactionId, 'newattachmentIdList': currentIdList, 'pageFrom': 'sent'},
        success: function(data) {
            $("#existingAttachments").html(data);
        }
    });
}