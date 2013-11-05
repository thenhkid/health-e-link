
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="main clearfix" role="main">

	<div class="col-md-12">
		<c:choose>
			<c:when test="${not empty savedStatus}" >
				<div class="alert alert-success">
					<strong>Success!</strong> 
					<c:choose>
						<c:when test="${savedStatus == 'updated'}">The message type data translations have been successfully updated!</c:when>
					</c:choose>
				</div>
			</c:when>
			<c:when test="${not empty param.msg}" >
				<div class="alert alert-success">
					<strong>Success!</strong> 
					<c:choose>
						<c:when test="${param.msg == 'updated'}">The data translations have been successfully updated!</c:when>
						<c:when test="${param.msg == 'created'}">The crosswalk has been successfully added!</c:when>
						<c:when test="${param.msg == 'deleted'}">The address has been successfully removed!</c:when>
					</c:choose>
				</div>
			</c:when>
		</c:choose>
	</div>

	<div class="col-md-6">
	
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">New Data Translation</h3>
			</div>
			<div class="panel-body">
				<div class="form-container">
					<div class="form-group">
						<label class="control-label" for="fieldNumber">Field</label>
						<select id="field" class="form-control half">
							<option value="">- Select -</option>
							<c:forEach items="${fields}" var="field" varStatus="fStatus">
								<option value="${fields[fStatus.index].id}">${fields[fStatus.index].fieldDesc} </option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label" for="fieldNumber">Crosswalk</label>
						<select id="crosswalk" class="form-control half">
							<option value="">- Select -</option>
							<c:forEach items="${crosswalks}" var="cwalk" varStatus="cStatus">
								<option value="${crosswalks[cStatus.index].id}">${crosswalks[cStatus.index].name} </option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</section>
	</div>
	
	<!-- Existing Crosswalks -->
	<div class="col-md-6">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Available Crosswalks</h3>
			</div>
			<div class="panel-body">
				<div class="form-container scrollable">
					<a href="#crosswalkModal" id="createNewCrosswalk" data-toggle="modal" class="btn btn-primary btn-sm pull-right" title="Create a new Crosswalk">
						<span class="glyphicon glyphicon-plus"></span>
					</a>
					<div id="crosswalksTable"></div>
				</div>
			</div>
		</section>
	</div>

</div>
	
<div class="main clearfix" role="main">	
	<!-- Existing Translations -->
	<div class="col-md-12">
		<section class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Existing Data Translations</h3>
			</div>
			<div class="panel-body">
				<div class="form-container scrollable" id="existingTranslations">
				
				</div>
			</div>
		</section>
	</div>
	
</div>

<!-- Provider Address modal -->
<div class="modal fade" id="crosswalkModal" role="dialog" tabindex="-1" aria-labeledby="Message Crosswalks" aria-hidden="true" aria-describedby="Message Crosswalks"></div>


<script type="text/javascript">

	$(document).ready(function() {
	    $("input:text,form").attr("autocomplete","off");

	    populateCrosswalks(1);
	    populateExistingTranslations();
	});

	function populateExistingTranslations() {
		$.ajax({  
	        url: 'getTranslations.do',  
	        type: "GET",  
	        success: function(data) {  
	            $("#existingTranslations").html(data);           
	        }  
	    });  
	}

	function populateCrosswalks(page) {
		$.ajax({  
	        url: 'getCrosswalks.do?page='+page,  
	        type: "GET",  
	        success: function(data) {  
	            $("#crosswalksTable").html(data);           
	        }  
	    });  
	}

	$(function() {
		//Fade out the updated/created message after being displayed.
		if($('.alert').length >0) {
			$('.alert').delay(2000).fadeOut(1000);
		}

		//This function will get the next/prev page for the crosswalk list
		$(document).on('click', '.nextPage',function() {
			var page = $(this).attr('rel');
			populateCrosswalks(page);
		});

		//The function that will be called when the "Save" button
		//is clicked
		$('#saveDetails').click(function(event) {
			$.ajax({  
		        url: 'translations',  
		        type: "POST",
		        success: function(data) { 
			       window.location.href="translations?msg=updated";
		        }	
		    }); 
		});

		//This function will launch the crosswalk overlay with the selected
		//crosswalk details
		$(document).on('click','.viewCrosswalk',function() {
			$.ajax({  
		        url: 'viewCrosswalk'+$(this).attr('rel'),  
		        type: "GET",  
		        success: function(data) {  
		            $("#crosswalkModal").html(data);           
		        }  
		    });  

		});


		//This function will launch the new crosswalk overlay with a blank form
	    $(document).on('click','#createNewCrosswalk',function() {
	    	$.ajax({  
		        url: 'newCrosswalk',  
		        type: "GET",  
		        success: function(data) {  
		            $("#crosswalkModal").html(data);           
		        }  
		    });  
		});


	    $(document).on('click', '#submitCrosswalkButton',function(event) {
		    var errorFound = 0;
		    var actionValue = $(this).attr('rel').toLowerCase();

		    //Make sure a title is entered
		    if($('#name').val() == '') {
		    	$('#crosswalkNameDiv').addClass("has-error");
				$('#crosswalkNameMsg').addClass("has-error");
				$('#crosswalkNameMsg').html('The crosswalk name is a required field!');
				errorFound = 1; 
			}

		    //Make sure a file is selected and is a text file
		    if($('#crosswalkFile').val() == '' || $('#crosswalkFile').val().indexOf('.txt') == -1) {
				$('#crosswalkFileDiv').addClass("has-error");
				$('#crosswalkFileMsg').addClass("has-error");
				$('#crosswalkFileMsg').html('The crosswalk file must be a text file!');
				errorFound = 1;
			}

		    if(errorFound == 1) {
				event.preventDefault();
			    return false;
			}

		    $('#crosswalkdetailsform').attr('action',actionValue+'Crosswalk');
			$('#crosswalkdetailsform').submit();
		    
		});

	    //This function will handle populating the data translation table
	    //The trigger will be when a crosswalk is selected along with a
	    //field
	    $(document).on('change','#crosswalk', function() {
		    var selectedField = $('#field').val();
		    var selectedFieldText = $('#field').find(":selected").text();
		    var selectedCW = $(this).val();
		    var selectedCWText = $(this).find(":selected").text();

		    var url = 'setTranslations?f='+selectedField+'&fText='+selectedFieldText+'&cw='+selectedCW+'&CWText='+selectedCWText;

		    if(selectedField != "" && selectedCW != "") {
		    	$.ajax({  
			        url: url,  
			        type: "GET",  
			        success: function(data) {  
			            $("#existingTranslations").html(data);   
			            //Need to clear out the select boxes
			            $('#field option:eq("")').prop('selected',true);  
			            $('#crosswalk option:eq("")').prop('selected',true);      
			        }  
			    });
			}
		});
		
		
		
	});

	function removeVariableFromURL(url_string, variable_name) {
		var URL = String(url_string);
	    var regex = new RegExp( "\\?" + variable_name + "=[^&]*&?", "gi");
	    URL = URL.replace(regex,'?');
	    regex = new RegExp( "\\&" + variable_name + "=[^&]*&?", "gi");
	    URL = URL.replace(regex,'&');
	    URL = URL.replace(/(\?|&)$/,'');
	    regex = null;
	    return URL;
	}

</script>