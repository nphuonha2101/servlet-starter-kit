/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/main/webapp/**/*.{html,jsp,js}",
    "./src/main/webapp/views/**/*.jsp",
    "./src/main/webapp/static/**/*.{html,js}",
    "./src/main/webapp/static/css/input.css",
    "./src/main/webapp/index.jsp"
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
  safelist: [
    'bg-blue-600', 'bg-green-600', 'bg-red-600', 'bg-yellow-600',
    'bg-blue-700', 'bg-green-700', 'bg-red-700', 'bg-yellow-700',
    'text-white', 'text-gray-900', 'text-gray-600', 'text-gray-500',
    'hover:bg-blue-700', 'hover:bg-green-700', 'hover:bg-red-700', 'hover:bg-yellow-700',
    'hover:text-white', 'hover:text-blue-900', 'hover:text-green-900', 'hover:text-red-900', 'hover:text-yellow-900',
    'border-blue-400', 'border-green-400', 'border-red-400', 'border-yellow-400',
    'border-blue-600', 'border-green-600', 'border-red-600', 'border-yellow-600',
    'ring-blue-500', 'ring-red-500', 'ring-green-500', 'ring-yellow-500',
    'focus:border-blue-500', 'focus:ring-blue-500',
    'shadow-sm', 'shadow-md', 'shadow-lg',
    'rounded', 'rounded-md', 'rounded-lg', 'rounded-full',
    'px-2', 'px-3', 'px-4', 'px-6', 'py-1', 'py-2', 'py-3', 'py-4', 'py-8', 'py-12',
    'mb-2', 'mb-4', 'mb-6', 'mb-8', 'mb-12', 'mt-1', 'mt-2', 'mt-6', 'mt-8', 'mt-12',
    'mr-1', 'mr-2', 'ml-4',
    'text-xs', 'text-sm', 'text-lg', 'text-xl', 'text-2xl', 'text-3xl', 'text-4xl', 'text-5xl',
    'font-bold', 'font-medium', 'font-semibold',
    'flex', 'inline-flex', 'grid', 'table', 'block', 'inline', 'hidden',
    'items-center', 'justify-center', 'justify-between', 'justify-end',
    'space-x-2', 'space-x-3', 'space-x-4', 'space-x-6', 'space-y-2',
    'gap-4', 'gap-6', 'gap-8', 'gap-x-4', 'gap-y-6',
    'grid-cols-1', 'grid-cols-2', 'grid-cols-3', 'grid-cols-4',
    'flex-col', 'flex-row',
    'w-full', 'w-6', 'min-w-full', 'max-w-md', 'max-w-2xl',
    'h-6', 'min-h-screen',
    'container', 'mx-auto',
    'transition', 'duration-200',
    'transform', 'animate-spin',
    'overflow-hidden', 'whitespace-nowrap',
    'divide-y', 'divide-gray-200',
    'bg-gradient-to-br', 'from-blue-50', 'to-indigo-100',
    'bg-opacity-50',
    'sm:inline', 'sm:grid-cols-2',
    'md:flex', 'md:hidden', 'md:grid-cols-3', 'md:grid-cols-4',
    'fixed', 'inset-0', 'right-4', 'top-4', 'z-50',
    'resize', 'list-inside', 'list-disc',
    'contents',
    'blur',
    'uppercase', 'lowercase', 'tracking-wider',
    'text-left', 'text-center',
    'border', 'border-b', 'border-b-2', 'border-t',
    'ring-2',
    'p-4', 'p-6', 'p-8', 'pb-4', 'pt-8',
    'bg-black', 'bg-blue-100', 'bg-blue-500', 'bg-gray-300', 'bg-gray-50', 'bg-gray-600', 'bg-gray-800',
    'bg-green-100', 'bg-green-500', 'bg-indigo-600', 'bg-red-100', 'bg-red-500',
    'bg-white', 'bg-yellow-100', 'bg-yellow-500',
    'text-blue-600', 'text-blue-700', 'text-gray-300', 'text-gray-500', 'text-gray-700', 'text-gray-800',
    'text-green-600', 'text-green-700', 'text-green-800', 'text-indigo-600', 'text-purple-600',
    'text-red-500', 'text-red-600', 'text-red-700', 'text-red-800', 'text-yellow-600', 'text-yellow-700'
  ]
}

