/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        maroon: {
          50: '#fdf2f2',
          100: '#fbe5e5',
          200: '#f7cfcf',
          300: '#f1acac',
          400: '#e67c7c',
          500: '#d85555',
          600: '#c13939',
          700: '#a22d2d',
          800: '#872828',
          900: '#4A0E1A',
          950: '#2a080f',
        },
        gold: {
          50: '#fbf9f0',
          100: '#f6f0d9',
          200: '#ece1b3',
          300: '#decb84',
          400: '#d0b157',
          500: '#D4AF37',
          600: '#b58d2a',
          700: '#906b23',
          800: '#785922',
          900: '#674c21',
          950: '#3c2a10',
        },
        beige: {
          50: '#FAF7F2',
          100: '#F5E6D3',
          200: '#e8d5bb',
          300: '#d6ba94',
          400: '#c19a6d',
          500: '#ae8051',
          600: '#a07046',
          700: '#855a3b',
          800: '#6d4a34',
          900: '#593d2d',
          950: '#302017',
        },
        primary: {
          50: '#fdf2f2',
          100: '#fbe5e5',
          200: '#f7cfcf',
          300: '#f1acac',
          400: '#e67c7c',
          500: '#d85555',
          600: '#c13939',
          700: '#a22d2d',
          800: '#872828',
          900: '#4A0E1A',
          950: '#2a080f',
        }
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        display: ['Poppins', 'sans-serif'],
      },
      animation: {
        'fade-in': 'fadeIn 0.4s ease-out forwards',
        'slide-up': 'slideUp 0.5s ease-out forwards',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { opacity: '0', transform: 'translateY(10px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        }
      }
    }
  },
  plugins: []
}
