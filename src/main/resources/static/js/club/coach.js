$(document).ready(function () {
    validCheckbox();
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