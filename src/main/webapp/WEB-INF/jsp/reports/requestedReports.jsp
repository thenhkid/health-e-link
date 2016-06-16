<%-- 
    Document   : requestedReports
    Created on : Jun 16, 2016, 10:09:24 AM
    Author     : chadmccue
--%>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">
            <ol class="breadcrumb">
                <li><a href="<c:url value='/profile'/>">My Account</a></li>
                <li><a href="#">Reports</a></li>
                <li class="active">Requested Reports</li>
            </ol>
           
            <div class="form-container scrollable">
               <div class="date-range-picker-trigger form-control pull-right daterange" style="width:265px; margin-left: 10px;">
                    <i class="glyphicon glyphicon-calendar"></i>
                    <span class="date-label"  rel="" rel2="">6/1/2016 - 6/30/2016</span> <b class="caret"></b>
                </div>
                <table class="table table-hover table-default" id="dataTable">
                    <caption style="display:none">Inbox</caption>
                    <thead>
                        <tr>
                            <th scope="col">Report</th>
                            <th scope="col" class="center-text">Date Requested</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td colspan="3" class="center-text">There are no requested reports.</td></tr>
                       </c:choose>                  
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
