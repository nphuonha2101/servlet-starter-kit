// Main JavaScript for JSP pages
console.log('Tailwind CSS + Vite development environment loaded!')

// Add interactive functionality
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, ready for interactive features')
    
    // Add smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault()
            const target = document.querySelector(this.getAttribute('href'))
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                })
            }
        })
    })
    
    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card')
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`
        card.classList.add('fade-in')
    })
})

// Export for potential module usage
export { default as utils } from './utils.js'
