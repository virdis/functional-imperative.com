$( document ).ready(function() {
    console.log( "ready!" );
    console.log( "url" +window.location.href );
    if ((window.location.href.indexOf("/gitDiscover") > -1))  {
        $('.list-group-item').click(function(event){
            console.log($( event.currentTarget ).data( "name" ));
            window.location.href= "/svirdi/" + "projecttimeseries/" + encodeURIComponent( $(event.currentTarget).data( "name" ) ) +"/"+ "Jan"
        });
    }

});
