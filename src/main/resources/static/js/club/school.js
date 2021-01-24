$(document).ready(function () {
    validCheckbox();
    initBrowse();
    selectCity();
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

function initBrowse() {
    function bs_input_file() {
        $(".input-file").before(
            function () {
                if (!$(this).prev().hasClass('input-ghost')) {
                    var element = $("<input type='file' class='input-ghost' accept='image/jpeg' style='visibility:hidden; height:0'>");
                    element.attr("name", $(this).attr("name"));
                    element.change(function () {
                        element.next(element).find('input').val(element.val());
                    });
                    $(this).find("button.btn-choose").click(function () {
                        element.click();
                    });

                    $(this).find('input').mousedown(function () {
                        $(this).parents('.input-file').prev().click();
                        return false;
                    });
                    return element;
                }
            }
        );
    }

    $(function () {
        bs_input_file();
    });
}