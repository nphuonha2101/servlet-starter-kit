<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 flex items-center justify-center px-4">
    <div class="max-w-md w-full bg-white rounded-lg shadow-lg p-8 text-center">
        <!-- Error Icon -->
        <div class="mb-6">
            <i class="fas fa-exclamation-triangle text-6xl text-red-500"></i>
        </div>
        
        <!-- Error Title -->
        <h1 class="text-3xl font-bold text-gray-900 mb-4">
            <c:choose>
                <c:when test="${not empty errorCode}">
                    Error ${errorCode}
                </c:when>
                <c:otherwise>
                    Error
                </c:otherwise>
            </c:choose>
        </h1>
        
        <!-- Error Message -->
        <p class="text-lg text-gray-600 mb-6">
            <c:choose>
                <c:when test="${not empty errorMessage}">
                    ${errorMessage}
                </c:when>
                <c:when test="${not empty pageContext.exception}">
                    ${pageContext.exception.message}
                </c:when>
                <c:otherwise>
                    An error occurred while processing your request.
                </c:otherwise>
            </c:choose>
        </p>
        
        <!-- Action Buttons -->
        <div class="flex gap-4 justify-center">
            <a href="/" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded-lg transition duration-200">
                <i class="fas fa-home mr-2"></i>Go Home
            </a>
            <button onclick="history.back()" class="bg-gray-600 hover:bg-gray-700 text-white font-bold py-2 px-6 rounded-lg transition duration-200">
                <i class="fas fa-arrow-left mr-2"></i>Go Back
            </button>
        </div>
        
        <!-- Technical Details (Only in development) -->
        <c:if test="${not empty pageContext.exception}">
            <details class="mt-8 text-left">
                <summary class="cursor-pointer text-sm text-gray-500 hover:text-gray-700">
                    <i class="fas fa-info-circle mr-1"></i>Technical Details
                </summary>
                <div class="mt-4 p-4 bg-gray-50 rounded text-xs font-mono text-gray-600 overflow-auto">
                    <pre>${pageContext.exception}</pre>
                </div>
            </details>
        </c:if>
    </div>
</div>

