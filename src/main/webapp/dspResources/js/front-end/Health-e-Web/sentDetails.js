$(document).ready(function() {
   
    //Function to handle the form actions
    $(document).on('change','#formAction',function() {
        
        if($(this).val() === 'print') {
            window.print();
        }
        
    })

});