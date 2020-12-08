var confirmField = document.getElementById("inputConfirmPassword");
var passwordField = document.getElementById("inputPassword");

function checkPasswordMatch() {
    var status = document.getElementById("errorName");
    var submit = document.getElementById("saveButton");

    status.innerHTML = "";
    submit.removeAttribute("disabled");

    if (confirmField.value === "")
        return;

    if (passwordField.value === confirmField.value)
        return;

    status.innerHTML = "Les mots de passe ne correspondent pas";
    submit.setAttribute("disabled", "disabled");
}

passwordField.addEventListener("change", function (event) {
    checkPasswordMatch();
});
confirmField.addEventListener("change", function (event) {
    checkPasswordMatch();
});

