/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
    
    populateExistingAttachments();
    
    $('.submitMessage').click(function() {
       var errorsFound = 0;
       var clickedBtn = $(this).attr('id');
       
       $('#action').val(clickedBtn);
       
       //Only check form fields if Sending or Releasing the message
       if(clickedBtn !== 'save') {
            errorsFound = checkFormFields();
            
            if(errorsFound == 0) {
                $('#messageForm').submit(); 
            }
            else {
                $('.alert-danger').show();
                $('.alert-danger').delay(2000).fadeOut(1000);
            }
       }
       else {
            $('#messageForm').submit(); 
       }
    });
    
    
    //Function to upload an attachment to the message
    new AjaxUpload('attachmentFileUpload', {
        action: '/Health-e-Web/uploadMessageAttachment',
        name: 'fileUpload',
        onSubmit: function(file, extension){
            this.setData({title: getTitle()});
        },
        onComplete: function(file, response) {
           var currentIdList = $('#attachmentIds').val();
           
           if(currentIdList == '') {
               $('#attachmentIds').val(response);
           }
           else {
               currentIdList = currentIdList + ',' + response;
               $('#attachmentIds').val(currentIdList);
           }
           
           $('#attachmentTitle').val('');
           
           populateExistingAttachments();
           
        }
    });
    
    //Function to remove the selected attachment
    $(document).on('click', '.removeAttachment', function() {
       
        var confirmed = confirm("Are you sure you want to remove this attachment?");
        
        if(confirmed) {
            
            var attachmentId = $(this).attr('rel');
            
            $.ajax({
                url: '/Health-e-Web/removeAttachment.do',
                type: 'POST',
                data: {'attachmentId': attachmentId},
                success: function(data) {
                   $('#attachmentRow-'+attachmentId).remove();
                }
            });
            
        }
        
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
    
    
    //Function to go get providers as the user types in letters
    $(document).on('change', '#orgProvider', function() {
        
        if($(this).val() > 0) {
            $.ajax({
                url: '/Health-e-Web/populateProvider.do',
                type: 'GET',
                data: {'providerId': $(this).val()},
                success: function(data) {
                    //fields 9 - 18
                    $('#provider_9').val(data.firstName);
                    $('#provider_10').val(data.lastName);
                                
                    if(data.providerAddresses.length > 0) {
                        $('#provider_12').val(data.providerAddresses[0].line1);
                        $('#provider_13').val(data.providerAddresses[0].line2);
                        $('#provider_14').val(data.providerAddresses[0].city);
                        $('#provider_15').val(data.providerAddresses[0].state);
                        $('#provider_16').val(data.providerAddresses[0].postalCode);
                        $('#provider_17').val(data.providerAddresses[0].phone1);
                        $('#provider_18').val(data.providerAddresses[0].fax);
                        
                        $('#fromOrgProviderLine1').html(data.providerAddresses[0].line1);
                        $('#fromOrgProviderLine2').html(data.providerAddresses[0].line2);
                        $('#fromOrgProviderRegion').html(data.providerAddresses[0].city+" "+data.providerAddresses[0].state);
                        $('#fromOrgProviderZip').html(data.providerAddresses[0].postalCode);
                        if(data.providerAddresses[0].phone1 != "") {
                            $('#fromOrgProviderPhone').html("phone: <span class='tel' >"+data.providerAddresses[0].phone1+"</span>");
                        }
                        if(data.providerAddresses[0].fax != "") {
                            $('#fromOrgProviderFax').html("fax: <span class='tel'>"+data.providerAddresses[0].fax+"</span>");
                        }
                    }

                    if(data.providerIds.length > 0) {
                         $('#provider_11').val(data.providerIds[0].idNum);
                    }
                    $('#fromOrgProviderName').html(data.firstName+" "+data.lastName);
                    
                    $('#fromorgProvider').show();
                    $('#fromOrgProviderChoose').hide();
                }
            });
        }
        else {
            $('#provider_9').val("");
            $('#provider_10').val("");
            $('#provider_11').val("");
            $('#provider_12').val("");
            $('#provider_13').val("");
            $('#provider_14').val("");
            $('#provider_15').val("");
            $('#provider_16').val("");
            $('#provider_17').val("");
            $('#provider_18').val("");
            $('#fromorgProvider').hide();
        }
        
    });
    
    //Function hide the selected provider and show the provider
    //drop down.
    $(document).on('click', '#fromOrgProviderChange', function() {
        $('#provider_9').val("");
        $('#provider_10').val("");
        $('#provider_11').val("");
        $('#provider_12').val("");
        $('#provider_13').val("");
        $('#provider_14').val("");
        $('#provider_15').val("");
        $('#provider_16').val("");
        $('#provider_17').val("");
        $('#provider_18').val("");
        $('#fromorgProvider').hide();
        $('#orgProvider').val("");
        $('#fromOrgProviderChoose').show();
    });
    
    //Function to handle the form actions
    $(document).on('change','#formAction',function() {
        
        if($(this).val() === 'print') {
            window.print();
        }
        
    })

});

function getTitle(){
   var titleval = $('#attachmentTitle').val();
   return titleval;
}

function populateExistingAttachments() {
    var transactionId = $('#transactionId').val();
    var currentIdList = $('#attachmentIds').val();
    
    $.ajax({
        url: '/Health-e-Web/populateExistingAttachments.do',
        type: 'GET',
        data: {'transactionId': transactionId, 'newattachmentIdList': currentIdList, 'pageFrom': 'pending'},
        success: function(data) {
            $("#existingAttachments").html(data);
        }
    });
}

function checkFormFields() {
    var errorFound = 0;
    
    $('div').removeClass("has-error");
    $('span.has-error').html("");
    
    //Look at all required fields.
    $('.required').each(function() {
         var fieldNo = $(this).attr('id');
         
         if($(this).val() === '') {
            $('#fieldDiv_'+fieldNo).addClass("has-error");
            $('#errorMsg_'+fieldNo).addClass("has-error");
            $('#errorMsg_'+fieldNo).html('This is a required field.');
            errorFound = 1;
         }
    });
    
    //Look at all Email validation types
    $('.Email').each(function() {
        var fieldNo = $(this).attr('id');
        var emailVal = $(this).val();
        
        if(emailVal != '') {
            var emailValidated = validateEmail(emailVal);

            if(emailValidated === false) {
                $('#fieldDiv_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).html('This is not a valid email address.');
                errorFound = 1;
            }
        }
    });
    
    //Look at all Phone Number validation types
    $('.Phone-Number').each(function() {
        var fieldNo = $(this).attr('id');
        var phoneVal = $(this).val();
        
        if(phoneVal != '') {
            var phoneValidated = validatePhone(phoneVal);

            if(phoneValidated === false) {
                $('#fieldDiv_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).html('This is not a valid phone number.');
                errorFound = 1;
            }
        }
    });
    
    //Look at all numeric validation types
    $('.Numeric').each(function() {
        var fieldNo = $(this).attr('id');
        var fieldVal = $(this).val();
        
        if(fieldVal != '') {
            var fieldValidated = validateNumericValue(fieldVal);

            if(fieldValidated === false) {
                $('#fieldDiv_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).html('The value must be numeric.');
                errorFound = 1;
            }
        }
    });
    
    //Look at all URL validation types
    $('.URL').each(function() {
        var fieldNo = $(this).attr('id');
        var URLVal = $(this).val();
        
        if(URLVal != '') {
            var URLValidated = validateURL(URLVal);

            if(URLValidated === false) {
                $('#fieldDiv_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).html('This is not a valid URL.');
                errorFound = 1;
            }
        }
    });
    
    //Look at all Date validation types
    $('.Date').each(function() {
        var fieldNo = $(this).attr('id');
        var dateVal = $(this).val();
        
        if(dateVal != '') {
            var DateValidated = validateDate(dateVal);

            if(DateValidated === false) {
                $('#fieldDiv_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).addClass("has-error");
                $('#errorMsg_'+fieldNo).html('This is not a valid Date, format should be mm/dd/yyyy.');
                errorFound = 1;
            }
        }
    });
    
    return errorFound;
}

function validateEmail($email) {
  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
  if( !emailReg.test( $email ) ) {
    return false;
  } 
  else {
    return true;
  }
}

function validatePhone($phone) {
   var phoneRegExp = /\(?([0-9]{3})\)?([ .-]?)([0-9]{3})\2([0-9]{4})/;
   if ( !phoneRegExp.test($phone)) {
       return false;
   }
   else {
       return true;
   }
}

function validateNumericValue($fieldVal) {
    var numericReg = /^\d*[0-9](|.\d*[0-9]|,\d*[0-9])?$/;
    if( !numericReg.test( $fieldVal ) ) {
      return false;
    } 
    else {
      return true;
    }
}

function validateURL($URL) {
    var URLReg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
    if( !URLReg.test( $URL ) ) {
      return false;
    } 
    else {
      return true;
    }
}

function validateDate($date) {
    var DateReg = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/; // mm/dd/yyyy
    var DateReg2 = /\b\d{4}[\-]\d{1,2}[\-]\d{1,2}\b/; // yyyy-mm-dd
    if( !DateReg.test( $date ) && !DateReg2.test($date) ) {
      return false;
    } 
    else {
      return true;
    }
}

