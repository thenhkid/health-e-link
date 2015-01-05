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
                    <h3>Health-e-link's Clinical Data Warehouse - A data warehouse solution meeting the data management, data analysis and data reporting needs across any number of health programs and 
                        collaborating partnerships!
                        </h3> 
                </blockquote>
                <br />
                <h4>Evolving Data Management Requirements</h4>
                <p>
                    Federal, state and local health agencies as well as collaborating healthcare providers collect more and more healthcare information from their provider communities for the purposes of healthcare oversight, 
                    population health management and program management. The challenge for these organizations however is finding a cost-effective and strategic approach to collecting, managing, analyzing and reporting this 
                    vital data. Siloed legacy systems developed over many years plague the effectiveness of data collection and management in an evolving health IT landscape. Competing priorities and system requirements 
                    complicate the selection, implementation and management of data warehouse solutions.
                </p>
                <br />
                <h4>Health-e-link's Clinical Data Warehouse Solution</h4>
                <p>
                    Health-e-link's Clinical Data Warehouse is a highly configurable and flexible data warehouse for collaborating healthcare professionals. Health-e-link serves as a single platform for managing data 
                    collection, data access, data management, data analysis and reporting needs across the healthcare community. By using the Health-e-link data warehouse, partner IT staffs can focus on a single set of 
                    technologies and retire outdated legacy systems. The Health-e-Link solution enables seamless data collection and data management across multiple programs of care and integrates with partner clinical 
                    and business systems. Health-e-Link provides easy-to-use templates to configure and operate one or multiple clinical data registries, all on a single platform. On a program-by-program basis, define 
                    patient profiles, clinical data sets, surveys and questionnaires, data import/export processes, user interfaces, reports and more.  
                </p>
                <br />
                <h4>Health-e-link's Clinical Data Warehouse Solves Community-wide Data Management Challenges</h4>
                <ul>
                    <li>Configure and support data warehouses for multiple programs concurrently;</li>
                    <li>Configure communities of participating organizations and associate them with specific programs;</li>
                    <li>Configure role-based user definitions to control data access and support privacy and security of client data;</li>
                    <li>Configure program specific client/patient demographic profiles;</li>
                    <li>Configure program specific clinical data sets to track relevant client/patient information;</li>
                    <li>Develop program specific user interfaces;</li>
                    <li>View client/patient history across programs of care;</li>
                    <li>Develop client/patient surveys, questionnaires and forms to support data collection requirements;</li>
                    <li>Utilize predefined and custom database query and reporting capabilities;</li>
                    <li>Customize master client/patient index to support identity resolution and management;</li>
                    <li>Configure interfaces to import patient data from participating clinical data systems;</li>
                    <li>Configure data extracts for submission to 3rd party data analysis tools.</li>
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

