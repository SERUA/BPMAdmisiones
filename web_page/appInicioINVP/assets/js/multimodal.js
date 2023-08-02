$(document).on('show.bs.modal', '.modal', function(event) {
    var zIndex = 1040 + (10 * $('.modal:visible').length);
    $(this).css('z-index', zIndex);
    setTimeout(function() {
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        $(".modal.fade").css("overflow-y", "auto");
    }, 0);
});

$(document).on('.shown.bs.modal', '.modal', function(event) {
    var zIndex = 1040 + (10 * $('.modal:visible').length);
    $(this).css('z-index', zIndex);
    setTimeout(function() {
        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        $(".modal.fade").css("overflow-y", "auto");
    }, 0);
});
$('.modal').on('.shown.bs.modal', function() {
    $("html").css({
        overflow: 'hidden'
    });
});
$(document).on('hidden.bs.modal', function() {
    if ($('.modal.fade.in').length > 1) {
        $('.modal.fade.in').css({
            'overflow-y': 'auto'
        });
        $("html").css({
            overflow: 'hidden'
        });
    } else {
        $("html").css({
            overflow: 'auto'
        });
    }
});