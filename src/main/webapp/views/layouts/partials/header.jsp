<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!-- Header Section -->
<header class="bg-blue-600 text-white shadow-lg">
    <div class="container mx-auto px-4">
        <div class="flex items-center justify-between py-4">
            <!-- Logo/Brand -->
            <div class="flex items-center">
                <a href="/" class="text-2xl font-bold hover:text-blue-200 transition duration-200">
                    <i class="fas fa-home mr-2"></i>Demo App
                </a>
            </div>
            
            <!-- Navigation Menu -->
            <nav class="hidden md:flex space-x-6">
                <a href="/" class="hover:text-blue-200 transition duration-200">
                    <i class="fas fa-home mr-1"></i>Home
                </a>
                <a href="/users" class="hover:text-blue-200 transition duration-200">
                    <i class="fas fa-users mr-1"></i>Users
                </a>
                <a href="/users/create" class="hover:text-blue-200 transition duration-200">
                    <i class="fas fa-user-plus mr-1"></i>Create User
                </a>
            </nav>
            
            <!-- Mobile Menu Button -->
            <div class="md:hidden">
                <button id="mobile-menu-button" class="text-white hover:text-blue-200 focus:outline-none">
                    <i class="fas fa-bars text-xl"></i>
                </button>
            </div>
        </div>
        
        <!-- Mobile Navigation Menu -->
        <div id="mobile-menu" class="md:hidden hidden pb-4">
            <div class="flex flex-col space-y-2">
                <a href="/" class="block py-2 hover:text-blue-200 transition duration-200">
                    <i class="fas fa-home mr-2"></i>Home
                </a>
                <a href="/users" class="block py-2 hover:text-blue-200 transition duration-200">
                    <i class="fas fa-users mr-2"></i>Users
                </a>
                <a href="/users/create" class="block py-2 hover:text-blue-200 transition duration-200">
                    <i class="fas fa-user-plus mr-2"></i>Create User
                </a>
            </div>
        </div>
    </div>
</header>

<script>
    // Mobile menu toggle
    document.getElementById('mobile-menu-button').addEventListener('click', function() {
        const mobileMenu = document.getElementById('mobile-menu');
        mobileMenu.classList.toggle('hidden');
    });
</script>
