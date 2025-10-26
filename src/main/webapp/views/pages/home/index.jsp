<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="bg-green-100  min-h-screen">
    <div class="container mx-auto px-4 py-12">
        <!-- Hero Section -->
        <div class="text-center mb-12">
            <h1 class="text-5xl font-bold text-gray-900 mb-4">
                Welcome to <span class="text-blue-600">Demo App</span>
            </h1>
            <p class="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
                A modern web application built with Java, JDBI, and Tailwind CSS. 
                Manage users efficiently with our intuitive interface.
            </p>
            <div class="flex justify-center space-x-4">
                <a href="/users" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg transition duration-200">
                    <i class="fas fa-users mr-2"></i>View Users
                </a>
                <a href="/users/create" class="bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-6 rounded-lg transition duration-200">
                    <i class="fas fa-user-plus mr-2"></i>Create User
                </a>
            </div>
        </div>

        <!-- Features Section -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
            <div class="bg-white rounded-lg shadow-md p-6 text-center">
                <div class="text-blue-600 text-4xl mb-4">
                    <i class="fas fa-users"></i>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 mb-2">User Management</h3>
                <p class="text-gray-600">
                    Create, read, update, and delete users with a clean and intuitive interface.
                </p>
            </div>
            
            <div class="bg-white rounded-lg shadow-md p-6 text-center">
                <div class="text-green-600 text-4xl mb-4">
                    <i class="fas fa-database"></i>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 mb-2">Database Integration</h3>
                <p class="text-gray-600">
                    Built with JDBI for efficient database operations and H2 for development.
                </p>
            </div>
            
            <div class="bg-white rounded-lg shadow-md p-6 text-center">
                <div class="text-purple-600 text-4xl mb-4">
                    <i class="fas fa-paint-brush"></i>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 mb-2">Modern UI</h3>
                <p class="text-gray-600">
                    Beautiful and responsive design powered by Tailwind CSS and Font Awesome.
                </p>
            </div>
        </div>

        <!-- Quick Stats -->
        <div class="bg-white rounded-lg shadow-md p-8">
            <h2 class="text-2xl font-bold text-gray-900 mb-6 text-center">Quick Stats</h2>
            <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div class="text-center">
                    <div class="text-3xl font-bold text-blue-600 mb-2">
                        <i class="fas fa-code"></i>
                    </div>
                    <p class="text-gray-600">Java Backend</p>
                </div>
                <div class="text-center">
                    <div class="text-3xl font-bold text-green-600 mb-2">
                        <i class="fas fa-database"></i>
                    </div>
                    <p class="text-gray-600">JDBI ORM</p>
                </div>
                <div class="text-center">
                    <div class="text-3xl font-bold text-purple-600 mb-2">
                        <i class="fas fa-css3-alt"></i>
                    </div>
                    <p class="text-gray-600">Tailwind CSS</p>
                </div>
                <div class="text-center">
                    <div class="text-3xl font-bold text-red-600 mb-2">
                        <i class="fas fa-server"></i>
                    </div>
                    <p class="text-gray-600">Jakarta EE</p>
                </div>
            </div>
        </div>
    </div>
</div>