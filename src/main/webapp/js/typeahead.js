/**
 * 
 */
$(function() {

	function log(message) {
		//alert(message)
		$("<div>").text(message).prependTo("#log");
		$("#log").scrollTop(0);
	}

	$("#name")
	.autocomplete(
			{
				source : function(request, response) {
					var param = 'pattern=' + request;
					$
					.ajax({
						url : "http://localhost:8080/typeahead/search/services/bigmemory/getHCPNames",
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
		var iurl = "http://localhost:8080/typeahead/search/services/bigmemory/searchHCO?name=doesnotexist";
		
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
						colNames : [ 'PID', 'Name', 'Speciality', 'Organization',
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
						             rowNum : 20,
						             pager : "#pager",
						             rowList : [ 10, 20, 30 ],
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

				var iurl = "http://localhost:8080/typeahead/search/services/bigmemory/searchHCO?name="
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
