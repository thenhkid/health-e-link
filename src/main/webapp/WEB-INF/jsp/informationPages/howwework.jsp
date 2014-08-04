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
                                    <a href="">About</a>
                                    <div id="active-page-nav">
                                        <ul class="nav" >
                                            <li ${param['page'] == 'about' ? 'class="active"' : ''}><a href="<c:url value='/about'/>" title="About Health-e-Link">About Health-e-Link</a></li>
                                            <li ${param['page'] == 'howwework' ? 'class="active"' : ''}><a href="<c:url value='/about/how-we-work'/>" title="How We Work">How We Work</a></li>
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

                <p>
                    At BOWLink, we work closely with our clients, large and small, because we realize the significant impact that healthcare information integration can have on health outcomes, 
                    clinical workflow, and resource utilization. Our project development process has been designed to foster community collaboration and to help all the stakeholders: healthcare 
                    providers, oversight agencies, payers, and patients achieve their goals. We assist our clients before, during, and after the implementation to ensure complete usability, 
                    satisfaction, and success

                    BOWlink uses a consultative approach to evaluating your requirements and defining an integrated information systems strategy that complements your existing resources and delivers 
                    maximum value. We utilize a four step process to ensure that the Health-e-Link Suite is successfully implemented and your staff is fully prepared to take advantage of the 
                    opportunities that data exchange offers to your organization and the healthcare community. 
                </p>

                <h3>STEP 1: Information Gathering</h3>
                <p>
                    BOWlink staff will conduct a thorough business and technical review of your organization and the community of healthcare providers and payers you collaborate with. Critical information gathered includes: 
                </p>
                <h4>Business Review</h4>
                <ul>
                    <li>Business strategy</li>
                    <li>Business relationships</li>
                    <li>Sources of data</li>
                    <li>Users of data </li>
                    <li>Workflow</li>
                    <li>Policies and procedures</li>
                </ul>
                <h4>Technical Review</h4>
                <ul>
                    <li>IT strategic plan</li>
                    <li>Infrastructure</li>
                    <li>Technical staff and resources</li>
                    <li>Software applications</li>
                    <li>Policies and procedures</li>
                </ul>
                <h3>STEP 2: Requirements Analysis</h3>
                <p>
                    Using the information gathered in Step 1, the Health-e-Link Team will develop a set of recommendations and requirements for your Health-e-Link system implementation. Key steps and documents include:  
                </p>
                <ul>
                    <li>Data analysis</li>
                    <li>Industry standards</li>
                    <li>Security requirements</li>
                    <li>Workflow enhancements</li>
                    <li>Cost/benefit analysis</li>
                    <li>Functional requirements specification</li>
                    <li>System configuration specification</li>
                </ul>
                <h3>STEP 3: System Configuration</h3>
                <p>
                    Your Health-e-Link Team will recommend, configure and test the Health-e-Link modules required to support your organization's healthcare information integration goals. The detailed 
                    specifications that are defined through the requirements analysis process are key to a successful implementation. Essential elements include:   
                </p>
                <ul>
                    <li>Portal setup</li>
                    <li>Organizations and user roles</li>
                    <li>Security setup</li>
                    <li>Legacy system access</li>
                    <li>Database configuration</li>
                    <li>Business relationships</li>
                    <li>Analysis, visualization, and reporting</li>
                    <li>Data entry</li>
                    <li>Data channels</li>
                    <li>Data translation</li>
                    <li>Quality Control</li>
                    <li>Processing requirements</li>
                    <li>Monitoring Requirements</li>
                    <li>Audit Requirements</li>
                </ul>
                <h3>STEP 4: Rollout</h3>
                <p>
                    Your Health-e-Link Team will ensure that your staff is fully trained and prepared to use and manage the Health-e-Link Suite from user and administrative perspectives. We work with 
                    you through a well defined process and ensure that your organization is prepared to take maximum advantage of Health-e-Link's data exchange capabilities. Key steps include:  
                </p>
                <ul>
                    <li>Implementation</li>
                    <li>Training including online and on-site </li>
                    <li>Transition</li>
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
