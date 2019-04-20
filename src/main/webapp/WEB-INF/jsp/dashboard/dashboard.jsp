<%-- 
    Document   : dashboard
    Created on : Jun 16, 2016, 9:01:49 AM
    Author     : chadmccue
--%>


<div class="container main-container">
    <div class="row">
        <div class="col-md-12">
            <div class="row">

                <div class="col-md-4 col-sm-4 col-xs-6">
                    <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Sent Referrals">
                        <div class="panel-body">
                            <span class="stat-number"><a href="/CareConnector/sent" title="Sent Referrals">220</a></span>
                            <h3>Sent Referrals</h3>
                            <a href="/CareConnector/sent" title="Sent Referrals" class="btn btn-primary btn-small">View all</a>
                        </div>
                    </section>
                </div>

                <div class="col-md-4 col-sm-4 col-xs-6">
                    <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Saved Referrals">
                        <div class="panel-body">
                            <span class="stat-number"><a href="/CareConnector/pending" title="Sent Referrals">2</a></span>
                            <h3>Saved Referrals</h3>
                            <a href="/CareConnector/pending" title="Saved Referrals" class="btn btn-primary btn-small">View all</a>
                        </div>
                    </section>
                </div>


                <div class="col-md-4 col-sm-4 col-xs-6">
                   <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Received Feedback Reports">
                        <div class="panel-body">
                            <span class="stat-number"><a href="/CareConnector/inbox" title="Received Feedback Reports">13</a></span>
                            <h3>Received Feedback Reports</h3>
                            <a href="/CareConnector/inbox" title="Received Feedback Reports" class="btn btn-primary btn-small">View all</a>
                        </div>
                    </section>
                </div>

            </div>
        </div>
    </div>
    <div calss='row'>
        <div class="col-md-12" style="margin-top: 20px;">
            <div class="col-md-6 center-text">
                <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Sent Referrals">
                    <div class="panel-heading">Referral Programs Sent</div>
                    <div class="panel-body">
                        <canvas id="myChart" width="200" height="200"></canvas>
                    </div>
                </section>
            </div>
            <div class="col-md-6">
                <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Sent Referrals">
                    <div class="panel-heading">Feedback Reports Received</div>
                    <div class="panel-body">
                        <canvas id="myChart2" width="200" height="200"></canvas>
                    </div>
                </section>
            </div>
        </div>
    </div>
    <div calss='row'>
        <div class="col-md-12" style="margin-top: 20px;">
            <div class="col-md-10 col-md-offset-1">
                <section class="panel panel-default panel-stats" role="widget" aria-labelleby="Sent Referrals">
                    <div class="panel-heading">Referral Programs Sent By Month</div>
                    <div class="panel-body">
                        <canvas id="myChart3" width="800" height="200"></canvas>
                    </div>
                </section>
            </div>
        </div>
    </div>
    </div>  