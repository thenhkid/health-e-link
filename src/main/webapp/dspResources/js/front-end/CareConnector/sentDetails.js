

require(['./main'], function () {
    require(['jquery'], function($) {

        populateExistingAttachments();
        populateExistingNotes();

        //Function to handle the form actions
        $(document).on('change','#formAction',function() {

            if($(this).val() === 'print') {
                window.print();
            }
            if($(this).val() === 'feedbackReports') {
               $('#viewTransactionDetails').attr("action","/CareConnector/feedbackReports");
               $('#viewTransactionDetails').submit();
            }
            else if($(this).val() === 'originalReferral') {
               if($('#fromPage').val() == 'inbox') {
                  $('#viewTransactionDetails').attr("action","/CareConnector/sent/messageDetails"); 
               }

               $('.transactionId').val($('#formAction').attr('rel'));
               $('#viewTransactionDetails').submit();
            }
            else if($.isNumeric($(this).val())) {
                $('#feedbackConfigId').val($(this).val());
                $('#newFeedbackForm').submit();
            }

        });

        //Function to display the note modal box when 
        //the internal status id is changed.
        $(document).on('change','#internalStatus',function() {
           //Open the note modal window
           $('#messageNoteModal').modal('show');
           $('#internalStatusId').val($('#internalStatus').val());
           $('#internalStatusText').val($('#internalStatus option[value="'+$('#internalStatus').val()+'"]').text());
           $('#messagetransactionId').val($('#transactionId').val());
           $('#statusDisplayText').html($('#internalStatus option[value="'+$('#internalStatus').val()+'"]').text());
           $('#selStatus').show();

        });

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

        //This function will handle opening up the note model window so the
        //user can enter a new note.
        $(document).on('click', '#createNewNote', function() {
            //Open the note modal window
           $('#messageNoteModal').modal('show');
           $('#internalStatusId').val(0);
           $('#internalStatusText').val('');
           $('#messagetransactionId').val($('#transactionId').val());
           $('#statusDisplayText').html("");
           $('#selStatus').hide();
        });

        //This function will handle submitting the new note
        $(document).on('click', '#submitMessageNote', function(event) {
            var transactionId = $('#transactionId').val();

            var formData = $("#messageNoteForm").serialize();

            $.ajax({
                url: '/CareConnector/submitMessageNote.do',
                data: formData,
                type: "POST",
                async: false,
                success: _RedirectToDetails()
            });

            event.preventDefault();
            return false;

        });

        //This function will handle removing an existing note
        $(document).on('click', '.removeNote', function() {
            
            var confirmed = confirm("Are you sure you want to remove this note?");

            if(confirmed) {
                var noteId = $(this).attr('rel');

                $.ajax({
                    url: '/CareConnector/removeNote.do',
                    type: 'POST',
                    data: {'noteId': noteId},
                    success: function(data) {
                       $('#noteRow-'+noteId).remove();
                    }
                });
            }
        });
        
        //This function will handle canceling a message
        $(document).on('click','.cancelMessage', function() {
           
            var confirmed = confirm("Are you sure you want to cancel this message?");

            if(confirmed) {
                
                var transactionId = $('#transactionId').val();

                $.ajax({
                    url: '/CareConnector/cancelMessage.do',
                    type: 'POST',
                    data: {'transactionId': transactionId, 'sent': true},
                    success: function(data) {
                       //send the user back to the sent items box.
                       window.location.href='/CareConnector/sent';
                    }
                });
            }
        });
    });
});

function _RedirectToDetails() {
    $('#viewTransactionDetails').submit();
}

function populateExistingAttachments() {
    var transactionId = $('#transactionId').val();
    var sentTransactionId = $('#sentTransactionId').val();
    var currentIdList = $('#attachmentIds').val();
    
    //If viewing an inbox transaction need to get attachments
    //for the orginating transaction=
    if(sentTransactionId > 0) {
        transactionId = sentTransactionId;
    }
    
    $.ajax({
        url: '/CareConnector/populateExistingAttachments.do',
        type: 'GET',
        data: {'transactionId': transactionId, 'newattachmentIdList': currentIdList, 'pageFrom': 'sent'},
        success: function(data) {
            $("#existingAttachments").html(data);
        }
    });
}

function populateExistingNotes() {
    var transactionId = $('#transactionId').val();
    
    $.ajax({
        url: '/CareConnector/populateExistingNotes.do',
        type: 'GET',
        data: {'transactionId': transactionId},
        success: function(data) {
            $("#existingNotes").html(data);
        }
    });
}
