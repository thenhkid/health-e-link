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
                
                <h4>Hosted and Licensed Solutions</h4>
                <p>
                    Health-e-link's technology-based solutions including Care Connect, Universal HIE and Clinical Data Warehouse. Health-e-link solutions are hosted within our secure data center and are available for customer 
                    use on an ongoing basis. Clients can be configured and operational on Health-e-link within a matter of days. Licensed software may be installed, configured and operated by your IT staff with 
                    Health-e-link resources available for technical support as required. Health-e-link solutions are developed on Java and Microsoft SQL Server platforms.
                </p>
                <br />
                <h4>Modular Design</h4>
                <p>
                    The Health-e-link Suite is modular and highly configurable to promote flexibility and ensure that our solution is configured to meet your specific needs. From network interfaces to user interfaces, 
                    Health-e-link can be configured to align with your existing capabilities, thereby minimizing the burden we impose on your existing infrastructure and IT staff. Health-e-link modular features are only 
                    installed if they are part of your network solution, simplifying all aspects of network management. 
                </p>
                <br />
                <h4>Pilot and Phased Implementations</h4>
                <p>
                    At Health-e-link, we pride ourselves on providing personalized, customized services that align with our client's unique needs and capabilities. Health-e-link's hosted solutions often serve as a 
                    pilot platform for customers that want to evaluate Health-e-link capabilities. Health-e-link implementations can range from straightforward implementations of our hosted Care Connect system that can be 
                    accomplished in a matter of days, to state-wide implementations of our clinical data warehouse that may take several months to complete.
                </p>
                <br />
                <h4>Health-e-link Support Staff</h4>
                <p>
                    For our hosted solution, Health-e-link technical support staff monitors and maintains the Health-e-link network on an ongoing basis. Health-e-link operations staff ensures that all data exchange 
                    activity meets customer expectations based on predefined service level agreements. Health-e-link customer service staff provides ongoing help desk and training resources and also serve as the primary 
                    point of contact for all customer issues. Support capabilities developed at Health-e-link are transferrable to your staffs through our training services if you choose to license and operate Health-e-link 
                    within your facilities.
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
