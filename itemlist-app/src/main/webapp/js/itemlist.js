$(document).ready(function() {

	$('#imginthefooter').click(function() {
		var href = $(this).find("a").attr("href");
		if (href) {
			window.location = href;
		}
	});

	$('#imginthefooter').hover(function() {
		$(this).css('cursor', 'pointer');
	});

});

$(function() {
	$(".thumbnail").draggable();
});
