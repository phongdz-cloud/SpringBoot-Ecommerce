function clearFilter(url) {
	window.location = url;
}

$(document).ready(function() {
	$(".link-delete").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		id = link.attr(nameId);
		$("#yesButton").attr("href", link.attr("href"));
		$("#confirmText").text("Are you sure you want to delete this " + nameTable + " ID: " + id);
		$("#confirmModal").modal();
	});
});
