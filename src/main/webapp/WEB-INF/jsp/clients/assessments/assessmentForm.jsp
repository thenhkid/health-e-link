<%-- 
    Document   : assessmentForm
    Created on : Jun 13, 2016, 9:56:52 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="date" class="java.util.Date" />

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

           <ol class="breadcrumb">
                <li><a href="<c:url value='/clients/search'/>">Clients</a></li>
                <li><a href="<c:url value='/clients/search'/>">Search</a></li>
                <li class="active">Chronic Disease Risk Assessment</li>
            </ol>
                
            <form:form id="assessmentForm" action="/clients/assessments/assessmentForm" role="form" class="form" method="post">
                <input type="hidden" name="clientId" value="${clientDetails.id}" />
                
                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Client Information:</h4></div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="firstName">First Name</label>
                                            <input disabled value="${clientDetails.firstName}" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="middleName">Middle Name</label>
                                            <input disabled value="${clientDetails.middleName}" class="form-control" type="text" />
                                        </div>
                                    </div> 
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="lastName">Last Name</label>
                                            <input disabled value="${clientDetails.lastName}" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="suffix">Suffix</label>
                                            <select class="form-control half">
                                                <option value=""></option>
                                            </select>
                                        </div>
                                    </div> 
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="DOB">DOB</label>
                                            <fmt:formatDate value="${clientDetails.DOB}" type="date" var="dobFormatted" pattern="MM/dd/yyyy" />
                                            <input disabled value="${dobFormatted}" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="line1">Address 1</label>
                                            <input disabled value="${clientDetails.line1}" class="form-control" type="text" />
                                        </div>
                                    </div>  
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="line2">Address 2</label>
                                            <input disabled value="${clientDetails.line2}" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="city">City</label>
                                            <input disabled value="${clientDetails.city}" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="state">State</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <c:forEach items="${stateList}" var="state">
                                                    <option value="${state.getKey()}" <c:if test="${clientDetails.state == state.getKey()}">selected</c:if>>${state.getKey()}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="postalCode">Postal Code</label>
                                            <input disabled value="${clientDetails.postalCode}" class="form-control" type="text" />
                                        </div>
                                    </div> 
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="phone1">Home Phone</label>
                                            <input disabled value="${clientDetails.phone1}" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="phone2">Mobile Phone</label>
                                            <input disabled value="${clientDetails.phone2}" class="form-control" type="text" />
                                        </div>
                                    </div>   
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="gender">Gender</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="1" <c:if test="${clientDetails.genderId == 1}">selected</c:if>>Male</option>
                                                <option value="2" <c:if test="${clientDetails.genderId == 2}">selected</c:if>>Female</option>
                                                <option value="3" <c:if test="${clientDetails.genderId == 3}">selected</c:if>>Transgender</option>
                                                <option value="4" <c:if test="${clientDetails.genderId == 4}">selected</c:if>>Unknown or Refused to Report</option>
                                            </select>
                                        </div>
                                    </div>  
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="language">Language</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="124" <c:if test="${clientDetails.primaryLanguageId == 124}">selected</c:if>>English</option>
                                                <option value="406" <c:if test="${clientDetails.primaryLanguageId == 406}">selected</c:if>>Spanish</option>
                                                <option value="386" <c:if test="${clientDetails.primaryLanguageId == 386}">selected</c:if>>American Sign</option>
                                                <option value="20" <c:if test="${clientDetails.primaryLanguageId == 20}">selected</c:if>>Arabic</option>
                                                <option value="61" <c:if test="${clientDetails.primaryLanguageId == 61}">selected</c:if>>Bosnian</option>
                                                <option value="485" <c:if test="${clientDetails.primaryLanguageId == 485}">selected</c:if>>Cape Verdean Creole</option>
                                                <option value="83" <c:if test="${clientDetails.primaryLanguageId == 83}">selected</c:if>>Chinese</option>
                                                <option value="140" <c:if test="${clientDetails.primaryLanguageId == 140}">selected</c:if>>French</option>
                                                <option value="169" <c:if test="${clientDetails.primaryLanguageId == 169}">selected</c:if>>Haitian Creole</option>
                                                <option value="74" <c:if test="${clientDetails.primaryLanguageId == 74}">selected</c:if>>Khmer</option>
                                                <option value="247" <c:if test="${clientDetails.primaryLanguageId == 247}">selected</c:if>>Lao</option>
                                                <option value="351" <c:if test="${clientDetails.primaryLanguageId == 351}">selected</c:if>>Polish</option>
                                                <option value="352" <c:if test="${clientDetails.primaryLanguageId == 352}">selected</c:if>>Portuguese</option>
                                                <option value="365" <c:if test="${clientDetails.primaryLanguageId == 365}">selected</c:if>>Russian</option>
                                                <option value="398" <c:if test="${clientDetails.primaryLanguageId == 398}">selected</c:if>>Somali</option>
                                                <option value="460" <c:if test="${clientDetails.primaryLanguageId == 460}">selected</c:if>>Vietnamese</option
                                                <option value="454" <c:if test="${clientDetails.primaryLanguageId == 454}">selected</c:if>>Other/Unknown</option>
                                            </select>
                                        </div>
                                    </div>  
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="ethnicity">Ethnicity</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="1" <c:if test="${clientDetails.hispanicId == 1}">selected</c:if>>Hispanic or Latino</option>
                                                <option value="2" <c:if test="${clientDetails.hispanicId == 2}">selected</c:if>>Not Hispanic or Latino</option>
                                                <option value="3" <c:if test="${clientDetails.hispanicId == 3}">selected</c:if>>Unknown or Refused to Report</option>
                                            </select>
                                        </div>
                                    </div>  
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="race">Race</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="3" <c:if test="${clientDetails.raceId == 3}">selected</c:if>>American Indian or Alaska Native</option>
                                                <option value="4" <c:if test="${clientDetails.raceId == 4}">selected</c:if>>Asian</option>
                                                <option value="2" <c:if test="${clientDetails.raceId == 2}">selected</c:if>>Black or African American</option>
                                                <option value="5" <c:if test="${clientDetails.raceId == 5}">selected</c:if>>Native Hawaiian or Other Pacific Islander</option>
                                                <option value="1" <c:if test="${clientDetails.raceId == 1}">selected</c:if>>White</option>
                                                <option value="7" <c:if test="${clientDetails.raceId == 7}">selected</c:if>>More than one Race</option>
                                                <option value="6" <c:if test="${clientDetails.raceId == 6}">selected</c:if>>Unknown or Refused to Report</option>
                                            </select>
                                        </div>
                                    </div>             
                                </div>  
                                <div class="form-section row">
                                    <div class="col-md-12"><h4 class="form-section-heading">Assessment Details: </h4></div>
                                    
                                    <div class="col-md-12">
                                        <div class="form-group" >
                                            <label class="control-label" for="healthRating">1. In general, how would you rate your health? </label>
                                            <br />
                                            <input name="healthRating" type="radio" /> <span  style="margin-right:10px;">Excellent</span>
                                            <input name="healthRating" type="radio" /> <span  style="margin-right:10px;">Very Good</span>
                                            <input name="healthRating" type="radio" /> <span  style="margin-right:10px;">Good</span>
                                            <input name="healthRating" type="radio" /> <span  style="margin-right:10px;">Fair</span>
                                            <input name="healthRating" type="radio" /> <span  style="margin-right:10px;">Poor</span>
                                        </div>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="exerciseMinutes">2. In the last 7 days, how often did you exercise for at least 20 minutes in a day?</label>
                                            <br />
                                            <input name="exerciseMinutes" type="radio" /> <span  style="margin-right:10px;">Every day</span>
                                            <input name="exerciseMinutes" type="radio" /> <span  style="margin-right:10px;">3-6 days</span>
                                            <input name="exerciseMinutes" type="radio" /> <span  style="margin-right:10px;">1-2 days</span>
                                            <input name="exerciseMinutes" type="radio" /> <span  style="margin-right:10px;">0 days</span>
                                        </div>
                                        <blockquote>
                                            <h5>Exercise includes walking, housekeeping, jogging, weights, a sport or playing with your kids. It can be done on the job, around the house, just for fun or as a work-out.</h5>
                                        </blockquote>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="servingsofFruit">3. In the last 7 days, how often did you eat 3 or more servings of fruits or vegetables in a day?</label>
                                            <br />
                                            <input name="servingsofFruit" type="radio" /> <span  style="margin-right:10px;">Every day</span>
                                            <input name="servingsofFruit" type="radio" /> <span  style="margin-right:10px;">3-6 days</span>
                                            <input name="servingsofFruit" type="radio" /> <span  style="margin-right:10px;">1-2 days</span>
                                            <input name="servingsofFruit" type="radio" /> <span  style="margin-right:10px;">0 days</span>
                                        </div>
                                        <blockquote>
                                            <h5>Each time you ate a fruit or vegetable counts as one serving. It can be fresh, frozen, canned, cooked or mixed with other foods.</h5>
                                        </blockquote>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="alcoholicdrinks">4. In the last 7 days, how often did you have (5 or more for men, 4 or more for women) alcoholic drinks at one time?</label>
                                            <br />
                                            <input name="alcoholicdrinks" type="radio" /> <span  style="margin-right:10px;">Never</span>
                                            <input name="alcoholicdrinks" type="radio" /> <span  style="margin-right:10px;">Once a week</span>
                                            <input name="alcoholicdrinks" type="radio" /> <span  style="margin-right:10px;">2-3 times a week</span>
                                            <input name="alcoholicdrinks" type="radio" /> <span  style="margin-right:10px;">More than 3 times during the week</span>
                                        </div>
                                        <blockquote>
                                            <h5>1 drink is 1 beer, 1 glass of wine, or 1 shot.</h5>
                                        </blockquote>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="tobacco" style="margin-right:10px;">5. In the last 30 days have you smoked or used tobacco?</label>
                                            <input name="tobacco" type="radio" /> <span  style="margin-right:10px;">Yes</span>
                                            <input name="tobacco" type="radio" /> <span  style="margin-right:10px;">No</span>
                                        </div>
                                        <div class="form-group" style="margin-left:20px;" >
                                            <label class="control-label" for="wanttoquit" style="color:green">If YES, Do you want to quit smoking or using tobacco?</label>
                                            <br />
                                            <input name="wanttoquit" type="radio" /> <span  style="margin-right:10px;">Yes</span>
                                            <input name="wanttoquit" type="radio" /> <span  style="margin-right:10px;">I am working on quitting or cutting back right now</span>
                                            <input name="wanttoquit" type="radio" /> <span  style="margin-right:10px;">No</span>
                                        </div>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="depressed">6. In the last 30 days, how often have you felt tense, anxious or depressed?</label>
                                            <br />
                                            <input name="depressed" type="radio" /> <span  style="margin-right:10px;">Almost every day</span>
                                            <input name="depressed" type="radio" /> <span  style="margin-right:10px;">Sometimes</span>
                                            <input name="depressed" type="radio" /> <span  style="margin-right:10px;">Rarely</span>
                                            <input name="depressed" type="radio" /> <span  style="margin-right:10px;">Never</span>
                                        </div>
                                    </div>
                                    <div class="col-md-12"><h4 class="form-section-heading">Member Results: </h4></div>
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="bloodPressure">Blood Pressure (xxx/xxx mmHg)</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group" >
                                            <label class="control-label" for="hypertension">Patient diagnosed with hypertension?</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                        </div>
                                    </div> 
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="height">Height (inches)</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                       <div class="form-group" >
                                            <label class="control-label" for="weight">Weight (lbs.)</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div> 
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="bmi">BMI (xx.x)</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-12 cb">
                                       <div class="form-group" >
                                            <label class="control-label" for="hypertension">In the context of all relevant clinical factors, does this BMI indicate need for weight management?</label>
                                            <select class="form-control half">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                        </div>
                                    </div> 
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="tobaccousestatus">Tobacco Use Status</label>
                                            <br />
                                            <input name="tobaccousestatus" type="checkbox" /> <span  style="margin-right:10px;">Never used tobacco</span>
                                            <input name="tobaccousestatus" type="checkbox" /> <span  style="margin-right:10px;">Previous tobacco user</span>
                                            <input name="tobaccousestatus" type="checkbox" /> <span  style="margin-right:10px;">Current tobacco cessation</span>
                                            <input name="tobaccousestatus" type="checkbox" /> <span  style="margin-right:10px;">Starting tobacco cessation</span>
                                            <input name="tobaccousestatus" type="checkbox" /> <span  style="margin-right:10px;">Tobacco user</span>
                                        </div>
                                    </div>
                                    <div class="col-md-6 cb">
                                       <div class="form-group" >
                                            <label class="control-label" for="cholesterolknown">Cholesterol known?</label>
                                            <select class="form-control">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                        </div>
                                     </div>
                                     <div class="col-md-6 ">    
                                        <div class="form-group" >
                                            <label class="control-label" for="highcholesterol">Patient diagnosed with high cholesterol?</label>
                                            <select class="form-control">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                        </div>
                                    </div> 
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="cholesterol"><strong>If cholesterol known is Yes:</strong></label>
                                        </div>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="cholesterol">Date of most recent test results:</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="cholesterol">Total cholesterol:</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                       <div class="form-group" >
                                            <label class="control-label" for="weight">LDL</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div> 
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="cholesterol">HDL:</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                       <div class="form-group" >
                                            <label class="control-label" for="weight">Triglycerides:</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div> 
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="nocholesterol"><strong>If cholesterol known is No:</strong></label>
                                            <br />
                                            <input name="cholesterol" type="radio" /> <span  style="margin-right:10px;">Screening not recommended</span>
                                            <input name="cholesterol" type="radio" /> <span  style="margin-right:10px;">Screening Ordered</span>
                                        </div>
                                    </div>
                                    <div class="col-md-6 cb">
                                       <div class="form-group" >
                                            <label class="control-label" for="bloodsugarknown">Blood sugar known?</label>
                                            <select class="form-control">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                        </div>
                                     </div>
                                     <div class="col-md-6 ">    
                                        <div class="form-group" >
                                            <label class="control-label" for="diabetes">Patient diagnosed with diabetes?</label>
                                            <select class="form-control">
                                                <option value="" label=" - Select - " ></option>
                                                <option value="Yes">Yes</option>
                                                <option value="No">No</option>
                                            </select>
                                        </div>
                                    </div> 
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="cholesterol"><strong>If blood sugar known is Yes:</strong></label>
                                        </div>
                                    </div>
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="cholesterol">Date of most recent test results:</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="FBS">FBS (xxx mg/dl):</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                       <div class="form-group" >
                                            <label class="control-label" for="A1C">A1C (xx.x%):</label>
                                            <input value="" class="form-control" type="text" />
                                        </div>
                                    </div> 
                                    <div class="col-md-12 cb">
                                        <div class="form-group" >
                                            <label class="control-label" for="noknownbloodsugar"><strong>If blood sugar known is No:</strong></label>
                                            <br />
                                            <input name="noknownbloodsugar" type="radio" /> <span  style="margin-right:10px;">Screening not recommended</span>
                                            <input name="noknownbloodsugar" type="radio" /> <span  style="margin-right:10px;">Screening Ordered</span>
                                        </div>
                                    </div>
                                     
                                </div>
                            </div>
                        </div>
                        <input type="button" id="search" class="btn btn-primary submit" value="Submit Assessment"/> 
                        <input type="button" id="cancel" class="btn btn-danger  cancel" value="Cancel Assessment"/> 
                    </div>
                </form:form>
            </div>
        </div>
    </div>
 </div>
