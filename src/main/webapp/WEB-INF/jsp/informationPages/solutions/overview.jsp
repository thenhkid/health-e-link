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
                    Health-e-Link's Product Suite provides cost-effective healthcare data integration solutions that seamlessly shares clinical information with providers, business associates, h
                    ealthcare oversight agencies and payers in your community.  
                </p>

                <h3>Health-e-Link Provides Solutions for:</h3>
                <ul>
                    <li><a href='#RHIO'>RHIOs</a></li>
                    <li><a href='#Federal'>Federal &AMP; State Health Agencies</a></li>
                    <li><a href='#Community'>Community Healthcare Providers</a></li>
                    <li><a href='#HIT'>HIT/HIS Vendors</a></li>
                    <li><a href='#Hospitals'>Hospitals &AMP; Healthcare Systems</a></li>
                    <li><a href='#Payers'>Payers</a></li>
                    <li><a href='#Private'>Private Practices</a></li>
                </ul>
                
                <a href="" id="RHIO"></a>
                <h3>For RHIOS</h3>
                <p>
                    <a href="<c:url value='/product-suite'/>" title="Product Suite">Health-e-Link's healthcare data integration suite</a> provides a low commitment integration solution for exchanging clinical information between key healthcare partners in your community. 
                    Using web-based templates, Health-e-Link enables providers in your community to share clinical data in virtually any format, using their existing technical resources. 
                    Engage Health-e-Link to: 
                </p>
                <ul>
                    <li>Support safe clinical decision-making by sharing a current and complete patient record with clinicians, labs, and referring physicians and agencies in your community; </li>
                    <li>Maximize your ability to integrate data from compliant AND non-compliant systems;</li>
                    <li>Minimize costs associated with establishing AND maintaining health information exchanges with your healthcare constituents;</li>
                    <li>Comply with all current and emerging government standards for security and privacy;</li>
                    <li>Participate in government-funded public health programs and improve accuracy and timeliness of reporting;</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
                
                <a href="" id="Federal"></a>
                <h3>For Federal and State Government Health Agencies</h3>
                <p>
                    Government sponsored health agencies are recognizing and pursuing opportunities for improving the quality and accuracy of patient data they collect for healthcare oversight activities. 
                    While annual aggregate patient data reporting from affiliated healthcare agencies was an acceptable standard just a few years ago, recent advances in health data exchange suggest that 
                    visit-based patient data collection, analysis, and reporting is a realistic goal. Health-e-Link helps to achieve this goal and: 
                </p>
                <ul>
                    <li>Take advantage of cost effective registry templates for development of data repository, data analysis, and reporting tools for participating in public health programs;</li>
                    <li>Collect patient data from your affiliated healthcare agencies, regardless of their ability to conform with code set and messaging standards;</li>
                    <li>Minimize the costs associated with establishing AND maintaining health information exchanges with your healthcare constituents;</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
                
                <a href="" id="Community"></a>
                <h3>For Community Healthcare Providers</h3>
                <p>
                    Community healthcare providers are faced with HIT resourcing challenges. Internal staffing and budgetary restrictions limit your ability to keep pace with advances in data sharing and 
                    interoperability. Federal and state agencies as well as other external funding sources require substantial patient-related reporting. Health-e-link provides a data exchange solution that 
                    works within the constraints of your existing systems and opens the door to long term HIT/HIS improvements. The Health-e-Link Suite allows you to:  
                </p>
                <ul>
                    <li>Maximize your ability to provide patient data to external sources regardless of codeset and message format requirements;</li>
                    <li>Minimize the costs associated with establishing AND maintaining health information exchanges with business partners;</li>
                    <li>Take advantage of <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">cost effective registry templates</a> for development of data repository, data analysis, and reporting tools for government-funded public health programs;</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
                
                <a href="" id="HIT"></a>
                <h3>For HIT/HIS Vendors</h3>
                <p>
                    Whether you specialize in creating complex HIT solutions for hospitals, payers, or government agencies, the Health-e-Link Suite of data integration tools expands your expertise by providing 
                    a hosted solution dedicated to data interoperability. For HIT vendors, the Health-e-Link Suite offers:
                </p>
                <ul>
                    <li>Seamlessly integration of data from disparate and proprietary healthcare information systems; </li>
                    <li>Conformity with all current and emerging government standards for security and privacy;</li>
                    <li>Secure, off-site hosting and technical support 24/7;</li>
                    <li>Scalable pricing and modular design make it practical to implement for a private practice or large healthcare system client;</li>
                    <li>The ability to integrate and manage affiliated healthcare organizations.</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>Interested in partnering with Health-e-Link? Complete the vendor application to receive more information and a demonstration of the power and flexibility of Health-e-Link suite of data integration solutions. For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
                
                <a href="" id="Hospitals"></a>
                <h3>For Hospitals & Healthcare Systems</h3>
                <p>
                    Your hospital or healthcare system has already made a huge investment in creating IT solutions tailored for your business and clinical needs. Health-e-Link <a href="<c:url value='/solutions'/>" title="Solutions Overview">data integration solutions</a> works
                    WITH your current systems to share vital health information between your existing systems and partners' systems outside of your four walls. Hospitals use the Health-e-Link Suite to: 
                </p>
                <ul>
                    <li>Maximize your ability to integrate data standards from compliant AND non-compliant systems;</li>
                    <li>Minimize the costs associated with establishing AND maintaining health information exchanges with your healthcare constituents;</li>
                    <li>Provide a standards-based framework for the enterprise user community allowing access to authorized systems as well as shared services;</li>
                    <li>Build on your current HIS infrastructure while minimizing cost of participating in external data exchange activities;</li>
                    <li>Comply with current and emerging government standards for security and privacy;</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
                
                <a href="" id="Payers"></a>
                <h3>For Payers</h3>
                <p>
                    Providers, health plans and insurance companies all contribute to delivering more cost-effective healthcare for their patients. Health information data exchange initiatives seek to drive down 
                    delivery costs by reducing redundant and unnecessary paperwork and procedures. All this means faster transfer of complete records, reduced medical errors and faster reimbursements. 
                    The Health-e-Link Suite: 
                </p>
                <ul>
                    <li>Supports cost-effective clinical decision-making by sharing a current and complete patient record with clinicians, labs and referring physicians in the community;</li>
                    <li>Facilitates seamless payment by exchanging complete patient information electronically with payers and billing companies;</li>
                    <li>Maximizes your ability to exchange and receive patient data with external sources regardless of source or format;</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
                
                <a href="" id="Private"></a>
                <h3>For Private Practices</h3>
                <p>
                    Private practices, small and large, will be expected to exchange patient records and relevant clinical information as they participate in the roll-out of regional health information 
                    networks. Limited internal IT staffing and resources make it impractical to keep pace with advances in data sharing and interoperability. For private practices, the Health-e-Link 
                    Suite offers a cost-effective data exchange solution that: 
                </p>
                <ul>
                    <li>Supports safe clinical decision-making by sharing a current and complete patient record with clinicians, labs, referring physicians, and agencies in your community; </li>
                    <li>Facilitates faster payment by exchanging patient information electronically with payers and billing companies;</li>
                    <li>Builds on your investment in your current office management system;</li>
                    <li>Maximizes your ability to exchange and receive patient data with external sources regardless of source or format;</li>
                    <li>Takes advantage of cost effective registry templates for development of data repository, data analysis, and reporting requirements for government-funded public health programs;</li>
                    <li>Enhance your <a href="<c:url value='/product-suite/health-e-net'/>" title="Health-e-Net">data exchange</a> capabilities using Health-e-Link modular components for <a href="<c:url value='/product-suite/health-e-data'/>" title="Health-e-Data">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> and <a href="<c:url value='/product-suite/health-e-web'/>" title="Health-e-Web">secure email messaging</a>.</li>
                </ul>
                <blockquote>
                    <p>For more information, contact Health-e-Link at <strong>(774) 272-0313</strong>.</p>
                </blockquote>
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
