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
                            <dd class="tel">(508) 721-1977</dd>

                            <dt>Contact Us By Fax</dt>
                            <dd class="tel">(508) 721-1978</dd>

                            <dt>Contact Us By Email</dt>
                            <dd class="email"><a href="" title="">info@health-e-link.net</a></dd>

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
                
                <h3>Cost-Effective Data Exchange</h3>
                <p>
                    Exchanging data between the plethora of different host platforms and sources is key to building the essential infrastructure necessary to support high-quality and more cost 
                    effective care delivery. Health-e-Link Exchange solution is the one web-enabled exchange portal that allows regional healthcare partners in your community to cost-effectively send, 
                    receive, and exchange patient healthcare information leveraging your existing business and clinical applications, and your current IT resources. 
                </p>

                <h3>Exchange Solves Data Integration Challenges with:</h3>
                <ul>
                    <li>A highly flexible data integration hub that collects, normalizes, analyzes, and reports data from virtually any system using standardized or proprietary formats and code sets;</li>
                    <li>A low commitment integration solution that is easy to install and maintain using your in-house technical support staff;</li>
                    <li>Easy-to-navigate web-based interface that simplifies data translation and reporting tasks;</li>
                    <li>Round the clock hosting in a secure environment;</li>
                    <li>Complies with ALL mandated requirements and emerging standards including HL7, HIPAA, SNOMED, LOINC, CPT and ICD;</li>
                    <li>Optional expansion into <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a>, <a href="<c:url value='/product-suite/health-e-data'/>" title="healthcare registry systems">data analysis and reporting</a>, and <a href="<c:url value='/product-suite/health-e-web'/>" title="Secuire Email Messaging">secure email communication</a> with compatible Health-e-link modules.</li>
                </ul>
                <blockquote>
                <p>Compiles with ALL mandated requirements and emerging standards </p>
                <footer>&#149; HL7</footer>
                <footer>&#149; HIPAA</footer>
                <footer>&#149; SNOMED</footer>
                <footer>&#149; LOINC</footer>
                <footer>&#149; CPT</footer>
                <footer>&#149; ICD</footer>
              </blockquote>
              
                <h3>Facilitating Regional Healthcare Information Exchange</h3>
                <p>
                    Health-e-Link allows you to define the specific business rules to ensure that patient privacy and security concerns are addressed for all collaborating organizations. The 
                    CommunityLink feature provides a predefined set of HIPAA compliant business rules that serve as the foundation for how data is shared and provides the framework for secure 
                    data exchange. CommunityLink eliminates the need for collaborating organizations to spend countless hours negotiating business rules for their data exchange by pre-defining 
                    the data sharing relationships between organizations:
                </p>
                <ul>
                    <li>Enroll organizations in your health information exchange community and configure the specific needs of each data sharing partner to meet their privacy and security concerns; </li>
                    <li>Control and assign data access privileges to users based on their clinical roles;</li>
                    <li>List users in the health partners directory which provides access to the CDC sponsored public health directory (the yellow pages for healthcare providers on the Internet).</li>
                </ul>
                <p>
                    Specifically designed to support the information needs for regional health information organizations, HEALTH-e-link Exchange provides simple and flexible data management tools that allow 
                    you to meet the information exchange needs specific to your regional healthcare organization, community and partners: 
                </p>
                <ul>
                    <li>Accommodates growth with scalable pricing with modular components to handle any number of patients, partners and services;</li>
                    <li>Simplifies reporting that meets agency requirements and facilitates funding;</li>
                    <li>View and analyze your data reports to support coordinated programs of care;</li>
                    <li>Facilitates reimbursements and revenues from payers and funding agencies;</li>
                    <li>Facilitates more thorough and timely patient care, resulting in increased patient and clinician satisfaction. </li>
                </ul>
                <p>
                    For a demonstration of Health-e-link's community healthcare information integration solutions, please contact us at (508) 721-1977 or email <a href="mailto:info@health-e-link.net">info@health-e-link.net</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div class="module content-call-out">
    <div class="container center-text">
        <p>For more information on how BOWlink Technologies can create a cost-effective healthcare information data exchange solution for your organization,
            <br/>please contact us at <strong>(508) 721-1977</strong> or email <strong><a href="mailto:info@health-e-link.net">info@health-e-link.net</a></strong>
        </p>
    </div>
</div>
