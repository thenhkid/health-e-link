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
                                            <li ${param['page'] == 'overview' ? 'class="active"' : ''}><a href="<c:url value='/#productSuite'/>" title="Product Suite Overview">Product Suite Overview</a></li>
                                            <li ${param['page'] == 'careConnect' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/careConnect'/>" title="Care Connect">Care Connect</a></li>
                                            <li ${param['page'] == 'universal-hie' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/universal-hie'/>" title="Universal HIE">Universal HIE</a></li>
                                            <li ${param['page'] == 'clinical-data-warehouse' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/clinical-data-warehouse'/>" title="Clinical Data Warehouse">Clinical Data Warehouse</a></li>
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
                 <blockquote>
                    <h3>Health-e-link's Care Connector Network - Helping people make healthy lifestyle choices, wherever they are!</h3>
                 </blockquote>
                <br />
                <h4>Evolving Collaborative Healthcare Landscape</h4>
                <p>
                    Healthcare providers, their community-based partners, and public health agencies are looking for innovative opportunities to 
                    help meet triple aim goals of better population health, better experience of care and lower costs. Today, there are evolving 
                    new models of care with non-traditional healthcare providers that address the specific needs of patients outside of their primary 
                    care settings. The challenge however is developing effective communications channels between healthcare providers and their collaborating partners. 
                    Today, these communications channels are nothing more than word of mouth or at best, paper-based fax communications. With evolving EMR/EHR and HIE capabilities 
                    across the nation's healthcare system, there must be a better way!
                </p>
                <br />
                <h4>Health-e-link's Care Connector Solution</h4>
                <p>
                    Health-e-link's Care Connector connects healthcare providers with their community-based partners and supports fully integrated communications regardless of the 
                    technology-based capabilities of the  partner organizations. Using Health-e-link's Care Connector, all transaction types such as referrals or transitions of care 
                    are managed from a single location. Health-e-Link's Care Connector spans the virtual boundaries between healthcare partners in your community.
                </p>     
                <p>    
                   Using state-of-the art technologies, Health-e-link provides a secure web-based network for transmitting standards-based documents (cCDA) including patient contact info, 
                   clinical data, notes and reports, audio files, images and more. Health-e-link's Care Connector supports standards-based and proprietary messages and communicates messages 
                   between parties using multiple data transport modes, document types and code sets - flexibility is the key to our solution!
                </p>
                <br />
                <h4>Health-e-link's Care Connector System Improves Collaboration and Care Coordination</h4>
                <ul>
                    <li>Create your own secure, integrated network of healthcare partners including healthcare providers and community-based organizations;</li>
                    <li>Enhance physician-to-physician and physician-to-community collaboration to develop more thorough care plans with smoother transitions of care;</li>
                    <li>Create your own transition of care messages such as CDSMP, falls prevention, hypertension, nutrition, weight loss and others;</li>
                    <li>Meet meaningful use stage 2 certification criteria for electronic transmission of a summary of care document (objective 5: health information exchange).</li>
                    <li>Specify content for each message type you create including provider information, patient demographics, clinical information and attachments;</li>
                    <li>Communicate status updates with detailed care information from community partners directly to provider EMR/EHR systems with no manual intervention;</li>
                    <li>Monitor and manage messages and key events from your own EMR/EHR system or use the Health-e-link Care Connector portal.</li>
                </ul>
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
