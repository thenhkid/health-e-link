<%--
    Document   : about
    Created on : Nov 27, 2013, 10:05:05 AM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div role="main">

    <div class="container main-container">
        <div class="row">
            <aside class="col-md-3 col-sm-3 secondary hidden-xs">
                <div class="fixed-region">
                    <div class="module sidebar-module">
                        <nav class="secondary-nav" role="navigation">
                            <ul class="nav nav-stacked">
                                <li>
                                    <a href="">Product Suite</a>
                                    <div id="active-page-nav">
                                        <ul class="nav" >
                                            <li ${param['page'] == 'overview' ? 'class="active"' : ''}><a href="<c:url value='/product-suite'/>" title="Product Suite Overview">Product Suite Overview</a></li>
                                            <li ${param['page'] == 'healthenet' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">Health-e-Net</a></li>
                                            <li ${param['page'] == 'healthedata' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">Health-e-Data</a></li>
                                            <li ${param['page'] == 'healtheweb' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">Health-e-Web</a></li>
                                        </ul>
                                    </div>
                                </li>
                            </ul>
                        </nav>
                    </div>
                    <div class="module sidebar-module">
                        <dl class="vcard info-list">
                            <dt>Contact Us By Phone</dt>
                            <dd class="tel">(774) 272-0313</dd>

                            <dt>Contact Us By Fax</dt>
                            <dd class="tel">(508) 721-1978</dd>

                            <dt>Contact Us By Email</dt>
                            <dd class="email"><a href="mailto:information@health-e-link.net" title="">information@health-e-link.net</a></dd>

                            <%--<dt>Connect</dt>
                            <dd class="margin-small top">
                                <a href="" title="" class="icon-social icon-facebook ir-inline">Facebook</a>
                                <a href="" title="" class="icon-social icon-linked-in ir-inline">Linked In</a>
                                <a href="" title="" class="icon-social icon-twitter ir-inline">Twitter</a>
                            </dd>--%>
                        </dl>
                    </div>
                </div>
            </aside>

            <div class="col-md-9 col-md-offset-0 col-sm-8 col-sm-offset-1 page-content">
                
                <h3>Ease the Transition to Electronic Document Management</h3>
                <p>
                    Today, healthcare providers are coordinating patient information from a wide array of specialized systems. The challenge however, is determining the best way to access and share the 
                    information captured through more traditional paper-based methods with information collected from practice management, electronic medical records and payer systems. 
                </p>

                <h3>Health-e-Link DOC-u-Link Integrates Electronic Medical Records</h3>
                <p>
                    DOC-u-Link is a new electronic document management solution that combines elements of document management, content management, knowledge management, electronic file storage and retrieval, 
                    virtual access, and workgroup messaging services into a simple-to-use browser-based product. DOC-u-Link makes it easy to file, find and retrieve electronic and paper-based medical records 
                    in an organized, familiar, and easy-to-search format that: 
                </p>
                <ul>
                    <li>Integrates patient notes or reports with Health-e-Link Exchange software for shared access;</li>
                    <li>Complies with HIPAA guidelines for preserving privacy and security;</li>
                    <li>Files and finds electronic documents quickly with powerful search options;</li>
                    <li>Access documents anytime, anywhere using a standard web browser;</li>
                    <li>Incorporates document management into existing clinical workflows;</li>
                    <li>Incorporates any electronic file and image types into medical records; including Excel spreadsheets, MS Word documents, Adobe PDF, x-rays, EKG's, and even voice messages;</li>
                    <li>Emails or faxes reports, results and clinical notes directly from DOC-u-Link;</li>
                    <li>Facilitates collaboration between partners to make important diagnostic and treatment plans.</li>
                </ul>
                <p>
                    For a demonstration of Health-e-link's community healthcare information integration solutions, please contact us at (774) 272-0313 or email <a href="mailto:information@health-e-link.net">information@health-e-link.net</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div class="module content-call-out">
    <div class="container center-text">
        <p>
            For more information or a demonstration of Health-e-link's interoperability and data management solutions,<br/> please contact us at <strong>(774) 272-0313</strong> or email <a href="mailto:information@health-e-link.net">information@health-e-link.net</a>
        </p>
    </div>
</div>
