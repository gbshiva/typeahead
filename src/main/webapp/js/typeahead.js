/**
 * 
 */
$(function() {

function log( message ) {
      $( "<div>" ).text( message ).prependTo( "#log" );
      $( "#log" ).scrollTop( 0 );
    }
 
    $( "#city" ).autocomplete({
      source: function( request, response ) {
    	  var param = 'pattern='+request;  
        $.ajax({
          url: "http://localhost:8080/typeahead/search/services/bigmemory/getHCPNames",
          dataType: "json",
          data: {
              pattern: request.term
            },
          success: function( data ) {
            response( data );
          },
          error: function( data){
        	  alert(data)
        	  response (" ");
          }
        });
      },
      minLength: 2,
      select: function( event, ui ) {
        log( ui.item ?
          "Selected: " + ui.item.label :
          "Nothing selected, input was " + this.value);
      },
      open: function() {
        $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
      },
      close: function() {
        $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
      }
    });
});
 