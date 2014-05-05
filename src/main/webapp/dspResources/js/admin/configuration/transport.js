

require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
    
        var selMethodId = $('#transportMethod').val() 
        //Show file/download/FTP fields
        if(selMethodId === "1" || selMethodId === "3" || selMethodId === "5") {
            $('#upload-downloadDiv').show();
        }

        if(selMethodId === "3") {
            $('#additionalFTPDiv').show();
        }

        if(selMethodId !== "2" && selMethodId !== "") {
            $('.assocMessageTypes').hide();
        }
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $('#transportMethod').change(function() {
           var methodId = $(this).val();

           //hide all section divs
           $('.methodDiv').hide();

           //Show file/download/FTP fields
           if(methodId === "1" || methodId === "3" || methodId === "5") {
               $('#upload-downloadDiv').show();
           }

           if(methodId === "3") {
               $('#additionalFTPDiv').show();
           }
          
           if(methodId !== "2" && methodId !== "") {
                $('.assocMessageTypes').hide();
            }
            else {
                $('.assocMessageTypes').hide();
            }
            

        });

        $('#useSource').click(function() {
            if($('#useSource').is(":checked")) {
                $('#targetFileName').val("USE SOURCE FILE");
            }
            else {
                $('#targetFileName').val("");
            }
        });



        //This function will save the messgae type field mappings
        $('#saveDetails').click(function() {
            $('#action').val('save');

            //Need to make sure all required fields are marked if empty.
            var hasErrors = 0;
            hasErrors = checkFormFields();

            if (hasErrors == 0) { 
                $('#transportDetails').submit();
            }
        });

        $('#next').click(function(event) {
            $('#action').val('next');

            var hasErrors = 0;
            hasErrors = checkFormFields();

            if (hasErrors == 0) {
                $('#transportDetails').submit();
            }
        });

        $('#existingtransportMethod').change(function() {
           var detailId = $(this).val();
           var configId = $('#configId').val();

           if(detailId > 0) {
                $.ajax({
                    url: 'copyExistingTransportMethod.do',
                    type: "POST",
                    data: {'detailId': detailId, 'configId': configId},
                    success: function(data) {
                         window.location.href = "transport";
                    }
                });
            }

        });
        
        //Set the default file extension when the file type is selected
        $('#fileType').change(function() {
           
           var fileType = $(this).val();
           
           if(fileType == 2) {
              $('#fileExt').val('txt'); 
           }
           else if(fileType == 3) {
              $('#fileExt').val('csv'); 
           }
           else if(fileType == 4) {
              $('#fileExt').val('hr'); 
           }
           else if(fileType == 5) {
              $('#fileExt').val('mdb'); 
           }
           else if(fileType == 6) {
              $('#fileExt').val('pdf'); 
           }
           else if(fileType == 7) {
              $('#fileExt').val('odbc'); 
           }
           else if(fileType == 8) {
              $('#fileExt').val('xls'); 
           }
           else if(fileType == 9) {
              $('#fileExt').val('xml'); 
           }
           else if(fileType == 10) {
              $('#fileExt').val('doc'); 
           }
            
        });
        
        //Test the FTP Push Connection
        $(document).on('click', '.testFTPPush', function() {
            
            var ftpSettingsId = $('#id').val();
            
            $.ajax({
                url: 'testFTPConnection.do',
                type: "GET",
                 data: {'method': 2, 'id': ftpSettingsId},
                success: function(data) {
                     alert(data);
                }
            });
        });
        
        //Test the FTP Get Connection
        $(document).on('click', '.testFTPGet', function() {
          
            var ftpSettingsId = $('#id').val();
            var configId = $('#configId').val();
            
            $.ajax({
                url: 'testFTPConnection.do',
                type: "GET",
                data: {'method': 1, 'id': ftpSettingsId, 'configId': configId},
                success: function(data) {
                     alert(data);
                }
            });
        });
        
        $(document).on('change','.ftpProtocol', function() {
           
            if($(this).val() == "SFTP") {
                $('#certificationfileDiv'+$(this).attr('rel')).show();
                if($(this).attr('rel') == 1) {
                    $('.testFTPGet').hide(); 
                }
                else {
                    $('.testFTPPush').hide();
                }
            }
            else {
               $('#certificationfileDiv'+$(this).attr('rel')).hide(); 
               $('#file'+$(this).attr('rel')).val(""); 
               if($(this).attr('rel') == 1) {
                    $('.testFTPGet').show();
                }
                else {
                    $('.testFTPPush').show();
                }
            }
            
        });
        
    });
});


function checkFormFields() {
    var hasErrors = 0;

    //Remove all has-error class
    $('div.form-group').removeClass("has-error");
    $('span.control-label').removeClass("has-error");
    $('span.control-label').html("");
    $('.alert-danger').hide();
    
    var selMethodId = $('#transportMethod').val() 
    
    //Make sure a transport method is chosen
    if($('#transportMethod').val() === "") {
       $('#transportMethodDiv').addClass("has-error");
       $('#transportMethodMsg').addClass("has-error");
       $('#transportMethodMsg').html('The transport method is a required field.');
       hasErrors = 1;
    }
    
    if (selMethodId === "1" || selMethodId === "3") {
       
       //Make sure the file size is numeric and greate than 0
       if($('#maxFileSize').val() <= 0 || !$.isNumeric($('#maxFileSize').val())) {
           $('#maxFileSizeDiv').addClass("has-error");
           $('#maxFileSizeMsg').addClass("has-error");
           $('#maxFileSizeMsg').html('The max file size is a required field and must be a numeric value.');
           hasErrors = 1;
       }
       
       //Make sure the file type is selected
       if($('#fileType').val() === "") {
           $('#fileTypeDiv').addClass("has-error");
           $('#fileTypeMsg').addClass("has-error");
           $('#fileTypeMsg').html('The file type is a required field.');
           hasErrors = 1;
       }
       
       //Make sure the file ext is entered
       if($('#fileExt').val() === "") {
           $('#fileExtDiv').addClass("has-error");
           $('#fileExtMsg').addClass("has-error");
           $('#fileExtMsg').html('The file extension is a required field.');
           hasErrors = 1;
       }
       else {
           //Remove any '.' in the extension
           $('#fileExt').val($('#fileExt').val().replace('.',''));
       }
       
       //Make sure the file delimiter is selected
       if($('#fileDelimiter').val() === "") {
           $('#fileDelimiterDiv').addClass("has-error");
           $('#fileDelimiterMsg').addClass("has-error");
           $('#fileDelimiterMsg').html('The file delimiter is a required field.');
           hasErrors = 1;
       }
       
       if(selMethodId === "3") {
        var IPReg = /^(\d\d?)|(1\d\d)|(0\d\d)|(2[0-4]\d)|(2[0-5])\.(\d\d?)|(1\d\d)|(0\d\d)|(2[0-4]\d)|(2[0-5])\.(\d\d?)|(1\d\d)|(0\d\d)|(2[0-4]\d)|(2[0-5])$/;
        
        //Check FTP Get Fields
        var getFieldsEntered = 0;

        if($('#ip1').val() !== "" || $('#username1').val() !== "" || $('#password1').val() !== "" || $('#directory1').val() !== "") {
             getFieldsEntered = 1;
        }

        if(getFieldsEntered == 1) {
            if($('#ip1').val() === "") {
                $('#ip1Div').addClass("has-error");
                $('#ip1Msg').addClass("has-error");
                $('#ip1Msg').html('The IP address is a required field.');
                hasErrors = 1;
            }
            else if(!IPReg.test( $('#ip1').val() )) {
               $('#ip1Div').addClass("has-error");
                $('#ip1Msg').addClass("has-error");
                $('#ip1Msg').html('The IP address entered is invalid.');
                hasErrors = 1; 
            }
            if($('#username1').val() === "") {
                $('#username1Div').addClass("has-error");
                $('#username1Msg').addClass("has-error");
                $('#username1Msg').html('The username is a required field.');
                hasErrors = 1;
            }
            
            if($('#protocol1').val() === "SFTP") {
                if($('#password1').val() === "" && $('#file1').val() === "" && $('#certification1').val() === "") {
                    $('#password1Div').addClass("has-error");
                    $('#password1Msg').addClass("has-error");
                    $('#password1Msg').html('The password or certification is a required field.');
                    hasErrors = 1;
                }
            }
            else {
                if($('#password1').val() === "") {
                    $('#password1Div').addClass("has-error");
                    $('#password1Msg').addClass("has-error");
                    $('#password1Msg').html('The password is a required field.');
                    hasErrors = 1;
                }
            }
            
            
            if($('#directory1').val() === "") {
                $('#directory1Div').addClass("has-error");
                $('#directory1Msg').addClass("has-error");
                $('#directory1Msg').html('The directory is a required field.');
                hasErrors = 1;
            }
        }

        //Check FTP push Fields
        var pushFieldsEntered = 0;

        if($('#ip2').val() !== "" || $('#username2').val() !== "" || $('#password2').val() !== "" || $('#directory2').val() !== "") {
             pushFieldsEntered = 1;
        }

        if(pushFieldsEntered == 1) {
            if($('#ip2').val() === "") { 
                $('#ip2Div').addClass("has-error");
                $('#ip2Msg').addClass("has-error");
                $('#ip2Msg').html('The IP address is a required field.');
                hasErrors = 1;
            }
            else if(!IPReg.test( $('#ip2').val() )) {
                $('#ip2Div').addClass("has-error");
                $('#ip2Msg').addClass("has-error");
                $('#ip2Msg').html('The IP address entered is invalid.');
                hasErrors = 1; 
            }
            if($('#username2').val() === "") {
                $('#username2Div').addClass("has-error");
                $('#username2Msg').addClass("has-error");
                $('#username2Msg').html('The username is a required field.');
                hasErrors = 1;
            }
            if($('#protocol2').val() === "SFTP") {
                if($('#password2').val() === "" && $('#file2').val() === "" && $('#certification2').val() === "") {
                    $('#password2Div').addClass("has-error");
                    $('#password2Msg').addClass("has-error");
                    $('#password2Msg').html('The password or certification is a required field.');
                    hasErrors = 1;
                }
            }
            else {
                if($('#password2').val() === "") {
                    $('#password2Div').addClass("has-error");
                    $('#password2Msg').addClass("has-error");
                    $('#password2Msg').html('The password is a required field.');
                    hasErrors = 1;
                }
            }
            if($('#directory2').val() === "") {
                $('#directory2Div').addClass("has-error");
                $('#directory2Msg').addClass("has-error");
                $('#directory2Msg').html('The directory is a required field.');
                hasErrors = 1;
            }
        }

        if(getFieldsEntered == 0 && pushFieldsEntered == 0) {
            $('#FTPDanger').show();
            hasErrors = 1;
        }
     
       }
       
       
    }
    
    //Make sure at least one message type is selected
    var $messageTypes = $('#transportDetails').find('input[class="availMessageTypes"]:checked');
    
    if(!$messageTypes.length) {
        $('#messageTypeDanger').show();
        hasErrors = 1;
    }
 

    return hasErrors;
}


