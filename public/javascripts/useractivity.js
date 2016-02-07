$( document ).ready(function(){
  if((window.location.href.indexOf("projectdetails")) > -1) {
    //Regular pie chart example
    $.ajax({
          url: '/svirdi/userActivity',
          data: {
            format: 'json'
          },
          success: function(data) {
             console.log("Pie Data "+JSON.stringify(data));

             nv.addGraph(function() {
                  var chart = nv.models.pieChart()
                      .x(function(d) { return d.label })
                      .y(function(d) { return d.value })
                      .showLabels(true);

                    d3.select(".testBlock svg")
                        .datum(data)
                        .transition().duration(350)
                        .call(chart);

                  return chart;
                });
          },
          type: 'GET'
          });
   }
});
