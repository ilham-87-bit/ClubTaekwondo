var form = $('#idAddStudentForm');
$(document).ready(function () {
    inputDate();
    inputButton();
});
// function buttonSubmit(){
//     checkModNationalNumber();
//     inputButton();
// }
function inputDate() {
    $('#birthDay').datepicker({
        endDate: new Date(),
        clearBtn: true,
        format: "dd/mm/yyyy",
        todayHighlight: true
    });
}

function inputButton() {
    $("[data-inputmask-alias]").inputmask();
}

function checkModNationalNumber() {
    var value = $('#nationalRegistry').val();
    if (value.length == 15) {
        value = value.replace(/\D/g, '');
    }
    var currentYear = new Date().getFullYear().toString();
    var modRes = Number(value.substr(value.length - 2));
    var res = Number(value.substr(0, value.length - 2));
    if (Number(value.substr(0, 2)) <= Number(currentYear.substr(2, 3))) {
        res = Number("2" + value.substr(0, value.length - 2));
    }
    if ((97 - res % 97) != modRes) {
        $("#nationalRegistry").next(".error").show().text("Le numéro de registre national n'est pas valide, veuillez réessayer.");
        // $('#nationalRegistry').setAttribute('data-inputmask-alias') ;
        $("[data-inputmask-alias]").inputmask();
        return false;
    }
    // else{
    //      $("#nationalRegistry").next(".error").hide();
    //  }
}