<%-- 
    Document   : contact
    Created on : Mar 13, 2014, 12:50:01 PM
    Author     : chadmccue
--%>

<div role="main">

    <div class="container main-container">
        <div class="row">
            <aside class="col-md-3 col-sm-3 secondary hidden-xs">
                <div class="fixed-region">
                    <div class="module sidebar-module">
                        <dl class="vcard info-list">
                            <dt>Contact Us By Phone</dt>
                            <dd class="tel">(617) 994-9869</dd>

                            <%--<dt>Contact Us By Fax</dt>
                            <dd class="tel">(508) 721-1978</dd>

                            <dt>Contact Us By Email</dt>
                            <dd class="email"><a href="" title="">info@health-e-link.net</a></dd>

                            <dt>Connect</dt>
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

                <h2>Contact Form</h2>

                <p>
                    The Massachusetts Department of Public Health (DPH) has staffed a help desk to receive and process requests from users of the e-Referral system. If you have questions or issues that 
                    require assistance, please feel free to contact DPH using the telephone number on the left or alternatively, fill out and submit the form provided below. 
                </p>
                <div class="form-container">
                    <div class="form-group">
                        <label for="name">Name *</label>
                        <input id="name" name="name" class="form-control" type="text" />
                    </div>
                    <div class="form-group">
                        <label for="company">Company / Organization *</label>
                        <input id="company" name="company" class="form-control" type="text" />
                    </div>
                    <div class="form-group">
                        <label for="address">Address *</label>
                        <input id="address" name="address" class="form-control" type="text" />
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-3">
                                <label for="city">City</label>
                                <input id="city" name="city" class="form-control" type="text" />
                            </div>
                            <div class="col-xs-2">
                                <label for="state">State </label>
                                <input id="state" name="state" class="form-control" type="text" />
                            </div>
                            <div class="col-xs-2">
                                <label for="zip">Zip</label>
                                <input id="zip" name="zip" class="form-control" type="text" />
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-4">
                                <label for="phone">Phone</label>
                                <input id="phone" name="phone" class="form-control" type="text" />
                            </div>
                            <div class="col-xs-2">
                                <label for="ext">Ext </label>
                                <input id="ext" name="state" class="form-control" type="text" />
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-xs-4">
                                <label for="phone">Fax</label>
                                <input id="phone" name="phone" class="form-control" type="text" />
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email">Email *</label>
                        <input id="email" name="email" class="form-control half" type="text" />
                    </div>
                    
                    <div class="form-group">
                        <label for="comments">Comments</label>
                        <textarea id="comments" class="form-control"></textarea>
                    </div>
                    <div class="form-group">
                        <input type="submit" class="btn btn-primary" value="Send"/>
                    </div>
                </div>

            </div>

        </div>
    </div>
</div>
