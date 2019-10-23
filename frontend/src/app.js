// Responsive
var windowW = $(window).width();
function responsive() {
    if (windowW < 576) {
        $('#langbar').prependTo('#mainmenu');
    } else {
        $('#langbar').appendTo('#header .header-top .container .left');
    }
    if ($('.header-alert:visible').length) {
        if(windowW < 1200){
            $('body').css('padding-top', '');
        } else{

        var alertH = $('.header-alert').outerHeight();
        var headerH = $('#header').outerHeight();
        $('body').css('padding-top', alertH);
        }
    }
};
$(document).ready(function () {
    responsive();
});
$(window).resize(function () {
    windowW = $(window).width();
    responsive();
});
/* Header alert */
$('.header-alert .alert').on('closed.bs.alert', function () {
    responsive();
})
