$( document ).ready(function(){
  if((window.location.href.indexOf("projectdetails")) > -1) {
    var month, etype;
    $( "#month li a" ).click(function(event){
      console.log("Event Text " +$(event.target).text());
      month = $(event.target).text();
      $( "#month :button" ).text(month);
    }); 
    $( "#gevent li a" ).click(function(event){
      console.log("Event Text " +$(event.target).text());
      etype = $(event.target).text();
      $( "#gevent :button" ).text(etype);
    }); 

    $( "#tform" ).click(function(){
      if ( month === undefined || etype === undefined || $( "#rname" ).val().length < 3) {
        alert("Fill out complete form before sumbit !!!!")
      } else {
         
      } 
    });

  }
});
