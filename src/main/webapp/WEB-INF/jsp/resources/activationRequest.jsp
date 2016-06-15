<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h3 class="panel-title">Activation Request</h3>
            <c:if test="${not empty success}" >
                <div class="alert alert-success" role="alert">
                    <strong>${success}</strong> 
                </div>
            </c:if>
        </div>
        <div class="modal-body">
            <div class="row" style="padding-bottom:20px;">
                <div class="col-md-12">
                    <div class="col-md-6">
                        <p><strong>Requested By</strong></p>
                        <p>
                            ${userdetails.firstName} ${userdetails.lastName}<br />
                            ${userdetails.email} ${srcOrgDetails.address2}
                        </p>
                    </div>
                </div>
            </div>
            <div class="row" style="padding-bottom:20px;">
                <div class="col-md-12">
                    <div class="col-md-6">
                        <p><strong>Source Organization</strong></p>
                        <p>
                            ${srcOrgDetails.orgName}<br />
                            ${srcOrgDetails.address} ${srcOrgDetails.address2}<br />
                            ${srcOrgDetails.city} ${srcOrgDetails.state}, ${srcOrgDetails.postalCode}<br />
                            <c:if test="${not empty srcOrgDetails.phone}">
                                p: ${srcOrgDetails.phone}<br />
                            </c:if>
                            <c:if test="${not empty srcOrgDetails.fax}">
                                f: ${srcOrgDetails.fax}
                            </c:if>    
                        </p>
                    </div>
                     <div class="col-md-6">
                         <p><strong>Target Organization</strong></p>
                         <p>
                            ${tgtOrgDetails.orgName}<br />
                            ${tgtOrgDetails.address} ${tgtOrgDetails.address2}<br />
                            ${tgtOrgDetails.city} ${tgtOrgDetails.state}, ${tgtOrgDetails.postalCode}<br />
                            <c:if test="${not empty tgtOrgDetails.phone}">
                                p: ${tgtOrgDetails.phone}<br />
                            </c:if>
                            <c:if test="${not empty tgtOrgDetails.fax}">
                                f: ${tgtOrgDetails.fax}
                            </c:if>    
                        </p>
                    </div>
                </div>
            </div>
            <div class="row" style="padding-bottom:20px;">
                <div class="col-md-12">
                    <div class="col-md-12">
                        <p><strong>Additional Notes</strong></p>
                        <textarea rows="8" class="form-control"></textarea>
                    </div>
                </div>   
            </div>
            <div class="row" style="padding-bottom:20px;">
                <div class="col-md-12">
                    <div class="col-md-12">
                        <input type="button" id="sendRequest"  role="button" class="btn btn-primary" value="Send Request"/>
                    </div>
                </div>   
            </div>                
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $("input:text,form").attr("autocomplete", "off");
    });
    $(document).on('click', '#sendRequest', function() {
        $('.modal-body').html("<p>Your activation request has been sent.</p>"); 
     });
</script>
