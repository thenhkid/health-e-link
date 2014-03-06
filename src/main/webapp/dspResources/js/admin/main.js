
require.config({
	urlArgs: "bust=" + (new Date()).getTime(),
	paths: {
		'jquery' : '../vendor/jquery-1.10.1.min',
		'bootstrap' : '../vendor/bootstrap',
		'responsive-tables' : '../vendor/responsive-tables',
		'mediaModal' : '../mediaModal',
		'overlay' : '../overlay',
		'sprintf' : '../vendor/sprintf'
	},
	shim: {
		'bootstrap': ['jquery'],
		'responsive-tables': ['jquery']
	}
});



define(['jquery',  'bootstrap', 'responsive-tables', 'mediaModal', 'overlay'], function ($) {
    
        $.ajaxSetup({
            cache: false
        });

	// tooltip demo
	$(document).tooltip({
		selector: "[data-toggle=tooltip]",
		container: "body"
	})

	// modify bootstrap modal to handle spacing for scroll bars more elegantly
	$(document).on('show.bs.modal',  '.modal', function () {
		var windowHeight = $(window).height();
		var contentHeight = $('.wrap').outerHeight();

		// if the window is NOT scrollable, remove the class "modal-open".
		// This gets rid of the right margin added to the page.
		if (windowHeight >= contentHeight) {
			$(document.body).removeClass('modal-open');
		}
	});

	// initalize popovers
	$('[data-toggle=popover]').popover();

	// initalize tooltips
	$('.badge-help').tooltip();

	// Log out confirmation
	$('.logout').on('click', function (e) {
		var confirmed = confirm('Are you sure you want to log out?');
		if (confirmed){
			return true;
		}
		else
		{
			e.preventDefault();
			return false;
		}
	});

	// overlay example:
	// show overlay:
	/*
		$('body').overlay({
			glyphicon : 'floppy-disk',
			message : 'Saving...'
		});
	*/

	// hide overlay:
	// $('body').overlay('hide');

});