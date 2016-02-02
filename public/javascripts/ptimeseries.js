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
            console.log("Search Data "+JSON.stringify(data));
          }
        }); 
      } 
    });

  }
});
