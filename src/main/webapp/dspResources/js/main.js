require.config({
	urlArgs: "bust=" + (new Date()).getTime(),
	paths: {
		'jquery' : 'vendor/jquery-1.10.1.min',
		'bootstrap' : 'vendor/bootstrap.min',
		'responsive-tables' : 'vendor/responsive-tables',
	},
	shim: {
		'bootstrap': ['jquery'],
		'responsive-tables': ['jquery']
	}
});

require(['jquery','bootstrap', 'responsive-tables'], function () {
	//console.log('scripts loaded');
});