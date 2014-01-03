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
       if(clickedBtn != 'save') {
            errorsFound = checkFormFields();
            
            if(errorsFound == 0) {
                $('#messageForm').submit(); 
            }
       }
       else {
            $('#messageForm').submit(); 
       }
    });
    
    
    //Function to upload an attachment to the message
    new AjaxUpload('attachmentFileUpload', {
        action: '../uploadMessageAttachment',
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


});

function getTitle(){
   var titleval = $('#attachmentTitle').val();
   return titleval;
}

function populateExistingAttachments() {
    var transactionId = $('#transactionId').val();
    var currentIdList = $('#attachmentIds').val();
    
    $.ajax({
        url: '../populateExistingAttachments.do',
        type: 'GET',
        data: {'transactionId': transactionId, 'newattachmentIdList': currentIdList},
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
    var DateReg = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/;
    if( !DateReg.test( $date ) ) {
      return false;
    } 
    else {
      return true;
    }
}

