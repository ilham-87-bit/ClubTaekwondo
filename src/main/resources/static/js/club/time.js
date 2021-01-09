$(document).ready(function () {
    // inputTime();
    selectCoachBySchool();
    selectCategoryByCoach();
});

// function inputTime() {
//     // $('#inputStartTime').timepicker();
//     // $('#inputStartTime').timepicker();
// }

function selectCoachBySchool() {
    $('.selectSchool').on('input', function () {
        for (var i = 0; i < $('.coachList').length; i++) {
            $('#co' + i).addClass('hidden');
        }
        $('#idCoach').get(0).value = " ";
        selectedCoach();
    });
}

function selectedCoach() {
    for (var i = 0; i < $('.coachList').length; i++) {
        if ($('#idSchool option:selected').val() === $('#co' + i).attr('name')) {
            $('#co' + i).removeClass('hidden');
        }
    }
}

function selectCategoryByCoach() {
    $('.selectCoach').on('input', function () {
        for (var y = 0; y < $('.coach').length; y++) {
            for (var i = 0; i < $('.categoryByCoach').length; i++) {
                for (var j = 0; j < $('.catList').length; j++) {
                    $('#cat' + j).addClass('hidden');
                }
            }
        }
        $('#idCategory').get(0).value = " ";
        selectedCategory();
    });
}

function selectedCategory() {
    for (var y = 0; y < $('.coach').length; y++) {
        for (var i = 0; i < $('.categoryByCoach').length; i++) {
            for (var j = 0; j < $('.catList').length; j++) {
                if ($('#idCoach option:selected').val() === $('#catCoach' + y).attr('name') && $('#catByCoach' + i).attr('value') === $('#cat' + j).val()) {
                    $('#cat' + j).removeClass('hidden');
                }
            }
        }
    }
}