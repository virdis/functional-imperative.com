
//Generate some nice data.
var exData = function exampleData() {
  return stream_layers(3,10+Math.random()*100,.1).map(function(data, i) {
    return {
      key: 'Stream #' + i,
      values: data
    };
  });
}
console.log("data"+JSON.stringify(exData()));
    var chart;
    nv.addGraph(function() {
        chart = nv.models.multiBarChart()
            .barColor(d3.scale.category20().range())
            .duration(300)
            .margin({bottom: 100, left: 70})
            .rotateLabels(45)
            .groupSpacing(0.1)
        ;
        chart.reduceXTicks(false).staggerLabels(true);
        chart.xAxis
            .axisLabel("ID of Furry Cat Households")
            .axisLabelDistance(35)
            .showMaxMin(false)
            .tickFormat(d3.format(',.6f'))
        ;
        chart.yAxis
            .axisLabel("Change in Furry Cat Population")
            .axisLabelDistance(-5)
            .tickFormat(d3.format(',.01f'))
        ;
        chart.dispatch.on('renderEnd', function(){
            nv.log('Render Complete');
        });
        d3.select('#chart1 svg')
            .datum(exData)
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

