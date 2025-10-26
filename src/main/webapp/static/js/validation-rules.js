$.validator.addMethod("alphanumeric", function(value, element) {
    return this.optional(element) || /^[a-zA-Z0-9_]+$/.test(value);
}, "Please enter only letters, numbers, and underscores");

$.validator.addMethod("strongPassword", function(value, element) {
    return this.optional(element) || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/.test(value);
}, "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");

$.validator.addMethod("uniqueEmail", function(value, element) {
    let isUnique = true;
    
    // Check if email is unique (AJAX call)
    $.ajax({
        url: '/users/check-email',
        type: 'POST',
        data: { email: value },
        async: false,
        success: function(response) {
            isUnique = response.unique;
        }
    });
    
    return isUnique;
}, "Email already exists");

$.validator.addMethod("uniqueUsername", function(value, element) {
    let isUnique = true;
    
    // Check if username is unique (AJAX call)
    $.ajax({
        url: '/users/check-username',
        type: 'POST',
        data: { username: value },
        async: false,
        success: function(response) {
            isUnique = response.unique;
        }
    });
    
    return isUnique;
}, "Username already exists");

$.validator.addMethod("vietnameseText", function(value, element) {
    return this.optional(element) || /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂÂÊÔưăâêô\s]+$/.test(value);
}, "Please enter only Vietnamese text");

$.validator.addMethod("phoneNumber", function(value, element) {
    return this.optional(element) || /^(\+84|84|0)[1-9][0-9]{8,9}$/.test(value);
}, "Please enter a valid Vietnamese phone number");

$.validator.addMethod("dateFormat", function(value, element) {
    return this.optional(element) || /^\d{2}\/\d{2}\/\d{4}$/.test(value);
}, "Please enter date in DD/MM/YYYY format");

// Custom validation messages for Vietnamese
$.validator.setDefaults({
    messages: {
        required: "Trường này là bắt buộc",
        email: "Vui lòng nhập địa chỉ email hợp lệ",
        minlength: "Vui lòng nhập ít nhất {0} ký tự",
        maxlength: "Vui lòng nhập không quá {0} ký tự",
        equalTo: "Vui lòng nhập lại giá trị giống nhau",
        digits: "Vui lòng chỉ nhập số",
        number: "Vui lòng nhập số hợp lệ",
        url: "Vui lòng nhập URL hợp lệ",
        date: "Vui lòng nhập ngày hợp lệ",
        dateISO: "Vui lòng nhập ngày theo định dạng ISO",
        creditcard: "Vui lòng nhập số thẻ tín dụng hợp lệ",
        accept: "Vui lòng chọn file có định dạng hợp lệ",
        max: "Vui lòng nhập giá trị không lớn hơn {0}",
        min: "Vui lòng nhập giá trị không nhỏ hơn {0}",
        range: "Vui lòng nhập giá trị trong khoảng {0} đến {1}",
        rangelength: "Vui lòng nhập từ {0} đến {1} ký tự",
        rangeWords: "Vui lòng nhập từ {0} đến {1} từ",
        remote: "Giá trị này đã tồn tại",
        step: "Vui lòng nhập bội số của {0}",
        extension: "Vui lòng chọn file có phần mở rộng hợp lệ"
    }
});

// Export validation functions
window.ValidationRules = {
    // User form validation rules
    userFormRules: {
        username: {
            required: true,
            minlength: 3,
            maxlength: 50,
            alphanumeric: true,
            uniqueUsername: true
        },
        email: {
            required: true,
            email: true,
            maxlength: 100,
            uniqueEmail: true
        },
        password: {
            required: true,
            minlength: 6,
            maxlength: 100,
            strongPassword: true
        },
        confirmPassword: {
            required: true,
            equalTo: "#password"
        },
        status: {
            required: true
        },
        firstName: {
            required: true,
            minlength: 2,
            maxlength: 50,
            vietnameseText: true
        },
        lastName: {
            required: true,
            minlength: 2,
            maxlength: 50,
            vietnameseText: true
        },
        phone: {
            required: false,
            phoneNumber: true
        },
        birthDate: {
            required: false,
            dateFormat: true
        }
    },
    
    // User form validation messages
    userFormMessages: {
        username: {
            required: "Tên đăng nhập là bắt buộc",
            minlength: "Tên đăng nhập phải có ít nhất 3 ký tự",
            maxlength: "Tên đăng nhập không được vượt quá 50 ký tự",
            alphanumeric: "Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới",
            uniqueUsername: "Tên đăng nhập đã tồn tại"
        },
        email: {
            required: "Email là bắt buộc",
            email: "Vui lòng nhập địa chỉ email hợp lệ",
            maxlength: "Email không được vượt quá 100 ký tự",
            uniqueEmail: "Email đã tồn tại"
        },
        password: {
            required: "Mật khẩu là bắt buộc",
            minlength: "Mật khẩu phải có ít nhất 6 ký tự",
            maxlength: "Mật khẩu không được vượt quá 100 ký tự",
            strongPassword: "Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường, một số và một ký tự đặc biệt"
        },
        confirmPassword: {
            required: "Xác nhận mật khẩu là bắt buộc",
            equalTo: "Mật khẩu xác nhận không khớp"
        },
        status: {
            required: "Trạng thái là bắt buộc"
        },
        firstName: {
            required: "Tên là bắt buộc",
            minlength: "Tên phải có ít nhất 2 ký tự",
            maxlength: "Tên không được vượt quá 50 ký tự",
            vietnameseText: "Tên chỉ được chứa chữ cái tiếng Việt"
        },
        lastName: {
            required: "Họ là bắt buộc",
            minlength: "Họ phải có ít nhất 2 ký tự",
            maxlength: "Họ không được vượt quá 50 ký tự",
            vietnameseText: "Họ chỉ được chứa chữ cái tiếng Việt"
        },
        phone: {
            phoneNumber: "Vui lòng nhập số điện thoại Việt Nam hợp lệ"
        },
        birthDate: {
            dateFormat: "Vui lòng nhập ngày theo định dạng DD/MM/YYYY"
        }
    },
    
    // Initialize validation for a form
    initValidation: function(formSelector, rules, messages) {
        return $(formSelector).validate({
            rules: rules,
            messages: messages,
            errorClass: "error",
            validClass: "valid",
            errorElement: "span",
            errorPlacement: function(error, element) {
                error.addClass("text-red-500 text-sm mt-1 block");
                element.after(error);
            },
            highlight: function(element, errorClass, validClass) {
                $(element).addClass("border-red-500 ring-red-500").removeClass("border-gray-300 ring-blue-500");
            },
            unhighlight: function(element, errorClass, validClass) {
                $(element).removeClass("border-red-500 ring-red-500").addClass("border-gray-300");
            },
            submitHandler: function(form) {
                // Custom submit handler can be added here
                return true;
            }
        });
    },
    
    // Validate single field
    validateField: function(fieldSelector, rules, messages) {
        const field = $(fieldSelector);
        const validator = field.closest('form').data('validator');
        
        if (validator) {
            return validator.element(fieldSelector);
        }
        
        return true;
    },
    
    // Clear validation errors
    clearErrors: function(formSelector) {
        $(formSelector).find('.error').remove();
        $(formSelector).find('input, select, textarea').removeClass('border-red-500 ring-red-500').addClass('border-gray-300');
    },
    
    // Show validation summary
    showValidationSummary: function(formSelector, errors) {
        const summary = $(`
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4" role="alert">
                <h4 class="font-bold">Please correct the following errors:</h4>
                <ul class="list-disc list-inside mt-2">
                    ${errors.map(error => `<li>${error}</li>`).join('')}
                </ul>
            </div>
        `);
        
        $(formSelector).prepend(summary);
    }
};
