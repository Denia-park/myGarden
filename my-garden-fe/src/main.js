import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import {store} from "@/scripts/store.js";
import {router, routerBeforeEach} from "@/scripts/router.js";

routerBeforeEach(router);

createApp(App).use(store).use(router).mount('#app')
