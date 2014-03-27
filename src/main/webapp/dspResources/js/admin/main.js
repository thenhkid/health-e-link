
require.config({
	urlArgs: "bust=" + (new Date()).getTime(),
	paths: {
		'jquery' : '../vendor/jquery-1.10.1.min',
		'bootstrap' : '../vendor/bootstrap',
		'responsive-tables' : '../vendor/responsive-tables',
		'mediaModal' : '../mediaModal',
		'overlay' : '../overlay',
		'sprintf' : '../vendor/sprintf',
		'moment' : '../vendor/moment',
		'daterangepicker' : '../vendor/daterangepicker'
	},
	shim: {
		'bootstrap': ['jquery'],
		'responsive-tables': ['jquery'],
		'daterangepicker': ['jquery', 'bootstrap']
	}
});



define(['jquery', 'moment', 'bootstrap', 'responsive-tables', 'mediaModal', 'overlay', 'daterangepicker'], function ($, moment) {
    
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
        
        $('.date-range-picker-trigger').daterangepicker(
	{
            ranges: {
                    'See All': [$('#fromDate').attr('rel2'), moment()],
                    'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                    'Last 7 Days': [moment().subtract('days', 6), moment()],
                    'Last 30 Days': [moment().subtract('days', 29), moment()],
                    'This Month': [moment().startOf('month'), moment().endOf('month')],
                    'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
            },
            startDate: $('#fromDate').attr('rel'),
            endDate: $('#toDate').attr('rel')
            },
            function(start, end) { 
                $('.daterange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                $('.daterange span').attr('rel',start.format('MMM DD 00:00:00 YYYY'));
                $('.daterange span').attr('rel2',end.format('MMM DD 23:59:59 YYYY'));
                searchByDateRange();
            }
	);


});