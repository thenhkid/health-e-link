<%-- 
    Document   : search
    Created on : Jun 13, 2016, 1:20:34 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/clients/search'/>">Clients</a></li>
                <li class="active">Search</li>
            </ol>
                
                
            <div id="searchError" class="alert alert-danger" role="alert" style="display:none;">
               At least one search criteria must be entered.
            </div> 

            <form:form id="clientsearchForm" action="/clients/search" role="form" class="form" method="post">
                
                <div class="col-md-12 form-section pull-left">

                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">Client Search Criteria</h2>
                        </div>
                        <div class="panel-body">
                            <div class="form-container scrollable">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="type">First Name</label>
                                        <input type="text" id="firstName" name="firstName" class="form-control" />
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="type">Last Name</label>
                                        <input type="text" id="lastName" name="lastName" class="form-control" />
                                    </div>
                                </div>
                                <div class="col-md-6" id="fieldDiv_dob">
                                    <div class="form-group">
                                        <label class="control-label" for="type">Date of Birth (mm/dd/yyyy)</label>
                                        <input type="text" id="dob" name="dob" class="form-control" />
                                    </div>
                                    <span id="errorMsg_dob" class="control-label"></span>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="control-label" for="type">Postal Code</label>
                                        <input type="text" id="postalCode" name="postalCode" class="form-control" />
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

