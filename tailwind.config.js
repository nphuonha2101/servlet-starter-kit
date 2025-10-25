/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/main/webapp/**/*.{html,jsp,js}",
    "./src/main/webapp/views/**/*.jsp",
    "./src/main/webapp/static/**/*.{html,js}",
    "./src/main/webapp/static/css/input.css"
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
        }
      }
    },
  },
  plugins: [],
}

