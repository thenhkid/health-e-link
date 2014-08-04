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
                
                <h3>Simplified Healthcare Oversight and Disease Surveillance Reporting</h3>
                <p>
                    Government, public health and regional health organizations collect more and more healthcare information from their provider communities for the purposes of healthcare oversight, 
                    disease surveillance, and program management. The challenge for providers however is finding a cost-effective and strategic approach to collecting, managing, analyzing and reporting 
                    this vital data. 
                </p>

                <h3>Rapid Registry Delivers to Public Health Registries</h3>
                <p>
                    Health-e-Link solutions enable seamless data exchange between disparate systems and agencies, including public health data and disease surveillance systems. Health-e-Link's Rapid Registry 
                    module is a totally new tool in data management that provides easy-to-use, standards-based templates to deliver your clinical data registry to selected public health agencies and partners.
                    
                    Health-e-Link's Rapid Registry module is designed to help RHIOs, physicians, community-based clinics and hospitals, by enabling healthcare providers to automatically share relevant patient 
                    information with local and federal healthcare oversight agencies. Rapid Registry is a web portal using continuity of care, personal health records, and electronic health record standards as 
                    a foundation. By incorporating the most current standards for EHRs and CCRs, Health-e-Link is committed to keeping clients up-to-date with emerging health information technology standards. 
                    
                    Through the Rapid Registry portal, providers can easily upload, review, and analyze essential data to: 
                </p>
                <ul>
                    <li>Provide coordinated care for chronically ill patients, with access to advanced directives; </li>
                    <li>Monitor HIV/AIDS and other infectious disease trends; </li>
                    <li>Prevent the spread of infectious diseases and possible pandemics; </li>
                    <li>Identify and treat patients with high risk factors; </li>
                    <li>Share patient immunization records; </li>
                    <li>Communicate family planning information and care plans.</li>
                </ul>
                <blockquote>
                <p>Cost-effective solution to collecting, managing, analyzing and reporting important data.</p>
              </blockquote>
              
                <h3>Preserve Funding with Automatic Submission to Healthcare Oversight Agencies</h3>
                <p>
                    Health-e-Link's Rapid Registry seamlessly submits the required patient information to customer-defined registries. Select the templates that make sense for your data management needs and 
                    let Rapid Registry deliver your clinical data registry automatically on a pre-determined schedule. Submissions and reporting are more timely, allowing physicians and agencies to identify 
                    AND treat immediate health threats in your community. With more timely and accurate reporting, agencies can more confidently secure essential federal funding to support these public health 
                    initiatives. 
                </p>
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
