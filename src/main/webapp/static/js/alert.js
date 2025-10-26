;(function (exports, global) {
    exports.showSwalSuccess = function (mes) {
        return Swal.fire({
            icon: "success",
            title: mes,
            showConfirmButton: false,
            showCancelButton: true,
            cancelButtonText: "Đóng",
        })
    }

    exports.showSwalWarning = function (mes) {
        return Swal.fire({
            icon: "warning",
            title: mes,
            confirmButtonText: "Xác nhận",
            confirmButtonColor: "#d33",
            showCancelButton: true,
            cancelButtonText: "Hủy",
        });
    }

    exports.showSwalWarningInfo = function (mes) {
        return Swal.fire({
            icon: "warning",
            title: mes,
            showConfirmButton: false,
            showCancelButton: true,
            cancelButtonText: "Đóng",
        });
    }

    exports.showSwalError = function (mes) {
        return Swal.fire({
            icon: "error",
            title: mes,
            showConfirmButton: false,
            showCancelButton: true,
            cancelButtonText: "Đóng",
        })
    }

    exports.showSwalInfo = function (mes) {
        return Swal.fire({
            icon: "info",
            title: mes,
            showConfirmButton: false,
            showCancelButton: true,
            cancelButtonText: "Đóng",
        })
    }

    exports.showSwalAlertInputText = function (title, label) {
        return Swal.fire({
            title: title,
            input: 'text',
            inputLabel: label,
            showCancelButton: true,
            confirmButtonText: "Xác nhận",
            cancelButtonText: "Hủy",
        })
    }

    exports.showSwalAlertInputEmail = function (title, label, placeholder) {
        return Swal.fire({
            title: title,
            input: 'email',
            inputLabel: label,
            showCancelButton: true,
            inputPlaceholder: placeholder,
            confirmButtonText: "Xác nhận",
            cancelButtonText: "Hủy",
        })
    }

    exports.showSwalAlertInputPassword = async function (title, label, placeholder) {
        const { value: password } = await Swal.fire({
            title: title,
            input: 'password',
            inputLabel: label,
            inputPlaceholder: placeholder,
            inputAttributes: {
                autocapitalize: 'off',
                autocorrect: 'off'
            },
            confirmButtonText: "Xác nhận",
            cancelButtonText: "Hủy",
        })

        if (password) {
            Swal.fire(`Mật khẩu đã nhập: ${password}`)
        }
    }

    // Custom confirmation dialog for delete actions
    exports.showSwalConfirmDelete = function (title, text, confirmText = "Xóa") {
        return Swal.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: confirmText,
            cancelButtonText: 'Hủy'
        });
    }

    // Custom confirmation dialog for activate/deactivate actions
    exports.showSwalConfirmAction = function (title, text, actionText, actionColor = '#3085d6') {
        return Swal.fire({
            title: title,
            text: text,
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: actionColor,
            cancelButtonColor: '#6c757d',
            confirmButtonText: actionText,
            cancelButtonText: 'Hủy'
        });
    }

    // Loading alert
    exports.showSwalLoading = function (title = "Đang xử lý...") {
        Swal.fire({
            title: title,
            allowOutsideClick: false,
            allowEscapeKey: false,
            showConfirmButton: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
    }

    // Close loading alert
    exports.hideSwalLoading = function () {
        Swal.close();
    }

    // Toast notifications
    exports.showSwalToast = function (message, type = 'success') {
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        });

        return Toast.fire({
            icon: type,
            title: message
        });
    }

    // Form validation with SweetAlert
    exports.showSwalValidationError = function (errors) {
        let errorMessage = '';
        if (Array.isArray(errors)) {
            errorMessage = errors.join('<br>');
        } else if (typeof errors === 'object') {
            errorMessage = Object.values(errors).flat().join('<br>');
        } else {
            errorMessage = errors;
        }

        return Swal.fire({
            icon: 'error',
            title: 'Lỗi xác thực',
            html: errorMessage,
            confirmButtonText: 'Đóng'
        });
    }

})(window.alert = window.alert || {}, window);
