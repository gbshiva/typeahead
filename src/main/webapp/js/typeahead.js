/**
 * 
 */
$(function() {

	function log(message) {
		//alert(message)
		$("<div>").text(message).prependTo("#log");
		$("#log").scrollTop(0);
	}
	
	function baseUrl() {
		   var href = window.location.href.split('/');
		   return href[0]+'//'+href[2]+'/';
		}

	$("#name")
	.autocomplete(
			{
				source : function(request, response) {
					var param = 'pattern=' + request;
					$
					.ajax({
						url : baseUrl()+"typeahead/search/services/bigmemory/getHCPNames",
						dataType : "json",
						data : {
							pattern : request.term
						},
						success : function(data) {
							response(data);
						},
						error : function(data) {
							// alert(data)
							response(" ");
						}
					});
				},
				minLength : 2,
				select : function(event, ui) {
					log(ui.item ? "Selected: " + ui.item.label
							: "Nothing selected, input was "
								+ this.value);
				},
				open : function() {
					$(this).removeClass("ui-corner-all").addClass(
					"ui-corner-top");
				},
				close : function() {
					$(this).removeClass("ui-corner-top").addClass(
					"ui-corner-all");
				}
			});

	
	
	
	
	function gridinit() {
		// alert(JSON.stringify(gdata));
		var iurl = baseUrl()+"typeahead/search/services/bigmemory/searchHCO?name=doesnotexist";
		
			$("#jqGrid").jqGrid(
					{
						url : iurl,
						datatype : "json",
						jsonReader : {
							repeatitems : false,
							root : function(obj) {
								return obj;
							},
							page : function(obj) {
								return 1;
							},
							total : function(obj) {
								return 1;
							},
							records : function(obj) {
								return obj.length;
							}
						},
						colNames : [ 'NPI ID', 'Name', 'Speciality', 'Organization',
						             'Address', 'City', 'State', 'Zip' ],
						             colModel : [ {
						            	 name : 'professionalEnrollmentID'

						             }, {
						            	 name : 'displayName' 
						             }, {
						            	 name : 'primarySpeciality'
						             }, {
						            	 name : 'organization'
						             }, {
						            	 name : 'address1'
						             }, {
						            	 name : 'city'
						             }, {
						            	 name : 'state'
						             }, {
						            	 name : 'zipcode'
						             } ],
						             rowNum : 50,
						             pager : "#jqGridPager",
						             rowList : [ 10, 50, 100 ],
						             gridview : true,
						             viewrecords : true,
						             loadError : function(xhr, st, err) {
						            	 alert("Unable to Load JQGrid");
						             },
						             loadOnce : false,
						             shrinkToFit:false,
						             forceFit:true,
						             autowidth:true

					});

	}

	

	$('#search')
	.on(
			'click',
			function(e) {

				var name = $('#name').val();
				var speciality = $('#speciality').val();
				var address = $('#address').val();
				var city = $('#city').val();
				var state = $('#state').val();
				var zipcode = $('#zipcode').val();

				var iurl = baseUrl()+"typeahead/search/services/bigmemory/searchHCO?name="
					+ name
					+ "&speciality="
					+ speciality
					+ "&address="
					+ address
					+ "&city="
					+ city
					+ "&state=" + state + "&zipcode=" + zipcode;

				//alert(iurl);
				
				$("#jqGrid").jqGrid('setGridParam',
						{
							url : iurl,
							datatype : "json",
							jsonReader : {
								repeatitems : false,
								root : function(obj) {
									return obj;
								},
								page : function(obj) {
									return 1;
								},
								total : function(obj) {
									return 1;
								},
								records : function(obj) {
									return obj.length;
								}
							}
							

						}).trigger("reloadGrid");

			});
	gridinit();

});
