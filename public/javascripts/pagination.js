$( document ).ready(function() {
    console.log( "ready!" );
    console.log( "url" +window.location.href ); 
    if ((window.location.href.indexOf("/posts/") > -1) || (window.location.href.indexOf("/about") > -1))  {
        console.log("");
       $( ".pager" ).remove()
    }
    var pageno, num;
    pageno = window.location.href.split( "?" )[1]
    if ( pageno !== undefined ){
        num = pageno.split( "=" )[1];
    }

    $( ".next a" ).click(function(){

        if ( num !== undefined ) {
            window.location.href = "/svirdi?page=" + (1 + parseInt(num));
        } else {
            window.location.href = "/svirdi?page=2";
        }
    });
});
