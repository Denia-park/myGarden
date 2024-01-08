import DailyRoutine from "@/components/dailyRoutine/DailyRoutine.vue";
import Login from "@/components/login/Login.vue";
import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {path: '/', component: DailyRoutine},
    {path: '/login', component: Login},
]

export const router = createRouter({
    history: createWebHistory(),
    routes
})
