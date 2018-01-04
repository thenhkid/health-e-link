/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require(['./main'], function () {
    require(['jquery'], function ($) {
        $(document).ready(function () {});


            var data = [
                {   
                    label: "DPP",
                    value: 30,
                    color: "#F7464A"
                }, {
                    label: "CDSMP",
                    value: 50,
                    color: "#E2EAE9"
                }, {
                    label: "Exercise",
                    value: 100,
                    color: "#D4CCC5"
                }, {
                    label: "Nutrition",
                    value: 40,
                    color: "#949FB1"
                }

            ]
            
            var options = {
                animation: true
            };

            //Get the context of the canvas element we want to select
            var c = $('#myChart');
            var ct = c.get(0).getContext('2d');
            var ctx = document.getElementById("myChart").getContext("2d");
            /*************************************************************************/
            myNewChart = new Chart(ct).Doughnut(data, options);
            
            
            var data2 = [
                {   
                    label: "DPP",
                    value: 8,
                    color: "#F7464A"
                }, {
                    label: "CDSMP",
                    value: 5,
                    color: "#E2EAE9"
                }

            ]
            
            var options = {
                animation: true
            };

            //Get the context of the canvas element we want to select
            var c2 = $('#myChart2');
            var ct2 = c2.get(0).getContext('2d');
            var ctx2 = document.getElementById("myChart2").getContext("2d");
            /*************************************************************************/
            myNewChart2 = new Chart(ct2).Doughnut(data2, options);
            
            
            var linedata = {
                labels: ["January", "February", "March", "April", "May", "June"],
                datasets: [
                    {
                        label: "My First dataset",
                        fill: false,
                        lineTension: 0.1,
                        backgroundColor: "rgba(75,192,192,0.4)",
                        borderColor: "rgba(75,192,192,1)",
                        borderCapStyle: 'butt',
                        borderDash: [],
                        borderDashOffset: 0.0,
                        borderJoinStyle: 'miter',
                        pointBorderColor: "rgba(75,192,192,1)",
                        pointBackgroundColor: "#fff",
                        pointBorderWidth: 1,
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(75,192,192,1)",
                        pointHoverBorderColor: "rgba(220,220,220,1)",
                        pointHoverBorderWidth: 2,
                        pointRadius: 1,
                        pointHitRadius: 10,
                        data: [65, 59, 80, 81, 56, 55]
                    }
                ]
            };
            
            var c3 = $('#myChart3');
            var ct3 = c3.get(0).getContext('2d');
            var ctx3 = document.getElementById("myChart3").getContext("2d");
            
            var chartInstance = new Chart(ctx3).Line(linedata,options);

    });
});