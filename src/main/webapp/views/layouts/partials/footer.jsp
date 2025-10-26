<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Footer Section -->
<footer class="bg-gray-800 text-white py-8 mt-12">
    <div class="container mx-auto px-4">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <!-- Company Info -->
            <div>
                <h3 class="text-lg font-semibold mb-4">
                    <i class="fas fa-building mr-2"></i>Demo App
                </h3>
                <p class="text-gray-300 text-sm">
                    A modern web application built with Java, JDBI, and Tailwind CSS.
                </p>
            </div>
            
            <!-- Quick Links -->
            <div>
                <h3 class="text-lg font-semibold mb-4">
                    <i class="fas fa-link mr-2"></i>Quick Links
                </h3>
                <ul class="space-y-2 text-sm">
                    <li><a href="/" class="text-gray-300 hover:text-white transition duration-200">
                        <i class="fas fa-home mr-1"></i>Home
                    </a></li>
                    <li><a href="/users" class="text-gray-300 hover:text-white transition duration-200">
                        <i class="fas fa-users mr-1"></i>Users
                    </a></li>
                    <li><a href="/users/create" class="text-gray-300 hover:text-white transition duration-200">
                        <i class="fas fa-user-plus mr-1"></i>Create User
                    </a></li>
                </ul>
            </div>
            
            <!-- Contact Info -->
            <div>
                <h3 class="text-lg font-semibold mb-4">
                    <i class="fas fa-envelope mr-2"></i>Contact
                </h3>
                <div class="text-gray-300 text-sm space-y-2">
                    <p><i class="fas fa-envelope mr-2"></i>contact@demoapp.com</p>
                    <p><i class="fas fa-phone mr-2"></i>+1 (555) 123-4567</p>
                    <p><i class="fas fa-map-marker-alt mr-2"></i>123 Demo Street, Demo City</p>
                </div>
            </div>
        </div>
        
        <div class="border-t border-gray-700 mt-8 pt-8 text-center">
            <p class="text-gray-300 text-sm">
                &copy; 2024 Demo App. All rights reserved. Built with 
                <i class="fas fa-heart text-red-500"></i> using Java & Tailwind CSS.
            </p>
        </div>
    </div>
</footer>
