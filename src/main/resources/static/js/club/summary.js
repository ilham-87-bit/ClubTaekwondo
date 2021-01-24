(function ($) {

    "use strict";
    // Add active state to sidbar nav links
    var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
    $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function () {
        if (this.href === path) {
            $(this).addClass("active");
        }
    });

    // Toggle the side navigation
    $("#sidebarToggle").on("click", function (e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });

})(jQuery);
$(document).ready(function () {
    inputMaskEvents();
});

function printPage() {
    window.print();
}
function selectCity() {
    $('.selectCity').on('input', function () {
        $('#postalCode').val('');
        selectPostalCodeByCity();
    });
}

function selectPostalCodeByCity() {
    for (var i = 0; i < $('.map').length; i++) {
        if ($('#city' + i).attr('name') === $('#idCity option:selected').val()) {
            $('#postalCode').val($('#city' + i).attr('value'));
        }
    }
}