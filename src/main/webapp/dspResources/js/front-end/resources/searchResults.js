/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function($) {
        
        $('.print').click(function() {
            window.print();
        });

       $(document).on('change', '.clientToDo', function() {
          
           var clientId = $(this).val().split("-")[0];
           var toDo = $(this).val().split("-")[1];
           
           if(toDo === "chronicDiseaseAssessment") {
               window.location.href="/clients/assessments/assessmentForm?clientId="+clientId+"&assessmentId=1";
           }
           else if(toDo === "createReferrals") {
               window.location.href="/CareConnector/create?clientId="+clientId;
           }
           
       });
       
       $(document).on('click', '#activationRequest', function() {
           $.ajax({
                url: '/resources/activationRequest',
                data: {'targetOrg': $(this).attr('rel')},
                type: "GET",
                success: function(data) {
                    $("#activationModal").html(data);
                }
           });
       });
       
    });
});      