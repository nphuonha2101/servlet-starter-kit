// Utility functions for JSP pages
export const utils = {
    // Format currency
    formatCurrency: (amount) => {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount)
    },
    
    // Format date
    formatDate: (date) => {
        return new Intl.DateTimeFormat('vi-VN').format(new Date(date))
    },
    
    // Show notification
    showNotification: (message, type = 'info') => {
        const notification = document.createElement('div')
        notification.className = `fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 ${
            type === 'success' ? 'bg-green-500' :
            type === 'error' ? 'bg-red-500' :
            type === 'warning' ? 'bg-yellow-500' :
            'bg-blue-500'
        } text-white`
        notification.textContent = message
        
        document.body.appendChild(notification)
        
        setTimeout(() => {
            notification.remove()
        }, 3000)
    },
    
    // Toggle mobile menu
    toggleMobileMenu: () => {
        const menu = document.getElementById('mobile-menu')
        if (menu) {
            menu.classList.toggle('hidden')
        }
    }
}
