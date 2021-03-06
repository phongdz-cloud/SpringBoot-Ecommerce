var buttonLoadStates;
var dropDownCountriesOfStates;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function() {
	buttonLoadStates = $("#buttonLoadCountriesOfStates");
	dropDownCountriesOfStates = $("#dropDownCountriesOfStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName")

	buttonLoadStates.click(function() {
		loadCountriesOfStates();
	});

	buttonAddState.click(function() {
		if (buttonAddState.val() == "Add") {
			addState();
		} else {
			changeFormStateToNewOfStates();
		}
	});

	dropDownCountriesOfStates.on("change", function() {
		loadStatesByCountry();
		changeFormStateToSelectedCountryOfStates();
	});

	dropDownStates.on("change", function() {
		changeFormStateToSelectedCountryOfStates();
	});

	buttonUpdateState.click(function() {
		updateState();
	});

	buttonDeleteState.click(function() {
		deleteState();
	})

});

function deleteState() {
	stateId = dropDownStates.val();
	url = contextPath + "states/delete/" + stateId;
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function() {
		$("#dropDownStates option[value='" + stateId + "']").remove();
		changeFormStateToNewOfStates();
		showToastMessage("The states has been deleted");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}


function updateState() {
	if (!validateFormState()) return;
	url = contextPath + "states/save";

	countryName = $("#dropDownCountriesOfStates option:selected").text();
	countryCode = dropDownCountriesOfStates.val().split("-")[1];
	countryId = dropDownCountriesOfStates.val().split("-")[0];
	country = { id: countryId, name: countryName, code: countryId };

	stateId = dropDownStates.val();
	stateName = fieldStateName.val();
	jsonData = { id: stateId, name: stateName, country: country };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropDownStates option:selected").text(stateName);
		showToastMessage("The state has been updated");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});

}

function validateFormState() {
	formState = document.getElementById("formState");
	if (!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}

	return true;
}

function addState() {
	if (!validateFormState()) return;
	url = contextPath + "states/save";

	countryName = $("#dropDownCountriesOfStates option:selected").text();
	countryCode = dropDownCountriesOfStates.val().split("-")[1];
	countryId = dropDownCountriesOfStates.val().split("-")[0];
	country = { id: countryId, name: countryName, code: countryId };

	stateName = fieldStateName.val();
	jsonData = { name: stateName, country: country };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddedState(stateId, stateName);
		showToastMessage("The new state has been added");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function selectNewlyAddedState(stateId, stateName) {

	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

	$("#dropDownStates option[value='" + optionValue + "']").prop("selected", true);

	fieldStateName.val("").focus();
};

function loadCountriesOfStates() {
	url = contextPath + "countries/list";
	console.log("Call ajax!");
	$.get(url, function(responseJSON) {
		dropDownCountriesOfStates.empty();
		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountriesOfStates);
		});
	}).done(function() {
		buttonLoadStates.val("Refresh Country List");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function loadStatesByCountry() {
	optionValue = dropDownCountriesOfStates.val();
	countryId = optionValue.split("-")[0];
	countryName = optionValue.split("-")[1];
	url = contextPath + "states/list_by_country/" + countryId;
	$.get(url, function(responseJSON) {
		dropDownStates.empty();
		$.each(responseJSON, function(index, state) {
			optionValue = state.id;
			$("<option>").val(optionValue).text(state.name).appendTo(dropDownStates);
		});
	}).done(function() {
		showToastMessage("All states have been loaded for country " + countryName);
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function changeFormStateToNewOfStates() {
	buttonAddState.val("Add");
	labelStateName.text("State Name:");

	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);

	fieldStateName.val("").focus();
}

function changeFormStateToSelectedCountryOfStates() {
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);

	labelStateName.text("State Name: ");
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);

}



function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}