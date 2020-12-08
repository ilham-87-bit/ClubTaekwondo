$(document).ready(function () {
    validCheckbox();
});

function validCheckbox() {
    var valid = false;

    $('#chek input[type=checkbox]').each(function () {
        if ($(this).prop('checked') === true) {
            valid = true;
            $('#chek input[type=checkbox]').removeAttr("required");
        }
    });
    if (valid === false) {
        $('#chek input[type=checkbox]').attr("required", true);
    }

}