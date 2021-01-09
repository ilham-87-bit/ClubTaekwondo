var url = $('#js-template-global').attr('data-succes-url');

$(document).ready(function () {
    nextStepSucces();
});

function nextStepSucces() {

    // document.Ajouter.action = "http://127.0.0.1:8000/user/paymentSucces";
    document.Ajouter.action = url;

    document.Ajouter.method = "Post"

    document.Ajouter.submit();

}