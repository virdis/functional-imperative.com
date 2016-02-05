

$( document ).ready(function(){
  if((window.location.href.indexOf("projectdetails")) > -1) {
// forceX , forceY to set the layout
    $.ajax({
      url: '/svirdi/projecttimeseries',
      data: {
        format: 'json'
      },
      success: function(data) {
        //console.log("Data "+JSON.stringify(data));
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


      },
      type: 'GET'

    });

  }
});

//Generate some nice data.
