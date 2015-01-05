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
                
                <p>
                    At Health-e-link, we pride ourselves on providing personalized, customized services that align with our client's unique needs and capabilities. Health-e-link implementations can range from 
                    straightforward implementations of our hosted eReferral system that can be accomplished in a day or two, to state-wide implementations of our data warehouse that may take several months to complete.
                </p>
                <p>
                    Using the Health-e-Link Suite of interoperability solutions as the foundation, Health-e-link staff consults with you before, during and after implementation to ensure that your goals and objectives 
                    are met. Whether you are a small community-based provider, a regional coalition of providers, a state health agency or a federal healthcare program, we provide our clients with highly qualified 
                    resources for resolving system interoperability and data management related challenges.
                </p>
                <br />
                <h4>Planning</h4>
                <p>
                    Prior to implementation, the Health-e-Link Team will meet with representatives from the collaborative group in order to take full advantage of opportunities than can be realized through data exchange. 
                    We review the collaborative organizational structure and propose data exchange and sustainability models that meet critical objectives and ensure a successful outcome for the engagement. Our 
                    staff uses an iterative consulting approach throughout the engagement to ensure that the client team understands and embraces the system to be implemented.
                </p>
                <ul>
                    <li>Business Review - We work with you to understand your community of care, your collaborating partners and healthcare challenges you hope to address. </li>
                    <li>Coalition Building - We'll help you to understand the challenges of data sharing across your coalition and ensure that you address issues to ensure healthy, productive and sustainable partnerships.</li>
                    <li>Technical Infrastructure Review - We understand the interoperability features of your clinical systems. Health-e-link will work with your in house and vendor IT staffs to configure the interfaces to connect you to the Health-e-link network.  </li>
                    <li>Formalize Network Requirements - Health-e-link provides preliminary test configurations of our solution for online review, and provides mockups for any custom features that may be required. Ultimately, you'll see your network in action before we begin installation and configuration. </li>
                </ul>
                <br />
                <h4>Installation and Configuration</h4>
                <p>
                    Whether using our hosted solution or licensing our software for installation, configuration and management within your own facilities, Health-e-link staff will ensure that the system, and your staffs, 
                    are prepared to meet your ongoing data management needs across your network of care. Due to our solution's highly flexible and configurable features, installation and configuration is greatly 
                    accelerated and is adaptable over time. With Heath-e-link, your network of care isn't bound by the constraints of typical interoperability solutions. As your network evolves, so will Health-e-link.
                </p>
                 <ul>
                    <li>Install Health-e-link Suite - Installation and initial configuration of Health-e-link will be performed by our staff in collaboration with your IT staff. Health-e-link staff will ensure that your staff are fully prepared to support the system on an ongoing basis.</li>
                    <li>Engage Partner IT Resources - We'll provide your partner IT staffs with the technical documentation and support necessary for them to configure the interfaces required to support data exchange with Health-e-link.</li>
                    <li>Finalize Technical Specifications - Technical specifications for eReferral, Clinical Data Warehouse, and/or Universal HIE system interfaces will be documented. Health-e-link will be configured in our test environment and your support staff will be participate in the configuration process as part of their training.</li>
                    <li>Configure System Interfaces - Health-e-link and partner system interfaces will be configured according to defined technical specifications. Interfaces will be fully tested in our test environment prior to implementation.</li>
                    <li>Test Baseline Configuration - A series of use cases will be defined and executed to verify that Health-e-link, participating partners and their clinical data systems are fully operational and meet baseline measures for transition to production.</li>
                </ul>
                <br />
                <h4>Implementation</h4>
                <ul>
                    <li>Train staff - We will train partner staffs on all facets of Health-e-link operation from data submission, to system operation, to data analysis and reporting. Health-e-link staff will provide in-person training sessions as well as webinars and prerecorded tutorials. </li>
                    <li>Activate Baseline Network - Health-e-link is activated and is made available for production use. Baseline implementations typically include a code set of system users and system features that ensure a successful implementation of the system.</li>
                    <li>Monitor Network Performance - Following activation of the network, operational and technical support staff track activity and ensure that operational and service level commitments are maintained.</li>
                    <li>Onboard/Enhance Partners - Following an initial period of performance, additional partners will be on-boarded onto the Health-e-link network. The on-boarding process is ongoing as new partners are added and their data exchange and data management needs evolve. </li>
                    <li>Evaluate Utilization - Network utilization is evaluated on an ongoing basis. Utilization of system features as well as network activity is reviewed and corrective actions are implemented. Corrective actions may include user training, modifications to system interfaces, and/or modification to system features.</li>
                </ul>
                <p>
                    For a demonstration of Health-e-link's interoperability and data management solutions, please contact us at (774) 272-0313 or email <a href="mailto:information@health-e-link.net">information@health-e-link.net</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div class="module content-call-out">
    <div class="container center-text">
        <p>For more information on how BOWlink Technologies can create a cost-effective healthcare information data exchange solution for your organization,
            <br/>please contact us at <strong>(774) 272-0313</strong> or email <strong><a href="mailto:information@health-e-link.net">information@health-e-link.net</a></strong>
        </p>
    </div>
</div>
