const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    );

    $(".checkbox-event").change(function () {
        let currentRow = $(this).closest('tr');
        let userId = currentRow.attr("id");
        let checked = this.checked;
        let urlEnable = ctx.ajaxUrl + userId + "/enable?enabled=" + checked;
        $.ajax({
            url: urlEnable,
            type: "PATCH"
        }).done(function (data) {
            data ? currentRow.removeClass("text-muted") : currentRow.addClass("text-muted");
        }).fail(function (jqXHR, textStatus) {
            currentRow.find("input[type=checkbox]").prop("checked", !checked);
        });
    });
});