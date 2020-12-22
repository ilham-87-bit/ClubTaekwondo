var form = $('#idAddSubscriptionForm');

$(document).ready(function () {
    formFunction();
    // inputButton();
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

// function formFunction() {
//     var cheked = false;
//     $('.rowSchool').each(function (index, element) {
//         if ($('#' + index).prop('checked') === true) {
//             cheked = true;
//         }
//     });
//     if(cheked === true){
//         $('#addSelect').removeClass('hidden');
//         $('#startDate').datepicker({
//             startDate: new Date(),
//             clearBtn: true,
//             format: "dd/mm/yyyy",
//             todayHighlight: true
//         });
//     }else {
//         $('#addSelect').addClass('hidden');
//     }
//
// }

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

    var list = $('#nbrMonth').children('li');
    var idPeriod = $(('#idPeriod' + index) + ' option:selected').val();
    $('#endDate' + index).val(null);

    list.each(function (indexPara, elem) {
        if ($(elem).attr('name') === idPeriod) {
            var cpt = $(elem).attr('value');
            var arr = $('#startDate' + index).val().split("/");
            var date = new Date(arr[2] + "-" + arr[1] + "-" + arr[0]);
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();
            date.setMonth(m + cpt);
            var endDate = new Date(date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear());
            var endDateAsString = endDate.getDate() + '/' + (endDate.getMonth() + 1) + '/' + endDate.getFullYear();
            $('#endDate' + index).val(endDateAsString);
        }
    });
}

// function inputButton(){
//     // $('#saveButton').on('click',function (e) {
//     //     e.preventDefault();
//     // var start =
//         $('.rowSchool').each(function (index, element) {
//             if ($('#' + index).prop('checked') === true) {
//                 $('#period').val($(('#idPeriod' + index)));
//                 $('#type').val($(('#idType' + index)));
//                 $('#startD').val(new Date($(('#startDate' + index)).val()));
//                 $('#endD').val(new Date($(('#endDate' + index))));
//             }
//         // });
//         //     form.submit();
//     });
// }

