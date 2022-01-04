
dropdownBrands = $("#brand");
dropdownCategories = $("#category")

$(document).ready(function() {
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});

	getCategoriesForNewForm();

});

function getCategoriesForNewForm(){
	catIdField = $("#categoryId");
	editMode = false;
	
	if(catIdField.length){
		editMode = true;
	}
	if(!editMode) getCategories();
}

function getCategories() {
	brandID = dropdownBrands.val();
	url = brandModuleURL + "/" + brandID + "/categories";
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}

function checkNameUnique(form) {

	urlRest = checkUniqueUrl;
	productName = $("#name").val();
	productId = $("#id").val();
	csrfValue = $("input[name='_csrf']").val();
	params = { id: productId, name: productName, _csrf: csrfValue };

	$.post(urlRest, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "DuplicateName") {
			showWarningModal("There is another product having the name: " + productName);
		} else {
			showErrorModal("Unkown response from server");
		}
	}).fail(function() {
		showErrorModal("Could not connect to the server")
	});
	return false;
}
