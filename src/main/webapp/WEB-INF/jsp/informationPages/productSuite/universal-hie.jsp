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
                    <h3>Health-e-link's Universal HIE - Removing the road blocks to effective health information exchange!</h3>
                </blockquote>
                <br />
                <h4>Evolving Health Information Exchange Landscape</h4>
                <p>
                    National, state, regional and local HIE initiatives have promoted clinical data sharing models and the potential of interoperable health information systems for many years. However, 
                    there has been limited success with health information exchange with the exception of targeted solutions in localized settings or alternatively, national systems focused on very limited 
                    data sets in specific program areas. For any collaborating healthcare organizations, sharing patient data has many challenges, one of which is that these organizations each employ technology-based 
                    systems with differing capabilities for data exchange. Often times, meeting the technical requirements of any data exchange project is overwhelming and in many cases, technical requirements of one 
                    initiative conflicts with the technical requirements of another. Conflicting code sets, message types,  and transport methods confuse and potentially overwhelm technical staffs.
                </p>
                <br />
                <h4>Health-e-link's Universal HIE Solution</h4>
                <p>
                    The Health-e-Link Universal HIE solution is the one "flexible" health information exchange network that allows healthcare partners in your community to share patient information even when their 
                    technology-based systems are incompatible. The Universal HIE allows you to define individual partnerships on the network and the technical specifications that support data exchange for that partnership. 
                    The Universal HIE specifies who can share data with whom, which message types are supported, which code sets are used within the supported message types, and finally, the processing rules associated 
                    with each data exchange configuration. Health-e-link's Universal HIE uses these configurable features to ensure that each customer's unique data exchange requirements are fully addressed. Health-e-link's 
                    Universal HIE serves as the single point of integration with all data exchange partners in your healthcare delivery network.
                </p>
                <br />
                <h4>Health-e-link's Universal HIE Solves System Integration Challenges</h4>
                <ul>
                    <li>A highly flexible data integration hub that collects, normalizes and delivers data from virtually any system using standardized or proprietary message formats and code sets;</li>
                    <li>Supports all types of data exchange from batch file upload/download, to SFTP/FTPS batch file exchange to web services/API integration.</li>
                    <li>Supports all message formats including txt files, csv files, xml files, mdb files and excel spreadsheets.</li>
                    <li>Supports all document types including CCR, CCD, HL7, JSON and proprietary document types.</li>
                    <li>Supports standard and proprietary code sets including CPT, ICD, SNOMED, LOINC and others. </li>
                    <li>Easy-to-navigate web-based interface that simplifies system configuration to meet individual data exchange needs;</li>
                    <li>Operational and auditing features that support automated processing, accountability and data accuracy;</li>
                    <li>A low commitment hosted integration solution that meets short term needs while providing a pathway to unlimited expansion;</li>
                    <li>Integration capabilities any established HIE platform including Direct;</li>
                    <li>Round the clock hosting and management in our secure data center.</li>
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

