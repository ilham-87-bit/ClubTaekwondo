$(document).ready(function () {
    nextStep();
});

function nextStep() {

    $('#transactionForm').attr('action', "https://esqa.moneris.com/HPPDP/index.php");
    // document.Ajouter.action="https://esqa.moneris.com/HPPDP/index.php";
    //
    // document.Ajouter.method = "Post"
    $('#transactionForm').submit();
    // document.Ajouter.submit();

}