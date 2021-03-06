$( document ).ready(function(){
  if((window.location.href.indexOf("projectdetails")) > -1) {
    var month, etype;
    $( "#month li a" ).click(function(event){
      console.log("Event Text " +$(event.target).text());
      month = $(event.target).text();
      $( "#month :button" ).text(month);
    }); 

    $( "#tform" ).click(function(){
      if ( month === undefined || $( "#rname" ).val().length < 3) {
        alert("Fill out complete form before sumbit !!!!")
      } else {
        $.ajax({
          type: 'GET',
          url: '/svirdi/searchts/' +encodeURIComponent($( "#rname" ).val()) +'/' + month,
          data: 'json',
          success: function(data) {
            //console.log("Search Data "+JSON.stringify(data));
            var chart;
            nv.addGraph(function() {
              chart = nv.models.multiBarChart()
                //.barColor(d3.scale.category20().range())
                .xDomain([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31])
                .duration(300)
                .margin({bottom: 100, left: 70})
                .rotateLabels(45)
                .groupSpacing(0.1);
              chart.reduceXTicks(false).staggerLabels(true);
              chart.xAxis
                .scale(d3.time.scale().range([1,31]))
                .axisLabel("Days of Month")
                .axisLabelDistance(-5)
                .showMaxMin(false)
                .tickFormat(d3.format(",f"))
                ;
              chart.yAxis
                .axisLabel("Event Types")
                .axisLabelDistance(-10)
                .tickFormat(d3.format(',f'))
                ;
              chart.dispatch.on('renderEnd', function(){
                nv.log('Render Complete');
              });
              $('#chart1 svg').empty();
              d3.select('#chart1 svg')
                .datum(data)
                .call(chart);
              nv.utils.windowResize(chart.update);
              chart.dispatch.on('stateChange', function(e) {
                nv.log('New State:', JSON.stringify(e));
              });
              chart.state.dispatch.on('change', function(state){
                nv.log('state', JSON.stringify(state));
              });
              return chart;
            });
          }
        });
        $.ajax({
          url: '/svirdi/userActivity?prjName=' +$( "#rname" ).val(),
          data: {
            format: 'json'
          },
          success: function(data) {
             //console.log("Pie Data "+JSON.stringify(data));

             nv.addGraph(function() {
                  var chart = nv.models.pieChart()
                      .x(function(d) { return d.label })
                      .y(function(d) { return d.value })
                      .showLabels(true);
                    $( ".testBlock svg" ).empty();
                    d3.select(".testBlock svg")
                        .datum(data)
                        .transition().duration(350)
                        .call(chart);

                  return chart;
                });
          },
          type: 'GET'
          });

          $.ajax({
              url: '/svirdi/comments?prjName=' +$( "#rname" ).val(),
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

  }
});
