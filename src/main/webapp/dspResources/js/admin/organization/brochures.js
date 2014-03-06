
require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        $("input:text,form").attr("autocomplete", "off");

        //This function will launch the new brochure overlay with a blank form
        $(document).on('click', '#createNewBrochure', function() {
            $.ajax({
                url: 'newBrochure',
                type: "GET",
                success: function(data) {
                    $("#systemBrochureModal").html(data);
                }
            });
        });

        //This function will launch the edit brochure overlay populating the fields
        //with the data of the clicked brochure.
        $(document).on('click', '.brochureEdit', function() {
            var detailsAction = 'brochure/' + $(this).attr('rel');

            $.ajax({
                url: detailsAction,
                type: "GET",
                success: function(data) {
                    $("#systemBrochureModal").html(data);
                }
            });
        });

        //This function will remove the clicked brochure and will remove the 
        //actual file from the system.
        $(document).on('click', '.brochureDelete', function() {

            var confirmed = confirm("Are you sure you want to remove this brochure?");

            if (confirmed) {
                var id = $(this).attr('rel');
                window.location.href = "brochureDelete/delete?i=" + id;
            }
        });

        //This function will handle searching for brochures
        $('#searchBrochureBtn').click(function() {
            $('#searchForm').submit();
        });


        //Function to submit the changes to an existing brochure or 
        //submit the new brochure fields from the modal window.
        $(document).on('click', '#submitButton', function(event) {
            var actionValue = $(this).attr('rel').toLowerCase();
            var errorFound = 0;

            //Remove any error message classes
            $('#brochureFileDiv').removeClass("has-error");
            $('#brochureFileMsg').removeClass("has-error");
            $('#brochureFileMsg').html('');
            $('#brochureTitleDiv').removeClass("has-error");
            $('#brochureTitleMsg').removeClass("has-error");
            $('#brochureTitleMsg').html('');

            //Make sure a title is added
            if ($('#brochureTitle').val() == '') {
                $('#brochureTitleDiv').addClass("has-error");
                $('#brochureTitleMsg').addClass("has-error");
                $('#brochureTitleMsg').html('The brochure title is a required field.');
                errorFound = 1;
            }
            //Make sure a file is uploaded
            if ($('#brochureFile').val() == '' && $('#id').val() == 0) {
                $('#brochureFileDiv').addClass("has-error");
                $('#brochureFileMsg').addClass("has-error");
                $('#brochureFileMsg').html('The brochure file is a required field.');
                errorFound = 1;
            }
            //Make sure the file is a correct format
            //(.jpg, .gif, .jpeg, .doc, .docx, .pdf, .txt)
            if ($('#brochureFile').val() != '' && ($('#brochureFile').val().indexOf('.jpg') == -1 &&
                    $('#brochureFile').val().indexOf('.jpeg') == -1 &&
                    $('#brochureFile').val().indexOf('.gif') == -1 &&
                    $('#brochureFile').val().indexOf('.pdf') == -1 &&
                    $('#brochureFile').val().indexOf('.doc') == -1 &&
                    $('#brochureFile').val().indexOf('.docx') == -1 &&
                    $('#brochureFile').val().indexOf('.doc') == -1 &&
                    $('#brochureFile').val().indexOf('.txt') == -1)) {

                $('#brochureFileDiv').addClass("has-error");
                $('#brochureFileMsg').addClass("has-error");
                $('#brochureFileMsg').html('The brochure file must be an image, word doc or pdf.');
                errorFound = 1;

            }


            if (errorFound == 1) {
                event.preventDefault();
                return false;
            }

            $('#brochuredetailsform').attr('action', actionValue + 'Brochure');
            $('#brochuredetailsform').submit();

        });
        
    });
});



