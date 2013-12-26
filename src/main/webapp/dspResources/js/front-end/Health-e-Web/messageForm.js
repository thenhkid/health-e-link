/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {
    $("input:text,form").attr("autocomplete", "off");
    
    $('.dateField').change(function() {
       var idVal = $(this).attr('rel');
       var monthVal = $('#dateMonth_'+idVal).val();
       var dayVal = $('#dateDay_'+idVal).val();
       var yearVal = $('#dateYear_'+idVal).val();
       
       if(monthVal == '' || dayVal == '' || yearVal == '') {
           $('#'+idVal).val('');
       }
       else {
           var selDate = new Date(yearVal, eval(monthVal-1), dayVal);
           $('#'+idVal).val(selDate);
       }
    });
    
    
    $('#saveReferral').click(function() {
       var errorsFound = 0;
       
       errorsFound = checkFormFields();
        
       if(errorsFound == 0) {
           $('#messageForm').submit(); 
       }
    });


});


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

