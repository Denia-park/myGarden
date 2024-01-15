import DailyRoutine from "@/pages/DailyRoutine.vue";
import Login from "@/pages/Login.vue";
import {createRouter, createWebHistory} from "vue-router";
import NotFound from "@/pages/NotFound.vue";
import SignUp from "@/pages/SignUp.vue";

const routes = [
    {path: '/login', component: Login},
    {path: '/daily-routine', component: DailyRoutine},
    {path: '/signup', component: SignUp},
    {path: '/notFound', component: NotFound},

    {path: '/', redirect: '/login'},
    {path: '/:pathMatch(.*)*', redirect: '/notFound'}
]

export const router = createRouter({
    history: createWebHistory(),
    routes
})
