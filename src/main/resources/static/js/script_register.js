$(document).ready(function() {
    $('#registerForm').on('submit', function(event) {
        event.preventDefault();

        // var firstName = $('#firstName').val();
        // var lastName = $('#lastName').val();
        var username = $('#username').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        var gender = $('input[name="gender"]:checked').val();

        if (password !== confirmPassword) {
            alert('密碼不匹配，請重新輸入。');
            return;
        }

        $.ajax({
            url: '/users/register',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: username,
                password: password
               
            }),
            success: function(response) {
                alert('註冊成功！');
                // 重新導向到登入頁面或其他操作
                window.location.href = '/login';
            },
            error: function(xhr) {
                alert('註冊失敗，請檢查您的輸入。');
            }
        });
    });
});
