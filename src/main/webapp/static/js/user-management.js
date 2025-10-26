$(function () {
    function collectUserFormData() {
        const formData = new FormData();
        formData.append('username', $('#username').val());
        formData.append('email', $('#email').val());
        formData.append('password', $('#password').val());
        formData.append('status', $('#status').val());
        return formData;
    }

    // Auto-generate username from display name
    $('#display_name').on('input', function () {
        const value = $(this).val();
        const kebab = value
            .match(/[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+/g)
            ?.map(x => x.toLowerCase()).join('-') ?? '';
        $('#username').val(kebab);
    });

    // Create user form submission
    $('#submit-btn-create').on('click', function (e) {
        e.preventDefault();
        if (!userManagement.validateUserForm()) return;

        const formData = collectUserFormData();
        userManagement.createUser(formData);
    });

    // Edit user form submission
    $('#submit-btn-edit').on('click', function (e) {
        e.preventDefault();
        if (!userManagement.validateUserForm()) return;

        const formData = collectUserFormData();
        const userId = $(this).data('userId');
        userManagement.updateUser(formData, userId);
    });

    // Delete user button
    $('button[data-action="delete"]').on('click', function (e) {
        e.preventDefault();
        const userId = $(this).data('user-id');
        const userName = $(this).data('user-name');
        
        alert.showSwalConfirmDelete(
            'Xác nhận xóa người dùng',
            `Bạn có chắc chắn muốn xóa người dùng "${userName}"? Hành động này không thể hoàn tác.`,
            'Xóa người dùng'
        ).then(function(result) {
            if (result.isConfirmed) {
                userManagement.deleteUser(userId);
            }
        });
    });

    // Activate/Deactivate user buttons
    $('button[data-action="activate"], button[data-action="deactivate"]').on('click', function (e) {
        e.preventDefault();
        const userId = $(this).data('user-id');
        const action = $(this).data('action');
        const userName = $(this).data('user-name');
        
        const actionText = action === 'activate' ? 'kích hoạt' : 'vô hiệu hóa';
        const actionColor = action === 'activate' ? '#10b981' : '#f59e0b';
        
        alert.showSwalConfirmAction(
            `Xác nhận ${actionText} người dùng`,
            `Bạn có chắc chắn muốn ${actionText} người dùng "${userName}"?`,
            actionText.charAt(0).toUpperCase() + actionText.slice(1),
            actionColor
        ).then(function(result) {
            if (result.isConfirmed) {
                if (action === 'activate') {
                    userManagement.activateUser(userId);
                } else {
                    userManagement.deactivateUser(userId);
                }
            }
        });
    });

    // Mobile menu toggle
    $('#mobile-menu-button').on('click', function() {
        $('#mobile-menu').toggleClass('hidden');
    });

    // Close mobile menu when clicking outside
    $(document).on('click', function(e) {
        if (!$(e.target).closest('#mobile-menu-button, #mobile-menu').length) {
            $('#mobile-menu').addClass('hidden');
        }
    });

    // Password visibility toggle
    $('.password-toggle').on('click', function() {
        const passwordField = $(this).siblings('input[type="password"], input[type="text"]');
        const icon = $(this).find('i');
        
        if (passwordField.attr('type') === 'password') {
            passwordField.attr('type', 'text');
            icon.removeClass('fa-eye').addClass('fa-eye-slash');
        } else {
            passwordField.attr('type', 'password');
            icon.removeClass('fa-eye-slash').addClass('fa-eye');
        }
    });

    // Search functionality
    $('#user-search').on('keyup', function() {
        const searchTerm = $(this).val().toLowerCase();
        $('table tbody tr').each(function() {
            const rowText = $(this).text().toLowerCase();
            if (rowText.includes(searchTerm)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

    // Table sort functionality
    $('th[data-sort]').on('click', function() {
        const column = $(this).data('sort');
        const table = $(this).closest('table');
        const tbody = table.find('tbody');
        const rows = tbody.find('tr').toArray();
        
        rows.sort(function(a, b) {
            const aVal = $(a).find(`td[data-${column}]`).text();
            const bVal = $(b).find(`td[data-${column}]`).text();
            
            if (column === 'id' || column === 'created') {
                return parseInt(aVal) - parseInt(bVal);
            } else {
                return aVal.localeCompare(bVal);
            }
        });
        
        tbody.empty().append(rows);
    });

    // Flash messages auto-hide
    if ($('.bg-green-100, .bg-red-100, .bg-yellow-100').length > 0) {
        setTimeout(function() {
            $('.bg-green-100, .bg-red-100, .bg-yellow-100').fadeOut(300);
        }, 5000);
    }

    // Form field focus effects
    $('input, select, textarea').on('focus', function() {
        $(this).addClass('ring-2 ring-blue-500');
    }).on('blur', function() {
        $(this).removeClass('ring-2 ring-blue-500');
    });

    // Table row hover effects
    $('table tbody tr').hover(
        function() {
            $(this).addClass('bg-gray-50');
        },
        function() {
            $(this).removeClass('bg-gray-50');
        }
    );

    // Responsive table handling
    if ($(window).width() < 768) {
        $('table').addClass('table-responsive');
    }
    
    $(window).on('resize', function() {
        if ($(window).width() < 768) {
            $('table').addClass('table-responsive');
        } else {
            $('table').removeClass('table-responsive');
        }
    });
});

(function (exports, global) {
    exports.validateUserForm = function () {
        const isEditForm = $('#submit-btn-edit').length > 0;
        
        const passwordRules = isEditForm ? {
            minlength: 6,
            maxlength: 100
        } : {
            required: true,
            minlength: 6,
            maxlength: 100
        };
        
        const passwordMessages = isEditForm ? {
            minlength: "Mật khẩu phải có ít nhất 6 ký tự",
            maxlength: "Mật khẩu không được vượt quá 100 ký tự"
        } : {
            required: "Mật khẩu không được để trống",
            minlength: "Mật khẩu phải có ít nhất 6 ký tự",
            maxlength: "Mật khẩu không được vượt quá 100 ký tự"
        };
        
        $("#userForm").validate({
            errorPlacement: function (error, element) {
                $(element).next('.text-red-600').remove();
                error.addClass('text-red-600 text-sm mt-1 block');
                error.insertAfter(element);
            },
            ignore: ':hidden:not(.validate)',
            errorElement: "span",
            errorClass: "input-error",
            validClass: "input-valid",
            rules: {
                username: {
                    required: true,
                    minlength: 3,
                    maxlength: 50,
                    alphanumeric: true
                },
                email: {
                    required: true,
                    email: true,
                    maxlength: 100
                },
                password: passwordRules,
                status: {
                    required: true
                }
            },
            messages: {
                username: {
                    required: "Tên đăng nhập không được để trống",
                    minlength: "Tên đăng nhập phải có ít nhất 3 ký tự",
                    maxlength: "Tên đăng nhập không được vượt quá 50 ký tự",
                    alphanumeric: "Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới"
                },
                email: {
                    required: "Email không được để trống",
                    email: "Vui lòng nhập địa chỉ email hợp lệ",
                    maxlength: "Email không được vượt quá 100 ký tự"
                },
                password: passwordMessages,
                status: {
                    required: "Trạng thái không được để trống"
                }
            },
            invalidHandler: function (form, validator) {
                let errors = validator.numberOfInvalids();
                if (errors) {
                    let firstInvalidElement = $(validator.errorList[0].element);
                    $('html,body').scrollTop(firstInvalidElement.offset().top);
                    firstInvalidElement.focus();
                }
            },
            submitHandler: function (form) {
                return false;
            },
            highlight: function (element, errorClass, validClass) {
                $(element).addClass('border-red-500');
            },
            unhighlight: function (element, errorClass, validClass) {
                $(element).removeClass('border-red-500');
            },
            success: function (label, element) {
                $(element).next('.text-red-600').remove();
            }
        });

        return $("#userForm").valid();
    };

    exports.createUser = function (data) {
        alert.showSwalLoading('Đang tạo người dùng...');
        
        $.ajax({
            url: '/ajax/users/create',
            type: 'POST',
            data: data,
            processData: false,
            contentType: false,
            success: function (response) {
                alert.hideSwalLoading();
                if (response.success) {
                    alert.showSwalSuccess('Thêm người dùng thành công!').then(function() {
                        window.location.href = '/users';
                    });
                } else {
                    alert.showSwalError(response.message || 'Thêm người dùng không thành công, vui lòng thử lại!');
                }
            },
            error: function (xhr, status, error) {
                alert.hideSwalLoading();
                let errorMessage = 'Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập vào.';
                if (xhr && xhr.responseJSON) {
                    const responseData = xhr.responseJSON;
                    if (responseData.message) {
                        errorMessage = responseData.message;
                    }
                }
                alert.showSwalError(errorMessage);
            }
        });
    };

    exports.updateUser = function (data, id) {
        alert.showSwalLoading('Đang cập nhật người dùng...');
        
        $.ajax({
            url: `/ajax/users/update/${id}`,
            type: 'POST',
            data: data,
            processData: false,
            contentType: false,
            success: function (response) {
                alert.hideSwalLoading();
                if (response.success) {
                    alert.showSwalSuccess('Cập nhật người dùng thành công!').then(function() {
                        window.location.href = `/users/view/${id}`;
                    });
                } else {
                    alert.showSwalError(response.message || 'Cập nhật người dùng không thành công, vui lòng thử lại!');
                }
            },
            error: function (xhr, status, error) {
                alert.hideSwalLoading();
                let errorMessage = 'Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập vào.';
                if (xhr && xhr.responseJSON) {
                    const responseData = xhr.responseJSON;
                    if (responseData.message) {
                        errorMessage = responseData.message;
                    }
                }
                alert.showSwalError(errorMessage);
            }
        });
    };

    exports.deleteUser = function (id) {
        alert.showSwalLoading('Đang xóa người dùng...');
        
        $.ajax({
            url: `/ajax/users/delete/${id}`,
            type: 'POST',
            success: function (response) {
                alert.hideSwalLoading();
                if (response.success) {
                    alert.showSwalSuccess('Xóa người dùng thành công!').then(function() {
                        window.location.href = '/users';
                    });
                } else {
                    alert.showSwalError(response.message || 'Xóa người dùng không thành công, vui lòng thử lại!');
                }
            },
            error: function (xhr, status, error) {
                alert.hideSwalLoading();
                let errorMessage = 'Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập vào.';
                if (xhr && xhr.responseJSON) {
                    const responseData = xhr.responseJSON;
                    if (responseData.message) {
                        errorMessage = responseData.message;
                    }
                }
                alert.showSwalError(errorMessage);
            }
        });
    };

    exports.activateUser = function (id) {
        alert.showSwalLoading('Đang kích hoạt người dùng...');
        
        $.ajax({
            url: `/ajax/users/activate/${id}`,
            type: 'POST',
            success: function (response) {
                alert.hideSwalLoading();
                if (response.success) {
                    alert.showSwalSuccess('Kích hoạt người dùng thành công!').then(function() {
                        window.location.reload();
                    });
                } else {
                    alert.showSwalError(response.message || 'Kích hoạt người dùng không thành công, vui lòng thử lại!');
                }
            },
            error: function (xhr, status, error) {
                alert.hideSwalLoading();
                let errorMessage = 'Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập vào.';
                if (xhr && xhr.responseJSON) {
                    const responseData = xhr.responseJSON;
                    if (responseData.message) {
                        errorMessage = responseData.message;
                    }
                }
                alert.showSwalError(errorMessage);
            }
        });
    };

    exports.deactivateUser = function (id) {
        alert.showSwalLoading('Đang vô hiệu hóa người dùng...');
        
        $.ajax({
            url: `/ajax/users/deactivate/${id}`,
            type: 'POST',
            success: function (response) {
                alert.hideSwalLoading();
                if (response.success) {
                    alert.showSwalSuccess('Vô hiệu hóa người dùng thành công!').then(function() {
                        window.location.reload();
                    });
                } else {
                    alert.showSwalError(response.message || 'Vô hiệu hóa người dùng không thành công, vui lòng thử lại!');
                }
            },
            error: function (xhr, status, error) {
                alert.hideSwalLoading();
                let errorMessage = 'Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập vào.';
                if (xhr && xhr.responseJSON) {
                    const responseData = xhr.responseJSON;
                    if (responseData.message) {
                        errorMessage = responseData.message;
                    }
                }
                alert.showSwalError(errorMessage);
            }
        });
    };

    exports.showLoading = function () {
        $('body').append('<div id="loading-overlay" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"><div class="bg-white rounded-lg p-6 flex items-center space-x-3"><div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div><span class="text-gray-700">Đang tải...</span></div></div>');
    };

    exports.hideLoading = function () {
        $('#loading-overlay').remove();
    };

    exports.showMessage = function (message, type = 'info') {
        const alertClass = type === 'success' ? 'bg-green-100 border-green-400 text-green-700' :
                          type === 'error' ? 'bg-red-100 border-red-400 text-red-700' :
                          type === 'warning' ? 'bg-yellow-100 border-yellow-400 text-yellow-700' :
                          'bg-blue-100 border-blue-400 text-blue-700';
        
        const messageHtml = `
            <div id="alert-message" class="fixed top-4 right-4 ${alertClass} border px-4 py-3 rounded shadow-lg z-50 max-w-md">
                <div class="flex items-center justify-between">
                    <span>${message}</span>
                    <button onclick="userManagement.hideMessage()" class="ml-4 text-lg font-bold">&times;</button>
                </div>
            </div>
        `;
        
        $('body').append(messageHtml);
        
        // Auto hide after 5 seconds
        setTimeout(function() {
            userManagement.hideMessage();
        }, 5000);
    };

    exports.hideMessage = function () {
        $('#alert-message').fadeOut(300, function() {
            $(this).remove();
        });
    };

})(window.userManagement = window.userManagement || {}, window);
