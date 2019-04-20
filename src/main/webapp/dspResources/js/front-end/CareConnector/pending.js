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
            $('.alert').delay(2000).fadeOut(1000);
        }

        var totalCheckBoxes = 0;
        //Count how many batches are ready to be sent
        $('input[type=checkbox]').each(function() {
            totalCheckBoxes+=1;
        });

        //If there are zero batches ready to be sent hide the button
        if(totalCheckBoxes == 0) {
            $('.sendBatches').hide();
        }
    
        $('.viewLink').click(function() {
           var batchId = $(this).attr('rel');

            $('#batchId').val(batchId);
            $('#viewBatchTransactions').submit();

        });

        //This function will launch the status detail overlay with the selected
        //status
        $(document).on('click', '.viewStatus', function() {
            $.ajax({
                url: 'viewStatus' + $(this).attr('rel'),
                type: "GET",
                success: function(data) {
                    $("#statusModal").html(data);
                }
            });
        });

        $('#searchBatchesBtn').click(function() { 
            $('#searchForm').submit();
        });

        //This function will handle sending batches
        $(document).on('click','.sendBatches', function() {

           $('.alert-danger').hide();

           var batchIdList = "";

           //Loop through all batch ids
           $('input[type=checkbox]').each(function() {
               if(this.checked) {
                    batchIdList += (batchIdList ==="" ? this.value : "," + this.value);
                }
           });

           if(batchIdList === "") {
               $('.alert-danger').html("At least one batch must be marked to send!");
               $('.alert-danger').show();
           }
           else {
               $('#batchIdList').val(batchIdList);
               $('#sendMarkedBatches').submit();
           }


        });

        $(document).on('click','.changePage', function() {
           $('#page').val($(this).attr('rel'));
           $('#searchForm').submit();
        });
      
        var aoColumns = [
           { "bSortable" : false},
           null,
           null,
           null,
           null,
           null,
           { "bSortable" : false}
        ];
        
        $('#dataTable').dataTable({
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sSearch": "_INPUT_",
                "sLengthMenu": '<select class="form-control" style="width:150px">'+
                      '<option value="10">10 Records</option>'+
                      '<option value="20">20 Records</option>'+
                      '<option value="30">30 Records</option>'+
                      '<option value="40">40 Records</option>'+
                      '<option value="50">50 Records</option>'+
                      '<option value="-1">All</option>'+
                      '</select>'
            },
           "aoColumns" : aoColumns,
           "aaSorting" : [[5, "desc"]]
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