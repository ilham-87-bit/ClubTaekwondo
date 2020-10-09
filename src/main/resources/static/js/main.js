var isExpandCollapsePressed = false;

/**
 * function to redirect to another url without using an href or a form.
 * @param url The url to redirect to
 */
function redirect(url, newTab) {
    if (url && url !== '#') {
        if (newTab) {
            window.open(url, '_blank');
        }
        else {
            window.location.href = url;
        }
    }
};

/**
 * Executes a function if it has been defined. Allow to pass the function name
 * with HTML's data attributes
 * @param functionName
 * @param parameters
 */
function callFunction(functionName, parameters) {
    if (variableExists(functionName)) {
        window[functionName](parameters);
    }
};

/**
 * Checks whether the variable has been defined
 * @param variableName
 * @returns {Boolean}
 */
function variableExists(variableName) {
    return window[variableName] !== undefined;
};

function containsFragment(data) {
    return $(data).filter('.fragment').length;
};

/**
 * Allow to include all the fragments in data in a page
 * @param data containing all the fragments to include in the page
 * @param fragmentPositions example: { warning : '.warningContainer', signature :
 *           false }
 */
function includeFragments(data, fragmentPositions) {
    if (!fragmentPositions) {
        // We iterate on the fragments and place them according to the 'data-placement' attribute
        $(data).filter('.fragment').each(function (i, fragment) {
            // Get the tag in which the fragment should be placed
            var container = $($(fragment).attr('data-placement') + ', #siteBody').first();
            insertFragment(container, fragment);
        });
    }
    else if (typeof fragmentPositions === 'object') {
        $.each(fragmentPositions, function (key, value) {
            // Get the fragment in the data variable
            var fragment = $(data).filter('.fragment.' + key);

            // Get the container or #siteBody
            var container = $(value + ', #siteBody').first();

            if (fragment.length) {
                // Replace the fragment if it exists
                insertFragment(container, fragment);
            }
        });
    }
};

/**
 * Perform the insertion (or replacement) of the fragment
 * @param container where to (re)place the fragment
 * @param fragment
 */
function insertFragment(container, fragment) {
    // Find out if the fragment already exists
    var existingFragment = container.find($(fragment).classSelector());

    if (existingFragment.length) {
        // Replace the fragment if it exists
        existingFragment.replaceWith(fragment);
    }
    else {
        // Append the fragment at the end otherwise
        container.append(fragment);
    }

    scripts.init($(fragment).classSelector());
};

function post(url, params, urlEncoded, newWindow) {
    var form = $('<form />').attr({
        'action': url,
        'method': 'POST',
        'target': newWindow ? '_blank' : '_self',
        'enctype': urlEncoded ? 'application/x-www-form-urlencoded' : 'multipart/form-data'
    }).hide();

    // Add the CSRF token
    addCsrfToken(params);

    $.each(params, function (i, param) {
        var input = $('<input type="hidden">').attr({
            'id': i,
            'name': i,
            'value': param
        });
        form.append(input);
    });

    $('body').append(form);
    form.submit();
};

function getCsrfToken() {
    return $('meta[name="_csrf"]').attr('content');
};

function addCsrfToken(params) {
    if (!params) {
        params = {};
    }
    params['_csrf'] = getCsrfToken();
};

/**
 * Get the menu counters via ajax and set them in the correct place.
 * @param url
 */
function getMenuCounters(url) {
    $.getJSON(url, function (data) {
        if (data != null) {
            data.workspace === 0 ? $('.cnt_workspace').html("") : $('.cnt_workspace').html(data.workspace);
            data.mail === 0 ? $('.cnt_mail').html("") : $('.cnt_mail').html(data.mail);
        }
    });
};

/**
 * Initialise events and buttons behaviour for a carousel
 */
function initCarousel() {
    $('.left.carousel-control').addClass("enable-carousel-control");
    $('.right.carousel-control').addClass("enable-carousel-control");
    var numberDisplayedItems = Number(3);
    var numberItems, maxId;

    for (var i = 0; i < numberDisplayedItems + 1; i++) {
        $(".scroll-content-item-" + i).removeClass("hide");
    }

    numberItems = maxId = Number($('.carousel-items').length);
    $('.left.carousel-control').addClass("disable-carousel-control");
    $('.left.carousel-control').removeClass("enable-carousel-control");
    if (numberItems <= numberDisplayedItems) {
        $('.right.carousel-control').addClass("disable-carousel-control");
        $('.right.carousel-control').removeClass("enable-carousel-control");
    }

    //numberItems = $('.carousel-items').length;

    if (numberItems <= numberDisplayedItems) {
        $('.carousel-indicators').append('<li class="active" id="indicator-1"  onclick="clickIndicator(this, ' + maxId + ' )"></li>');
    }
    else {
        var nbIndicator = Math.ceil(numberItems / numberDisplayedItems);
        if (nbIndicator > 5) {
            nbIndicator = 5;
        }
        for (var i = Number(0); i < nbIndicator; i++) {
            if (i == 0) {
                $('.carousel-indicators').append('<li class="active" id="indicator-1" data-slide-to="1" onclick="clickIndicator(this, ' + maxId + ')"></li>');
            }
            else if (i == (nbIndicator - 1)) {
                if ((numberItems % numberDisplayedItems) != 0) {
                    $('.carousel-indicators').append(
                        '<li id="indicator-' + (i + 1) + '" data-slide-to="' + (((i - 1) * numberDisplayedItems + 1) + (numberItems % numberDisplayedItems))
                        + '" onclick="clickIndicator(this, ' + maxId + ')"></li>');
                }
                else {
                    $('.carousel-indicators').append(
                        '<li id="indicator-' + (i + 1) + '" data-slide-to="' + (i * numberDisplayedItems + 1) + '" onclick="clickIndicator(this, ' + maxId
                        + ')"></li>');

                }
            }
            else {
                $('.carousel-indicators').append(
                    '<li id="indicator-' + (i + 1) + '" data-slide-to="' + (i * numberDisplayedItems + 1) + '" onclick="clickIndicator(this, ' + maxId + ')"></li>');
            }
        }
    }

    initCarouselButtons(maxId);

    setActiveClient();
}

function initCarouselButtons(maxId) {

    $(".left.carousel-control").click(function () {

        $('.carousel-control').removeClass("disable-carousel-control");
        $('.carousel-control').addClass("enable-carousel-control");
        var id = Number($('.carousel-items').not(".hide").first().attr('id'));
        id = id - 1;

        if (id > 0) {
            $('#' + id).removeClass("hide");
            $('.carousel-items').not(".hide").last().addClass("hide");
        }
        if (id == 1) {
            $('.left.carousel-control').addClass("disable-carousel-control");
            $('.left.carousel-control').removeClass("enable-carousel-control");

            if ($(this).hasClass('simple-control')) {
                carouselShiftLeft();
            }
        }

        var id2 = Number($('.carousel-items').not(".hide").first().attr('id'));
        var o = $('[id*="indicator-"][data-slide-to="' + id2 + '"]');
        if (o.length > 0) {
            changeIndicator(o);
        }

    });

    $(".right.carousel-control").click(function () {

        $('.carousel-control').removeClass("disable-carousel-control");

        if ($(this).hasClass('simple-control')) {
            carouselShiftRight();
        }

        $('.carousel-control').addClass("enable-carousel-control");
        var id = Number($('.carousel-items').not(".hide").last().attr('id'));
        id = id + 1;

        if (!(id > maxId)) {
            $('#' + id).removeClass("hide");
            $('.carousel-items').not(".hide").first().addClass("hide");
        }
        if (id == maxId) {
            $('.right.carousel-control').addClass("disable-carousel-control");
            $('.right.carousel-control').removeClass("enable-carousel-control");
        }

        var id2 = Number($('.carousel-items').not(".hide").first().attr('id'));
        var o = $('[id*="indicator-"][data-slide-to="' + id2 + '"]');
        if (o.length > 0) {
            changeIndicator(o);
        }

    });

}

function changeIndicator(o) {
    $('[id*="indicator-"][class*="active"]').removeClass('active');
    $(o).addClass('active');
}

function clickIndicator(o, maxId) {
    $('[id*="indicator-"][class*="active"]').removeClass('active');
    $(o).addClass('active');

    var slideTo = Number($('[id*="indicator-"][class*="active"]').attr('data-slide-to'));

    if ($(o).closest('ol').hasClass('simple-indicators')) {
        if (slideTo === 1) {
            carouselShiftLeft();
        } else {
            carouselShiftRight();
        }
    }

    var currentSlide = Number($('.carousel-items').not(".hide").first().attr('id'));

    while (slideTo != currentSlide) {
        if (currentSlide < slideTo) {
            $('.carousel-control').removeClass("disable-carousel-control");
            $('.carousel-control').addClass("enable-carousel-control");
            var id = Number($('.carousel-items').not(".hide").last().attr('id'));
            id = id + 1;

            if (!(id > maxId)) {
                $('#' + id).removeClass("hide");
                $('.carousel-items').not(".hide").first().addClass("hide");
            }
            if (id == maxId) {
                $('.right.carousel-control').addClass("disable-carousel-control");
                $('.right.carousel-control').removeClass("enable-carousel-control");
            }

            currentSlide++;
        }
        else if (slideTo < currentSlide) {
            $('.carousel-control').removeClass("disable-carousel-control");
            $('.carousel-control').addClass("enable-carousel-control");
            var id = Number($('.carousel-items').not(".hide").first().attr('id'));
            id = id - 1;

            if (id > 0) {
                $('#' + id).removeClass("hide");
                $('.carousel-items').not(".hide").last().addClass("hide");
            }

            if (id == 1) {
                $('.left.carousel-control').addClass("disable-carousel-control");
                $('.left.carousel-control').removeClass("enable-carousel-control");
            }

            currentSlide--;
        }
        else {
            break;
        }
    }
}

function carouselShiftLeft() {
    $('ul.carousel-inner').addClass('carousel-shift-left');
    $('ul.carousel-inner').removeClass('carousel-simple');
}

function carouselShiftRight() {
    $('ul.carousel-inner').removeClass('carousel-shift-left');
    $('ul.carousel-inner').addClass('carousel-simple');
}

function setActiveClient(clientPosition) {
    var position;
    var preSelectedClient = $('li.carousel-item:not(.hide):not([id=0]):first').attr('id');

    if (clientPosition === undefined) {
        position = parseInt($('#preselectedClientPositionNumber').val());
    }
    else {
        position = parseInt(clientPosition);
    }

    if (position >= 1) {
        $('li.carousel-item.active').removeClass('active');
        $('li.carousel-item#' + position).addClass('active');
        if (position > preSelectedClient) {
            for (var i = preSelectedClient; i < position; i++) {
                $(".right.carousel-control").click();
            }
        }
        if (position < preSelectedClient) {
            for (var i = position; i < preSelectedClient; i++) {
                $(".left.carousel-control").click();
            }
        }
        $('#preselectedClientPositionNumber').val(position);
    }
    else if (position === 0) {
        $('.carousel-item[id=0]').addClass('active');
        $('#preselectedClientPositionNumber').val(position);
    }
    else if (position === -1) {
        $('.carousel-item[id=-1]').addClass('active');
        $('#preselectedClientPositionNumber').val(position);
    }
}

/****  These functions allow to handle one expand-collapse-all button and multiple expand-collapse arrow buttons.  ****/

/**
 * Handle the behaviour of items to collapse/expand one element.
 * This function closes all open elements when we open another one.
 */
function expandCollapseBehaviour() {
    var arrow = $(this).find('.arrow');
    var opening = arrow.hasClass('fa-chevron-down');

    if (opening) {
        $('.panel-collapse.in').collapse('hide');
    }

    toggleExpandCollapseAllButton();
}

/****  These functions allow to handle subsections expand-collapse-all button and multiple expand-collapse arrow buttons.  ****/

/**
 * Handle the behaviour of items to collapse/expand one sub-element.
 * This function closes all open sub-elements when we open another one.
 */
function expandCollapseSubElementsBehaviour() {
    var arrow = $(this).find('.arrow');
    var opening = arrow.hasClass('fa-chevron-down');

    if (opening) {
        $('.panel-collapse.sub.in').collapse('hide');
    }

    toggleExpandCollapseAllButton();
}

/**
 * Change the arrow direction when the element is expanded or collapsed
 */
function toggleExpandCollapseButton() {
    var arrow = $("[aria-controls='" + $(this).attr('id') + "'] .arrow");
    arrow.toggleClass('fa-chevron-down').toggleClass('fa-chevron-up');
    toggleExpandCollapseAllButton();
}

function initExpandCollapseAll() {
    isExpandCollapsePressed = false;

    $('.panel-collapse').on('shown.bs.collapse', function (e) {
        var countExpanded = parseInt($('.panel-collapse.in').length);
        var countTotal = parseInt($('.panel-collapse').length);
        if (countExpanded == countTotal) {
            isExpandCollapsePressed = false;
        }
    });

    $('.panel-collapse').on('hidden.bs.collapse', function (e) {
        var countExpanded = parseInt($('.panel-collapse.in').length);
        if (countExpanded == 0) {
            isExpandCollapsePressed = false;
        }
    });
}

/**
 * Handle the behaviour of items to collapse/expand all elements.
 */
function expandCollapseAllBehaviour() {
    if (!isExpandCollapsePressed) {
        isExpandCollapsePressed = true;
        if (!$('a.js-collapse-all').hasClass('hidden')) {
            $('.panel-collapse.in').collapse('hide');
            showExpandAllButton();
        }
        else if (!$('a.js-expand-all').hasClass('hidden')) {
            $('.panel-collapse:not(.in)').collapse('show');
            showCollapseAllButton();
        }
    }
}

/**
 * Change button for expand/collapse all
 */
function toggleExpandCollapseAllButton() {
    // if at least one closed, then show "expand all"
    var countArrowDown = $('span.fa-chevron-down').length;
    if (countArrowDown > 0) {
        showExpandAllButton();
    }
    // if all opened, then show "collapse all"
    if (countArrowDown == 0) {
        showCollapseAllButton();
    }
}

function showExpandAllButton() {
    $('.js-expand-all').removeClass('hidden');
    $('.js-collapse-all').addClass('hidden');
}

function showCollapseAllButton() {
    $('.js-collapse-all').removeClass('hidden');
    $('.js-expand-all').addClass('hidden');
}

// sort passed select by field (default = text)
function sortSelect(elem, field, ignoreValue) {
    elem = $(elem);
    field = field || 'text';

    // Retain selected value before sorting
    var selectedValue = elem.val();

    // Grab all existing options
    var options = elem.find('option');
    // Sort options by "field" attribute
    options.sort(function (a, b) {
        if (!ignoreValue && !a.value) {
            return -1;
        }
        if (!ignoreValue && !b.value) {
            return 1;
        }
        if (a[field] == b[field]) {
            return 0;
        }
        return a[field] < b[field] ? -1 : 1;
    });

    // Set sorted elements
    elem.html(options);

    // Restore selected index after sorting
    elem.val(selectedValue);
}

//reset one passed form
function resetForm(_form) {
    var form = $(_form);
    var isForm = form.is('form');
    if (isForm) {
        form[0].reset();
    }
    else {
        form.find("input").each(function (index, element) {
            $(element).val(element.defaultValue);
        });
    }

    if (isForm) {
        form.clearValidation();
    }
    else {
        form.find("input").removeClass("error");
        form.find("label.error").remove();
        form.find("input").closest('.has-error').removeClass("has-error");
    }

    //date

    // Reset date elements
    if (form.find('input.js-reset-date').length) {
        $.each(form.find('input.js-reset-date'), function (index, element) {
            $(element).val($(element).attr('data-value'));
        });
    }

    //reset checkboxes as they are not always resetted by form.reset()
    form.find(':checkbox').each(function (i, item) {
        this.checked = item.defaultChecked;
    });

    //reset icheckbox elements of Wijs
    form.find(".icheckbox").each(function (i, item) {
        if ($(item).find("[type=checkbox]").is(":checked")) {
            $(item).addClass("checked");
        }
        else {
            $(item).removeClass("checked");
        }
    })

    //reset iradio elements of Wijs
    form.find(".iradio").each(function (i, item) {
        if ($(item).find("[type=radio]").is(":checked")) {
            $(item).addClass("checked");
        }
        else {
            $(item).removeClass("checked");
        }
    })

    //custom attributes which I added to set default values to classes of elements
    var elements = $(form).find("[data-has-class]");

    $.each(elements, function (index, value) {
        var parts = $(value).attr("data-has-class").split(",");
        var subpart;
        for (var i = 0; i < parts.length; i++) {
            subpart = parts[i].split(":");
            if (subpart[1] === "true") {
                $(value).addClass(subpart[0]);
            }
            else {
                $(value).removeClass(subpart[0]);
            }
        }

    });

    //hopefully this solves any typeahead problems (if they are any) with reset
    $(form).find("*[class^='js-typeahead']").each(function () {
        clearTypeahead($(this));
    });
};

/**
 * Format Iban number from a String value
 * @param value String object of the Iban number
 * @returns the formatted Iban
 */
function getformattedIban(value) {
    var inputIban = value.replace(/[^0-9a-zA-Z]/g, '');
    var length = inputIban.length;
    if (inputIban.length >= 14) {
        var i = 4;
        var formattedIban = "";
        while (i < length) {
            formattedIban = formattedIban + inputIban.substring(i - 4, i) + " ";
            i = i + 4;
        }
        inputIban = (formattedIban + inputIban.substring(i - 4, length)).toUpperCase();
    }
    return inputIban;
};

/**
 * Format a Date object as DD/MM/YYYY
 * @param date
 * @returns {String} the formatted date
 */
function formatDate(date) {
    var day = parseInt(date.getDate(), 10);
    var month = parseInt(date.getMonth(), 10);
    var year = date.getFullYear();
    return addLeadingZeros(day, 2) + '/' + addLeadingZeros(month + 1, 2) + '/' + year;
};

function addLeadingZeros(number, size) {
    // Correspond to StringUtils.leftPad(number, size, '0')
    var prefix = Array(size).join('0');
    return (prefix + number.toString(10)).slice(-size);
};

function formatCurrencyAmount(amount) {
    return formatAmountNumber(amount.amount.value) + ' ' + amount.currencyCode;
}

function formatAmountNumber(value) {
    return parseFloat(value).toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, "$1 ").replace(/(\.)/g, ",");
}

/**
 * Check the value of the object and change it to minimum value if it's lower then minimum
 * or to maximum value if it's greater than maximum. Do nothing if the value is empty.
 * @param o
 */
function setInputNumberBetweenMinMax(o) {
    var value = Number($(o).val());
    var max = Number($(o).attr("max"));
    var min = Number($(o).attr("min"));

    if ($(o).val() == "") {
        return;
    }

    if (value > max) {
        $(o).val(max);
    }
    else if (value < min) {
        $(o).val(min);
    }
}

function initPickDate(element) {
    $(element).ready(function () {
        $('#minDate, #maxDate').datepicker();

        $('#minDate').datepicker("option", "onClose", function (dateText, element) {
            $('#maxDate').datepicker("option", "minDate", dateText);
            if (dateText) {
                $('#minDate').removeClass('validationError');
                $('#minDate').valid();
            }
        });

        $('#maxDate').datepicker("option", "onClose", function (dateText, element) {
            $('#minDate').datepicker("option", "maxDate", dateText ? dateText : new Date());
            $('#maxDate').valid();
        });

        $('#minDate, #maxDate').datepicker("option", "maxDate", new Date());
    });

    $.validator.addClassRules('chronologicalDates', {
        chronologicalDates: {
            firstDate: '#minDate',
            secondDate: '#maxDate'
        }
    });

    $.validator.addClassRules('limitedMaxDate', {
        limitedMaxDate: [$.datepicker.formatDate('dd/mm/yy', new Date())]
    });
}

/**
 * Managing modal back-drop
 */
function initModal() {
    $('.modal').on('shown.bs.modal', function (e) {
        // Force a padding to 0px due to a bug with Bootstrap
        $("body").css("padding-right", "0px");
        $('#site-wrapper').removeClass('modal-open');
        $('#site-wrapper').addClass('modal-open');
        $('.modal-backdrop').appendTo("#site-wrapper");
        //$('#messageResultModal').find('div[data-placement=#messageResultModal]').remove();
    });

    $('.modal').on('hidden.bs.modal', function (e) {
        $('#site-wrapper').removeClass('modal-open');
    });
}

function submitToUrl(path, params, target, method) {
    method = method || "post"; // Set method to post by default, if not specified.

    var form = $(document.createElement("form")).attr({
        "method": method,
        "action": path,
        "target": target,
        "style": "display:none"
    });

    $.each(params, function (key, value) {
        $.each(value instanceof Array ? value : [value], function (i, val) {
            $(document.createElement("input")).attr({
                "type": "hidden",
                "name": key,
                "value": val
            }).appendTo(form);
        });
    });

    form.appendTo(document.body);
    form.submit();
    form.remove();
};

//return true if accountNumber valid
function checkSepaIbanFormat(value) {
    if (value && value.length) {
        var accountNumber = value.replace(/[^0-9a-zA-Z]/g, '');
        var pattern = /^[a-zA-Z][a-zA-Z]/;
        if (accountNumber.length >= 14 && pattern.test(accountNumber)) {
            accountNumber = accountNumber.toUpperCase();
            return getIbanLength(accountNumber.substring(0, 2)) === accountNumber.length && checkDigitIban(accountNumber);
        }
    }
    return false;
};

function getIbanLength(countryCode) {
    var ibanLengthMap = {
        "SK": 24,
        "SM": 27,
        "TF": 27,
        "TN": 24,
        "AD": 24,
        "AE": 23,
        "AL": 28,
        "AT": 20,
        "BA": 20,
        "BE": 16,
        "BG": 22,
        "BH": 22,
        "BV": 15,
        "CH": 21,
        "CY": 28,
        "CZ": 24,
        "DE": 22,
        "DK": 18,
        "DO": 28,
        "EE": 20,
        "ES": 24,
        "FI": 18,
        "FO": 18,
        "FR": 27,
        "GB": 22,
        "GE": 22,
        "GF": 27,
        "GG": 22,
        "GI": 23,
        "GL": 18,
        "GP": 27,
        "GR": 27,
        "HR": 21,
        "HU": 28,
        "IE": 22,
        "IL": 23,
        "IM": 22,
        "IS": 26,
        "IT": 27,
        "JE": 22,
        "KW": 30,
        "KZ": 20,
        "LB": 28,
        "LI": 21,
        "LT": 20,
        "LU": 20,
        "LV": 21,
        "MC": 27,
        "ME": 22,
        "MK": 19,
        "MQ": 27,
        "MR": 27,
        "MT": 31,
        "MU": 30,
        "NC": 27,
        "NL": 18,
        "NO": 15,
        "PF": 27,
        "PL": 28,
        "PM": 27,
        "PT": 25,
        "RE": 27,
        "RO": 24,
        "RS": 22,
        "SA": 24,
        "SE": 24,
        "SI": 19,
        "SJ": 15,
        "TR": 26,
        "VA": 27,
        "WF": 27
    };
    return ibanLengthMap[countryCode];
};

function checkDigitIban(value) {
    var accountNumber = value.replace(/[^0-9a-zA-Z]/g, '').toUpperCase();
    var ibanNumber = accountNumber.substring(4, accountNumber.length) + accountNumber.substring(0, 4);
    var offset = "A".charCodeAt(0) - 10;
    ibanNumber = ibanNumber.replace(/[A-Z]/g, function (match) {
        return match.charCodeAt(0) - offset;
    });

    return modulo(ibanNumber, "97") === "1";
};

function modulo(divident, divisor) {
    var cDivident = '';
    var cRest = '';

    for (var i = 0; i < divident.length; i++) {
        var cChar = divident.charAt(i);
        var cOperator = cRest + '' + cDivident + '' + cChar;

        if (cOperator < parseInt(divisor)) {
            cDivident += '' + cChar;
        }
        else {
            cRest = cOperator % divisor;
            if (cRest === 0) {
                cRest = '';
            }
            cDivident = '';
        }
    }
    cRest += '' + cDivident;
    return cRest;
};

function hideDatePickerOnResize() {
    // Save size and add a delta because in Chrome mobile windo.resize() is always fired when the scrollbar is displayed
    var document_width = $(document).width();
    var document_height = $(document).height();
    var delta = 20;

    $(window).resize(function () {
        if (Math.abs(document_width - $(document).width()) > delta || Math.abs(document_height - $(document).height()) > delta) {
            var field = $(document.activeElement);
            if (field.is('.hasDatepicker')) {
                field.datepicker('hide');
            }
        }

    });
};

function initSelector(selector) {
    // If selector is undefined or an object or a function (e.g., when first init), we initialize it with an empty string.
    // On the other hand, if a selector is passed, we add a space to select in the children of 'selector'
    return !selector || typeof selector === 'object' || typeof selector === 'function' ? '' : selector + ' ';
}

function initFixedBar() {
    var $window = $(window);
    $window.scroll(function () {
        if ($window.scrollTop() == 0) {
            $('body').find('.newbar').removeClass('bar');
            //$('body').find('header').removeClass('hidden');
            //	$('body').find('#rootMenuBar').removeClass('hidden');
        }
        else {
            $('body').find('.newbar').addClass('bar');
            // $('body').find('header').addClass('hidden');
            //$('body').find('#rootMenuBar').addClass('hidden');
        }
    });
};

function unformatAmountField(isAndroidOs, elem) {
    if (isAndroidOs) {
        if (elem.val()) {
            var amount = elem.val().replace(' EUR', '');
            amount = amount.replace('+', '');
            amount = amount.replace(/ /g, '');
            elem.val(amount);
        }
    }
};


function removeAllRequired() {
    $('.required').removeClass('required');
}