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
                
                <h3>The Solution to HIPAA-compliant Email Communication</h3>
                <p>
                    Healthcare providers will have increasing access to clinical information about their patients and with it, greater opportunities to share with this information electronically with peers and 
                    providers. Using CommLink's email encryption technology, physicians in your community can collaborate more effectively to achieve accurate, complete treatment plans for their patients.
                </p>

                <h3>Health-e-Web Meets HIPAA Requirements for Secure Email Communication</h3>
                <p>
                    Health-e-Web module provides a secure email communication solution that spans the virtual boundaries between selected healthcare partners in your community. Using state-of-the art, 
                    email encryption and authentication procedures, CommLink provides a web-based secure network for transmitting correspondence, clinical notes and reports, audio files, images and more to 
                    anywhere you can access an email address. 
                    
                    Health-e-Web is a secure email communication solution for sharing clinical records between your network of physicians, providers and payers. RererralLink allows you to create and share 
                    secure email communications that meets the privacy and security concerns for each partner in your community using a pre-defined set of HIPAA compliant rules. Use Health-e-Web to securely: 
                </p>
                <ul>
                    <li>Meet all HIPAA standards for safeguarding patient privacy for storing, transferring and accessing healthcare data.</li>
                    <li>Implement a low-overhead email encryption solution that requires minimal resources to implement and maintain;</li>
                    <li>Send secure, encrypted emails that meet a variety of patient communication needs;</li>
                    <li>Obtain referrals and authorizations for medical services; </li>
                    <li>Enhance physician-to-physician collaboration to develop more thorough care plans with smoother referrals;</li>
                    <li>Provide superior anti-virus and anti-spam protection.</li>
                </ul>
                <blockquote>
                <p>Share patient information seamlessly and securely with physicians, providers and payers.</p>
                </blockquote>
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
