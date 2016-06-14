<%-- 
    Document   : assessmentConclusion
    Created on : Jun 13, 2016, 11:27:33 PM
    Author     : chadmccue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form id="enrollClientForm" action="/clients/assessments/enrollClientIntoPrograms" role="form" class="form" method="post">
     <input type="hidden" id="selectedPrograms" name="selectedPrograms" value="" />
     <input type="hidden" name="clientId" value="${clientId}" />
</form:form>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

           <ol class="breadcrumb">
                <li><a href="<c:url value='/clients/search'/>">Clients</a></li>
                <li><a href="<c:url value='/clients/search'/>">Search</a></li>
                <li class="active">Chronic Disease Risk Assessment Conclusion</li>
            </ol>
            
            <h4>Chronic Disease Risk Assessment Conclusion</h4>
            <p>
                Based on the patients chronic disease risk assessment submission we recommend the patient 
                be referred for the following programs.
                <ul>
                    <li>Diabetes Prevention</li>
                    <li>CDSMP</li>
                    <li>Nutrition</li>
                    <li>Exercise</li>
                </ul>
            </p>
            <p>
               To start a referral please select each of the programs you wish to enroll the patient in. Along with
               selecting a program please also select where the patient would be enrolled.
            </p>
            <div>
                 <table class="table table-striped table-hover table-default" >
                    <thead>
                        <tr>
                            <th scope="col">Program</th>
                            <th scope="col" >Location</th>
                            <th scope="col" class="center-text">Refer?</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td scope="row">Diabetes Prevention</td>
                            <td class="center-text">
                               <select id="targetOrg_9" class="form-control">
                                   <option value="10-27" selected="">YMCA in Columbia</option>
                                </select> 
                            </td>
                            <td class="actions-col center-text" >
                               <input type="checkbox" name="configs" class="configs" value="9" />
                            </td>
                        </tr>
                        <tr>
                            <td scope="row">CDSMP</td>
                            <td class="center-text">
                               <select id="targetOrg_1" class="form-control">
                                    <option value="">- Select Location -</option>
                                </select> 
                            </td>
                             <td class="actions-col center-text" >
                               <input type="checkbox" name="configs" class="configs" value="1" />
                            </td>
                        </tr> 
                        <tr>
                            <td scope="row">Nutrition</td>
                            <td class="center-text">
                               <select id="targetOrg_2" class="form-control">
                                    <option value="">- Select Location -</option>
                                </select> 
                            </td>
                            <td class="actions-col center-text" >
                               <input type="checkbox" name="configs" class="configs" value="2" />
                            </td>
                        </tr> 
                        <tr>
                            <td scope="row">Exercise</td>
                            <td class="center-text">
                               <select id="targetOrg_3" class="form-control">
                                    <option value="">- Select Location -</option>
                                </select> 
                            </td>
                            <td class="actions-col center-text" >
                               <input type="checkbox" name="configs" class="configs" value="3" />
                            </td>
                        </tr> 
                    </tbody>
                </table>
            </div>
            <input type="button" id="enrollClients" class="btn btn-primary" value="Create Referrals"/>
        </div>
    </div>
</div>
