<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-900">Edit User</h1>
        <div class="flex space-x-2">
            <a href="/users/view/${user.id}" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
                <i class="fas fa-eye mr-2"></i>View User
            </a>
            <a href="/users" class="bg-gray-600 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
                <i class="fas fa-arrow-left mr-2"></i>Back to List
            </a>
        </div>
    </div>

    <!-- Flash Messages -->
    <c:if test="${not empty successMessage}">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4" role="alert">
            <span class="block sm:inline">${successMessage}</span>
        </div>
    </c:if>
    
    <c:if test="${not empty errorMessage}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4" role="alert">
            <span class="block sm:inline">${errorMessage}</span>
        </div>
    </c:if>

    <!-- Edit User Form -->
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <div class="px-6 py-4 bg-gray-50 border-b border-gray-200">
            <h2 class="text-xl font-semibold text-gray-900">Edit User Information</h2>
            <p class="text-sm text-gray-600 mt-1">User ID: ${user.id}</p>
        </div>
        <form method="post" action="/users/update/${user.id}" class="px-6 py-4" data-validate="user" id="userForm">
            <div class="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <!-- Username -->
                <div class="form-field">
                    <label for="username" class="block text-sm font-medium text-gray-700 mb-2">
                        Username <span class="text-red-500">*</span>
                    </label>
                    <input type="text" 
                           id="username" 
                           name="username" 
                           required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                           placeholder="Enter username"
                           value="${not empty param.username ? param.username : user.username}">
                </div>

                <!-- Email -->
                <div class="form-field">
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                        Email <span class="text-red-500">*</span>
                    </label>
                    <input type="email" 
                           id="email" 
                           name="email" 
                           required
                           class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                           placeholder="Enter email address"
                           value="${not empty param.email ? param.email : user.email}">
                </div>

                <!-- Password -->
                <div class="form-field">
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                        Password
                    </label>
                    <input type="password" 
                           id="password" 
                           name="password" 
                           class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                           placeholder="Enter new password (optional)"
                           value="${param.password}">
                    <p class="mt-1 text-sm text-gray-500">Leave empty to keep current password</p>
                </div>

                <!-- Status -->
                <div class="form-field">
                    <label for="status" class="block text-sm font-medium text-gray-700 mb-2">
                        Status
                    </label>
                    <select id="status" 
                            name="status" 
                            class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                        <option value="ACTIVE" ${(not empty param.status ? param.status : user.status) == 'ACTIVE' ? 'selected' : ''}>Active</option>
                        <option value="INACTIVE" ${(not empty param.status ? param.status : user.status) == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
            </div>

            <!-- Additional Information -->
            <div class="mt-6 p-4 bg-gray-50 rounded-lg">
                <h3 class="text-sm font-medium text-gray-700 mb-2">Additional Information</h3>
                <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 text-sm text-gray-600">
                    <div>
                        <span class="font-medium">Created:</span> 
                        <fmt:formatDate value="${user.createdAtAsDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                    </div>
                    <div>
                        <span class="font-medium">Last Updated:</span> 
                        <fmt:formatDate value="${user.updatedAtAsDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                    </div>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="mt-6 flex justify-end space-x-3">
                <a href="/users/view/${user.id}" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded-lg transition duration-200">
                    Cancel
                </a>
                <button type="button" id="submit-btn-edit" data-user-id="${user.id}" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
                    <i class="fas fa-save mr-2"></i>Update User
                </button>
            </div>
        </form>
    </div>
</div>