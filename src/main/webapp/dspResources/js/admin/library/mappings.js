
require(['./main'], function() {
    require(['jquery'], function($) {


        $("input:text,form").attr("autocomplete", "off");

        //Fade out the updated/created message after being displayed.
        if ($('.alert').length > 0) {
            $('.alert').delay(2000).fadeOut(1000);
        }

        //Function that will display the new field module
        $(document).on('click', '#addNewField', function() {
            var bucketNo = $(this).attr('rel');
            var displayPos = $('.dspPos_' + bucketNo + ':last').val();
            var fieldNo = $('#maxFieldNo').val();

            $.ajax({
                url: 'addNewField',
                type: "GET",
                data: {'bucketNo': bucketNo, 'displayPOS': displayPos, 'maxfieldNo': fieldNo},
                success: function(data) {
                    $("#newFieldModal").html(data);
                }
            });
        });

        //Function that will handle changing a display position and
        //making sure another field in the same bucket does not have
        //the same position selected. It will swap display position
        //values with the requested position.
        $('.dspPos').change(function() {
            //Store the current position
            var currDspPos = $(this).attr('rel2');
            var bucketVal = $(this).attr('rel');
            var newDspPos = $(this).val();

            $('.dspPos_' + bucketVal).each(function() {
                if ($(this).attr('rel2') == newDspPos) {
                    $(this).val(currDspPos);
                    $(this).attr('rel2', currDspPos);
                }
            });

            $(this).val(newDspPos);
            $(this).attr('rel2', newDspPos);

        });

        //Loop through all selected table names to populate the columns
        var currTableName = "";
        var currTableCols = [];

        $('.tableName').each(function() {
            var row = $(this).attr('rel');
            var tableName = $(this).val();
            if (tableName !== "") {

                if ((currTableName == "" || tableName != currTableName)) {
                    currTableName = tableName;

                    currTableCols = populateTableColumns(tableName);
                }

                var colVal = $('#tableCols' + row).attr('rel2');

                var html = '<option value="">- Select - </option>';
                var len = currTableCols.length;
                for (var i = 0; i < len; i++) {
                    if (colVal == currTableCols[i]) {
                        html += '<option value="' + currTableCols[i] + '" selected>' + currTableCols[i] + '</option>';
                    }
                    else {
                        html += '<option value="' + currTableCols[i] + '">' + currTableCols[i] + '</option>';
                    }
                }
                $('#tableCols' + row).html(html);
            }
        });

        var currAutoTableName = "";
        var currAutoTableCols = [];

        //Loop through all selected table names to populate the columns
        $('.autoPopulatetableName').each(function() {
            var row = $(this).attr('rel');
            var tableName = $(this).val();
            if (tableName !== "") {

                if ((currAutoTableName == "" || tableName != currAutoTableName)) {
                    currAutoTableName = tableName;

                    currAutoTableCols = populateAutoTableColumns(tableName);
                }

                var colVal = $('#autoPopulatetableCols' + row).attr('rel2');

                var html = '<option value="">- Select - </option>';
                var len = currAutoTableCols.length;
                for (var i = 0; i < len; i++) {
                    if (colVal == currAutoTableCols[i]) {
                        html += '<option value="' + currAutoTableCols[i] + '" selected>' + currAutoTableCols[i] + '</option>';
                    }
                    else {
                        html += '<option value="' + currAutoTableCols[i] + '">' + currAutoTableCols[i] + '</option>';
                    }
                }
                $('#autoPopulatetableCols' + row).html(html);

            }
        });

        //Need to populate the table columns or the selected table
        $(document).on('change', '.tableName', function() {
            var row = $(this).attr('rel');
            var tableName = $(this).val();
            currTableCols = populateTableColumns(tableName);

            var colVal = $('#tableCols' + row).attr('rel2');

            var html = '<option value="">- Select - </option>';
            var len = currTableCols.length;
            for (var i = 0; i < len; i++) {
                if (colVal == currTableCols[i]) {
                    html += '<option value="' + currTableCols[i] + '" selected>' + currTableCols[i] + '</option>';
                }
                else {
                    html += '<option value="' + currTableCols[i] + '">' + currTableCols[i] + '</option>';
                }
            }
            $('#tableCols' + row).html(html);

        });

        //Need to populate the auto populate table columns or the selected table
        $(document).on('change', '.autoPopulatetableName', function() {
            var row = $(this).attr('rel');
            var tableName = $(this).val();
            currAutoTableCols = populateAutoTableColumns(tableName);

            var colVal = $('#autoPopulatetableCols' + row).attr('rel2');

            var html = '<option value="">- Select - </option>';
            var len = currAutoTableCols.length;
            for (var i = 0; i < len; i++) {
                if (colVal == currAutoTableCols[i]) {
                    html += '<option value="' + currAutoTableCols[i] + '" selected>' + currAutoTableCols[i] + '</option>';
                }
                else {
                    html += '<option value="' + currAutoTableCols[i] + '">' + currAutoTableCols[i] + '</option>';
                }
            }
            $('#autoPopulatetableCols' + row).html(html);


        });

        //This function will save the messgae type field mappings
        $('#saveDetails').click(function(event) {

            //Need to make sure all required fields are marked if empty.
            var errorsFound = 0;
            var row = 0;

            $('#mappingErrorMsgDiv').hide();

            //Check field labels
            $('.fieldLabel').each(function() {
                row = $(this).attr('rel');
                $('#fieldLabel_' + row).removeClass("has-error");
                $('#fieldLabelMsg_' + row).html("");
                if ($(this).val() == '') {
                    $('#fieldLabel_' + row).addClass("has-error");
                    $('#fieldLabelMsg_' + row).html("The field label is required!");

                    errorsFound = 1;

                }
            });

            //Check table name has been selected
            $('.tableName').each(function() {
                row = $(this).attr('rel');
                $('#tableName_' + row).removeClass("has-error");
                $('#tableNameMsg' + row).html("");
                if ($(this).val() == "") {
                    $('#tableName_' + row).addClass("has-error");
                    $('#tableNameMsg_' + row).html("The table is required!");

                    errorsFound = 1;
                }
            });

            //Check table column has been selected
            $('.tableCol').each(function() {
                row = $(this).attr('rel');
                $('#tableCol_' + row).removeClass("has-error");
                $('#tableColMsg_' + row).html("");

                if ($(this).val() == "") {
                    $('#tableCol_' + row).addClass("has-error");
                    $('#tableColMsg_' + row).html("The column is required!");

                    errorsFound = 1;
                }
            });


            if (errorsFound == 0) {

                /*$('body').overlay({
                 glyphicon : 'floppy-disk',
                 message : 'Saving...'
                 });*/

                var formData = $("#fieldMappings").serialize();

                $.ajax({
                    url: 'mappings',
                    data: formData,
                    type: "POST",
                    async: false,
                    success: function(data) {
                        if (data == 1) {
                            $('.mappingsUpdated').show();
                            $('.alert').delay(2000).fadeOut(1000);
                        }
                    }
                });
                event.preventDefault();
                return false;
                //$('#fieldMappings').submit();

            }
            else {
                $('#mappingErrorMsgDiv').show();
            }

        });


    });
});


//This functin will be used to populate the tableCols drop down.
//function takes in the name of the selected table name and the
//row it is working with.
function populateTableColumns(tableName) {

    var result = [];

    $.ajax({
        async: false,
        url: 'getTableCols.do',
        data: {'tableName': tableName},
        dataType: 'json',
        success: function(data) {
            result = data;
        }
    });

    return result;
}

//This function will be used to populate the autoPopulatetableCols drop down.
//function takes in the name of the selected table name and the
//row it is working with.


function populateAutoTableColumns(tableName) {

    var result = [];

    $.ajax({
        async: false,
        url: 'getTableCols.do',
        data: {'tableName': tableName},
        dataType: 'json',
        success: function(data) {
            result = data;
        }
    });

    return result;
}


