/**
 * 
 */
$(function() {

function log( message ) {
	  alert(message)
      $( "<div>" ).text( message ).prependTo( "#log" );
      $( "#log" ).scrollTop( 0 );
    }
 
    $( "#name" ).autocomplete({
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

    function gridinit() {
		
        $("#jqGrid").jqGrid({
			
            colModel: [
                { label: 'OrderID', name: 'OrderID', key: true, width: 75 },
                { label: 'Customer ID', name: 'CustomerID', width: 150 },
                { label: 'Order Date', name: 'OrderDate', width: 150 },
                { label: 'Freight', name: 'Freight', width: 150 },
                { label:'Ship Name', name: 'ShipName', width: 150 }
            ],
			viewrecords: true,
            pager: "#jqGridPager"
        });
    }
       
       gridinit();

    
    
    
    
    
    
    
    
    
});
 