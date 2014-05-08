/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require(['./main'], function () {
    require(['jquery'], function($) {


        $("input:text,form").attr("autocomplete", "off");
        
        $('.showOtherCriteria').click(function() {
            $('#additionalCriteria').show();
            $('#search1').hide();
            $('#showOther').hide();
            $('#search2').show();
            $('#hideOther').show();
        });
            
        $('.hideOtherCriteria').click(function() {
            $('#additionalCriteria').hide();
            $('#search1').show();
            $('#showOther').show();
            $('#search2').hide();
            $('#hideOther').hide();
        });
        
        $(document).on('click','.search', function() {
            $('#searchHistoryForm').submit();
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

