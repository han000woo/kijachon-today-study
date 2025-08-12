import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    proxy: {
      '/submit-study': 'http://localhost:8080',
      '/api': 'http://localhost:8080'
    }
  }
});
