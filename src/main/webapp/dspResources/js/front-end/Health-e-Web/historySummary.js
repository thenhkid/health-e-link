/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require(['./main'], function() {
    require(['jquery'], function($) {

        $('.viewDetals').click(function() {
            $('#selorgId').val($(this).attr('rel'));
            $('#selmessageTypeId').val($(this).attr('rel2'));

            $('#viewHistoryDetails').submit();
        });

        $('.print').click(function() {
            window.print();
        });

        $('#dataTableReceived').dataTable({
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sSearch": "_INPUT_",
                "sLengthMenu": '<select class="form-control" style="width:150px">' +
                        '<option value="10">10 Records</option>' +
                        '<option value="20">20 Records</option>' +
                        '<option value="30">30 Records</option>' +
                        '<option value="40">40 Records</option>' +
                        '<option value="50">50 Records</option>' +
                        '<option value="-1">All</option>' +
                        '</select>'
            }
        });

    });
});

