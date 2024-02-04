import {fileURLToPath, URL} from 'node:url'

import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default ({mode}) => {
    process.env = {...process.env, ...loadEnv(mode, process.cwd())};

    const {VITE_API_URL} = process.env;

    return defineConfig({
        plugins: [
            vue(),
        ],
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url))
            }
        },
        server: {
            proxy: {
                '/api': VITE_API_URL || 'https://localhost'
            }
        },
        build: {
            outDir: '../my-garden-be/src/main/resources/static'
        }
    });
}
