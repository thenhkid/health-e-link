$(document).ready(function() {
    
    populateExistingAttachments();
   
    //Function to handle the form actions
    $(document).on('change','#formAction',function() {
        
        if($(this).val() === 'print') {
            window.print();
        }
        
    })

});

function populateExistingAttachments() {
    var transactionId = $('#transactionId').val();
    var currentIdList = $('#attachmentIds').val();
    
    $.ajax({
        url: '../populateExistingAttachments.do',
        type: 'GET',
        data: {'transactionId': transactionId, 'newattachmentIdList': currentIdList, 'pageFrom': 'sent'},
        success: function(data) {
            $("#existingAttachments").html(data);
        }
    });
}