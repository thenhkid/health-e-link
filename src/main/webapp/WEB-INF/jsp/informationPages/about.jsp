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
                                            <li ${param['page'] == 'networkcapabilities' ? 'class="active"' : ''}><a href="<c:url value='/about/network-capabilities'/>" title="Network Capabilities">Network Capabilities</a></li>
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

                <h4>Advancing Community-based Healthcare</h4>
                <p>
                    With a foundation in community-based healthcare and public health programs, Health-e-link was established to provide cost-effective solutions for managing healthcare information for community-based 
                    organizations and public health agencies. Historically, Health-e-link has focused specifically on helping our clients meet the data management needs of federal and state funded healthcare programs.
                </p>
                <p>
                    Health-e-link's leadership understands that the widespread adoption of health information technology including electronic medical record systems and health information exchange networks are creating 
                    new opportunities for sharing information and improving the nation's healthcare system through technology. 
                </p>
                <p>
                    However, Health-e-link's leadership also understands that the highly complex standards being promoted by the Nation-wide Health Information Network (NwHIN) strategy often impose constraints that limit 
                    or even prevent data exchange in many networks of care. By providing a standards-based, but highly flexible health information exchange and data management solutions, Health-e-link positions healthcare providers, 
                    community-based organizations, and public health agencies to take advantage of new patient care models in their networks of care.
                </p>
                <br />
                <h4>The Health-e-Link Solution</h4>
                <p>
                    The <a href="<c:url value="/product-suite" />">Health-e-link Suite</a> of products provides a healthcare information exchange and data management solution for all collaborating partners in any any community - local, regional or national. We've ensured that our solutions meet HIPAA 
                    privacy and security standards, thereby ensuring that patient privacy concerns are addressed. Health-e-link has embraced widely accepted health information technology standards that ensure that an 
                    investment with Health-e-link is a strategic investment for any organization. With our modular approach to implementation, Health-e-Link offers capabilities for <a href="<c:url value='/product-suite/eReferral'/>" title="eReferral">eReferral systems</a>, 
                    <a href="<c:url value='/product-suite/clinical-data-warehouse'/>" title="Clinical Data Warehouse">program registry systems</a> and <a href="<c:url value='/product-suite/universal-hie'/>" title="Universal HIE">health information exchange networks</a> that are configured to meet the 
                    specific needs of any network of healthcare providers and their partners.
                </p>
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
