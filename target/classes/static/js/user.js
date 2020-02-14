"use strict";
var REST_SERVER_NAME = "http://localhost:8080";

generateUserTable();
fillUserRoles();

function generateUserTable() {
    $.getJSON(REST_SERVER_NAME + "/restUserList", function (data) {
        var rows = '';
        data.forEach(function (item) {
            var row = '<tr style="text-align:center">';

            var userRoles = item.userRoles;
            var userRolesText = '';
            userRoles.forEach(function (item) {
                userRolesText += item.name + '<br/>';
            });

            row += '<td>' + item.id + '</td>';
            row += '<td>' + userRolesText + '</td>';
            row += '<td>' + item.name + '</td>';
            row += '<td>' + '********' + '</td>';
            row += '<td>' + '<button id="buttonUserEdit" data-user-id="' + item.id + '" class="btn btn-primary btn-sm" data-target="#userEdit" data-toggle="modal" type="button" onclick="editUser(this)">Edit</button>'
                + '<button id="buttonUserRemove"  data-user-id="' + item.id + '" class="btn btn-danger btn-sm" type="button" onclick="removeUser(this)">Remove</button>'
                + '</td>';

            row = row + '</tr>';
            rows += row;
        });
        $('#userList').append(rows);
    });
}

function editUser(button) {
    var id = $(button).data("user-id");
    fillUserData(id);
}

function addUser() {
    var isValid = true;

    var userLogin = $('#inputUserAddLogin');
    var userLoginHelp = $("#inputUserAddLoginHelp");
    if (userLogin.val().length < 4) {
        userLoginHelp.css('display', '');
        userLoginHelp.text('Enter login minimum 4 chars');
        isValid = false;
    } else {
        userLoginHelp.css('display', 'none');
    }

    var userPassword = $('#inputUserAddPassword');
    var userPasswordHelp = $("#inputUserAddPasswordHelp");
    if (userPassword.val().length < 4) {
        userPasswordHelp.css('display', '');
        userPasswordHelp.text('Enter password minimum 4 chars');
        isValid = false;
    } else {
        userPasswordHelp.css('display', 'none');
    }
    if (!isValid) {
        return isValid;
    }

    var url = REST_SERVER_NAME + "/admin";
    var user = {};
    user.id = 0;
    user.name = userLogin.val();
    user.password = userPassword.val();

    var userRoles = $('#inputUserAddRoles');
    var userRolesSelected = [];
    var userRolesArray = userRoles.val();

    for (var index = 0; index < userRolesArray.length; index++) {
        var userRole = {};
        userRole.id = userRolesArray[index];
        userRolesSelected.push(userRole);
    }

    user.userRoles = userRolesSelected;

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(user),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: false
    });

    document.location.reload();
}

function updateUser() {
    var userId = $('#inputModalId');
    var userLogin = $('#inputModalLogin');
    var userPassword = $('#inputModalPassword');

    var url = REST_SERVER_NAME + "/admin";
    var user = {};
    user.id = userId.val();
    user.name = userLogin.val();
    user.password = userPassword.val();

    var userRoles = $('#inputModalRoles');
    var userRolesSelected = [];
    var userRolesArray = userRoles.val();

    for (var index = 0; index < userRolesArray.length; index++) {
        var userRole = {};
        userRole.id = userRolesArray[index];
        userRolesSelected.push(userRole);
    }

    user.userRoles = userRolesSelected;

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(user),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        async: false
    });

    document.location.reload();
}

function removeUser(button) {
    var id = $(button).data("user-id");
    var url = REST_SERVER_NAME + "/admin/user/remove?id=" + id;

    $.get(url);
    $(button).closest('tr').remove();
}

function fillUserRoles() {
    var url = REST_SERVER_NAME + "/restUserRoles";
    $(document).ready(function () {
        $.getJSON(url, function (data) {
            var select = $('#inputModalRoles');
            data.forEach(function (item) {
                select.append('<option value=' + item.id + '>' + item.name + '</option>');
            });

            select = $('#inputUserAddRoles');
            data.forEach(function (item) {
                select.append('<option value=' + item.id + '>' + item.name + '</option>');
            });
        });
    });
}

function fillUserData(userId) {
    var url = REST_SERVER_NAME + "/restUser?id=" + userId;

    $.getJSON(url, function (data) {
        var modal = $("#userEdit");
        modal.find("#inputModalId").val(data.id);
        modal.find("#inputModalLogin").val(data.name);

        var userRoles = data.userRoles;
        userRoles.forEach(function (item) {
            modal.find("#inputModalRoles option[value='" + item.id + "'] ").prop("selected", true)
        });
    });
}