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

                <h3>Privacy and Security Statement:</h3>
                <p>
                    Health-e-Link takes your privacy seriously. Please read the following to learn more about our policy. 
                </p>
                
                <h3>What this Privacy Policy Covers</h3>
                <p>
                    This Privacy Policy covers Health-e-Link's treatment of personally identifiable information that Health-e-Link collects when you are on the Health-e-Link site. 
                </p>
                
                <h3>Information Collection and use</h3>
                <p>
                    Health-e-Link may collect personally identifiable information when you contact us or join our email list such as your name, company name, email address, and other information.  
                </p>
                
                <h3>Information Sharing and Disclosure</h3>
                <p>
                    Health-e-Link will not sell trade or rent your personally identifiable information to anyone. [Health-e-Link may provide aggregate statistics about its customers, traffic patterns, and 
                    related site information to reputable third-party vendors, but these statistics will include no personally identifying information.]

                    Health-e-Link may also release account information when we believe, in good faith that such release is reasonably necessary to (i) comply with the law, (ii) enforce or apply the terms of 
                    our site use agreement or (iii) protect the rights, property or safety Health-e-Link , our users or others.  
                </p>
                
                <h3>Links</h3>
                <p>
                    This site contains external links to other web sites. Health-e-Link is not responsible for the privacy policies of these sites and the information they may gather expressly or automatically. 
                </p>
                
                <h3>Privacy Policy and Site Use Agreement</h3>
                <p>
                    Your use of this site constitutes your consent to the collection and use of the information described in this Privacy Policy by Health-e-Link .  
                </p>
                
                <h3>Changes to this Privacy Policy</h3>
                <p>
                   Health-e-Link may edit this policy from time to time. If we make any substantial changes we will notify you by posting a prominent announcement on our pages. 
                </p>
                
                <h3>Questions or suggestions</h3>
                <p>
                    If you have any questions or suggestions send an email to <a href="mailto:info@health-e-link.net">info@health-e-link.net</a>.  
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
