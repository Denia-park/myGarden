import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import {createRouter, createWebHistory} from 'vue-router'
import DailyRoutine from "@/components/dailyRoutine/DailyRoutine.vue";
import Login from "@/components/login/Login.vue";

const routes = [
    {path: '/', component: DailyRoutine},
    {path: '/login', component: Login},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

createApp(App).use(router).mount('#app')
