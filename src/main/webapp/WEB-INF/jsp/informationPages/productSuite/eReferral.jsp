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
                                            <li ${param['page'] == 'eReferral' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/eReferral'/>" title="eReferral">eReferral</a></li>
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
                    <h3>Health-e-link's eReferral Network - Helping people make healthy lifestyle choices, wherever they are!</h3>
                 </blockquote>
                <br />
                <h4>Evolving Collaborative Healthcare Landscape</h4>
                <p>
                    Healthcare providers, their community-based partners, and public health agencies are looking for innovative opportunities to help meet triple aim goals of better population health, better experience of care 
                    and lower costs. Today, there are evolving new models of care with non-traditional healthcare providers that address the specific needs of patients outside of their primary care settings. The challenge 
                    however is developing effective communications channels between healthcare providers and their collaborating partners. Today, these communications channels are nothing more than word of mouth or at best, 
                    paper-based fax communications. With evolving EMR/EHR and HIE capabilities across the nation's healthcare system, there must be a better way!
                </p>
                <br />
                <h4>Health-e-link's eReferral Solution</h4>
                <p>
                    Health-e-link's eReferral system connects healthcare providers with their community-based partners and supports fully integrated eReferral communications regardless of the technology-based capabilities of 
                    the eReferral partners. Using Health-e-link's eReferral system, all referral types with all collaborating partners are managed from a single location. Health-e-Link's eReferral system spans the virtual 
                    boundaries between healthcare partners in your community. 
                </p>     
                <p>    
                    Using state-of-the art technologies, Health-e-link provides a secure web-based network for transmitting referrals including patient contact info, clinical data, notes and reports, audio files, images and 
                    more. Health-e-link's eReferral system supports any referral content and communicates referrals between parties using multiple data transport modes, document types and code sets - flexibility is the key 
                    to our solution! 
                </p>
                <br />
                <h4>Health-e-link's eReferral System Improves Collaboration and Care Coordination</h4>
                <ul>
                    <li>Create your own secure, integrated network of healthcare partners including healthcare providers and community-based organizations;</li>
                    <li>Enhance physician-to-physician and physician-to-community collaboration to develop more thorough care plans with smoother referrals;</li>
                    <li>Create your own referral types such as CDSMP, falls prevention, hypertension, nutrition, weight loss and others;</li>
                    <li>Specify referral content for each referral type you create including provider information, patient demographics, clinical information and attachments;</li>
                    <li>Communicate feedback reports with detailed care information from community partners directly to provider EMR/EHR systems with no manual intervention;</li>
                    <li>Monitor and manage referrals and feedback reports from your own EMR/EHR system or use the Health-e-link eReferral portal;</li>
                    <li>Receive referrals from consumers and field staff with our mobile apps.</li>
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
