/**
 * This is the global documentReady that is applied to all pages.
 */

var docReady = {
    init: function () {

        inputMaskEvents();

        $('#logout').on('click', function () {
            killSession();
        });

        // Hide responsive menu on resizing window
        $(window).resize(function () {
            if ($('.navmenu').hasClass('in')) {
                $('.navmenu').offcanvas('hide');
            }
        });

        // init tooltip
        $('[data-toggle="tooltip"]').tooltip();

        initModal();

        initTimerDialog();

        hideDatePickerOnResize();

        // initialise all formsw
        validateJs('form');

    }
};

function initAtLeastOneChecked() {
    // update checkboxAll status if needed
    $('.checkboxGroup').on('click', function () {
        var form = $(this).closest("form");
        var checkboxAll = form.find('.checkboxAll');

        if (checkboxAll.length > 0) {
            if (!$(this).is(':checked')) {
                checkboxAll.prop('checked', false);
                checkboxAll.removeClass('is-active');
            } else if (form.find('.checkboxGroup:not(:checked)').length === 0) {
                checkboxAll.prop('checked', true);
                checkboxAll.addClass('is-active');
            }
        }
    });

    $('.checkboxGroup,.checkboxAll').on('click', function (e) {
        var form = $(this).closest("form");

        // remove error message from button
        if (form.length && form.find('.checkboxGroup:checked').length > 0) {
            validateAtLeastOneChecked(form);
        }
    });
};

function validateAtLeastOneChecked(form) {
    var form = $(form);
    var checkboxAll = form.find('.checkboxAll');

    checkboxAll.addClass('atLeastOneChecked');

    var valid = form.length && form.valid();

    checkboxAll.removeClass('atLeastOneChecked');

    // remove red color from checkboxAll
    checkboxAll.parent().parent().removeClass('has-error');

    return valid;
}

function refreshMessageCounter() {
    $('span.messageCounter').load($('#js-global').attr('data-get-unread-message-counter-url'));
};

function refreshNotificationCounter() {
    $('span.notificationCounter').load($('#js-global').attr('data-get-unread-notification-counter-url'));
};