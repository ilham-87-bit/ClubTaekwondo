var form = $('#idAddCoachForm');

$(document).ready(function () {
    validCheckbox();
    selectCategory();
    checkedListCategory();
});

function validCheckbox() {
    var valid = false;

    $('#chekCo input[type=checkbox]').each(function () {
        if ($(this).prop('checked') === true) {
            valid = true;
            $('#chekCo input[type=checkbox]').removeAttr("required");
        }
    });
    if (valid === false) {
        $('#chekCo input[type=checkbox]').attr("required", true);
    }

}

function selectCategory() {
    $('.selectSchool').on('input', function () {
        for (var j = 0; j < $('.rowCategory').length; j++) {
            $('#checkbox' + j).addClass("hidden");
            $('#labelCat' + j).addClass("hidden");
        }
        selectCategoryBySchool();
    });
}

function selectCategoryBySchool() {

    for (var i = 0; i < $('.categoryBySchool').length; i++) {
        for (var j = 0; j < $('.rowCategory').length; j++) {
            if ($('#catBySchool' + i).attr('name') === $('#idSchool option:selected').val() && $('#catBySchool' + i).attr('value') === $('#checkbox' + j).val()) {
                $('#checkbox' + j).removeClass("hidden");
                $('#labelCat' + j).removeClass("hidden");
            }
        }
    }
}

function checkedListCategory() {
    if ($('.listCat').length !== 0) {
        for (var i = 0; i < $('.listCat').length; i++) {
            for (var j = 0; j < $('.rowCategory').length; j++) {
                if ($('#cat' + i).attr('value') === $('#checkbox' + j).val()) {
                    $('#checkbox' + j).removeClass("hidden");
                    $('#labelCat' + j).removeClass("hidden");
                }
            }
        }
    }
}