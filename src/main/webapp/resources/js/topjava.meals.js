const userAjaxUrl = "user/meals/";

const mealFilterAjaxUrl = userAjaxUrl + "filter";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    filterAjaxUrl: mealFilterAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function filterTable() {
    let filterForm = $('#filterForm')
    $.get({
        url: ctx.filterAjaxUrl,
        data: filterForm.serialize()
    }).done(function (data) {
        redrawTable(data)
    });
}

$("#cleanFormButton").click(function () {
    $("#filterForm").find(":input").val("");
});

