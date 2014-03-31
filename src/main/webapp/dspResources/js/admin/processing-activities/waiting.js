/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        $("input:text,form").attr("autocomplete", "off");
        
        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }
        
        //This function will launch the status detail overlay with the selected
        //status
        $(document).on('click', '.process', function() {
            $.ajax({
                url: 'processTransaction',
                data:{'transactionId': $(this).attr('rel')},
                type: "GET",
                success: function(data) {
                    window.location.href = "/administrator/processing-activity/waiting?msg=processed";
                }
            });
        });
        
   });
});


function searchByDateRange() {
   var fromDate = $('.daterange span').attr('rel');
   var toDate = $('.daterange span').attr('rel2');
    
   $('#fromDate').val(fromDate);
   $('#toDate').val(toDate);
   
   $('#searchForm').submit();

}
