<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentBucket" value="0" />

<div class="main clearfix" role="main">

	<div class="col-md-12">
		<c:choose>
			<c:when test="${not empty savedStatus}" >
				<c:choose>
					<c:when test="${savedStatus == 'updated'}"><div class="alert alert-success"><strong>Success!</strong> The message type data translations have been successfully updated!</div></c:when>
					<c:when test="${savedStatus == 'created'}"><div class="alert alert-success"><strong>Success!</strong> The crosswalk has been successfully created!</div></c:when>
					<c:when test="${savedStatus == 'error'}"><div class="alert alert-danger"><strong>Error!</strong> The uploaded crosswalk did not have the correct delimiter!</div></c:when>
				</c:choose>
			</c:when>
			<c:when test="${not empty param.msg}" >
				<div class="alert alert-success">
					<strong>Success!</strong> 
					<c:choose>
						<c:when test="${param.msg == 'updated'}">The data translations have been successfully updated!</c:when>
						<c:when test="${param.msg == 'created'}">The crosswalk has been successfully added!</c:when>
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
								<c:if test="${currentBucket != fields[fStatus.index].bucketNo}">
									<c:if test="${currentBucket > 0}"></optgroup></c:if>
									<c:set var="currentBucket" value="${fields[fStatus.index].bucketNo}" />
									<c:choose>
										<c:when test="${currentBucket == 1}">
											<optgroup label="Bucket 1 (Sender Information)">
										</c:when>
										<c:when test="${currentBucket == 2}">
											<optgroup label="Bucket 2 (Recipient Information)">
										</c:when>
										<c:when test="${currentBucket == 3}">
											<optgroup label="Bucket 3 (Patient Information)">
										</c:when>
										<c:when test="${currentBucket == 4}">
											<optgroup label="Bucket 4 (Other)">
										</c:when>
									</c:choose>
								</c:if>
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
				<div id="translationMsgDiv"  rel="${id}" class="alert alert-danger" style="display:none;">
					<strong>You must click SAVE above to submit the data translations listed below!</strong>
				</div>
				<div class="form-container scrollable" id="existingTranslations"></div>
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
	    populateExistingTranslations(0);
	});

	function populateExistingTranslations(reload) {
		$.ajax({  
	        url: 'getTranslations.do?reload='+reload,  
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
			var messageTypeId = $('#translationMsgDiv').attr('rel');

			$.ajax({  
		        url: 'translations?id='+messageTypeId,  
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

	    	$('#crosswalkNameDiv').removeClass("has-error");
			$('#crosswalkNameMsg').removeClass("has-error");
			$('#crosswalkNameMsg').html('');
			$('#crosswalkDelimDiv').removeClass("has-error");
			$('#crosswalkDelimMsg').removeClass("has-error");
			$('#crosswalkDelimMsg').html('');
			$('#crosswalkFileDiv').removeClass("has-error");
			$('#crosswalkFileMsg').removeClass("has-error");
			$('#crosswalkFileMsg').html('');

		    var errorFound = 0;
		    var actionValue = $(this).attr('rel').toLowerCase();

		    //Make sure a title is entered
		    if($('#name').val() == '') {
		    	$('#crosswalkNameDiv').addClass("has-error");
				$('#crosswalkNameMsg').addClass("has-error");
				$('#crosswalkNameMsg').html('The crosswalk name is a required field!');
				errorFound = 1; 
			}

			//Need to make sure the crosswalk name doesn't already exist.
			$.ajax({  
		        url: 'checkCrosswalkName.do',  
		        type: "POST", 
		        async: false,  
		        data: { 'name' : $('#name').val(), 'orgId' : 0 },
		        success: function(data) {  
		           if(data == 1) {
		        		$('#crosswalkNameDiv').addClass("has-error");
						$('#crosswalkNameMsg').addClass("has-error");
						$('#crosswalkNameMsg').html('The name entered is already associated with another crosswalk in the system!');
						errorFound = 1;
				  }        
		        }  
		    }); 

		    //Make sure a delimiter is selected
		    if($('#delimiter').val() == '') {
		    	$('#crosswalkDelimDiv').addClass("has-error");
				$('#crosswalkDelimMsg').addClass("has-error");
				$('#crosswalkDelimMsg').html('The file delimiter is a required field!');
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
				        $('#translationMsgDiv').show();
			            $("#existingTranslations").html(data);   
			            //Need to clear out the select boxes
			            $('#field option:eq("")').prop('selected',true);  
			            $('#crosswalk option:eq("")').prop('selected',true);      
			        }  
			    });
			}
		});
		
	    //Function that will handle changing a process order and
		//making sure another field does not have the same process 
		//order selected. It will swap display position
		//values with the requested position.
		$(document).on('change','.processOrder', function() {
			//Store the current position
			var currDspPos = $(this).attr('rel');
			var newDspPos = $(this).val();

			$('.processOrder').each(function() {
				if($(this).val() == newDspPos) {
					//Need to update the saved process order
					$.ajax({  
				        url: 'updateTranslationProcessOrder?currProcessOrder='+currDspPos+'&newProcessOrder='+newDspPos,  
				        type: "POST",  
				        success: function(data) { 
				        	$('#translationMsgDiv').show();
				        	populateExistingTranslations(1);
				        }  
				    });
					$(this).val(currDspPos);
					$(this).attr('rel',currDspPos);
				}
			});

			$(this).val(newDspPos);
			$(this).attr('rel',newDspPos);
			
		});

		//Function that will handle removing a line item from the
		//existing data translations. Function will also update the
		//processing orders for each displayed.
		$(document).on('click','.removeTranslation',function() {
			var currPos = $(this).attr('rel2');
			var fieldId = $(this).attr('rel');
			
			//Need to remove the translation
			$.ajax({  
		        url: 'removeTranslations?fieldId='+fieldId+'&processOrder='+currPos,  
		        type: "POST",  
		        success: function(data) { 
		        	$('#translationMsgDiv').show();
		        	populateExistingTranslations(1);
		        }  
		    });
			
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