<%-- 
    Document   : messageDetailsForm
    Created on : Dec 12, 2013, 2:47:34 PM
    Author     : chadmccue
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container main-container" role="main">
    <div class="row">
        <div class="col-md-12 page-content">

            <ol class="breadcrumb">
                <li><a href="#">My Account</a></li>
                <li><a href="#">eRG</a></li>
                <li class="active">Create New Referral</li>
            </ol>

            <h2 class="form-title">Create New Message</h2>
            <form role="form" class="form">

                <div class="panel-group form-accordion" id="accordion">

                    <div class="panel panel-default panel-form">
                        <div class="panel-heading">
                            <h3 class="accordion-toggle disabled" data-toggle="collapse" data-parent="#accordion" href="#collapseThree"> Referral Details </h3>
                        </div>
                        <div id="collapseThree" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <div class="form-section row">
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Originating Organization: </h4>
                                        <dl class="vcard">
                                            <c:forEach items="${transaction.transactionRecords[0]}" var="fromFields">
                                                <dd>${fromFields.fieldLabel}</dd>
                                            </c:forEach>
                                        </dl>
                                    </div>
                                    <div class="col-md-6">
                                        <h4 class="form-section-heading">Recipient Organization:</h4>
                                        <dl class="vcard">
                                             <c:forEach items="${transaction.transactionRecords[1]}" var="fromFields">
                                                <dd>${fromFields.fieldLabel}</dd>
                                            </c:forEach>
                                        </dl>
                                    </div>
                                </div>
                                <div class="form-section row">
                                    <div class="col-md-12">
                                        <h4 class="form-section-heading">Form Section Title</h4>
                                        <div class="form-group">
                                            <label for="fieldNumber">Configuration Status *</label>
                                            <div>
                                                <label class="radio-inline form-control-disabled">
                                                    <input type="radio" disabled=""> Active
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio"> Clear Error
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="macro">Organization *</label>
                                            <select id="macro" class="form-control half">
                                                <option value="comboBox">ComboBox</option>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Configuration Type *</label>
                                            <div>
                                                <label class="radio-inline">
                                                    <input type="radio"> Source Organization
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio"> Target Organization
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="crosswalk">Message Type *</label>
                                            <select id="fieldNumber" class="form-control half">
                                                <option value="comboBox">ComboBox</option>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label for="fieldA">Configuration Name*</label>
                                            <input id="fieldA" class="form-control" type="text">
                                        </div>
                                    </div>
                                </div>




                                <div class="form-section row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label for="name">Name *</label>
                                            <input id="name" name="name" class="form-control" type="text" />
                                        </div>
                                        <div class="form-group">
                                            <label for="company">Company / Organization *</label>
                                            <input id="fname" name="company" class="form-control" type="text" />
                                        </div>
                                        <div class="form-group">
                                            <label for="address">Address *</label>
                                            <input id="fname" name="address" class="form-control" type="text" />
                                        </div>
                                        <div class="form-group">
                                            <div class="row">
                                                <div class="col-xs-3">
                                                    <label for="city">City</label>
                                                    <input id="fname" name="city" class="form-control" type="text" />
                                                </div>
                                                <div class="col-xs-2">
                                                    <label for="state">State </label>
                                                    <input id="fname" name="state" class="form-control" type="text" />
                                                </div>
                                                <div class="col-xs-2">
                                                    <label for="state">Zip</label>
                                                    <input id="fname" name="state" class="form-control" type="text" />
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
                                            <input id="fname" name="email" class="form-control half" type="text" />
                                            <a href="" title="">Privacy Policy</a>
                                        </div>
                                        <div class="form-group">
                                            <label for="interest">I am Intersted In:</label>
                                            <div class="row">
                                                <div class="col-xs-3">
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox" value="">
                                                            Exchange
                                                        </label>
                                                    </div>
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox" value="">
                                                            Rapid Registry
                                                        </label>
                                                    </div>
                                                </div>
                                                <div class="col-xs-3">
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox" value="">
                                                            Doc-u-Link
                                                        </label>
                                                    </div>
                                                    <div class="checkbox">
                                                        <label>
                                                            <input type="checkbox" value="">
                                                            Referral Link
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12">
                                        <h4 class="form-section-heading">Diagnosis</h4>
                                        <div class="form-group">
                                            <label for="email" class="sr-only">Diagnosis</label>
                                            <textarea class="form-control">
                                            </textarea>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-section row">
                                    <div class="col-md-12">
                                        <div class="form-inline form-inline-varient">
                                            <div class="form-group">
                                                <label for="email">Referral ID</label> <input type="text" value="" class="form-control"/>
                                            </div>
                                            <div class="form-group">
                                                <label for="email">Priority</label>
                                                <input type="text" value="" class="form-control"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <input type="submit" class="btn btn-primary btn-action" value="Send Referral"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        </form>

    </div>
</div>