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
    
    //This function will handle submitting the new note
    $(document).on('click', '#submitMessageNote', function(event) {
        var transactionId = $('#transactionId').val();
        
        var formData = $("#messageNoteForm").serialize();
        
        $.ajax({
            url: '/Health-e-Web/submitMessageNote.do',
            data: formData,
            type: "POST",
            async: false,
            success: _RedirectToDetails()
        });
        
        event.preventDefault();
        return false;
        
    });
    

});

function _RedirectToDetails() {
    $('#viewTransactionDetails').submit();
}

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
