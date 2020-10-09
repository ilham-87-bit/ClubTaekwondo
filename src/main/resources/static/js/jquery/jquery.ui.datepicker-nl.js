/* French initialisation for the jQuery UI date picker plugin. */
/* Written by Keith Wood (kbwood{at}iinet.com.au) and Stéphane Nahmani (sholby@sholby.net). */
jQuery(function ($) {
    $.datepicker.regional['nl'] = {
        closeText: 'sluiten',
        closeStatus: 'Onveranderd sluiten ',
        prevText: '<vorige',
        prevStatus: 'Zie de vorige maand',
        nextText: 'volgende>',
        nextStatus: 'Zie de volgende maand',
        currentText: 'Huidige',
        currentStatus: 'Bekijk de huidige maand',
        monthNames: ['januari', 'februari', 'maart', 'april', 'mei', 'juni',
            'juli', 'augustus', 'september', 'oktober', 'november', 'december'],
        monthNamesShort: ['jan', 'feb', 'mrt', 'apr', 'mei', 'jun',
            'jul', 'aug', 'sep', 'okt', 'nov', 'dec'],
        monthStatus: 'Bekijk een andere maand', yearStatus: 'Bekijk nog een jaar',
        weekHeader: 'Sm', weekStatus: '',
        dayNames: ['zondag', 'maandag', 'dinsdag', 'woensdag', 'donderdag', 'vrijdag', 'zaterdag'],
        dayNamesShort: ['zo', 'ma', 'di', 'wo', 'do', 'vr', 'za'],
        dayNamesMin: ['zo', 'ma', 'di', 'wo', 'do', 'vr', 'za'],
        dayStatus: 'Gebruik DD als de eerste dag van de week', dateStatus: 'Kies DD, MM d',
        dateFormat: 'dd/mm/yy',
        firstDay: 1,
        initStatus: 'Kies een datum',
        isRTL: false,
        howMonthAfterYear: false,
        yearSuffix: ''
    };
    $.datepicker.setDefaults($.datepicker.regional['nl']);
});