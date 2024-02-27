import type { Config } from 'tailwindcss';

const config: Config = {
  content: ['./**/*.vue'],
  plugins: [require('@catppuccin/tailwindcss')],
  theme: {
    extend: {
      backgroundImage: {
        'img-minecraft-bg': "url('/minecraft-bg.png')",
      },
    },
  },
};

export default config;
