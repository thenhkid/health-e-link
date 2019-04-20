<%-- 
    Document   : providerMap
    Created on : Jan 30, 2015, 2:44:44 PM
    Author     : chadmccue
--%>

<div role="main">

    <div class="container main-container">
        <div class="row">
            <div>
                <div class="pull-left">
                    <span>Pins might be overlapped, zoom in and move the map to see all partners.</span> <br />
                    <span><strong>${totalPartners} Total Partners</strong></span>
                </div>
                <div class="pull-right">
                    <p>
                        <img src="/dspResources/img/front-end/location-pin-provider.png" /> = Healthcare Provider<br />
                        <img src="/dspResources/img/front-end/location-pin-cbo.png" /> = CBO
                    </p>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div id="map" style="height:750px; border: 2px solid #ccc;"></div>
            </div>
        </div>
    </div>
</div>