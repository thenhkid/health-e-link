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

                <h3>Advancing Community Healthcare</h3>
                <p>
                    With a foundation in not-for-profit healthcare, BOWLink was established in 2003 to provide a cost-effective solution for exchanging vital healthcare information for community-based organizations
                    that did not have the resources for extensive, custom IT development.
                </p>
                <p>
                    BOWlink understands that the widespread availability of the Internet and the connectivity it provides, across the enterprise or community, continues to spawn new opportunities for sharing
                    information and transforming that information into community-wide knowledge. By providing standards-based templates across the web, BOWLink provides cost-effective solutions for resolving the
                    technical issues involved in healthcare data exchange. For all involved in providing healthcare in your community, accurate data exchange is a critical step in delivering safer, patient-centric
                    healthcare decisions and services.
                </p>
                <h3>The Health-e-Link Data Exchange Solution</h3>
                <p>
                    BOWLink's <a href="<c:url value='/product-suite'/>" title="Product Suite">Health-e-link Suite</a> of products provides a healthcare information integration solution for community and hospital-based providers, payers, and healthcare
                    oversight agencies. We've ensured that the health information exchange meets HIPAA privacy and security standards, thereby ensuring that patient privacy concerns are addressed. BOWlink has
                    embraced widely accepted health information technology standards that ensure that an investment with BOWlink is a strategic investment for any organization. With its modular approach to
                    implementation, Health-e-Link offers expanded capabilities for <a href="<c:url value='/product-suite/health-e-data'/>" title="healthcare registry systems">healthcare registry systems</a>, <a href="<c:url value='/product-suite/doc-u-link'/>" title="DOC-u-Link">document management</a> 
                    and <a href="<c:url value='/product-suite/health-e-web'/>" title="Secuire Email Messaging">secure email messaging</a>.
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
