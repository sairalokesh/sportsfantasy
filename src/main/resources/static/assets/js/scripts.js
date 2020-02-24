(function($) {
	'use strict';
	$(window).on('load', function() {
		var preLoder = $(".loader-wrapper");
		preLoder.delay(700).fadeOut(500);
		$('body').addClass('loaded');
	});

    $('a.page-scroll').on('click', function(event) {
        if ( location.pathname.replace(/^\//, '') === this.pathname.replace(/^\//, '') && location.hostname === this.hostname ) {
          var target = $(this.hash),
              speed= $(this).data("speed") || 800;
              target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
          if (target.length) {
            event.preventDefault();
            $('html, body').animate({
              scrollTop: target.offset().top - 60
            }, speed);
          }
        }
    });
	
	$(window).on('scroll', function() {
		var scroll = $(window).scrollTop();
	    if (scroll >= 80) {
	        $('header').addClass('nav-fixed');
	    } else {
	        $('header').removeClass('nav-fixed');
	    }

	});
	
	$( document ).ready( function () {
		$( '.dropdown-menu a.dropdown-toggler' ).on( 'click', function ( e ) {
			var $el = $( this );
			var $parent = $( this ).offsetParent( ".dropdown-menu" );
			if ( !$( this ).next().hasClass( 'show' ) ) {
				$( this ).parents( '.dropdown-menu' ).first().find( '.show' ).removeClass( "show" );
			}
			var $subMenu = $( this ).next( ".dropdown-menu" );
			$subMenu.toggleClass( 'show' );
			
			$( this ).parent( "li" ).toggleClass( 'show' );
	
			$( this ).parents( 'li.nav-item.dropdown.show' ).on( 'hidden.bs.dropdown', function ( e ) {
				$( '.dropdown-menu .show' ).removeClass( "show" );
			} );
			
			return false;
		} );
	} );
	
	var navBar = $(".header_wrap");
	var navbarLinks = navBar.find(".navbar-collapse ul li a.nav_item");

    $.each( navbarLinks, function( i, val ) {
      var navbarLink = $(this);
        navbarLink.on('click', function () {
          navBar.find(".navbar-collapse").collapse('hide');
		  $("header").removeClass("active");
        });

    });

	$('.navbar-toggler').on('click', function() {
		$("header").toggleClass("active");
	});	
	$(document).on("ready", function () {
	if ($(window).width() > 991) {
		$("header").removeClass("active");
	}
	$(window).on("resize", function () {
	if ($(window).width() > 991) {
			$("header").removeClass("active");
		}
	})
	})
	
	$(document).ready(function() {
	$("#lng_select,#doc_select").msDropdown();
	})
	
    $('.tk_countdown_time').each(function() {
        var endTime = $(this).data('time');
        $(this).countdown(endTime, function(tm) {
            $(this).html(tm.strftime('<span class="counter_box"><span class="tk_counter days">%D </span><span class="tk_text">Days</span></span><span class="counter_box"><span class="tk_counter hours">%H</span><span class="tk_text">Hours</span></span><span class="counter_box"><span class="tk_counter minutes">%M</span><span class="tk_text">Minutes</span></span><span class="counter_box"><span class="tk_counter seconds">%S</span><span class="tk_text">Seconds</span></span>'));
        });
    });
	

	$(window).scroll(function() {
		if ($(this).scrollTop() > 150) {
			$('.scrollup').fadeIn();
		} else {
			$('.scrollup').fadeOut();
		}
	});
	
	$(".scrollup").on('click', function (e) {
		e.preventDefault();
		$('html, body').animate({
			scrollTop: 0
		}, 600);
		return false;
	});
	
	$('.content-popup').magnificPopup({
		type: 'inline',
		preloader: true,
		mainClass: 'mfp-zoom'
	});
	
	$(function() {
	
		function ckScrollInit(items, trigger) {
			items.each(function() {
				var ckElement = $(this),
					AnimationClass = ckElement.attr('data-animation'),
					AnimationDelay = ckElement.attr('data-animation-delay');
	
				ckElement.css({
					'-webkit-animation-delay': AnimationDelay,
					'-moz-animation-delay': AnimationDelay,
					'animation-delay': AnimationDelay,
					opacity: 0
				});
	
				var ckTrigger = (trigger) ? trigger : ckElement;
	
				ckTrigger.waypoint(function() {
					ckElement.addClass("animated").css("opacity", "1");
					ckElement.addClass('animated').addClass(AnimationClass);
				}, {
					triggerOnce: true,
					offset: '90%'
				});
			});
		}
	
		ckScrollInit($('.animation'));
		ckScrollInit($('.staggered-animation'), $('.staggered-animation-wrap'));
	
	});
	
	jQuery(document).ready(function($) {
		jQuery('.counter').counterUp({
			delay: 10,
			time: 1000
		});
	});
	
	$(".icon").on("click", function() {
		$(".color-switch").toggleClass("switch-active");
		$(this).toggleClass("switch-active");
	});
	
	$(function() {
		$('#doc_select').change(function(){
			$('.document_tab .tab-pane').removeClass('show active');
			$('#' + $(this).val()).addClass('show active');
		});
	});
			
	$( window ).on( "load", function() {
		document.onkeydown = function(e) {
			if(e.keyCode == 123) {
			 return false;
			}
			if(e.ctrlKey && e.shiftKey && e.keyCode == 'I'.charCodeAt(0)){
			 return false;
			}
			if(e.ctrlKey && e.shiftKey && e.keyCode == 'J'.charCodeAt(0)){
			 return false;
			}
			if(e.ctrlKey && e.keyCode == 'U'.charCodeAt(0)){
			 return false;
			}
		
			if(e.ctrlKey && e.shiftKey && e.keyCode == 'C'.charCodeAt(0)){
			 return false;
			}      
		 }
		 
		$("html").on("contextmenu",function(){
			return false;
		});
	});
	 			
})(jQuery);

