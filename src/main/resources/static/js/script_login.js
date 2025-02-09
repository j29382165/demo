//登入
$(document).ready(function() {
    $('#loginForm').on('submit', function(event) {
        event.preventDefault();

        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            url: '/users/login',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: username, password: password }),
            success: function(response) {
                alert('登入成功!');
                // Redirect or perform other actions
            },
            error: function(xhr) {
                alert('登入失敗，帳號密碼有誤');
            }
        });
    });
});
