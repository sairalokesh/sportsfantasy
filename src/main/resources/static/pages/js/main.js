jQuery(document).ready(function ($) {
    "use strict";
$('.video-popup').magnificPopup({
    type: 'iframe',
    iframe: {
        markup: '<style>.mfp-iframe-holder .mfp-content {max-width: 900px;height:500px}</style>' +
            '<div class="mfp-iframe-scaler" >' +
            '<div class="mfp-close"></div>' +
            '<iframe class="mfp-iframe" frameborder="0" allowfullscreen></iframe>' +
            '</div></div>'
    }
});

$('.single_screen_slide').owlCarousel({
    loop: true,
    margin: 0,
    dots: true,
    autoplay: true,
    autoplayTimeout: 4000,
    smartSpeed: 1000,
    mouseDrag: true,
    touchDrag: false,
    center: true,
    items: 1,
});

$('.list_screen_slide').owlCarousel({
    loop: true,
    responsiveClass: true,
    nav: true,
    margin: 5,
    autoplay: true,
    autoplayTimeout: 4000,
    smartSpeed: 500,
    center: true,
    navText: ['<span class="ti-angle-left"></span>', '<span class="ti-angle-right"></span>'],
    responsive: {
        0: {
            items: 1,
        },
        600: {
            items: 3
        },
        1200: {
            items: 5
        }
    }
});


$('.bg-slide').owlCarousel({
    loop: true,
    nav: false,
    margin: 0,
    autoplay: true,
    autoplayTimeout: 4000,
    smartSpeed: 500,
    items: 1,
    animateIn: 'fadeIn',
    animateOut: 'fadeOut'
});

$('.home_text_slide').owlCarousel({
    loop: true,
    margin: 0,
    dots: true,
    autoplay: true,
    autoplayTimeout: 4000,
    smartSpeed: 1000,
    mouseDrag: true,
    touchDrag: false,
    center: true,
    items: 1,
});
    
$(".carousel-inner .item:first-child").addClass("active");    
$(".mainmenu ul.nav.navbar-nav li a").on("click", function () {
    $(".mainmenu .navbar-collapse").removeClass("in");
});    
    

$('.work-popup').magnificPopup({
    type: 'image',
    removalDelay: 300,
    mainClass: 'mfp-with-zoom',
    gallery: {
        enabled: true
    },
    zoom: {
        enabled: true,
        duration: 300,
        easing: 'ease-in-out',
        opener: function (openerElement) {
            return openerElement.is('img') ? openerElement : openerElement.find('img');
        }
    }
});

$('#myCarousel').carousel({
    interval: 2500
});

$('[id^=carousel-selector-]').click(function () {
var id_selector = $(this).attr("id");
try {
var id = /-(\d+)$/.exec(id_selector)[1];
console.log(id_selector, id);
jQuery('#myCarousel').carousel(parseInt(id));
} catch (e) {
console.log('failed!', e);
}
});
$('#myCarousel').on('slid.bs.carousel', function (e) {
     var id = $('.item.active').data('slide-number');
    $('#carousel-text').html($('#slide-content-'+id).html());
});

$('.feature-area a').on('mouseenter', function () {
    $(this).tab('show');
});

}(jQuery));
