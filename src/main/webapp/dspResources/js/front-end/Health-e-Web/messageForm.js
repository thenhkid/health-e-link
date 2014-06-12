/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require(['./main'], function() {
    require(['jquery'], function($) {

        $("input:text,form").attr("autocomplete", "off");

        populateExistingAttachments();

        $('.submitMessage').click(function() {
            var errorsFound = 0;
            var clickedBtn = $(this).attr('id');

            $('#action').val(clickedBtn);

            //Only check form fields if Sending or Releasing the message
            if (clickedBtn !== 'save') {
                errorsFound = checkFormFields();

                if (errorsFound == 0) {
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
            onSubmit: function(file, extension) {
                this.setData({title: getTitle()});
            },
            onComplete: function(file, response) {
                var currentIdList = $('#attachmentIds').val();

                if (currentIdList == '') {
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

            if (confirmed) {

                var attachmentId = $(this).attr('rel');

                $.ajax({
                    url: '/Health-e-Web/removeAttachment.do',
                    type: 'POST',
                    data: {'attachmentId': attachmentId},
                    success: function(data) {
                        $('#attachmentRow-' + attachmentId).remove();
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

            if ($(this).val() > 0) {
                $.ajax({
                    url: '/Health-e-Web/populateProvider.do',
                    type: 'GET',
                    data: {'providerId': $(this).val()},
                    success: function(data) {
                        //fields 9 - 18
                        $('#provider_9').val(data.firstName);
                        $('#provider_10').val(data.lastName);

                        if (data.providerAddresses.length > 0) {
                            $('#provider_12').val(data.providerAddresses[0].line1);
                            $('#provider_13').val(data.providerAddresses[0].line2);
                            $('#provider_14').val(data.providerAddresses[0].city);
                            $('#provider_15').val(data.providerAddresses[0].state);
                            $('#provider_16').val(data.providerAddresses[0].postalCode);
                            $('#provider_17').val(data.providerAddresses[0].phone1);
                            $('#provider_18').val(data.providerAddresses[0].fax);

                            $('#fromOrgProviderLine1').html(data.providerAddresses[0].line1 + '<br />');
                            if (data.providerAddresses[0].line2 != '') {
                                $('#fromOrgProviderLine2').html(data.providerAddresses[0].line2 + '<br />');
                            }
                            else {
                                $('#fromOrgProviderLine2').html("");
                            }
                            $('#fromOrgProviderRegion').html(data.providerAddresses[0].city + " " + data.providerAddresses[0].state + ", ");
                            $('#fromOrgProviderZip').html(data.providerAddresses[0].postalCode);
                            if (data.providerAddresses[0].phone1 != "") {
                                $('#fromOrgProviderPhone').html("phone: <span class='tel' >" + data.providerAddresses[0].phone1 + "</span>");
                            }
                            if (data.providerAddresses[0].fax != "") {
                                $('#fromOrgProviderFax').html("fax: <span class='tel'>" + data.providerAddresses[0].fax + "</span>");
                            }
                        }
                        else {
                            $('#fromOrgProviderLine1').html("");
                            $('#fromOrgProviderLine2').html("");
                            $('#fromOrgProviderRegion').html("");
                            $('#fromOrgProviderZip').html("");
                            $('#fromOrgProviderPhone').html("");
                            $('#fromOrgProviderFax').html("");
                        }

                        if (data.providerIds.length > 0) {
                            $('#provider_11').val(data.providerIds[0].idNum);
                        }
                        $('#fromOrgProviderName').html(data.firstName + " " + data.lastName);

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

        //Function to go get providers as the user types in letters
        $(document).on('change', '#targetorgProvider', function() {

            if ($(this).val() > 0) {
                $.ajax({
                    url: '/Health-e-Web/populateProvider.do',
                    type: 'GET',
                    data: {'providerId': $(this).val()},
                    success: function(data) {
                        //fields 9 - 18
                        $('#tgtprovider_27').val(data.firstName);
                        $('#tgtprovider_28').val(data.lastName);

                        if (data.providerAddresses.length > 0) {
                            $('#tgtprovider_30').val(data.providerAddresses[0].line1);
                            $('#tgtprovider_31').val(data.providerAddresses[0].line2);
                            $('#tgtprovider_32').val(data.providerAddresses[0].city);
                            $('#tgtprovider_33').val(data.providerAddresses[0].state);
                            $('#tgtprovider_34').val(data.providerAddresses[0].postalCode);
                            $('#tgtprovider_35').val(data.providerAddresses[0].phone1);
                            $('#tgtprovider_36').val(data.providerAddresses[0].fax);

                            $('#toOrgProviderLine1').html(data.providerAddresses[0].line1 + '<br />');
                            if (data.providerAddresses[0].line2 != '') {
                                $('#toOrgProviderLine2').html(data.providerAddresses[0].line2 + '<br />');
                            }
                            else {
                                $('#toOrgProviderLine2').html("");
                            }
                            $('#toOrgProviderRegion').html(data.providerAddresses[0].city + " " + data.providerAddresses[0].state + ", ");
                            $('#toOrgProviderZip').html(data.providerAddresses[0].postalCode);
                            if (data.providerAddresses[0].phone1 != "") {
                                $('#toOrgProviderPhone').html("phone: <span class='tel' >" + data.providerAddresses[0].phone1 + "</span>");
                            }
                            if (data.providerAddresses[0].fax != "") {
                                $('#toOrgProviderFax').html("fax: <span class='tel'>" + data.providerAddresses[0].fax + "</span>");
                            }
                        }
                        else {
                            $('#toOrgProviderLine1').html("");
                            $('#toOrgProviderLine2').html("");
                            $('#toOrgProviderRegion').html("");
                            $('#toOrgProviderZip').html("");
                            $('#toOrgProviderPhone').html("");
                            $('#toOrgProviderFax').html("");
                        }

                        if (data.providerIds.length > 0) {
                            $('#tgtprovider_29').val(data.providerIds[0].idNum);
                        }
                        $('#toOrgProviderName').html(data.firstName + " " + data.lastName);

                        $('#toorgProvider').show();
                        $('#fromOrgProviderChoose').hide();
                    }
                });
            }
            else {
                $('#tgtprovider_27').val("");
                $('#tgtprovider_28').val("");
                $('#tgtprovider_29').val("");
                $('#tgtprovider_30').val("");
                $('#tgtprovider_31').val("");
                $('#tgtprovider_32').val("");
                $('#tgtprovider_33').val("");
                $('#tgtprovider_34').val("");
                $('#tgtprovider_35').val("");
                $('#tgtprovider_36').val("");
                $('#fromorgProvider').hide();
            }

        });

        //Function hide the selected provider and show the provider
        //drop down.
        $(document).on('click', '#toOrgProviderChange', function() {
            $('#tgtprovider_27').val("");
            $('#tgtprovider_28').val("");
            $('#tgtprovider_29').val("");
            $('#tgtprovider_30').val("");
            $('#tgtprovider_31').val("");
            $('#tgtprovider_32').val("");
            $('#tgtprovider_33').val("");
            $('#tgtprovider_34').val("");
            $('#tgtprovider_36').val("");
            // $('#provider_18').val("");
            $('#fromorgProvider').hide();
            $('#targetorgProvider').val("");
            $('#fromOrgProviderChoose').show();
        });

        //Function to handle the form actions
        $(document).on('change', '#formAction', function() {

            if ($(this).val() === 'print') {
                window.print();
            }
            else if ($.isNumeric($(this).val())) {
                $('#originalTransactionId').val($(this).val());
                $('#viewOriginalTransaction').submit();
            }

        });

        //Function to delete the message 
        $(document).on('click', '.deleteMessage', function() {

            var confirmed = confirm("Are you sure you want to delete this message?");

            if (confirmed) {

                var transactionId = $('#transactionId').val();

                $.ajax({
                    url: '/Health-e-Web/deleteMessage.do',
                    type: 'POST',
                    data: {'transactionId': transactionId},
                    success: function(data) {
                        //send the user back to the pending items box.
                        window.location.href = '/Health-e-Web/pending';
                    }
                });
            }

        });

        //This function will handle canceling a message
        $(document).on('click', '.cancelMessage', function() {

            var confirmed = confirm("Are you sure you want to cancel this message?");

            if (confirmed) {

                var transactionId = $('#transactionId').val();

                $.ajax({
                    url: '/Health-e-Web/cancelMessage.do',
                    type: 'POST',
                    data: {'transactionId': transactionId, 'sent': false},
                    success: function(data) {
                        //send the user back to the pending items box.
                        window.location.href = '/Health-e-Web/pending';
                    }
                });
            }
        });


    });
});

function getTitle() {
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

        if ($(this).val() === '') {
            $('#fieldDiv_' + fieldNo).addClass("has-error");
            $('#errorMsg_' + fieldNo).addClass("has-error");
            $('#errorMsg_' + fieldNo).html('This is a required field.');
            errorFound = 1;
        }
    });

    //Look at all Email validation types
    $('.Email').each(function() {
        var fieldNo = $(this).attr('id');
        var emailVal = $(this).val();

        if (emailVal != '') {
            var emailValidated = validateEmail(emailVal);

            if (emailValidated === false) {
                $('#fieldDiv_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).html('This is not a valid email address.');
                errorFound = 1;
            }
        }
    });

    //Look at all Phone Number validation types
    $('.Phone-Number').each(function() {
        var fieldNo = $(this).attr('id');
        var phoneVal = $(this).val();

        if (phoneVal != '') {
            var phoneValidated = validatePhone(phoneVal);

            if (phoneValidated === false) {
                $('#fieldDiv_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).html('This is not a valid phone number.');
                errorFound = 1;
            }
        }
    });

    //Look at all numeric validation types
    $('.Numeric').each(function() {
        var fieldNo = $(this).attr('id');
        var fieldVal = $(this).val();

        if (fieldVal != '') {
            var fieldValidated = validateNumericValue(fieldVal);

            if (fieldValidated === false) {
                $('#fieldDiv_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).html('The value must be numeric.');
                errorFound = 1;
            }
        }
    });

    //Look at all URL validation types
    $('.URL').each(function() {
        var fieldNo = $(this).attr('id');
        var URLVal = $(this).val();

        if (URLVal != '') {
            var URLValidated = validateURL(URLVal);

            if (URLValidated === false) {
                $('#fieldDiv_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).html('This is not a valid URL.');
                errorFound = 1;
            }
        }
    });

    //Look at all Date validation types
    $('.Date').each(function() {
        var fieldNo = $(this).attr('id');
        var dateVal = $(this).val();

        if (dateVal != '') {
            var DateValidated = validateDate(dateVal);

            if (DateValidated === false) {
                $('#fieldDiv_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).addClass("has-error");
                $('#errorMsg_' + fieldNo).html('This is not a valid Date, format should be mm/dd/yyyy.');
                errorFound = 1;
            }
        }
    });

    return errorFound;
}

function validateEmail($email) {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    if (!emailReg.test($email)) {
        return false;
    }
    else {
        return true;
    }
}

function validatePhone($phone) {
    //var phoneRegExp = /^(?:(?:\+?1\s*(?:[.-]\s*)?)?(?:\(\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\s*\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\s*(?:[.-]\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\s*(?:[.-]\s*)?([0-9]{4})(?:\s*(?:#|x\.?|ext\.?|extension)\s*(\d+))?$/;
    var phoneRegExp = /^[0-9-+]+$/;
    if (!phoneRegExp.test($phone)) {
        return false;
    }
    else {
        return true;
    }
}

function validateNumericValue($fieldVal) {
    var numericReg = /^\d*[0-9](|.\d*[0-9]|,\d*[0-9])?$/;
    if (!numericReg.test($fieldVal)) {
        return false;
    }
    else {
        return true;
    }
}

function validateURL($URL) {
    var URLReg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
    if (!URLReg.test($URL)) {
        return false;
    }
    else {
        return true;
    }
}

function validateDate($date) {

    var currVal = $date;

    if (currVal == '')
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


    /*

    var DateReg = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/; // mm/dd/yyyy
    var DateReg2 = /\b\d{4}[\-]\d{1,2}[\-]\d{1,2}\b/; // yyyy-mm-dd
    if (!DateReg.test($date) && !DateReg2.test($date)) {
        return false;
    }
    else {
        return true;
    } */
}

