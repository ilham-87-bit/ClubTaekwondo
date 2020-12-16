(function ($) {
    $.get = function (url, data, successCallback, errorCallback) {
        return $.ajax({
            url: url,
            type: 'get',
            cache: false,
            data: data,
            success: getMessageSuccessCallback(successCallback, data),
            error: getAjaxErrorCallback(errorCallback)
        });
    };

    $.post = function (url, data, successCallback, errorCallback) {
        // if no data -> get method (post without arguments not accepted)
        if (!data || typeof data === 'function') {
            return $.get(url, data, successCallback);
        }

        return $.ajax({
            url: url,
            type: 'post',
            data: data,
            cache: false,
            success: getMessageSuccessCallback(successCallback, data),
            error: getAjaxErrorCallback(errorCallback, data)
        });
    };

    $.postJSON = function (url, data, successCallback, errorCallback) {
        // if no data -> get method (post without arguments not accepted)
        if (!data || typeof data === 'function') {
            return $.getJSON(url, data, successCallback);
        }

        return $.ajax({
            url: url,
            type: 'post',
            data: data,
            cache: false,
            dataType: 'json',
            success: getMessageSuccessCallback(successCallback, data),
            error: getAjaxErrorCallback(errorCallback, data)
        });
    };

    $.fn.getFragment = function (url, data, successCallback, noScroll, errorCallback) {
        // Stop execution if 'this' is empty or no url has been provided
        if (!$(this).length || !url) {
            return this;
        }

        // In case no data has been provided (the success callback is in 'data' and the optional
        // error callback is in 'successCallback', which leaves 'errorCallback' empty)
        var noDataDefined = typeof data === 'function' && !errorCallback;

        var scrollPosition = $(document).scrollTop();

        // Extending the successCallback with the load default behaviour
        var extendedSuccessCallback = (function (_super, element) {
            return function () {

                if (noScroll) {
                    $(document).scrollTop(scrollPosition);
                }
                // Perform the default behabiour of the load function
                $(element).html(arguments[0]);
                // And execute the successCallback if it has been defined
                return _super ? _super.apply(this, arguments) : null;
            };
        })(noDataDefined ? data : successCallback, this);

        if (noDataDefined) {
            // Recall: The optional error callback is in 'successCallback'
            return $.get(url, extendedSuccessCallback, successCallback);
        }
        return $.get(url, data, extendedSuccessCallback, errorCallback);
    };

    $.fn.load = function (url, data, successCallback, noScroll, errorCallback) {
        // Stop execution if 'this' is empty or no url has been provided
        if (!$(this).length || !url) {
            return this;
        }

        // In case no data has been provided (the success callback is in 'data' and the optional
        // error callback is in 'successCallback', which leaves 'errorCallback' empty)
        var noDataDefined = typeof data === 'function' && !errorCallback;

        var scrollPosition = $(document).scrollTop();

        // Extending the successCallback with the load default behaviour
        var extendedSuccessCallback = (function (_super, element) {
            return function () {
                if (noScroll) {
                    $(document).scrollTop(scrollPosition);
                }

                // Perform the default behabiour of the load function
                $(element).html(arguments[0]);
                // And execute the successCallback if it has been defined
                return _super ? _super.apply(this, arguments) : null;
            };
        })(noDataDefined ? data : successCallback, this);

        if (noDataDefined) {
            // Recall: The optional error callback is in 'successCallback'
            return $.post(url, extendedSuccessCallback, successCallback);
        }
        return $.post(url, data, extendedSuccessCallback, errorCallback);
    };

    var getMessageSuccessCallback = function (successCallback, data) {

        // Extending the successCallback
        successCallback = (function (_super, element) {
            return function () {

                if (containsFragment(arguments[0])) {
                    includeFragments(arguments[0]);
                }

                $(document).scrollTop(0);

                // And execute the errorCallback if it has been defined
                return _super ? _super.apply(this, arguments) : null;
            };
        })(successCallback, this);

        return successCallback;
    };

    var getAjaxErrorCallback = function (errorCallback) {
        // Extending the errorCallback
        errorCallback = (function (_super, element) {
            return function () {
                // Include the error fragment
                if (arguments[0].status === 401 || arguments[0].getResponseHeader("X-FHO-Redirect")) {
                    return;
                }

                if (arguments[0].status === 403 || arguments[0].status === 500) {
                    document.open();
                    document.write(arguments[0].responseText);
                    document.close();
                    return;
                }

                // And execute the errorCallback if it has been defined
                return _super ? _super.apply(this, arguments) : null;
            };
        })(errorCallback, this);

        return errorCallback;
    };

    $.fn.enableElement = function () {
        return $(this).removeClass('disabled');
    };

    $.fn.disableElement = function () {
        return $(this).addClass('disabled');
    };

    $.fn.isElementDisabled = function () {
        return $(this).hasClass('disabled');
    };

    // Enable all form fields (and save disabled field set)
    $.fn.enableFormFields = function () {
        var disableFields = $(this).find(':disabled');

        if (disableFields.length) {
            // Save the set as JS property for later restore
            this.savedDisableFields = disableFields;

            disableFields.each(function () {
                $(this).removeAttr('disabled');
            });
        }

        // Keep chainability
        return $(this);
    };

    // Restore previous state by disabling saved fields
    $.fn.restoreDisableFormFields = function () {
        if (this.savedDisableFields && this.savedDisableFields.length) {
            this.savedDisableFields.each(function () {
                $(this).attr('disabled', 'disabled');
            });
            delete this.savedDisableFields;
        }

        // Keep chainability
        return $(this);
    };

    // Disable all form fields (and save enabled field set)
    $.fn.disableFormFields = function () {
        var enableFields = $(this).find(':enabled');

        if (enableFields.length) {
            // Save the set as JS property for later restore
            this.savedEnableFields = enableFields;

            enableFields.each(function () {
                $(this).attr('disabled', 'disabled');
            });
        }

        // Keep chainability
        return $(this);
    };

    // Restore previous state by enabling saved fields
    $.fn.restoreEnableFormFields = function () {
        if (this.savedEnableFields && this.savedEnableFields.length) {
            this.savedEnableFields.each(function () {
                $(this).removeAttr('disabled');
            });
            delete this.savedEnableFields;
        }

        // Keep chainability
        return $(this);
    };

    // Structured characters only control handler
    $.fn.forceStructuredCharactersOnly = function () {
        return this.each(function () {
            $(this).keyup(function (event) {
                $(this).val($(this).val().replace(/[^\d\/]/g, ''));
            });
        });
    };

    $.fn.forceNumbersOnly = function () {
        return this.each(function () {
            $(this).keyup(function (event) {
                $(this).val($(this).val().replace(/[^\d]/g, ''));
            });
        });
    };

    $.fn.preventDoubleSubmission = function () {
        $(this).bind('submit', function (e) {
            if ($(this).data('submitted') === true) {
                // Previously submitted - don't submit again
                e.preventDefault();
            } else {
                // If a validator has been defined, we call the '.valid' method. Otherwise,
                // the form will not be submitted and must therefore not be tagged has 'submitted'
                if (!$(this).data('validator') || $(this).valid()) {
                    // Mark this so that the next submit can be ignored
                    $(this).data('submitted', true);
                }
            }
        });

        // Keep chainability
        return $(this);
    };

    $.fn.autoCompleteOnEnterKey = function () {
        $(this).keyup(function (e) {
            if ((e.which === 10 || e.which === 13) && $(this).data('autocomplete') != 'undefined') {
                if ($(this).data('autocomplete').menu.element.is(':visible') && $(this).data('autocomplete').menu.element.children().size() === 1) {
                    $(this).data('autocomplete').menu.element.children().first().find('a').trigger('mouseover');
                    $(this).data('autocomplete').menu.element.children().first().find('a').trigger('click');
                }
            }
        });

        // Keep chainability
        return $(this);
    };

    // Function used to submit form without calling validation
    // validation is disabled than DOM form is submitted followed by triggering of submit events
    $.fn.submitWithoutValidation = function () {
        if ($($(this).data('validator')).length) {
            // way to remove validator -- no elements to check
            // remove the data may generate a js exception
            $(this).data('validator').elements = function () {
                return $();
            };
            $(this)[0].submit();
            $(this).triggerHandler('submit');
        } else {
            $(this).submit();
        }

        // Keep chainability
        return $(this);
    };

    // Function used to perform validation with unWantedValidation classes as the classes you don t want to valid
    $.fn.filteredValidation = function (unWantedValidationClasses) {
        // unWantedValidationClasses can be spaces, commas, or ; delimited strings
        var reg = new RegExp('[ ,;]+', 'g');
        var unWantedTab = unWantedValidationClasses.split(reg);
        var retrievedClass = [];
        var i = 0;
        var unWanted = '';
        // remove unwanted classes
        for (var k = 0; k < unWantedTab.length; k++) {
            unWanted = unWantedTab[k];
            if ($(this).hasClass(unWanted)) {
                $(this).toggleClass(unWanted, false);
                retrievedClass[i] = unWanted;
                i++;
            }
        }
        var returnValue = $(this).valid();
        // re - add unwanted classes
        for (var j = 0; j < retrievedClass.length; j++) {
            $(this).toggleClass(retrievedClass[j], true);
        }
        return returnValue;
    };

    $.fn.clearValidation = function (selector) {
        if (!$(this).is('form')) {
            return;
        }

        // Internal $.validator is exposed through $(form).validate()
        var validator = $(this).validate(), errorFields = selector;

        if (typeof selector === 'object') {
            // Filter error fields from a set of elements
            errorFields = selector.filter('.' + validator.settings.errorClass);
        } else {
            // Find error fields using a selector if provided
            errorFields = $(this).find((selector || '') + ' :input.' + validator.settings.errorClass);
        }

        // Iterate through elements in error inside of the form
        errorFields.each(function () {
            // Remove the labels
            validator.settings.success(validator.errorsFor(this));
            // Unhighlight the input fields
            validator.settings.unhighlight(this, validator.settings.errorClass);
        });

        // Remove all error and success data
        validator.reset();

        // Keep chainability
        return $(this);
    };

    $.fn.disableSelection = function () {
        return this.attr('unselectable', 'on').css('user-select', 'none').on('selectstart', false);
    };

    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    $.fn.selectRange = function (start, end) {
        if (!end) {
            end = start;
        }
        return this.each(function () {
            if (this.setSelectionRange) {
                this.focus();
                this.setSelectionRange(start, end);
            } else if (this.createTextRange) {
                var range = this.createTextRange();
                range.collapse(true);
                range.moveEnd('character', end);
                range.moveStart('character', start);
                range.select();
            }
        });
    };

    $.fn.classList = function () {
        return $(this).attr('class').split(/\s+/);
    };

    $.fn.classSelector = function () {
        if ($(this).attr('class')) {
            return '.' + $(this).attr('class').replace(/\s+/g, '.');
        }
        return '';
    };

    // bind an event on the top of stack
    // this event is disabled on the first click
    jQuery.fn.oneFirst = function (type, parameters, fn) {
        attachFirst(this, type, parameters, fn, true);
    };

    // bind an event on the top of stack
    // this event is never disabled
    jQuery.fn.onFirst = function (type, parameters, fn) {
        attachFirst(this, type, parameters, fn, false);
    };

    function attachFirst(element, type, parameters, fn, one) {
        type = type.split(/\s+/);
        return element.each(function () {
            var len = type.length;
            while (len--) {
                if (typeof parameters === 'function') {
                    if (one) {
                        jQuery(this).one(type[len], parameters);
                    } else {
                        jQuery(this).on(type[len], parameters);
                    }
                } else {
                    if (one) {
                        jQuery(this).one(type[len], parameters, fn);
                    } else {
                        jQuery(this).on(type[len], parameters, fn);
                    }
                }
                var evt = jQuery._data(this, 'events')[type[len]];
                evt.splice(0, 0, evt.pop());
            }
        });
    }

    $._funcNull = function () {
    };

})(jQuery);
