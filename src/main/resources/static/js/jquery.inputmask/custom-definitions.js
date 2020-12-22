(function ($) {

    $
        .extend(
            $.inputmask.defaults.aliases,
            {
                fh_date: {
                    alias: "dd/mm/yyyy",
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
                }
            });
})(jQuery);


function inputMaskEvents() {
    $("[data-inputmask-alias]").inputmask();
}

