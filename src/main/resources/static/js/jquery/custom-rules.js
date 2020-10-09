function validateJs(form, opt) {
    validationManager.addForm(form);

    var options = opt || {};
    options.onkeyup = false;
    options.errorClass = "validationError";
    // the errorPlacement has to take the table layout into account
    options.errorPlacement = function (error, element) {
        if (!error.text().length) { // there is actually no error
            return;
        }

        element = $(element);
        var tooltipSettings = getTooltipSettings(element);
        // If the element has a substitute to attach the error on
        if (element.attr('data-substitute') !== undefined) {
            var substitute = $('#' + element.attr('data-substitute'));
            // add the validationError class to the substitue as well
            if (element.hasClass(this.errorClass)) {
                substitute.addClass(this.errorClass);
            }
            element = substitute;
        }

        // validationError message is displayed as tooltip
        addErrorTooltip(element, error, tooltipSettings);

        // add validationError class to the addon or button, if any
        if (element.next().hasClass('input-group-addon') || element.next().hasClass('input-group-btn')) {
            element.next().addClass(this.errorClass);
        }
        if (element.prev().hasClass('input-group-addon') || element.prev().hasClass('input-group-btn')) {
            element.prev().addClass(this.errorClass);
        }
    };

    options.highlight = function (element, errorClass, validClass) {
        element = $(element);
        element.addClass(errorClass).closest('tr, div').addClass('has-error');

        var tooltipId = '#' + element.attr('aria-describedby');

        if ($(tooltipId).length) {
            var errorText = element.parent().find('.visible-print').html();
            $(tooltipId).find('.tooltip-inner').html(errorText);
        }
    };

    options.unhighlight = function (element, errorClass, validClass) {
        element = $(element);

        if (element.attr('data-substitute') !== undefined) {
            // Modification for Selectize plugin
            var substitute = $('#' + element.attr('data-substitute'));
            if (element.hasClass('selectized')) {
                substitute.removeClass(errorClass).closest('div.select-awesome').removeClass('has-error');
            }
            substitute.removeClass(errorClass).closest('tr, div').removeClass('has-error');
            removeTooltip(substitute);
        } else {
            element.removeClass(errorClass).closest('tr, div').removeClass('has-error');
            removeTooltip(element);
        }
    };

    options.success = function (element) {
        element.remove();
        if ($('.' + this.errorClass).length == 0) {
            $('#validationFieldsInError').hide();
        }
    };

    options.invalidHandler = function (form, validator) {
        $('body').removeClass('loading');
    };

    options.resetElement = function (element) {
        this.currentElements.filter(element).removeClass(this.settings.validClass + ' ' + this.settings.errorClass);
    };

    return $(form).validate(options);
}

function addErrorLabel(element, error) {
    if (element.next().hasClass('input-group-addon') || element.next().hasClass('input-group-btn') || element.prev().hasClass('input-group-addon') || element.prev().hasClass('input-group-btn') || element.is('input:radio') || element.is('input:checkbox')) {
        element.parent().children().wrapAll('<div class="input-group wrapper"></div>'); // wrap field and addon
        element = element.parent();
    }
    element.after(error); // add the error message after the wrapper element
}

function addErrorTooltip(element, error, tooltipSettings) {
    var fieldId = element.attr('id');
    if (element.next().hasClass('input-group-addon') || element.next().hasClass('input-group-btn') || element.prev().hasClass('input-group-addon') || element.prev().hasClass('input-group-btn') || element.is("input:radio") || element.is("input:checkbox")) {
        if (!element.parent().attr('data-original-title') || !element.parent().next().hasClass('tooltip')) { // there is no tooltip yet
            element.parent().children().wrapAll('<div class="input-group wrapper"></div>'); // wrap field and addon
        }
        element = element.parent(); // take the wrapper to later attach the error to it
    }

    // Attach the tooltip
    element.attr('title', error.text()).tooltip(tooltipSettings).tooltip('show');
    element.attr('title', error.text()).tooltip('_fixTitle').tooltip('show'); //needed to update the error message of the tooltip

    // Attach label to show in printing
    removePrintErrorLabel(element, fieldId); // if one already exists
    //addPrintErrorLabel(element, error, fieldId);
}

function removeTooltip(element) {
    var elementToUnwrap, fieldId = element.attr('id');
    if (element.parent().hasClass('wrapper')) {
        elementToUnwrap = element;
        element = element.parent();
    }
    element.tooltip('dispose'); // remove the tooltip
    removePrintErrorLabel(element, fieldId); // remove the print label

    if (elementToUnwrap) {
        elementToUnwrap.unwrap(); // get rid of the wrapper
    }
}

function getTooltipSettings(element) {
    return {
        animation: true,
        trigger: 'manual',
        // decides location of the error message (left/right/top/bottom). Right by default
        placement: element.attr("data-placement") !== undefined ? element.attr("data-placement") : 'bottom',
        // different template becasuse we want to add an extra class
        template: '<div class="tooltip hidden-print validationError"><div class="arrow"></div><div class="tooltip-inner validationError"></div></div>'
        // note the tooltip will be hidden on printings
    };
}

/**
 * Add the error message as a small label under the field as the tooltip are not
 * positioned properly on the printings
 * @param element to insert the error label after
 * @param error to be modified before being inserted
 * @param fieldId id of the field
 */
function addPrintErrorLabel(element, error, fieldId) {
    error.attr('id', 'printError_' + fieldId); // set an Id to remove it easily
    error.addClass('visible-print'); // make it visible only on printings
    error.attr('for', ''); // remove the 'for' attribute used by Jquery.validate
    element.next().after(error); // insert label (after tooltip!! -> use 'next')
}

function removePrintErrorLabel(element, fieldId) {
    element.parent().find('label#printError_' + fieldId).remove();
}

/**
 * The validationManager encapsulates the logic of tooltips' position
 * refreshment. When the window is resized, the validationError tooltip
 * positions have to be computed again in order to follow the field, or be
 * converted into labels.
 */
var validationManager = (function () {
    var resizeTO = null;
    var jQueryForms = [];

    /**
     * Enforce validation of forms (in 'jQueryForms' array) having tooltip(s)
     * displayed
     */
    var refreshTooltipPositions = function () {
        resizeTO = null; // tells the timer is out
        $.each(jQueryForms, function (index) {
            if ($(this).find('div.tooltip.validationError').length) {
                $(this).valid();
            }
        });
    };

    /**
     * Schedule a refreshment of the tooltips' position (if any) for all the
     * validation forms by bounding a timeout to the 'resize' event of the window
     */
    function scheduleRefreshmentOnResize() {
        $(window).resize(function () {
            // if timer exists, reset it
            if (resizeTO) {
                clearTimeout(resizeTO);
                resizeTO = setTimeout(refreshTooltipPositions, 250);
            } else {
                resizeTO = setTimeout(refreshTooltipPositions, 250);
            }
        });
    }

    return {
        /**
         * The only public method allows to add a form to the list of form to be
         * checked when the window is resized.
         */
        addForm: function (form) {
            if (jQueryForms.length === 0) {
                scheduleRefreshmentOnResize();
            }
            jQueryForms.push(form);
        }
    };
})();

/**
 * VALIDATION CLASS RULES
 */

$.validator.addClassRules("requiredIfEnabled", {
    required: function (e) {
        return !$(e).attr("disabled");
    }
});

$.validator.addClassRules("changePasswordInput", {
    changePasswordInput: true
});

$.validator.addClassRules("textInput", {
    textInput: true
});

$.validator.addClassRules("userIdInput", {
    userIdInput: true
});

$.validator.addClassRules("nameInput", {
    maxlength: 70,
    nameInput: true
});

$.validator.addClassRules("aliasInput", {
    maxlength: 30,
    aliasInput: true
});

$.validator.addClassRules("sameInput", {
    sameInput: true
});

$.validator.addClassRules("addressInput", {
    maxlength: 70,
    addressInput: true
});

$.validator.addClassRules("messageInput", {
    messageInput: true
});

$.validator.setDefaults({
    ignore: ':hidden:not([class~=selectized]),:hidden > .selectized, .selectize-control .selectize-input input'
});
$.validator.addClassRules("emailInput", {
    emailInput: true
});

$.validator.addClassRules("responseInput", {
    responseInput: true
});

$.validator.addClassRules("statementNb", {
    statementNb: true
});


//Check if at least one field has been filled
$.validator.addMethod('atLeastOneChecked', function (value, element, param) {
    // get form
    var form = $(element).closest("form");
    // get all checked input in form
    var inputs = form.find('input[name="' + param + '"]:checked');
    // return false if no input checked
    return inputs.length > 0;
}, 'At least one of these field has to be checked');

//Note: if a prefix is added, do the same in 'resources/template/fhome/phonenumber.ftl'
var internationalPrefixes = '1|7|20|27|30|31|32|33|34|36|39|40|41|43|44|45|46|47|48|49|51|52|53|54|55|56|57|58|60|61|62|63|64|65|66|81|82|84|86|90|91|92|93|94|95|98|211|212|213|216|218|220|221|222|223|224|225|226|227|228|229|230|231|232|233|234|235|236|237|238|239|240|241|242|243|244|245|246|247|248|249|250|251|252|253|254|255|256|257|258|260|261|262|263|264|265|266|267|268|269|290|291|297|298|299|350|351|352|353|354|355|356|357|358|359|370|371|372|373|374|375|376|377|378|380|381|382|385|386|387|389|420|421|423|500|501|502|503|504|505|506|507|508|509|590|591|592|593|594|595|596|597|598|599|670|672|673|674|675|676|677|678|679|680|681|682|683|685|686|687|688|689|690|691|692|850|852|853|855|856|880|886|960|961|962|963|964|965|966|967|968|970|971|972|973|974|975|976|977|992|993|994|995|996|998';
var belgianMobilePrefixes = '49|48|47|46';

$.validator.addClassRules("numberWithInternationalPrefix", {
    numberWithInternationalPrefix: function (e) {
        var params = {
            intPrefixes: internationalPrefixes
        };
        if ($(e).hasClass("mobileNumberInput")) {
            // We include the belgian prefixes in order to accept '04'
            // starting mobile number without raising validation error
            params.belPrefixes = belgianMobilePrefixes;
        }
        return params;
    }
});

$.validator.addClassRules("mobileNumberInput", {
    mobileNumberInput: {
        prefixes: belgianMobilePrefixes
    }
});

//Note: No need of using 'numberWithInternationalPrefix' validation when
//using the below class as prefix is automagically added by the formatting
$.validator.addClassRules("belgianMobileNumberInput", {
    belgianMobileNumberInput: {
        prefixes: belgianMobilePrefixes
    }
});

$.validator.addClassRules("phoneNumberInput", {
    phoneNumberInput: true
});

$.validator.addClassRules("faxNumberInput", {
    faxNumberInput: true
});

$.validator.addClassRules("phoneNumberNationalityInput", {
    phoneNumberNationalityInput: true
});

$.validator.addClassRules("sepaAccountInput", {
    sepaAccountInput: true
});

$.validator.addClassRules("belgianSepaAccountInput", {
    belgianSepaAccountInput: true
});

$.validator.addClassRules("structuredCommunicationInput", {
    structuredCommunicationInput: true
});

$.validator.addClassRules("euStructuredCommunicationInput", {
    euStructuredCommunicationInput: true
});

$.validator.addClassRules("freeCommunicationInput", {
    freeCommunicationInput: true
});

$.validator.addClassRules("nationalRegistryInput", {
    checkModNationalNumber: true
});

$.validator.addClassRules("noHtml", {
    noHtml: true
});

$.validator.addClassRules("alertSmsGeneralCondition", {
    alertSmsGeneralCondition: true
});

$.validator.addClassRules("urgentPaymentCutOffInput", {
    urgentPaymentCutOffInput: true
});

$.validator.addClassRules("urgentPaymentIsWorkingDayInput", {
    checkUrgenPaymentIsWorkingDay: true
});

$.validator.addClassRules("integerBiggerThanOne", {
    checkIfInteger: true,
    checkIfBiggerThanOne: true
});

$.validator.addClassRules("biggerThanZeroInput", {
    biggerThanZeroInput: true
});

$.validator.addClassRules("dateValid", {
    defaultDateFormat: true,
    dateValid: true
});

$.validator.addClassRules("cbeInput", {
    checkCBE: true
});

$.validator.addClassRules("workingDayInput", {
    workingDayInput: true
});

$.validator.addClassRules("bimonthlyDateInput", {
    bimonthlyDateInput: true
});

$.validator.addClassRules("weeklyDateInput", {
    weeklyDateInput: true
});

$.validator.addClassRules('validAmount', {
    validAmount: true
});

$.validator.addClassRules('androidValidAmount', {
    androidValidAmount: true
});

$.validator.addClassRules('androidValidAmountAbs', {
    androidValidAmountAbs: true
});

$.validator.addClassRules('decimalNumberInput', {
    decimalNumberValidator: true
});

$.validator.addClassRules('notEmpty', {
    notEmpty: true
});

$.validator.addClassRules("notZero", {
    notZeroValidator: true
});

$.validator.addClassRules("integerInput", {
    numberInput: true
});

$.validator.addClassRules("numberOfEmployee", {
    numberOfEmployeeValidator: true
});

$.validator.addClassRules("notZeroOrEmpty", {
    notZeroAndNotEmpty: true
});

$.validator.addClassRules("smallerThan", {
    smallerThanValidator: true
});
$.validator.addClassRules("smallerAmount", {
    smallerAmountValidator: true
});
$.validator.addClassRules("smallerDuration", {
    smallerDurationValidator: true
});
$.validator.addClassRules("smallerCoverageRate", {
    smallerCoverageRateValidator: true
});
$.validator.addClassRules("smallerAmountLoan", {
    smallerAmountLoanValidator: true
});

$.validator.addClassRules("monthsMax", {
    monthsMaxValidator: true
});

$.validator.addClassRules("yearsMax", {
    yearMaxValidator: true
});

$.validator.addClassRules("interestRateMax", {
    interestRateMax: true
});

$.validator.addClassRules("biggerOrEqualZero", {
    biggerOrEqualZero: true
});

$.validator.addClassRules("atLeastOneUpload", {
    uploadRequired: true
});

$.validator.addClassRules("required", {
    required: true
});

$.validator.addClassRules("correctPercentage", {
    correctPercentageValidator: true
});

$.validator.addClassRules("guarantyDuration", {
    guarantyDurationValidator: true
});

/**
 * VALIDATION METHODS
 */

$.validator.addMethod('required', function (value, element) {
    if (value) {
        return value.trim();
    }
    return false;
});

$.validator.addMethod("uploadRequired", function (value, elem) {
    return Boolean(value);
});

$.validator.addMethod("biggerOrEqualZero", function (value, elem) {
    return parseFloat(value) >= 0;
});


$.validator.addMethod("smallerThanValidator", function (value, element) {
    var elem = $(element).parents('form').find('input#investmentAmountTotal');
    return parseFloat(elem.val()) >= parseFloat(value);
});

$.validator.addMethod("smallerAmountValidator", function (value, element) {
    var elem = $(element).parents('form').find('input#totalAmountLoan');
    return parseFloat(elem.val()) > parseFloat(value);
});
$.validator.addMethod("smallerDurationValidator", function (value, element) {
    var elem = $(element).parents('form').find('input#guaranteeDurationMax');
    return parseInt(elem.val()) > parseInt(value);
});

$.validator.addMethod("smallerCoverageRateValidator", function (value, element) {
    var elem = $(element).parents('form').find('input#coverageRateMax');
    return parseFloat(elem.val()) >= parseFloat(value);
});

$.validator.addMethod("smallerAmountLoanValidator", function (value, element) {
    var elem = $(element).parents('form').find('input#totalAmount');
    var elemVal = elem.val().replace(" ", "");
    return parseFloat(elemVal) >= parseFloat(value);
});


$.validator.addMethod("guarantyDurationValidator", function (value, element) {
    var elem = $(element).parents('form').find('input#commitmentYears');
    return elem.val() >= value;
});

$.validator.addMethod("notZeroAndNotEmpty", function (value) {
    return value != '0' && value;
});

$.validator.addMethod("numberOfEmployeeValidator", function (value) {
    return value < 100;
});

$.validator.addMethod("yearMaxValidator", function (value) {
    return value < 100;
});

$.validator.addMethod("interestRateMax", function (value) {
    if (value) {
        return parseFloat(value) < 1000;
    }
    return true;
});

$.validator.addMethod("correctPercentageValidator", function (value) {
    if (parseFloat(value) < 0 || parseFloat(value) > 99) {
        return false;
    }
    return true;
});

$.validator.addMethod("monthsMaxValidator", function (value) {
    return value < 12;
});

$.validator.addMethod("notZeroValidator", function (value) {
    return value != "0,00" && value != 0;
}, "la valeur ne peut pas être zéro");

$.validator.addMethod("notEmpty", function (value) {
    return value;
}, "champ obligatoire");

$.validator.addMethod("numberInput", function (value, element, param) {
    var pattern = /^\d*$/;
    return this.optional(element) || pattern.test(value);
}, "Seuls les caractères suivants sont autorisés : 123456789\+ ");

$.validator.addMethod("decimalNumberValidator", function (value, element) {
    var regExp = new RegExp('[\\d]{0,},?[\\d]{1,2}');
    return value.match(regExp) || !value;
}, "Seuls les nombres sont autorisés");

$.validator.addMethod("textInput", function (value, element, param) {
    var pattern = /^[a-zA-Z0-9\/\-\?:\(\)\.,'\+ ]*$/;
    return this.optional(element) || pattern.test(value);
}, "Seuls les caractères suivants sont autorisés : abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/-?:().,''\+ ");

$.validator.addMethod("userIdInput", function (value, element, param) {
    var pattern = /^[a-zA-Z0-9]{10}$/;
    return this.optional(element) || pattern.test(value);
}, "Incorrect userID");

$.validator.addMethod("changePasswordInput", function (value, element, param) {
    return value == $(".firstInputPassword").val();
}, "Please enter the same password as above");

$.validator.addMethod("nameInput", function (value, element, param) {
    var pattern = /^[a-zA-ZÀ-Ÿ-\s]*$/;
    return this.optional(element) || pattern.test(value);
}, "The name cannot contain the following characters: ²³&|@#§!(){}[]°_\"^¨$*%´`µ£€<>?,;.:+=~0123456789");

$.validator.addMethod("aliasInput", function (value, element, param) {
    var pattern = /^[a-zA-Z0-9À-Ÿ-\s()+\.]*$/;
    return this.optional(element) || pattern.test(value);
}, "The alias cannot contain the following characters: ²³&|@#§!{}[]°_\"^¨$*%´`µ£€<>?,;:=~");

$.validator.addMethod("sameInput", function (value, element, param) {
    confirmId = $(element).attr("data-confirm");
    return value == $("#" + confirmId).val();
}, "Input does not match");

$.validator.addMethod("addressInput", function (value, element, param) {
    var pattern = /^[a-zA-Z0-9À-Ÿ-\s()+\.,'\\\/]*$/;
    return this.optional(element) || pattern.test(value);
}, "The address cannot contain the following characters: ²³&|@#§!{}[]°_\"^¨$*%´`µ£€<>?;:=~");

$.validator.addMethod("messageInput", function (value, element, param) {
    var pattern = /^[a-zA-Z0-9À-Ÿ-\s()+\.,'\\\/²³&\|@#§!\{\}\[\]°_\"^¨\$\*%´`µ£<>\?;:=~]*$/;
    return this.optional(element) || pattern.test(value);
}, "The message subject and body cannot contain the following character: €");

$.validator.addMethod("emailInput", function (value, element, param) {
    var pattern = /^([0-9a-zA-Z]{1,61}(\.|-|_|\+)){0,}[0-9a-zA-Z]{1,61}@(([a-zA-Z0-9]{1,61}-){0,}[a-zA-Z0-9]{1,61}\.){1,}[a-zA-Z]{2,61}$/i;
    return this.optional(element) || pattern.test(value);
}, "Incorrect email address");


$.validator.addMethod("responseInput", function (value, element, param) {
    value = value.replace(/_*\s?_*/g, '');
    $(element).val(value);
    var pattern = /^[0-9]{1,4} ?[0-9]{1,4}$/;
    return this.optional(element) || pattern.test(value);
}, "Incorrect format of signature response");

$.validator.addMethod("statementNb", function (value, element, param) {
    // removes placeholder from string
    value = value.split("_").join("");

    // if the value was just the mask, remove it (so that it appears empty for the optional function)
    if (value == '.')
        $(element).val('');

    var pattern = /^[0-9]{2}\.[0-9]{3}$/i;
    return this.optional(element) || pattern.test(value);
}, "Incorrect statement number");

//Check if two statement numbers are ordered
$.validator.addMethod("statementNbOrdered", function (value, element, param) {
    var ordered = true;
    var firstStmtNumber = $(param.firstStmtNumber).val().replace("\.", "");
    var secondStmtNumber = $(param.secondStmtNumber).val().replace("\.", "");
    if (firstStmtNumber && secondStmtNumber) {
        if (firstStmtNumber.localeCompare(secondStmtNumber) > 0) {
            ordered = false;
        }
    }
    return this.optional(element) || ordered;
}, "Statement numbers are not correctly ordered");


/**
 * Phone numbers
 */
//Check if the phone number has an international prefix
$.validator.addMethod("numberWithInternationalPrefix", function (value, element, param) {
    var number = cleanNumber(value);
    return this.optional(element) || hasInternationalPrefix(number, param['intPrefixes'], param['belPrefixes']);
}, "Enter a valid international phone number prefix");

//Check if the mobile number is in the right format
$.validator.addMethod("mobileNumberInput", function (value, element, param) {
    var number = cleanNumber(value);
    return this.optional(element) || isValidMobileNumber(number, param['prefixes']);
}, "Enter a mobile number in a correct format");

//Check if the gsm number is in the right format (belgian only)
$.validator.addMethod("belgianMobileNumberInput", function (value, element, param) {
    var number = cleanNumber(value);
    return this.optional(element) || isValidBelgianMobileNumber(number, param['prefixes']);
}, "Enter a gsm number in a correct format");

//Check if the phone number is in the right format
$.validator.addMethod("phoneNumberInput", function (value, element, param) {
    var number = cleanNumber(value);
    return this.optional(element) || isValidPhoneNumber(number);
}, "Enter a phone number in a correct format");

//Check if the fax number is in the right format
$.validator.addMethod("faxNumberInput", function (value, element, param) {
    var number = cleanNumber(value);
    return this.optional(element) || isValidPhoneNumber(number);
}, "Enter a fax number in a correct format");

//Check if the number is not in a wrong country
$.validator.addMethod("phoneNumberNationalityInput", function (value, element, param) {
    var number = cleanNumber(value);
    return this.optional(element) || !hasUsPrefix(number);
}, "This international prefix is not accepted");


function hasInternationalPrefix(number, intPrefixes, belPrefixes) {
    if (!!belPrefixes && hasBelgianMobilePrefix(number)) {
        // Special case: if the mobile number starts by '04' (which we accept),
        // we perform the belgian mobile number validation instead
        return isValidBelgianMobileNumber(number, belPrefixes);
    }
    var regExp = new RegExp('^(\\+|00)(' + intPrefixes + ')', 'g');
    return number.match(regExp);
}

// Check if it's a valid mobile number (with special verif for belgian ones)
function isValidMobileNumber(number, belgianPrefixes) {
    if (!!belgianPrefixes && hasBelgianMobilePrefix(number)) {
        return isValidBelgianMobileNumber(number, belgianPrefixes);
    }
    return isValidPhoneNumber(number);
}

function isValidBelgianMobileNumber(number, belPrefixes) {
    var regExp = new RegExp('^(\\+32|0032|0)(' + belPrefixes + ')(\\d{7})$', 'g');
    return number.match(regExp);
}

function isValidPhoneNumber(number) {
    // In case there is no prefix, the field should not be longer than the DB limit
    var unformattedNumber = unformatPhoneNumber(number);
    return 9 <= unformattedNumber.length && unformattedNumber.length <= 15;
}

function hasBelgianMobilePrefix(number) {
    var regExp = new RegExp('^(\\+32|0032|04)', 'g');
    return number.match(regExp);
}

function hasUsPrefix(number) {
    return number.match(/^(00|\+)1\d*$/i);
}

// Keep only digits and '+' character
function cleanNumber(number) {
    return !!number ? number.replace(/[^\+\d]/g, '') : '';
}

// Keep only digits
function unformatPhoneNumber(number) {
    return !!number ? number.replace(/[^\d]/g, '') : '';
}

/**
 * Accounts
 */
//Check Sepa Account Number format For Sepa Transfers And Beneficiaries
$.validator.addMethod("sepaAccountInput", function (value, element, param) {
    var correct = true;
    var pattern = /^[a-zA-Z][a-zA-Z]/;
    var patternNumber = /^\d+/;
    var accountNumber = value.replace(/[^0-9a-zA-Z]/g, '');
    var maxLength = 35;
    if (accountNumber.length == 12 && patternNumber.test(accountNumber)) {
        var firstNum = accountNumber.substring(0, 10);
        firstNum %= 97;
        if (firstNum == 0) {
            firstNum = 97;
        }
        if (firstNum != parseInt(accountNumber.substring(10, 12), 10)) {
            correct = false;
        } else {
            correct = true;
        }
    } else if (pattern.test(accountNumber) && accountNumber.length <= maxLength) {
        correct = checkSepaIbanFormat(accountNumber);
    } else {
        correct = false;
    }
    return this.optional(element) || correct;
}, "The account number is not valid or is not a european account.");

//Check Belgian Sepa Account Number format
$.validator.addMethod("belgianSepaAccountInput", function (value, element, param) {
    var correct = true;
    var pattern = /^[a-zA-Z][a-zA-Z]/;
    var patternNumber = /^\d+/;
    var accountNumber = value.replace(/[^0-9a-zA-Z]/g, '');
    if (accountNumber.length == 12 && patternNumber.test(accountNumber)) {
        var firstNum = accountNumber.substring(0, 10);
        firstNum %= 97;
        if (firstNum == 0) {
            firstNum = 97;
        }
        if (firstNum != parseInt(accountNumber.substring(10, 12), 10)) {
            correct = false;
        } else {
            correct = true;
        }
    } else if (accountNumber.length == 16 && pattern.test(accountNumber)) {
        correct = checkSepaIbanFormat(accountNumber);
    } else {
        correct = false;
    }
    return this.optional(element) || correct;
}, "The account number is not a valid Belgian account number");

$.validator.addMethod("checkCBE", function (value, element, param) {
    if (value.length != 12) {
        return false;
    }

    for (var i = 2; i < value.length; i++) {
        if (isNaN(value.charAt(i))) {
            return false;
        }
    }

    if (value.charAt(0) != 'B' || value.charAt(1) != 'E') {
        return false;
    }

    if (value.charAt(2) != 0) {
        return false;
    }

    var eightFirstChar = value.substr(3, 7);
    var lastTwoChar = value.substr(10);
    if (lastTwoChar != 97 - (eightFirstChar % 97)) {
        return false;
    }


    return true;
}, "Invalid cbe number");

$.validator.addMethod("checkModNationalNumber", function (value, element, param) {
    if (value.length == 15) {
        value = value.replace(/\D/g, '');
    }
    var currentYear = new Date().getFullYear().toString();
    var modRes = Number(value.substr(value.length - 2));
    var res = Number(value.substr(0, value.length - 2));
    if (Number(value.substr(0, 2)) <= Number(currentYear.substr(2, 3))) {
        res = Number("2" + value.substr(0, value.length - 2));
    }
    return (97 - res % 97) == modRes;
}, "Het rijksregisternummer is ongeldig"),
//Check if benef account is different from payer account
    $.validator.addMethod("checkIfDifferentFromPayerAccount", function (value, element, param) {
        var different = true;
        try {
            var payerAccount = $('select' + '[name="' + param.selectId + '"] option:selected').text().substring(0, 19).replace(/[ ]/g, "");
            if (typeof (payerAccount) != 'undefined') {
                if ($(element).val().replace(/[ ]/g, "") == payerAccount) {
                    different = false;
                }
            }
        } catch (exception) {

        }
        return this.optional(element) || different;
    }, "The beneficiary account number must be different from the payer account number");

function checkSepaAccountNumberGeneric(value, element, param) {
    var correct = true;
    var pattern = /^[a-zA-Z][a-zA-Z]/;
    var patternNumber = /^\d+/;
    var accountNumber = value.replace(/[^0-9a-zA-Z]/g, '');
    if (accountNumber.length == 12 && patternNumber.test(accountNumber)) {
        var firstNum = accountNumber.substring(0, 10);
        firstNum %= 97;
        if (firstNum == 0) {
            firstNum = 97;
        }
        if (firstNum != parseInt(accountNumber.substring(10, 12), 10)) {
            correct = false;
        } else {
            correct = true;
        }
    } else if (pattern.test(accountNumber)) {
        correct = checkSepaIbanFormat(accountNumber);
    } else {
        correct = false;
    }
    return this.optional(element) || correct;
}

/**
 * Communications
 */
//Check the structured communication
$.validator.addMethod("structuredCommunicationInput", function (value, element, param) {
    var communication = value.replace(/[^\d]/g, '');
    element.value = communication;
    // Note: as this field has an applied mask, we have to remove the mask's
    // specific characters before checking wether field is optional or not
    if (this.optional(element)) {
        return true;
    }
    var correctInput = true;
    try {
        if (typeof (communication) != 'undefined') {
            var number = parseInt(communication.substring(0, 10), 10);
            number = number % 97;
            if (number == 0) {
                number = 97;
            }
            if (number != parseInt(communication.substring(10), 10)) {
                correctInput = false;
            }
        }
    } catch (exception) {
    }
    return correctInput;
}, "The structured communication is not correct");

//Check the european structured communication.
$.validator.addMethod("euStructuredCommunicationInput", function (value, element, param) {
    var communication = value.replace(/[^0-9a-zA-Z]/g, '');
    // Note: as this field has an applied mask, we have to check if something more than
    // the mask is present (has been typed) before checking wether field is optional or not
    if (communication === 'RF') {
        element.value = '';
    }
    if (this.optional(element)) {
        return true;
    }
    var correctInput = true;
    try {
        if (typeof (communication) != 'undefined') {
            if (communication.length > 25 || communication.substring(0, 2) !== 'RF') {
                correctInput = false;
            }
            communication = communication.substring(4, 27) + communication.substring(0, 4);
            communication = communication.toUpperCase();
            var charConst = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
            for (var i = 0; i < charConst.length; i++) {
                var re = new RegExp(charConst[i], 'g');
                communication = communication.replace(re, i + 10);
            }
            if (modulo(communication, "97") !== "1") {
                correctInput = false;
            }
        }
    } catch (exception) {
    }
    return correctInput;
}, "The European structured communication is not correct");

//Check the free communication
$.validator.addMethod("freeCommunicationInput", function (value, element, param) {
    var pattern = /^[a-zA-Z0-9À-Ÿ-\s()+\.,'\\\/]*$/;
    return this.optional(element) || pattern.test(value);
}, "The free communication cannot contain the following characters: ²³&|@#§!{}[]°_\"^¨$*%´`µ£€<>?;:=~");

/**
 * Others
 */

$.validator.addMethod('chequeSerieValid', function (value, element, param) {
    var valid = true;
    var firstNumber = parseInt($(param.chequeFromNumber).val());
    var secondeNumber = parseInt($(param.chequeToNumber).val());
    if ((secondeNumber - firstNumber) > 50 || (firstNumber > secondeNumber)) {
        valid = false;
    }

    return this.optional(element) || valid;
}, 'At least one of these field has to be checked');

//checks if the value doesn't contain html tag
$.validator.addMethod("noHtml", function (value, element, param) {
    var pattern = /.*<([a-z]+)[^<]*(?:\>(.*)<\/\1>|\s*?\/?>).*/;
    return this.optional(element) || !pattern.test(value);
}, "This field contains html tag");

//Check if the search dates are spaced by 6 months
$.validator.addMethod("condOnStatementSearchMonth", function (value, element, param) {
    var tooMuchSpaced = false;
    if ($(param.account).val() == "X") {
        var firstDate = getDateFromFormattedDate($(param.firstDate).val());
        var secondDate = getDateFromFormattedDate($(param.secondDate).val());
        if (firstDate != null && secondDate != null) {
            if (addMonths(firstDate, 6) < secondDate) {
                tooMuchSpaced = true;
            }
        }
        if (firstDate != null && secondDate == null) {
            if (addMonths(firstDate, 6) < new Date()) {
                tooMuchSpaced = true;
            }
        }
        if (firstDate == null) {
            tooMuchSpaced = true;
        }
    }
    return this.optional(element) || !tooMuchSpaced;
}, "The search on all accounts can not be done if search dates are spaced by more than six months");

// checkIf param[0] < param[1] ; params are of type Date
$.validator.addMethod("checkIfNumberOfExecutionsExceeded", function (value, element, param) {
    var correct = true;
    if (param[0]() > param[1]()) {
        correct = false;
    }
    return this.optional(element) || correct;
}, "This number of payment is too big, the last payment date must be lower than {1}");

//Check if a mandatory checkbox is checked
$.validator.addMethod("alertSmsGeneralCondition", function (value, element, param) {
    var checked = true;
    if (!$(element).is(":checked") && $('input[type=radio][name="alert.channel"]:checked').val() == "SMS") {
        checked = false;
    }
    return checked;
}, "You must agree with the general conditions");

//Check if a mandatory checkbox is checked
$.validator.addMethod("checkBoxRequired", function (value, element, param) {
    return $(element).is(":checked")
}, "You must agree with the general conditions");

//Check if first payment date is in suspension months : param[0] is the Date of first payment
// parm[1] is the tab of suspension months
$.validator.addMethod("checkMonthTabConsistency", function (value, element, param) {
    var correct = true;
    var fistPaymentDate;
    var listSuspensionMonth;
    if (typeof (param[0]) == 'function') {
        fistPaymentDate = param[0]();
    } else {
        fistPaymentDate = param[0];
    }
    if (typeof (param[1]) == 'function') {
        listSuspensionMonth = param[1]();
    } else {
        listSuspensionMonth = param[1];
    }
    if (typeof (fistPaymentDate) != 'undefined' && typeof (listSuspensionMonth) != 'undefined') {
        var month = fistPaymentDate.getMonth() + 1;
        for (var i = 0; i < listSuspensionMonth.length; i++) {
            if (month == listSuspensionMonth[i]) {
                correct = false;
            }
        }
    }
    return this.optional(element) || correct;
}, jQuery.validator.format("You can not choose a month that is unchecked in the list of suspension months"));

//Check if next payment date is in suspension months : param[0] is the Date of next payment
//parm[1] is the tab of suspension months
$.validator.addMethod("checkMonthTabConsistencyForNextPayment", function (value, element, param) {
    var correct = true;
    var fistPaymentDate;
    var listSuspensionMonth;
    if (typeof (param[0]) == 'function') {
        fistPaymentDate = param[0]();
    } else {
        fistPaymentDate = param[0];
    }
    if (typeof (param[1]) == 'function') {
        listSuspensionMonth = param[1]();
    } else {
        listSuspensionMonth = param[1];
    }
    if (typeof (fistPaymentDate) != 'undefined' && typeof (listSuspensionMonth) != 'undefined') {
        var month = fistPaymentDate.getMonth() + 1;
        for (var i = 0; i < listSuspensionMonth.length; i++) {
            if (month == listSuspensionMonth[i]) {
                correct = false;
            }
        }
    }
    return this.optional(element) || correct;
}, jQuery.validator.format("You can not choose a month that is unchecked in the list of suspension months"));

//Check if the cut off time is not ellapsed
$.validator.addMethod("urgentPaymentCutOffInput", function (value, element, param) {
    var response = true;
    if ($(element).is(":checked")) {

        var formatTime = function () {
            var dt = new Date();

            var hours = dt.getHours();
            var minutes = dt.getMinutes();

            if (hours < 10)
                hours = '0' + hours;

            if (minutes < 10)
                minutes = '0' + minutes;

            return hours + ":" + minutes;
        };

        var now = formatTime();
        var cutOffTime = param[0];

        var cutOffResponse = timeCompare(now, cutOffTime);

        if (cutOffResponse == 1) {
            response = false;
        }
    }
    return this.optional(element) || response;
}, jQuery.validator.format("Urgent payments can not be done after {0}"));

//Check if it's working bank day
$.validator.addMethod("urgentPaymentIsWorkingDayInput", function (value, element, param) {
    var response = true;
    if ($(element).is(":checked")) {

        var d = new Date();
        var curr_date = d.getDate();
        var curr_month = d.getMonth() + 1; //Months are zero based
        var curr_year = d.getFullYear();

        var today = getDateFromFormattedDate(curr_date + '/' + curr_month + '/' + curr_year);
        var openDate = actionBeforeShowDay(today);

        if (!openDate[0]) {
            response = false;
        }
    }
    return this.optional(element) || response;
}, "Urgent payments are only possible during working days");

/**
 * Amounts
 */
//Check if amount comply with limits given in param array : param[0]=minAmount
$.validator.addMethod("limitedAmount", function (value, element, param) {
    var betweenLimit = true;
    var amount = getAmountFromFormatedAmount(value);
    var lowerLimit;
    if (typeof (param[0]) == 'function') {
        lowerLimit = param[0]();
    } else {
        lowerLimit = param[0];
    }
    var upperLimit;
    if (typeof (param[1]) == 'function') {
        upperLimit = param[1]();
    } else {
        upperLimit = param[1];
    }
    upperLimit = upperLimit.replace(/[,]/g, ".");
    if (typeof (upperLimit) != 'undefined' && typeof (amount) != 'undefined') {
        if (Number(amount) > Number(upperLimit)) {
            betweenLimit = false;
        }
    }
    lowerLimit = lowerLimit.replace(/[,]/g, ".");
    if (typeof (lowerLimit) != 'undefined' && typeof (amount) != 'undefined') {
        if (Number(amount) < Number(lowerLimit)) {
            betweenLimit = false;
        }
    }
    if (typeof (upperLimit) != 'undefined') {
        param[1] = upperLimit.replace(/[\.]/g, ",");
    }
    if (typeof (lowerLimit) != 'undefined') {
        param[0] = lowerLimit.replace(/[\.]/g, ",");
    }
    return this.optional(element) || betweenLimit;
}, jQuery.validator.format("The amount must be between {0} and {1}"));

//Check if value return by function param[1]() is bigger than param[0]
$.validator.addMethod("checkMaxLength", function (value, element, param) {
    var ok = true;
    var value;
    var limit;
    if (typeof (param[1]) == 'function') {
        value = param[1]();
    } else {
        value = param[1];
    }
    if (typeof (param[0]) == 'function') {
        limit = param[0]();
    } else {
        limit = param[0];
    }
    if (value > limit) {
        ok = false;
    }
    return this.optional(element) || ok;
}, jQuery.validator.format("Please, do not input more than {0} characters."));

//Check if amount at format 99.999,99 comply with correct order
//param contains as firstAmount and secondAmount the ELEMENTS for input amounts to test
$.validator.addMethod("amountsOrdered", function (value, element, param) {
    var ordered = true;
    var firstAmount = getAmountFromFormatedAmount($(param.firstAmount).val());
    var secondAmount = getAmountFromFormatedAmount($(param.secondAmount).val());
    if (typeof (firstAmount) != 'undefined' && typeof (secondAmount) != 'undefined') {
        if (firstAmount > secondAmount) {
            ordered = false;
        }
    }
    return this.optional(element) || ordered;
}, "Amounts are not correctly ordered");

//Check pattern for a input currency amount
$.validator.addMethod("checkCurrencyPattern", function (value, element, param) {
    var amount = value.replace(/\s/g, "").replace(/[,]/g, ".");
    var pattern;
    if (typeof (param[0]) == 'function') {
        pattern = param[0]();
    } else {
        pattern = param[0];
    }
    var nbrDcm;
    if (typeof (param[1]) == 'function') {
        nbrDcm = param[1]();
        param[1] = nbrDcm;
    } else {
        nbrDcm = param[1];
    }
    var re = new RegExp(pattern);
    var test = this.optional(element) || re.test(amount);
    if (test)
        toNumberFormatExt(element, nbrDcm);
    return test;
}, jQuery.validator.format("The amount must be a number, with maximum {1} digit(s) after the comma"));

//Check pattern for a input currency amount - doesn't change the element value format
$.validator.addMethod("verifyCurrencyPattern", function (value, element, param) {
    var amount = value.replace(/\s/g, "").replace(/[,]/g, ".");
    var pattern;
    if (typeof (param[0]) == 'function') {
        pattern = param[0]();
    } else {
        pattern = param[0];
    }
    var nbrDcm;
    if (typeof (param[1]) == 'function') {
        nbrDcm = param[1]();
    } else {
        nbrDcm = param[1];
    }
    var re = new RegExp(pattern);
    var test = this.optional(element) || re.test(amount);
    return test;
}, jQuery.validator.format("The amount must be a number, with maximum {1} digit(s) after the comma"));

//get amount from string amount at format 99 999,99
function getAmountFromFormatedAmount(amount) {
    var correctedAmount;
    try {
        correctedAmount = amount.replace(/[.\s]/g, "");
        correctedAmount = correctedAmount.replace(/[,]/g, ".");
        correctedAmount = parseFloat(correctedAmount);
    } catch (err) {

    }
    return correctedAmount;
}

/**
 * Numbers
 */
//Check if input is a positive integer
$.validator.addMethod("checkIfInteger", function (value, element, param) {
    var pattern = /\d+/;
    return this.optional(element) || pattern.test(value);
}, "Insert an integer");

//check if input is bigger than one
$.validator.addMethod("checkIfBiggerThanOne", function (value, element, param) {
    var biggerThanOne = true;
    try {
        if (value <= 1) {
            biggerThanOne = false;
        }
    } catch (exception) {

    }
    return this.optional(element) || biggerThanOne;
}, "The number must be bigger than one");

//check if input is bigger than zero
$.validator.addMethod("biggerThanZeroInput", function (value, element, param) {
    var biggerThanZero = true;
    var number = parseFloat(getAmountFromFormatedAmount(value));
    try {
        if (number <= 0) {
            biggerThanZero = false;
        }
    } catch (exception) {
    }
    return this.optional(element) || biggerThanZero;
}, "The number must be bigger than zero");

//Check if account digit number is greater than three
$.validator.addMethod("checkIntegerNotHigherThan", function (value, element, param) {
    var correct = true;
    var max;
    if (typeof (param[0]) == 'function') {
        max = param[0]();
    } else {
        max = param[0];
    }
    var val = parseInt(value, 10);
    if (val > max) {
        correct = false;
    }
    return this.optional(element) || correct;
}, jQuery.validator.format("The number must not be higher than {0}"));

/**
 * Dates
 */
//Check if defaultDate has format DD/MM/YYYY 
$.validator.addMethod("defaultDateFormat", function (value, element, param) {
    var pattern = /^\d\d?\/\d\d?\/\d\d\d\d$/;
    var date = allDateToGeneralFormat(value);
    return this.optional(element) || pattern.test(date);
}, "Enter a date to format DD/MM/YYYY");

//Check if date at format DD/MM/YYYY is coherent
$.validator.addMethod("dateValid", function (value, element, param) {
    var date = getFormattedDate(value);
    var day, month, year;
    if (date.length > 0) {
        day = date.substring(0, 2);
        month = date.substring(2, 4);
        year = date.substring(4, 8);
    }
    return this.optional(element) || ((date.length > 0) && isValidDate(day, month, year));
}, "Please enter a valid date");

//check if the supplied date is a working day
$.validator.addMethod("workingDayInput", function (value, element, param) {
    var date = getDateFromFormattedDate(value);
    var isWorkingDay = true;
    if (!!date && (isWeekend(date) || isHoliday(date))) {
        isWorkingDay = false;
    }
    return this.optional(element) || isWorkingDay;
}, jQuery.validator.format("The date must be a working day."));

//Check date for weekly frequency 
$.validator.addMethod("weeklyDateInput", function (value, element, param) {
    var correct = true;
    var date = getDateFromFormattedDate($('#firstPaymentDate').val());
    if ($('#selectedPeriodicity').val() == "WEEKLY" && typeof date != 'undefined') {
        if (date.getDay() == 0 || date.getDay() == 6) {
            correct = false;
        }
    }
    return correct;
}, "The first payment date cannot be a week-end day if the periodicity is weekly");

//Check date for bimonthly frequency 
$.validator.addMethod("bimonthlyDateInput", function (value, element, param) {
    var correct = true;
    var date = getDateFromFormattedDate($('#firstPaymentDate').val());
    if ($('#selectedPeriodicity').val() == "BIMONTHLY" && typeof date != 'undefined') {
        if (date.getDate() != 15 && date.getDate() != 1) {
            correct = false;
        }
    }
    return correct;
}, "The date must be the 01st or the 15th of the month");

//Check if dates at format DD/MM/YYYY comply with chronological order
//param contains as firstDate and secondDate the ELEMENTS for input dates to test
$.validator.addMethod("chronologicalDates", function (value, element, param) {
    var chronological = true;
    var firstDate = getDateFromFormattedDate($(param.firstDate).val());
    var secondDate = getDateFromFormattedDate($(param.secondDate).val());
    if (typeof (firstDate) != 'undefined' && typeof (secondDate) != 'undefined') {
        if (firstDate > secondDate) {
            chronological = false;
        }
    }
    return this.optional(element) || chronological;
}, "Dates don't respect chronological order");

//Check if date at format DD/MM/YYYY comply with limits given as  param[0] < param[1] or is equal to param[2]
$.validator.addMethod("nextPaymentDateRule", function (value, element, param) {
    var betweenLimit = checkLimitedDate(value, element, param);
    var exceptionDate;
    if (typeof (param[2]) == 'function') {
        exceptionDate = param[2]();
    } else {
        exceptionDate = param[2];
    }
    // compare strings
    var datesEqual = getFormattedDate(exceptionDate) == getFormattedDate(value);
    return this.optional(element) || betweenLimit || datesEqual;
}, jQuery.validator.format("The date must be between {0} and {1}"));

//Check if date at format DD/MM/YYYY comply with limits given as  param[0] < param[1]
$.validator.addMethod("limitedDate", function (value, element, param) {
    var betweenLimit = checkLimitedDate(value, element, param);
    return this.optional(element) || betweenLimit;
}, jQuery.validator.format("The date must be between {0} and {1}"));

//Check if date at format DD/MM/YYYY comply with max limit given as  param[0]
$.validator.addMethod("limitedMaxDate", function (value, element, param) {
    var betweenLimit = true;
    var maxDate;
    if (typeof (param[0]) == 'function') {
        maxDate = getDateFromFormattedDate(param[0]());
    } else {
        maxDate = getDateFromFormattedDate(param[0]);
    }
    var date = getDateFromFormattedDate(value);
    if (typeof (maxDate) != 'undefined' && typeof (date) != 'undefined') {
        if (date > maxDate) {
            betweenLimit = false;
        }
    }
    param[1] = $.datepicker.formatDate('dd/mm/yy', maxDate);
    return this.optional(element) || betweenLimit;
}, jQuery.validator.format("The date must be before {0}"));

//Check if date at format DD/MM/YYYY comply with min limit given as param[0]
$.validator.addMethod("limitedMinDate", function (value, element, param) {
    var betweenLimit = true;
    var minDate;
    if (typeof (param[0]) == 'function') {
        minDate = getDateFromFormattedDate(param[0]());
    } else {
        minDate = getDateFromFormattedDate(param[0]);
    }
    var date = getDateFromFormattedDate(value);
    if (typeof (minDate) != 'undefined' && typeof (date) != 'undefined') {
        if (date < minDate) {
            betweenLimit = false;
        }
    }
    param[1] = $.datepicker.formatDate('dd/mm/yy', minDate);
    return this.optional(element) || betweenLimit;
}, jQuery.validator.format("The date must be after {0}"));

function checkLimitedDate(value, element, param) {
    var betweenLimit = true;
    var minDate;
    var maxDate;
    if (typeof (param[0]) == 'function') {
        minDate = getDateFromFormattedDate(param[0]());
    } else {
        minDate = getDateFromFormattedDate(param[0]);
    }
    if (typeof (param[1]) == 'function') {
        maxDate = getDateFromFormattedDate(param[1]());
    } else {
        maxDate = getDateFromFormattedDate(param[1]);
    }
    var date = getDateFromFormattedDate(value);
    if (typeof (minDate) != 'undefined' && typeof (date) != 'undefined') {
        if (date < minDate) {
            betweenLimit = false;
        }
    }
    if (typeof (maxDate) != 'undefined' && typeof (date) != 'undefined') {
        if (date > maxDate) {
            betweenLimit = false;
        }
    }

    return betweenLimit;
}

/*
 * Compare time - Format hh:mm
 */
function timeCompare(time1, time2) {
    var t1 = new Date();
    var parts = time1.split(":");
    t1.setHours(parts[0], parts[1], 0, 0);
    var t2 = new Date();
    parts = time2.split(":");
    t2.setHours(parts[0], parts[1], 0, 0);

    // returns 1 if greater, -1 if less and 0 if the same
    if (t1.getTime() >= t2.getTime())
        return 1;
    if (t1.getTime() < t2.getTime())
        return -1;
    return 0;
}

/* Function to test if a date is valid
 * This function takes care of month number and bisextil years
 * @param day The day of the date that must be tested
 * @param month The month of the date that must be tested
 * @param year The year of the date that must be tested
 * @return true if the date is a valid date (day is coherent with month and year)
 */
function isValidDate(day, month, year) {
    if (month < 1 || month > 12 || day > 31 || day < 1) {
        return false;
    }
    if (month == 4 || month == 6 || month == 9 || month == 11) {
        if (day > 30) {
            return false;
        }
    } else if (month == 2) {
        var bisextile = isBisextile(year);
        if (bisextile && day > 29) {
            return false;
        } else
            return !(!bisextile && day > 28);
    }
    return true;
}

/* Check if a year is a bisextile year
 * A bisextile year can be divided by 4 but not by 100 except if it can be divided by 400
 * @param year The year to check if bisextil
 * @return true if the year is bisextile.
 */
function isBisextile(year) {
    if (eval(year % 4) == 0 && (eval(year % 100) != 0 || eval(year % 400) == 0)) {
        return true;
    }
    return false;
}

/* Get date to format DDMMYYYY. This function does not check coherency of the date
 * @param date Date that respect pattern DDMMYYYY or DD/MM/YYYY
 * @return date at format DDMMYYYY
 * */
function getFormattedDate(date) {
    var pattern = /^\d\d\/\d\d\/\d\d\d\d$/;
    var day = "", month = "", year = "";

    if (!pattern.test(date)) {
        date = allDateToGeneralFormat(date);
    }

    var tabDate = date.split('/');
    day = tabDate[0];
    month = tabDate[1];
    year = tabDate[2];

    return day + month + year;
}

function getDateFromFormattedDate(date) {
    var formattedDate = getFormattedDate(date);
    var day, month, year;
    if (formattedDate.length == 8) {
        day = parseInt(formattedDate.substring(0, 2), 10);
        // month go from 0 -> 11
        month = parseInt(formattedDate.substring(2, 4), 10) - 1;
        year = parseInt(formattedDate.substring(4, 8), 10);
        return new Date(year, month, day);
    }
    return null;
}

function allDateToGeneralFormat(date) {
    var day = "", month = "", year = "";

    var pattern = /^\d\d?\/\d\d?\/\d\d\d\d$/;
    if (pattern.test(date)) {
        var tabDate = date.split('/');
        day = tabDate[0];
        month = tabDate[1];
        year = tabDate[2];

        if (day.length == 1) {
            day = "0" + day;
        }
        if (month.length == 1) {
            month = "0" + month;
        }

        return day + "/" + month + "/" + year;
    }

    var pattern = /^\d\d\d\d\d\d$/;
    if (pattern.test(date)) // 191205 -> 19/12/2005
    {
        day = date.substring(0, 2);
        month = date.substring(2, 4);
        year = date.substring(4, 6);

        if (parseInt(year, 10) < 50)
            year = "20" + year;
        else
            year = "19" + year;

        return day + "/" + month + "/" + year;
    }

    pattern = /^\d\d\d\d\d\d\d\d$/; // 19122005 -> 19/12/2005
    if (pattern.test(date)) {
        day = date.substring(0, 2);
        month = date.substring(2, 4);
        year = date.substring(4, 8);

        return day + "/" + month + "/" + year;
    }

    pattern = /^\d\d?\/\d\d?\/\d\d$/; // 19/12/05 -> 19/12/2005
    if (pattern.test(date)) {
        var tabDate = date.split('/');
        day = tabDate[0];
        month = tabDate[1];
        year = tabDate[2];

        if (day.length == 1) {
            day = "0" + day;
        }
        if (month.length == 1) {
            month = "0" + month;
        }

        if (parseInt(year, 10) < 50)
            year = "20" + year;
        else
            year = "19" + year;

        return day + "/" + month + "/" + year;
    }

    pattern = /^\d\d?\s\d\d?\s\d\d\d\d$/; // 19 12 2005 -> 19/12/2005
    if (pattern.test(date)) {
        var tabDate = date.split(' ');
        day = tabDate[0];
        month = tabDate[1];
        year = tabDate[2];

        if (day.length == 1) {
            day = "0" + day;
        }
        if (month.length == 1) {
            month = "0" + month;
        }

        return day + "/" + month + "/" + year;
    }

    pattern = /^\d\d?\s\d\d?\s\d\d$/; // 19 12 05 -> 19/12/2005
    if (pattern.test(date)) {
        var tabDate = date.split(' ');
        day = tabDate[0];
        month = tabDate[1];
        year = tabDate[2];

        if (day.length == 1) {
            day = "0" + day;
        }
        if (month.length == 1) {
            month = "0" + month;
        }

        if (parseInt(year, 10) <= 50)
            year = "20" + year;
        else
            year = "19" + year;

        return day + "/" + month + "/" + year;
    }

    return date;
}

//get formatted date from a date object
function getFormattedDateFromDate(date) {
    var formattedDate;
    try {
        var day = date.getDate().toString();
        if (day.length == 1) {
            day = '0' + day;
        }
        var month = date.getMonth() + 1;
        month = month.toString();
        if (month.length == 1) {
            month = '0' + month;
        }
        var fullYear = date.getFullYear();
        formattedDate = day + '/' + month + '/' + fullYear;
    } catch (err) {
    }
    return formattedDate;
}

// validates if the lower amount is given when the upper amount is given too
$.validator.addMethod("upperAmountWithLowerAmount", function (value, element, param) {
    var lowerAmount = getAmountFromFormatedAmount($(param[0]).val());
    var upperAmount = getAmountFromFormatedAmount($(param[1]).val());

    if (!$.isNumeric(lowerAmount)) {
        return !$.isNumeric(upperAmount);
    } else {
        return true;
    }
});

// validates if the ip address is given
$.validator.addMethod("unvalidIpAddress", function (value, element, param) {
    var pattern = /[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}/;
    return this.optional(element) || pattern.test(value);
});

//validates if date is less than today
$.validator.addMethod("afterToday", function (value, element, param) {
    var afterToday = true;
    var today = new Date();
    var date = getDateFromFormattedDate(value);
    if (typeof (today) != 'undefined' && typeof (date) != 'undefined') {
        if (date > today) {
            afterToday = false;
        }
    }
    return this.optional(element) || afterToday;
});

$.validator.addMethod("minDateRequired", function (value, element, param) {
    var minDateRequired = true;
    var firstDate = getDateFromFormattedDate($(param.firstDate).val());
    var secondDate = getDateFromFormattedDate($(param.secondDate).val());
    if ($(param.secondDate).val().length > 0 && $(param.firstDate).val().length === 0) {
        minDateRequired = false;
    }
    return this.optional(element) || minDateRequired;
});

$.validator.addMethod('validAmount', function (value, element, param) {
    return value.search(/^(€ )?([0]|([1-9][0-9]{0,2})?(\.\d{3})*|[1-9][0-9]*)([,|\.]\d{1,2})?$/g) >= 0;
}, 'The amount format is not correct');

$.validator.addMethod('androidValidAmountAbs', function (value, element, param) {
    return value.search(/^[0-9]+([,|\.][0-9][0-9]?)?$/) >= 0 && parseFloat(value) <= 9999999999.99;
}, 'The amount format is not correct');

$.validator.addMethod('androidValidAmount', function (value, element, param) {
    return value.search(/^(-)?[0-9]+([,|\.][0-9][0-9]?)?$/) >= 0 && parseFloat(value) <= 9999999999.99 && parseFloat(value) >= -9999999999.99;
}, 'The amount format is not correct');

$.validator.addMethod("require_from_group", function (value, element, param) {
    var numberRequired = param[0];
    var selector = param[1];
    var fields = $(selector, element.form);
    var filled_fields = fields.filter(function () {
        // it's more clear to compare with empty string
        return $(this).val() != "";
    });
    var empty_fields = fields.not(filled_fields);
    // we will mark only first empty field as invalid
    if (filled_fields.length < numberRequired && empty_fields[0] == element) {
        return false;
    }
    return true;
    // {0} below is the 0th item in the options field
}, "Please fill out at least {0} of these fields.");