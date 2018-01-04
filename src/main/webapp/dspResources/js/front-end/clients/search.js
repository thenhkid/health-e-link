/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {


        $("input:text,form").attr("autocomplete", "off");

        //Fade out the alert message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(5000).fadeOut(1000);
        }


       $(document).on('click', '#search', function() {
           var errorFound = 0;
           
           $('#fieldDiv_dob').removeClass("has-error");
           $('#errorMsg_dob').removeClass("has-error");
           $('#errorMsg_dob').html("");
           
           if($('#firstName').val() === "" 
                   && $('#lastName').val() === "" 
                   && $('#dob').val() === ""
                   && $('#postalCode').val() === "") {
               
               $('#searchError').show();
               errorFound = 1;
               
           }
           if ($('#dob').val() !== "") {
               var DateValidated = validateDate($('#dob').val());

               if (DateValidated === false) {
                   $('#fieldDiv_dob').addClass("has-error");
                   $('#errorMsg_dob').addClass("has-error");
                   $('#errorMsg_dob').html('This is not a valid Date, format should be mm/dd/yyyy.');
                   errorFound = 1;
               }
            }
            
           if(errorFound === 0) {
               $('#clientsearchForm').submit();
           }
       });
       
    });
});

function validateDate($date) {

    var currVal = $date;

    if (currVal === "")
        return false;

    //Declare Regex  
    var rxDatePattern = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4})$/;
    var dtArray = currVal.match(rxDatePattern); // is format OK?

    if (dtArray == null)
        return false;

    //Checks for mm/dd/yyyy format.
    dtMonth = dtArray[1];
    dtDay = dtArray[3];
    dtYear = dtArray[5];

    if (dtMonth < 1 || dtMonth > 12)
        return false;
    else if (dtDay < 1 || dtDay > 31)
        return false;
    else if ((dtMonth == 4 || dtMonth == 6 || dtMonth == 9 || dtMonth == 11) && dtDay == 31)
        return false;
    else if (dtMonth == 2)
    {
        var isleap = (dtYear % 4 == 0 && (dtYear % 100 != 0 || dtYear % 400 == 0));
        if (dtDay > 29 || (dtDay == 29 && !isleap))
            return false;
    }
    return true;

}

