
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container main-container" role="main">
    <div class="row">
        <aside class="col-md-3 col-sm-3 secondary hidden-xs">
            <div class="fixed-region">
                <div class="module sidebar-module">
                    <dl class="vcard info-list">
                        <dt>Contact Us By Phone</dt>
                        <dd class="tel">(508) 721-1977</dd>

                        <dt>Contact Us By Fax</dt>
                        <dd class="tel">(508) 721-1978</dd>

                        <dt>Contact Us By Email</dt>
                        <dd class="email"><a href="" title="">info@health-e-link.net</a></dd>

                        <dt>Connect</dt>
                        <dd class="margin-small top">
                            <a href="" title="" class="icon-social icon-facebook ir-inline">Facebook</a>
                            <a href="" title="" class="icon-social icon-linked-in ir-inline">Linked In</a>
                            <a href="" title="" class="icon-social icon-twitter ir-inline">Twitter</a>
                        </dd>
                    </dl>
                </div>
            </div>
        </aside>
        <div class="col-md-9 col-md-offset-0 col-sm-8 col-sm-offset-1 page-content">
            <h2>Oops, An error has occurred.</h2>
            <p>
                Lorem ipsum <em>emphasised text</em> dolor sit amet, <strong>strong text</strong> 
                consectetur adipisicing elit, <abbr title="">abbreviated text</abbr> sed do eiusmod tempor 
                incididunt ut labore et dolore magna aliqua. Ut 
                <q>quoted text</q> enim ad minim veniam, quis nostrud exercitation <a href="/">link text</a> 
                ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute 
                <ins>inserted text</ins> irure dolor in reprehenderit in voluptate velit esse cillum 
                dolore eu fugiat nulla pariatur. Excepteur sint occaecat <code>code text</code> cupidatat 
                non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            </p>
            <p>
                ${messageBody}
            </p>
        </div>
    </div>
</div>
