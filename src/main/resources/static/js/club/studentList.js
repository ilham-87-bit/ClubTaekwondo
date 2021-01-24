$(document).ready(function () {
    selectStudentByCategory();
});

function selectStudentByCategory() {
    $('.selectCat').on('input', function () {
        for (var i = 0; i < $('.listStudent').length; i++) {
            if ($('#cat' + i).val() === $('#idCategory option:selected').val()) {
                $('#student' + i).removeClass('hidden');
            } else {
                $('#student' + i).addClass('hidden');
            }
        }
    });
}