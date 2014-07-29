
require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(5000);
        }

        $("input:text,form").attr("autocomplete", "off");

        //this function will send them to table view
        $('#searchdataItemBtn').click(function() {
            $('#searchForm').submit();
        });   

        //This function will remove the macro is clicked 
        $(document).on('click', '.articleDelete', function() {
            var confirmed = confirm("Are you sure you want to remove this article?");
            if (confirmed) {
                var id = $(this).attr('rel');
                window.location.href = "news/delete?i=" + id;
            }
        });


        //This function will launch the new dataItem overlay with a blank screen
        $(document).on('click', '#createNewArticle', function() {

            $.ajax({
                    url:  'news/create',
                type: "GET",
                success: function(data) {
                    $("#articleModal").html(data);
                }
            });
        });



    //This function will launch the edit article item overlay populating the fields
        $(document).on('click', '.articleEdit', function() {

            var articleDetailsAction = "news/view?i=" + $(this).attr('rel');

            $.ajax({
                url: articleDetailsAction,
                type: "GET",
                success: function(data) {
                    $("#articleModal").html(data);
                }
            });
        });


        $(document).on('click', '#submitButton', function(event) {
            var currentPage = $('#currentPageHolder').attr('rel');
            var formData = $("#articleform").serialize();
            var actionValue = "news/" + $(this).attr('rel').toLowerCase();
            $.ajax({
                url: actionValue,
                data: formData,
                type: "POST",
                async: false,
                success: function(data) {
                    /** **/
                    if (data.indexOf('articleUpdated') != -1) {
                            var goToUrl = "news?msg=updated";
                            
                            window.location.href = goToUrl;
                    } else if (data.indexOf('articleCreated') != -1) {
                            var goToUrl = "news?msg=created";
                        
                        window.location.href = goToUrl;
                    } else {
                            $("articleModal").html(data);
                    }

                }

            });
            event.preventDefault();
            return false;

        });
        
    });
});



