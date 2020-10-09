(function ($) {

    $
        .extend(
            $.inputmask.defaults.aliases,
            {
                fh_amount: {
                    alias: "currency",
                    radixPoint: ",",
                    groupSeparator: " ",
                    autogroup: true,
                    allowMinus: true,
                    digits: 2,
                    digitsOptional: false,
                    radixFocus: false,
                    prefix: "",
                    placeholder: "0",
                    max: 10000000000.00,
                    min: -10000000000.00,
                    decimalProtect: true
                },
                fh_amountSearch: {
                    alias: "fh_amount",
                    autoUnmask: true,
                    removeMaskOnSubmit: true
                },
                fh_amountAbs: {
                    alias: "fh_amount",
                    allowPlus: false,
                    allowMinus: false,
                    autoUnmask: true
                },
                fh_amountAbs_decimal: {
                    alias: "fh_amountAbs",
                    onBeforeMask: function (value, opts) {
                        return value.toString().replace('.', ',');
                    }
                },
                fh_amountAbsSearch: {
                    alias: "fh_amountAbs",
                    autoUnmask: true,
                    removeMaskOnSubmit: true,
                    autoUnmask: true,
                    removeMaskOnSubmit: true
                },
                fh_date: {
                    alias: "datetime",
                    mask: "1/2/y",
                    placeholder: "_",
                    inputFormat: "dd/mm/yyyy",
                    min: "01/01/1900",
                    max: "31/12/2099",
                    insertMode: true,
                    removeMaskOnSubmit: true
                },
                fh_rrn: {
                    mask: "9{2}.9{2}.9{2}-9{3}.9{2}",
                    autoUnmask: true,
                    placeholder: "_",
                    insertMode: true,
                    clearMaskOnLostFocus: false,
                    removeMaskOnSubmit: true
                },
                fh_cbe: {
                    mask: "09{3}.9{3}.9{3}",
                    autoUnmask: true,
                    clearMaskOnLostFocus: false,
                    insertMode: true,
                    placeholder: "_",
                    removeMaskOnSubmit: true,
                    onBeforePaste: function (pastedValue) {
                        pastedValue = pastedValue.replace(/\./g, '').trim();

                        if (pastedValue.length == 10) {
                            pastedValue = pastedValue.substring(1);
                        }

                        return pastedValue;
                    },
                    onUnMask: function (maskedValue, unmaskedValue) {

                        if (unmaskedValue.length > 0) {
                            return 'BE' + maskedValue.replace(/\./g, '');
                        }

                        return unmaskedValue;
                    },
                    onBeforeMask: function (value, opts) {

                        var firstThree = value.toString().substring(0, 3);
                        var firstOne = value.substring(0, 1);
                        if (firstThree == 'BE0') {
                            return value.toString().substring(3);
                        } else if (firstOne == '0') {
                            return value.substring(1);
                        }
                    }
                },
                fh_numApp: {
                    mask: "9{4}/9{6}/9{2}",
                    autoUnmask: true,
                    placeholder: "_",
                    removeMaskOnSubmit: true
                }
            });
})(jQuery);


function inputMaskEvents() {
    $("[data-inputmask-alias]").inputmask();
}

