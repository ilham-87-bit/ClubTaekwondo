var form = $('#idAddSubscriptionForm2');

$(document).ready(function () {
    inputDate();
    calculateEndDateFormFunction();
});

function inputDate() {
    $('#startDate').datepicker({
        minDate: new Date(),
        clearBtn: true,
        dateFormat: "dd/mm/yy"
    });
}

function calculateEndDateFormFunction() {

    $('.calculateDate').on('input', function () {
        calculateEndDate();

    });
}

function calculateEndDate() {

    var list = $('#nbrMonth').children('li');
    var idPeriod = $('#idPeriod option:selected').val();
    $('#endDate').val(null);

    list.each(function (indexPara, elem) {
        if ($(elem).attr('name') === idPeriod) {
            var cpt = $(elem).val();
            var arr = $('#startDate').val().split("/");
            var date = new Date(arr[2] + "-" + arr[1] + "-" + arr[0]);
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();
            date.setMonth(m + cpt);
            var endDate = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();
            // var endDateAsString = endDate.getDate() + '/' + (endDate.getMonth() + 1) + '/' + endDate.getFullYear();
            $('#endDate').val(endDate);
        }
    });
}
