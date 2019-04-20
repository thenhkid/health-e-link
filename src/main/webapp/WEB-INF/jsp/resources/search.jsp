<%-- 
    Document   : search
    Created on : Jun 13, 2016, 1:20:34 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/resources/search'/>">Resources</a></li>
                <li class="active">Search</li>
            </ol>
                
                
            <div id="searchError" class="alert alert-danger" role="alert" style="display:none;">
               At least one search criteria must be entered.
            </div> 

            <form:form id="resourcesearchForm" action="/resources/search" role="form" class="form" method="post">
                
                <div class="col-md-12 form-section pull-left">

                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">Resource Search Criteria</h2>
                        </div>
                        <div class="panel-body">
                            <div class="form-container scrollable">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="programType">Program Type</label>
                                        <select id=programType" name="programType" class="form-control">
                                            <option value="">- Select -</option>
                                            <c:forEach items="${messageTypes}" var="messageType">
                                                <c:if test="${!fn:contains(messageType.name, 'Feedback Report')}">
                                                    <option value="${messageType.id}">${messageType.dspName}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="town">Town</label>
                                        <input type="text" id="town" name="town" class="form-control" />
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="county">County</label>
                                        <input type="text" id="county" name="county" class="form-control" />
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="state">State</label>
                                        <select id=state" name="state" class="form-control">
                                            <option value="">- Select -</option>
                                            <c:forEach items="${stateList}" var="state">
                                                <option value="${state.getKey()}">${state.getKey()}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>  
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="postalCode">Postal Code</label>
                                        <input type="text" id="postalCode" name="postalCode" class="form-control" />
                                    </div>
                                </div>  
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="setting">Setting</label>
                                        <select id=setting" name="setting" class="form-control">
                                            <option value="">- Select -</option>
                                            <option value="">All</option>
                                            <option value="">Hospital</option>
                                            <option value="">Community Health Center</option>
                                            <option value="">Primary Care Clinic</option>
                                            <option value="">Other</option>
                                        </select>
                                    </div>
                                </div> 
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="populations">Populations Serviced/Specialty Focus</label>
                                        <select id=populations" name="populations" class="form-control">
                                            <option value="">- Select -</option>
                                            <option value="">All</option>
                                            <option value="">All Adults</option>
                                            <option value="">Young Adults Specialty</option>
                                            <option value="">Adolescents/Children</option>
                                            <option value="">Homeless Specialty</option>
                                            <option value="">Pregnant or Parenting Women Specialty</option>
                                            <option value="">Military and Veterans Specialty</option>
                                            <option value="">HGender Specific Men or Women Specialty</option>
                                        </select>
                                    </div>
                                </div> 
                                 <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="paymentMethods">Payment Methods</label>
                                        <select id=paymentMethods" name="paymentMethods" class="form-control">
                                            <option value="">- Select -</option>
                                            <option value="">All</option>
                                            <option value="">DHHS-supported</option>
                                            <option value="">Sliding Fee Scale</option>
                                            <option value="">Medicaid/Medicare/Expanded Medicaid</option>
                                            <option value="">Private Insurers</option>
                                            <option value="">TriCare</option>
                                            <option value="">Self Pay</option>
                                        </select>
                                    </div>
                                </div> 
                            </div>
                        </div>
                    </section>
                    <input type="button" id="search" class="btn btn-primary search" value="Search"/>                     
                </div>

            </form:form>    

        </div>
    </div>
</div>

