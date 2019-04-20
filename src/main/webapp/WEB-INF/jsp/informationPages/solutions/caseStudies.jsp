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
                
                <h3 style="color: red">The Challenge:</h3>
                <h4>Data Exchange From 20+ PM and EMR Systems</h4>
                
                <h3>Health-e-Link Solves a Data Exchange Dilemma for CFHC</h3>
                <p>
                    The California Family Health Council (CFHC) is a non-profit healthcare oversight organization responsible for providing oversight and technical assistance to healthcare providers that are affiliated with 
                    the Title X Family Planning Program. CFHC reports aggregate patient information from over 300 clinical sites across the State of California to the Office of Family Planning (OFP). Recently, 
                    the OFP asked CFHC to promote the electronic capture and reporting of patient data with their affiliated providers in order
                </p>

                <h3 style="color: red">Solution:</h3>
                
                <h3>The Challenge: Data Exchange from 20+ PM and EMR Systems</h3>
                <p>
                    The CFHC clinical sites collectively used more than 20 different practice management and electronic medical record (EMR) systems for capturing patient data. These disparate clinical sites had no 
                    consistency in technical capabilities, data interfaces, record format, or medical code sets used by their systems. The challenge was to find a solution that would integrate these sites to a single 
                    data repository, where centralized resources could be leveraged to translate, store, manage, and report the data collected from these sites with a low-commitment integration solution. Health-e-link was 
                    engaged to provide a solution that would capture the data from the source systems, translate the data to a consistent standard, and feed the data to a central data repository. Using the data exchange 
                    features of the Health-e-Link Exchange module, CFHC integrated 40 sites during the first year and has plans to rollout to 250 additional sites. Using Health-e-Link, CFHC and their clinical partners 
                    implemented a solution at little or no incremental cost to the clinical sites using their current in-house staff. The resulting system dramatically improves CFHC's and affiliated agencies' ability to 
                    report and analyze their data.
                </p>
                
                <blockquote>
                    <p>
                        "Our agency requires patient data to be submitted annually from over 300 clinics statewide. Our goal was to build a centralized data system to collect over 1 million visit level encounter records from various clinic systems, store the information, and conduct data analysis and reporting. 
                        <br/><br/>
                        Health-e-link offered the perfect solution. Health-e-link allows us to accept data in any format from over 20 different clinic computer systems and translate it to a consistent format. Since 
                        Health-e-link provides the data translation, it allows for great flexibility and a minimum amount of work required by clinic staff. In terms of support, Health-e-link staff offers exceptional 
                        customer service for their products. Any issues that arise are handled promptly and efficiently. Health-e-link staff goes to great lengths to ensure customer satisfaction"               
                    </p>
                    <footer>Tanya Parker, Centralized Data System Project Manager, CFHC</footer>
                </blockquote>
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