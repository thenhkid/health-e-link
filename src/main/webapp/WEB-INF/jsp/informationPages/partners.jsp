<%-- 
    Document   : contact
    Created on : Mar 13, 2014, 12:50:01 PM
    Author     : chadmccue
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div role="main">

    <div class="container main-container">
        <div class="row">
            <aside class="col-md-3 col-sm-3 secondary hidden-xs">
                <div class="fixed-region">
                    <div class="module sidebar-module">
                        <dl class="vcard info-list">
                            <dt>Contact Us By Phone</dt>
                            <dd class="tel">(508) 721-1977</dd>

                            <dt>Contact Us By Fax</dt>
                            <dd class="tel">(508) 721-1978</dd>

                            <dt>Contact Us By Email</dt>
                            <dd class="email"><a href="" title="">info@health-e-link.net</a></dd>

                        </dl>
                    </div>
                </div>
            </aside>

            <div class="col-md-9 col-md-offset-0 col-sm-8 col-sm-offset-1 page-content">

                <c:if test="${not empty sent}">
                    <div id="translationMsgDiv"  class="alert alert-success">
                        <strong>Your partner request form has been submitted!</strong>
                    </div>
                </c:if>

                <h2>Partner Request Form</h2>

                <p>
                    Health-e-link partners with leaders in healthcare information technology applications and consulting to provide modular solutions that expand their capacity to provide data 
                    exchange solutions to their current customers. For more information about how the Health-e-Link Suite of products can solve your integration needs, please complete and send the form below. 
                </p>
                <form:form id="partnerForm" method="post" role="form">
                    <div class="form-container">
                        <div id="fieldDiv_name" class="form-group">
                            <label for="name" class="control-label">Name *</label>
                            <input id="name" name="name" class="form-control required" type="text" />
                            <span id="errorMsg_name" class="control-label"></span> 
                        </div>
                        <div id="fieldDiv_company" class="form-group">
                            <label for="company" class="control-label">Company / Organization *</label>
                            <input id="company" name="company" class="form-control required" type="text" />
                            <span id="errorMsg_company" class="control-label"></span> 
                        </div>
                        <div class="form-group">
                            <label for="title" class="control-label">Title</label>
                            <input id="title" name="title" class="form-control" type="text" />
                        </div>
                        <div class="form-group">
                            <label for="URL" class="control-label">URL</label>
                            <input id="URL" name="URL" class="form-control" type="text" />
                        </div>
                        <div class="form-group">
                            <label for="address">Address</label>
                            <input id="address" name="address" class="form-control" type="text" />
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-xs-3">
                                    <label for="city">City</label>
                                    <input id="city" name="city" class="form-control" type="text" />
                                </div>
                                <div class="col-xs-2">
                                    <label for="state">State </label>
                                    <input id="state" name="state" class="form-control" type="text" />
                                </div>
                                <div class="col-xs-2">
                                    <label for="zip">Zip</label>
                                    <input id="zip" name="zip" class="form-control" type="text" />
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div id="fieldDiv_phone" class="col-xs-4">
                                    <label for="phone" class="control-label">Phone</label>
                                    <input id="phone" name="phone" class="form-control Phone-Number" type="text" />
                                    <span id="errorMsg_phone" class="control-label"></span>
                                </div>
                                <div class="col-xs-2">
                                    <label for="ext">Ext </label>
                                    <input id="ext" name="ext" class="form-control" type="text" />
                                </div>
                            </div>
                        </div>
                        <div id="fieldDiv_fax" class="form-group">
                            <div class="row">
                                <div class="col-xs-4">
                                    <label for="fax" class="control-label">Fax</label>
                                    <input id="fax" name="fax" class="form-control Phone-Number" type="text" />
                                    <span id="errorMsg_fax" class="control-label"></span> 
                                </div>
                            </div>
                        </div>
                        <div id="fieldDiv_email" class="form-group">
                            <label for="email" class="control-label">Email *</label>
                            <input id="email" name="email" class="form-control half required Email" type="text" />
                            <span id="errorMsg_email" class="control-label"></span> 
                            <span class="control-label"><a href="#privacyModal" data-toggle="modal" class="btn btn-link userEdit">Privacy Policy</a></span>
                        </div>
                        <div class="form-group">
                            <label for="comments">Comments</label>
                            <textarea id="comments" name="comments" class="form-control" rows="5"></textarea>
                        </div>
                        <div class="form-group">
                            <input type="button" class="btn btn-primary submitMessage" value="Send"/>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<!-- Privacy Policy modal -->
<div class="modal fade" id="privacyModal" role="dialog" tabindex="-1" aria-labeledby="Privacy Policy" aria-hidden="true" aria-describedby="Privacy Policy">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="panel-title">Privacy Policy</h3>
            </div>
            <div class="modal-body page-content">
                <h3>Privacy and Security Statement:</h3>
                <p style="font-size: 12px">
                    Health-e-Link takes your privacy seriously. Please read the following to learn more about our policy. 
                </p>

                <h3>What this Privacy Policy Covers</h3>
                <p style="font-size: 12px">
                    This Privacy Policy covers Health-e-Link's treatment of personally identifiable information that Health-e-Link collects when you are on the Health-e-Link site. 
                </p>

                <h3>Information Collection and use</h3>
                <p style="font-size: 12px">
                    Health-e-Link may collect personally identifiable information when you contact us or join our email list such as your name, company name, email address, and other information.  
                </p>

                <h3>Information Sharing and Disclosure</h3>
                <p style="font-size: 12px">
                    Health-e-Link will not sell trade or rent your personally identifiable information to anyone. [Health-e-Link may provide aggregate statistics about its customers, traffic patterns, and 
                    related site information to reputable third-party vendors, but these statistics will include no personally identifying information.]

                    Health-e-Link may also release account information when we believe, in good faith that such release is reasonably necessary to (i) comply with the law, (ii) enforce or apply the terms of 
                    our site use agreement or (iii) protect the rights, property or safety Health-e-Link , our users or others.  
                </p>

                <h3>Links</h3>
                <p style="font-size: 12px">
                    This site contains external links to other web sites. Health-e-Link is not responsible for the privacy policies of these sites and the information they may gather expressly or automatically. 
                </p>

                <h3>Privacy Policy and Site Use Agreement</h3>
                <p style="font-size: 12px">
                    Your use of this site constitutes your consent to the collection and use of the information described in this Privacy Policy by Health-e-Link .  
                </p>

                <h3>Changes to this Privacy Policy</h3>
                <p style="font-size: 12px">
                    Health-e-Link may edit this policy from time to time. If we make any substantial changes we will notify you by posting a prominent announcement on our pages. 
                </p>
            </div>
        </div>
    </div>
</div>
