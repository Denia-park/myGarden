import DailyRoutine from "@/pages/DailyRoutine.vue";
import Login from "@/pages/Login.vue";
import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {path: '/', component: DailyRoutine},
    {path: '/login', component: Login},
]

export const router = createRouter({
    history: createWebHistory(),
    routes
})
