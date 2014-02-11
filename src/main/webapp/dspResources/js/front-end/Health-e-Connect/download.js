/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$.ajaxSetup({
    cache: false
});


jQuery(document).ready(function($) {
    
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
    
    //This function will update the status of the batch when the download link is clicked.
    $(document).on('click', '.downloadFile', function() {
        var batchId = $(this).attr('rel');
        var fileName = $(this).attr('rel1');
        var orgId = $(this).attr('rel2');
        
         $.ajax({
            url: '/Health-e-Connect/downloadBatch.do',
            type: 'POST',
            data: {'batchId': batchId},
            success: function(data) {
                
               //Need to rewrite the status of the clicked batch
               var statusHTML = '<a href="#statusModal" data-toggle="modal" class="btn btn-link viewStatus" rel="22" title="View this Status">SDL&nbsp;<span class="badge badge-help" data-placement="top" title="" data-original-title="">?</span></a>';
               $('#statusDiv'+batchId).html(statusHTML);
               
               window.location.href = "/downloadFile.do?filename="+fileName+"&foldername=output files&orgId="+orgId;
                
            }
        });
        
    });
});


