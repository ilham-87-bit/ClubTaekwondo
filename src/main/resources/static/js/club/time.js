$(document).ready(function () {
    selectCoachBySchool();
    selectCategoryByCoach();
    validCheckbox();
    validChekCoach();
});

function selectCoachBySchool() {
    $('.selectSchool').on('input', function () {
        for (var i = 0; i < $('.rowCoach').length; i++) {
            $('#check' + i).addClass("hidden");
            $('#check' + i).removeProp('checked');
            $('#labelCo' + i).addClass("hidden");
        }
        for (var j = 0; j < $('.rowCategory').length; j++) {
            $('#checkCat' + j).addClass("hidden");
            $('#labelCat' + j).addClass("hidden");
        }
        selectedCoach();
    });
}

function selectedCoach() {
    for (var i = 0; i < $('.coachBySchool').length; i++) {
        if ($('#school' + i).attr('name') === $('#idSchool option:selected').val()) {
            for (var j = 0; j <= $('.rowCoach').length; j++) {
                if ($.inArray($('#check' + j).val(), $('#school' + i).val()) > -1) {
                    $('#check' + j).removeClass("hidden");
                    $('#labelCo' + j).removeClass("hidden");
                }
            }
        }
    }
}

function selectCategoryByCoach() {
    $('.checkedCoach').on('input', function () {
        for (var j = 0; j < $('.rowCategory').length; j++) {
            $('#checkCat' + j).addClass("hidden");
            $('#labelCat' + j).addClass("hidden");
        }
        selectCategories();
    });
}

function selectCategories() {
    for (var i = 0; i < $('.categoryByCoach').length; i++) {
        for (var j = 0; j <= $('.rowCoach').length; j++) {
            for (var y = 0; y <= $('.rowCategory').length; y++) {
                if ($('#coach' + i).attr('name') === $(('#check' + j) + ':checked').val() && $.inArray($('#checkCat' + y).val(), $('#coach' + i).val()) > -1) {
                    $('#checkCat' + y).removeClass("hidden");
                    $('#labelCat' + y).removeClass("hidden");
                }
            }
        }
    }
}

function validChekCoach() {
    var validCoach = false;

    $('#chekCoach input[type=checkbox]').each(function () {
        if ($(this).prop('checked') === true) {
            validCoach = true;
            $('#chekCoach input[type=checkbox]').removeAttr("required");
        }
    });
    if (!validCoach) {
        $('#chekCoach input[type=checkbox]').attr("required", true);
    }
}

// function validCheckbox() {
//     var valid = false;
//
//     $('#chek input[type=checkbox]').each(function () {
//         if ($(this).prop('checked') === true) {
//             valid = true;
//             $('#chek input[type=checkbox]').removeAttr("required");
//         }
//     });
//     if (valid === false) {
//         $('#chek input[type=checkbox]').attr("required", true);
//     }
//
// }