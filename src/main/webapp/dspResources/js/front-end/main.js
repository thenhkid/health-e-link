require.config({
	urlArgs: "bust=" + (new Date()).getTime(),
	paths: {
		'jquery' : '../vendor/jquery-1.10.1',
		'bootstrap' : '../vendor/bootstrap',
		'responsive-tables' : '../vendor/responsive-tables',
		'mediaModal' : '../mediaModal',
		'overlay' : '../overlay',
		'sprintf' : '../vendor/sprintf',
		'fixed-header' : '../fixed-header',
		'moment' : '../vendor/moment',
		'daterangepicker' : '../vendor/daterangepicker'
	},
	shim: {
		'bootstrap': ['jquery'],
		'responsive-tables': ['jquery'],
		'daterangepicker': ['jquery', 'bootstrap']
	}
});

define(['jquery', 'fixed-header', 'moment', 'bootstrap', 'responsive-tables', 'mediaModal', 'overlay', 'daterangepicker'], function ($, fixedHeader, moment) {

	var primaryNav = $('.primary-nav');
	var primaryNavHeight = primaryNav.outerHeight();
        
        $.ajaxSetup({
            cache: false
        });
    
        // left nav fixed nav bar
	$('.fixed-region').affix({
		offset: {
			top: function () {
				return $('.main-container').position().top + primaryNavHeight;
			},
			bottom: function () {
				// calculate how far down the content section goes down the page
				var bottomNum = $('.wrap').outerHeight() - ($('.main-container').position().top + $('.main-container').outerHeight());
				return bottomNum;
			}
		}
	});
        
        // tooltip demo
	$(document).tooltip({
		selector: "[data-toggle=tooltip]",
		container: "body"
	});

	// popover demo
	$(document).popover	({
		selector: "[data-toggle=popover]",
		container: "body"
	});

	// overwrite scrollspy to get rid of activating parent list items
	$.fn.scrollspy.Constructor.prototype.activate = function (target) {
		this.activeTarget = target;

		$(this.selector)
			.parents('.active')
			.removeClass('active');

		var selector = this.selector + '[data-target="' + target + '"],' + this.selector + '[href="' + target + '"]';

		var active = $(selector)
			.parent() //EDIT//
			.addClass('active');

		if (active.parent('.dropdown-menu').length)  {
			active = active
			.closest('li.dropdown')
			.addClass('active');
		}

		active.trigger('activate');
	};
        
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


	// left nav scrollspy
	$('body').scrollspy({ target: '#active-page-nav' });

	// initialized fixed header
	fixedHeader.init();

});