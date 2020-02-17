(function () {
    var vid = document.getElementById("cvideo"),
    pauseButton = document.getElementById("vbutton");
    vid.playbackRate = 1.0;

    function vidFade() {
        vid.classList.add("stopfade");
    }
    vid.addEventListener('ended', function () {
        vid.pause();
        vidFade();
    });
    pauseButton.addEventListener("click", function () {
        vid.classList.toggle("stopfade");
        if (vid.paused) {
            vid.play();
            pauseButton.innerHTML = '<span class="ti-control-pause" ></span>';
            $('.section-video .section-bg,.section-video-text').addClass("fadeOut animated");
            $('.section-video .section-bg').animate({
                'top': '0%'
            });
            $('.section-video-text').animate({
                'top': '0%'
            });
            $('.text-data').animate({
                'top': '100%'
            });
        } else {
            vid.pause();
            pauseButton.innerHTML = '<span class="ti-control-play" ></span>';
            $('.section-video .section-bg,.section-video-text').removeClass("fadeOut animated");
            $('.section-video .section-bg').animate({
                'top': '0%'
            });
            $('.section-video-text').animate({
                'top': '0%'
            });
            $('.text-data').animate({
                'top': '50%'
            });
        }
    });
    vid.addEventListener("click", function () {
        vid.classList.toggle("stopfade");
        if (vid.paused) {
            vid.play();
            pauseButton.innerHTML = '<span class="ti-control-pause" ></span>';
            $('.section-video .section-bg,.section-video-text').addClass("fadeOut animated");
            $('.section-video .section-bg').animate({
                'top': '0%'
            });
            $('.section-video-text').animate({
                'top': '0%'
            });
            $('.text-data').animate({
                'top': '100%'
            });
        } else {
            vid.pause();
            pauseButton.innerHTML = '<span class="ti-control-play" ></span>';
            $('.section-video .section-bg,.section-video-text').removeClass("fadeOut animated");

            $('.section-video .section-bg').animate({
                'top': '0%'
            });
            $('.section-video-text').animate({
                'top': '0%'
            });
            $('.text-data').animate({
                'top': '50%'
            });
        }
    });
}());