/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        //Fade out the alert message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }
        
        $('.providerBody').mouseenter(function() {
             $(this).css('background-color','#F5F5F5');
        });
        
         $('.providerBody').mouseleave(function() {
             $(this).css('background-color','white');
        });
        
        //This function will remvoe the clicked provider 
        $(document).on('click', '.providerDelete', function() {

            var confirmed = confirm("Are you sure you want to remove this provider?");

            if (confirmed) {
                var id = $(this).attr('rel');
                window.location.href = "/OrgProfile/providerDelete/delete?i=" + id;
            }
        });
       
    }); 
});