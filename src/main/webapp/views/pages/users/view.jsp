<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="container mx-auto px-4 py-8">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-900">User Details</h1>
        <div class="flex space-x-2">
            <a href="/users/edit/${user.id}" class="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200">
                <i class="fas fa-edit mr-2"></i>Edit User
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

    <!-- User Details Card -->
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <div class="px-6 py-4 bg-gray-50 border-b border-gray-200">
            <h2 class="text-xl font-semibold text-gray-900">User Information</h2>
        </div>
        <div class="px-6 py-4">
            <dl class="grid grid-cols-1 gap-x-4 gap-y-6 sm:grid-cols-2">
                <div>
                    <dt class="text-sm font-medium text-gray-500">User ID</dt>
                    <dd class="mt-1 text-sm text-gray-900">${user.id}</dd>
                </div>
                <div>
                    <dt class="text-sm font-medium text-gray-500">Username</dt>
                    <dd class="mt-1 text-sm text-gray-900">${user.username}</dd>
                </div>
                <div>
                    <dt class="text-sm font-medium text-gray-500">Email</dt>
                    <dd class="mt-1 text-sm text-gray-900">
                        <a href="mailto:${user.email}" class="text-blue-600 hover:text-blue-800">${user.email}</a>
                    </dd>
                </div>
                <div>
                    <dt class="text-sm font-medium text-gray-500">Status</dt>
                    <dd class="mt-1">
                        <c:choose>
                            <c:when test="${user.status == 'ACTIVE'}">
                                <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                                    <i class="fas fa-check-circle mr-1"></i>Active
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">
                                    <i class="fas fa-times-circle mr-1"></i>Inactive
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </dd>
                </div>
                <div>
                    <dt class="text-sm font-medium text-gray-500">Created At</dt>
                    <dd class="mt-1 text-sm text-gray-900">
                        <fmt:formatDate value="${user.createdAtAsDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                    </dd>
                </div>
                <div>
                    <dt class="text-sm font-medium text-gray-500">Last Updated</dt>
                    <dd class="mt-1 text-sm text-gray-900">
                        <fmt:formatDate value="${user.updatedAtAsDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                    </dd>
                </div>
            </dl>
        </div>
    </div>

    <!-- Action Buttons -->
    <div class="mt-6 flex justify-center space-x-4">
        <c:choose>
            <c:when test="${user.status == 'ACTIVE'}">
                <form method="post" action="/users/deactivate/${user.id}" class="inline">
                    <button type="submit" class="bg-yellow-600 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200"
                            onclick="return confirm('Are you sure you want to deactivate this user?')">
                        <i class="fas fa-user-times mr-2"></i>Deactivate User
                    </button>
                </form>
            </c:when>
            <c:otherwise>
                <form method="post" action="/users/activate/${user.id}" class="inline">
                    <button type="submit" class="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200"
                            onclick="return confirm('Are you sure you want to activate this user?')">
                        <i class="fas fa-user-check mr-2"></i>Activate User
                    </button>
                </form>
            </c:otherwise>
        </c:choose>
        
        <form method="post" action="/users/delete/${user.id}" class="inline">
            <button type="submit" class="bg-red-600 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200"
                    onclick="return confirm('Are you sure you want to delete this user? This action cannot be undone.')">
                <i class="fas fa-trash mr-2"></i>Delete User
            </button>
        </form>
    </div>
</div>
