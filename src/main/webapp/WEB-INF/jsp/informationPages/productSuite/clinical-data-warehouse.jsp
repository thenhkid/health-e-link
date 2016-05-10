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
                                            <li ${param['page'] == 'careConnector' ? 'class="active"' : ''}><a href="<c:url value='/product-suite/careConnector'/>" title="Care Connector">Care Connector</a></li>
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
                    <h3>
                        Health-e-link's Clinical Data Warehouse - A data warehouse and program registry solution supporting health programs across your network of care!
                    </h3> 
                </blockquote>
                <br />
                <h4>Evolving Data Management Requirements</h4>
                <p>
                    Federal, state and local health agencies as well as collaborating healthcare providers collect more and more healthcare information from their provider communities for the purposes of healthcare 
                    oversight, population health management and program management. These organizations are often burdened with outdated and siloed  systems using outdated and incompatible technologies. The challenge 
                    for these organizations is to find a cost-effective and strategic approach to collecting, managing, analyzing and reporting this vital data. Siloed legacy systems developed over many years plague 
                    the effectiveness of data collection and management in an evolving health IT landscape. Competing priorities and system requirements for multiple programs of care complicate the selection, implementation
                    and management of data warehouse solutions. Is there a strategic solution that can meet long term program and data management needs across evolving networks of care?
                </p>
                <br />
                <h4>Health-e-link's Clinical Data Warehouse Solution</h4>
                <p>
                    Health-e-link's Clinical Data Warehouse is a highly configurable and flexible data warehouse and program registry for collaborating healthcare professionals. Health-e-link serves as a single platform 
                    for managing data collection, data access, data management, data analysis and reporting needs across the healthcare community. By using the Health-e-link data warehouse, partner IT staffs can focus on a 
                    single set of technologies and retire outdated legacy systems. The Health-e-Link solution enables seamless data collection and data management across multiple programs of care and integrates with 
                    partner clinical and business systems. Health-e-Link provides easy-to-use templates to configure and operate one or multiple clinical data registries, all on a single platform. On a program-by-program 
                    basis, define patient profiles, clinical data sets, surveys and questionnaires, data import/export processes, user interfaces, reports and more.   
                </p>
                <br />
                <h4>Health-e-link's Clinical Data Warehouse Solves Community-wide Data Management Challenges</h4>
                <ul>
                    <li>Configure and support data warehouses for multiple programs concurrently;</li>
                    <li>Configure communities of participating organizations and associate them with specific programs;</li>
                    <li>Configure role-based user definitions to control data access to meet privacy and security obligations;</li>
                    <li>Configure program specific client/patient demographic profiles;</li>
                    <li>Configure program specific clinical data sets to track relevant client/patient information;</li>
                    <li>Build program specific user interfaces and program-specific functionality;</li>
                    <li>View client/patient history across programs of care;</li>
                    <li>Develop client/patient surveys, questionnaires and forms to support data collection requirements;</li>
                    <li>Utilize predefined and custom database query and reporting capabilities;</li>
                    <li>Customize master client/patient index to support identity resolution and management;</li>
                    <li>Configure data exchange interfaces to import patient data from participating clinical data systems;</li>
                    <li>Configure data extracts for submission to 3rd party data analysis tools.</li>
                </ul>
                <br />
                <h4>Health-e-link Enables Data Sharing</h4>
                <p>
                  See how Health-e-link promotes communication and collaboration across integrted healthcare systems.  
                </p>
                <video width="520" height="340" preload="yes" controls="true">
                    <source src="../../../../dspResources/health-e-link-animation7.webm" type="video/webm">
                    <source src="../../../../dspResources/health-e-link-animation7.mp4" type="video/mp4">
                    <source src="../../../../dspResources/health-e-link-animation7.mov" type="video/mov">
                    <source src="../../../../dspResources/health-e-link-animation7.ogv" type="video/ogv">
                </video>
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

