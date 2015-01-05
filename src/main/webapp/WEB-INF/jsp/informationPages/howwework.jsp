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
                
                <h4>Hosted and Licensed Solutions</h4>
                <p>
                    Health-e-link's technology-based solutions including eReferral, Universal HIE and Clinical Data Warehouse are hosted within our secure data center and are available for customer use on an ongoing basis. 
                    Health-e-link's hosted solutions often serve as a pilot platform for customers that want to evaluate Health-e-link capabilities. Health-e-link also licenses our technology solutions to those entities or 
                    organizations that have the resources to configure, operate and manage the network on their own. Health-e-link solutions are developed on Java and Microsoft SQL Server platforms.
                </p>
                <br />
                <h4>Health-e-link Support Staff</h4>
                <p>
                    Health-e-link technical support staff monitors and maintains the Health-e-link network on an ongoing basis. Health-e-link operations staff ensures that all data exchange activity meets customer 
                    expectations based on predefined service level agreements. Health-e-link customer service staff provides ongoing help desk and training resources and also serve as the primary point of contact for 
                    all customer issues.
                </p>
                <br />
                <h4>Consulting Services</h4>
                <p>
                    Health-e-link staff collaborates with our customers to understand partnerships within your network of care, the level of maturity of your collaborative business models, your vision for integrated 
                    care models, current and planned utilization of technology, and to assist you with the development of transformative care delivery and management models enabled by our technology solutions.
                </p>
                <p>
                    At Health-e-link, we work closely with our clients, large and small, because we realize the significant impact that healthcare information integration can have on health outcomes, clinical workflow, 
                    and resource utilization. Our consulting services and associated project management processes are designed to foster community collaboration and to help all stakeholders - patients, healthcare providers, 
                    community-based organizations and public health agencies achieve their goals.
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
