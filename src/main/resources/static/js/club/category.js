var deleteCategoryUrl = $('#js-template-global').attr('data-delete-category-url');
$(document).ready(function () {
    initButtons();
    initModalButtons();
});
function initButtons() {
    $('#deleteCategory').on('click', function (e) {
        e.preventDefault();
        $('#checkboxAll').attr('data-substitute', $(this).attr('id'));
        if (validateAtLeastOneChecked($('#categoryForm'))) {
            $('#deleteCategoryConfirmationModal').modal('show');
        }
    });
}

function initModalButtons() {
    $('#yesButton').on('click', function (e) {
        e.preventDefault();
        var form = $($('#deleteCategory').attr('data-target'));
        if (validateAtLeastOneChecked(form)) {
            var categoriesList = $('input[name*=category]:checked');
            $('#categoryForm').attr('action', deleteCategoryUrl).submit();

        }
    });

    $('#noButton').on('click', function () {
        $('#deleteCategoryConfirmationModal').modal('hide');
    });
}