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
                                    <a href="">Solutions</a>
                                    <div id="active-page-nav">
                                        <ul class="nav" >
                                            <li ${param['page'] == 'overview' ? 'class="active"' : ''}><a href="<c:url value='/solutions'/>" title="Solutions Overview">Solutions Overview</a></li>
                                            <li ${param['page'] == 'services' ? 'class="active"' : ''}><a href="<c:url value='/solutions/services'/>" title="Services">Services</a></li>
                                            <li ${param['page'] == 'casestudies' ? 'class="active"' : ''}><a href="<c:url value='/solutions/case-studies'/>" title="Case Studies">Case Studies</a></li>
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
                
                <h3>Helping Clients Achieve their Data Interoperability Objectives</h3>
                <p>
                    At BOWLink, we pride ourselves on providing personalized, customized services that meet our client's unique needs, networks, and capabilities. Using the <a href="<c:url value='/product-suite'/>" title="Product Suite">Health-e-Link Suite of integration 
                    solutions</a> as the foundation, BOWLink consults with you before, during and after implementation to ensure that your goals and use objectives are being met. Whether you are a small group 
                    practice or a large community agency, we provide our clients with a one-stop source for resolving data interoperability-related challenges cost-effectively. 
                </p>

                <h3>Consulting Services</h3>
                <p>
                    Prior to implementation, the Health-e-Link Team will meet with representatives from the collaborative group in order to take full advantage of opportunities than can be realized through data 
                    exchange. We review the collaborative organizational structure and propose data exchange and sustainability models that meet critical objectives and ensure a successful outcome for the 
                    engagement. Our staff uses an iterative consulting approach throughout the engagement to ensure that the client team understands and embraces the system to be implemented. 
                </p>
                
                <h3>Database Management Services</h3>
                <p>
                    A critical feature of any data exchange implementation is the database that stores and manages the data from the healthcare constituents that share critical patient information on your network. 
                    Health-e-link supports industry-wide best practices for database monitoring and performance, database integrity and security, and database backup and recovery. These practices ensure that 
                    your data is available to authorized individuals, and just as importantly, is protected from adverse events that may compromise your business. 
                </p>
                
                <h3>Training Services</h3>
                <p>
                    Your Health-e-Link Team will work with your staff to ensure that your Health-e-Link implementation is usable and practical. Training via a comprehensive web-based tutorial is available anytime. 
                    Additional on-site training can be provided as required. 
                </p>
                
                <h3>Technical Support Services</h3>
                <p>
                    Because information technology may not be your primary area of expertise, we support our clients all through the design and implementation process with onsite visits, email and phone support. 
                    
                    BOWLink's standard technical support is available by phone or email Monday through Friday 8-6PM EST to keep things running smoothly. Additional support 24 x 7 can be arranged as needed. 
                </p>
                <blockquote>
                    <p>For more information on how Health-e-Link Technologies consulting services can help you achieve your data interoperability objectives, contact Health-e-Link at <strong>(508) 721-1977</strong>.</p>
                </blockquote>
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
