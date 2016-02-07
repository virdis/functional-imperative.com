
$(document).ready(function () {
    if((window.location.href.indexOf("projectdetails")) > -1) {
        $.ajax({
            url: '/svirdi/comments',
            data: 'json',
            success: function(data) {
                  //Donut chart example
                  console.log("Donut Data "+JSON.stringify(data));
                    nv.addGraph(function() {
                      var chart = nv.models.pieChart()
                          .x(function(d) { return d.label })
                          .y(function(d) { return d.value })
                          .showLabels(true)     //Display pie labels
                          .labelThreshold(.05)  //Configure the minimum slice size for labels to show up
                          .labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
                          .donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
                          .donutRatio(0.35)     //Configure how big you want the donut hole size to be.
                          ;

                        d3.select("#test2")
                            .datum(data)
                            .transition().duration(350)
                            .call(chart);

                      return chart;
                    });

            }
          });
    }


});