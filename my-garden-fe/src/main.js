import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import {store} from "@/scripts/store.js";
import {router} from "@/scripts/router.js";

createApp(App).use(store).use(router).mount('#app')
