var form = $('#idAddSubscriptionForm');

$(document).ready(function () {
    formFunction();
    inputDate();
});

function formFunction() {
    var i = 0;
    do {
        if ($('#' + i).prop('checked') === true) {
            $('#addSelect' + i).removeClass('hidden');
            i++;
        } else {
            $('#addSelect' + i).addClass('hidden');
            i++;
        }
    } while (i <= $('#inputSchool').length);

}

function inputDate() {
    $('#startDate').date("minDate", new Date());
}
