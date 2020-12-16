var form = $('#idAddSubscriptionForm');

$(document).ready(function () {
    formFunction();
    // inputDate();
    inputMaskEvents();
    calculateEndDateFormFunction();
});

function formFunction() {
    $('.rowSchool').each(function (index, element) {
        if ($('#' + index).prop('checked') === true) {
            $('#addSelect' + index).removeClass('hidden');
            $('#startDate' + index).datepicker({
                startDate: new Date(),
                clearBtn: true,
                format: "dd/mm/yyyy",
                todayHighlight: true
            });
        } else {
            $('#addSelect' + index).addClass('hidden');
        }
    });

}

function calculateEndDateFormFunction() {

    $('.calculateDate').on('input', function () {
        $('.rowSchool').each(function (index, element) {
            if ($('#' + index).prop('checked') === true) {
                calculateEndDate(index);
            }
        });
    });
}

function calculateEndDate(index) {
    // var expdisp;
    //
    // $('#startDate' + index).on('changeDate', function () {
    //     expdisp = $('#startDate' + index).val();
    // });
    var list = $('#nbrMonth').children('li');
    var idPeriod = $(('#idPeriod' + index) + ' option:selected').val();
    $('#endDate' + index).val(null);

    list.each(function (indexPara, elem) {
        if ($(elem).attr('name') === idPeriod) {
            var cpt = $(elem).attr('value');
            // var arr = $('#startDate' + index).val().split("/");
            // var date = new Date(arr[2] + "-" + arr[1] + "-" + arr[0]);
            // date.setUTCMonth(date.getUTCMonth()+cpt +1);
            // var d = date.getDate();
            // var m = date.getUTCMonth();
            //     // .valueOf();
            // var y = date.getFullYear();
            // date.setUTCMonth(m + cpt);
            // Date.prototype.addMonth=function(n){
            //     this.setUTCMonth(this.getUTCMonth()+n)
            // }

            var end = $('#startDate' + index).val();
            end.setUTCMonth(end.getUTCMonth() + cpt + 1);
            // var end = new Date(date.getDate(),date.getUTCMonth()+1,date.getFullYear());
            $('#endDate' + index).val(new Date(end));
        }
    });
}

// // $('.rowSchool').each(function (index, element) {
// //     if ($('#' + index).prop('checked') === true) {
// // $('#startDate' + index).on('changeDate', function () {
// var expdisp = $('#startDate' + index).val();
// var cpt = $('#idPeriod' + index).;
// // if ($(('#idPeriod' + index) + ' option:selected').text().includes("semesttre") && $('#startDate' + index).val() != '') {
// var arr = expdisp.split("/");
// var date = new Date(arr[2] + "-" + arr[1] + "-" + arr[0]);
// var d = date.getDate();
// var m = date.getMonth();
// var y = date.getFullYear();
// var end = new Date(y, m + cpt, d);
// $('#endDate' + index).val(end);
// // } else {
// //     if ($(('#idPeriod' + index) + ' option:selected').text().includes("annuelle") && $('#startDate' + index).val() != '') {
// //         var arr = expdisp.split("/");
// //         var date = new Date(arr[2] + "-" + arr[1] + "-" + arr[0]);
// //         var d = date.getDate();
// //         var m = date.getMonth();
// //         var y = date.getFullYear();
// //         var end = new Date(y + 1, m, d);
// //         $('#endDate' + index).val(end);
// //     } else {
// //         $('#endDate' + index).val('');
// //     }
// // }


