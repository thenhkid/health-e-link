<%-- 
    Document   : request
    Created on : Jun 16, 2016, 9:29:19 AM
    Author     : chadmccue
--%>

<div class="container main-container">
    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                
                <div class="col-md-6">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">Start Date</h2>
                        </div>
                        <div class="panel-body">
                            <div class="input-group">
                                <input type="text" id="startSearchDate" class="form-control" />
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar bigger-110"></i></span>
                            </div>
                        </div>
                    </section>
                </div>
                <div class="col-md-6">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">End Date</h2>
                        </div>
                        <div class="panel-body">
                            <div class="input-group">
                                <input type="text" id="endSearchDate" class="form-control" />
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar bigger-110"></i></span>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">Report Type</h2>
                        </div>
                        <div class="panel-body">
                            <select class="form-control" >
                                <option value=""> Detail Report</option>
                                <option value=""> Summary Count Report</option>
                            </select>
                        </div>
                    </section>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">Resources</h2>
                        </div>
                        <div class="panel-body">
                            <select class="form-control" >
                                <option value="">YMCA in Columbia</option>
                            </select>
                        </div>
                    </section>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <section class="panel panel-default">
                        <div class="panel-heading">
                            <h2 class="panel-title">Program</h2>
                        </div>
                        <div class="panel-body">
                            <select class="form-control" >
                                <option value="">- Select Program - </option>
                                <option value="">CDSMP</option>
                                <option value="">Diabetes Prevention</option>
                                <option value="">Exercise</option>
                                <option value="">Nutrition</option>
                            </select>
                        </div>
                    </section>
                </div>
            </div>
            <input type="button" id="enrollClients" class="btn btn-primary" value="Generate Report"/>
        </div>
    </div>
</div>
